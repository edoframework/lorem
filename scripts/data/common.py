"""Shared helpers for the data scripts: downloads into a cache outside the repository, the lock
file that records what was downloaded, and the JSON writer for the data files.

The build never runs these scripts. They need Python 3.10+ and nothing outside its standard
library; segmenting Chinese, Japanese and Thai also needs a JDK (see segment.py)."""

import bz2
import datetime
import hashlib
import json
import os
import pathlib
import time
import urllib.parse
import urllib.request

REPO = pathlib.Path(__file__).resolve().parents[2]
DATA = REPO / "shared" / "resources" / "dev" / "taihei" / "mihon" / "data"
LOCK = pathlib.Path(__file__).resolve().parent / "sources.lock.json"
# Raw downloads stay outside the repository: nothing from them may reach a published jar.
CACHE = pathlib.Path(os.environ.get("MIHON_DATA_CACHE", pathlib.Path.home() / ".cache" / "taihei-mihon-data"))
USER_AGENT = "taihei-mihon-data-scripts/5.0 (https://github.com/taiheiframework/mihon; hello@taihei.dev)"

if CACHE.resolve().is_relative_to(REPO):
    raise SystemExit(f"MIHON_DATA_CACHE must be outside the repository: {CACHE}")


def _lock():
    return json.loads(LOCK.read_text()) if LOCK.exists() else {}


def _record(key, url, path):
    lock = _lock()
    digest = hashlib.sha256(path.read_bytes()).hexdigest()
    entry = lock.get(key)
    if entry and entry["sha256"] != digest:
        raise SystemExit(f"{key}: the cached file differs from the one recorded in sources.lock.json")
    if not entry:
        lock[key] = {
            "url": url,
            "sha256": digest,
            "bytes": path.stat().st_size,
            "retrieved": datetime.date.today().isoformat(),
        }
        LOCK.write_text(json.dumps(dict(sorted(lock.items())), indent=2, ensure_ascii=False) + "\n")


def fetch(key, url):
    """The file at `url`, downloaded once into the cache under `key`, and recorded in the lock
    file with its SHA-256. A later run uses the cached copy and checks it against the lock."""
    path = CACHE / key
    if not path.exists():
        path.parent.mkdir(parents=True, exist_ok=True)
        req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
        with urllib.request.urlopen(req, timeout=300) as resp:
            tmp = path.with_suffix(path.suffix + ".part")
            tmp.write_bytes(resp.read())
            tmp.rename(path)
    _record(key, url, path)
    return path


def sparql(key, query):
    """The bindings of a Wikidata Query Service query, cached under `key` and recorded like a
    download, so a rebuild repeats the same data."""
    path = CACHE / "wikidata" / f"{key}.json"
    url = "https://query.wikidata.org/sparql?" + urllib.parse.urlencode({"query": query, "format": "json"})
    if not path.exists():
        path.parent.mkdir(parents=True, exist_ok=True)
        last = None
        for attempt in range(4):
            try:
                req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT, "Accept": "application/sparql-results+json"})
                with urllib.request.urlopen(req, timeout=120) as resp:
                    body = resp.read()
                json.loads(body)
                path.write_bytes(body)
                break
            except Exception as e:  # a time-out or a throttled request: wait and try again
                last = e
                time.sleep(10 * (attempt + 1))
        else:
            raise SystemExit(f"wikidata {key}: {last}")
    _record(f"wikidata/{key}.json", "https://query.wikidata.org/sparql (query in scripts/data)", path)
    rows = json.loads(path.read_text())["results"]["bindings"]
    return [{k: v["value"] for k, v in row.items()} for row in rows]


def read_bz2_lines(path):
    with bz2.open(path, "rt", encoding="utf-8") as f:
        for line in f:
            yield line.rstrip("\n")


def _dump(value, indent):
    """JSON with one list entry per line, so a change to the data reads as a small diff."""
    if isinstance(value, dict) and indent == 0:
        items = [f'  {json.dumps(k, ensure_ascii=False)}: {_dump(v, 1)}' for k, v in value.items()]
        return "{\n" + ",\n".join(items) + "\n}"
    if isinstance(value, list) and indent <= 1 and value and isinstance(value[0], (list, dict)):
        pad = "  " * (indent + 1)
        inner = ",\n".join(pad + json.dumps(v, ensure_ascii=False, separators=(", ", ": ")) for v in value)
        return "[\n" + inner + "\n" + "  " * indent + "]"
    return json.dumps(value, ensure_ascii=False, separators=(", ", ": "))


def write_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(_dump(value, 0) + "\n", encoding="utf-8")
