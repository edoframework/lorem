"""Unicode CLDR 47 (Unicode License v3): country names per language, quotation marks and the
likely region of each language. Fetched from the cldr-json packages, pinned to 47.0.0."""

import json

from common import fetch, sparql

VERSION = "47.0.0"
BASE = "https://cdn.jsdelivr.net/npm"

# Two-letter territory codes CLDR names that are not countries of ISO 3166-1: groupings,
# private use and exceptional reservations.
NOT_COUNTRIES = {"AC", "CP", "CQ", "DG", "EA", "EU", "EZ", "IC", "QO", "TA", "UN", "XA", "XB", "XK", "ZZ"}


def _json(package, path):
    key = f"cldr-{VERSION}/{package}/{path}"
    return json.loads(fetch(key, f"{BASE}/{package}@{VERSION}/{path}").read_text(encoding="utf-8"))


def licence_text():
    return fetch(f"cldr-{VERSION}/cldr-core/LICENSE", f"{BASE}/cldr-core@{VERSION}/LICENSE").read_text(encoding="utf-8")


def territories(locale):
    """{code: name} of the countries in `locale`, the plain form of each name (no -alt-)."""
    data = _json("cldr-localenames-full", f"main/{locale}/territories.json")
    names = data["main"][locale]["localeDisplayNames"]["territories"]
    return {k: v for k, v in names.items() if len(k) == 2 and k.isalpha() and k not in NOT_COUNTRIES}


def quotes(locale):
    d = _json("cldr-misc-full", f"main/{locale}/delimiters.json")["main"][locale]["delimiters"]
    return [d["quotationStart"], d["quotationEnd"]]


def likely_region(tag):
    subtags = _json("cldr-core", "supplemental/likelySubtags.json")["supplemental"]["likelySubtags"]
    full = subtags.get(tag) or subtags[tag.split("-")[0]]
    return full.split("-")[-1]


def latin_territories(codes):
    """Latin, which CLDR lacks: the Latin labels of the countries on Wikidata (CC0), by ISO code."""
    rows = sparql("countries-la", """
        SELECT ?code ?label WHERE {
          ?c wdt:P297 ?code; rdfs:label ?label .
          FILTER(LANG(?label) = "la")
          FILTER NOT EXISTS { ?c wdt:P576 ?end }
        }""")
    out = {}
    for r in sorted(rows, key=lambda r: (r["code"], r["label"])):
        if r["code"] in codes and r["code"] not in out:
            out[r["code"]] = r["label"]
    return out
