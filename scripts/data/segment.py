"""Word segmentation for Chinese, Japanese and Thai, which write no space between words, with
ICU4J's dictionary-based BreakIterator. ICU4J (Unicode License v3) is fetched from Maven Central
at an exact version and used only here, never by the build or the library."""

import json
import pathlib
import subprocess
import tempfile

from common import CACHE, fetch

ICU4J_VERSION = "77.1"
ICU4J = f"https://repo1.maven.org/maven2/com/ibm/icu/icu4j/{ICU4J_VERSION}/icu4j-{ICU4J_VERSION}.jar"
# The jar is run, so its SHA-256 is pinned here, not only recorded on first download. It matches
# the .sha1 Maven Central publishes for it.
ICU4J_SHA256 = "b3640b9f416a4411fd33c59abbeea8fd57d024c23e1819bf9673220a97499fe3"
SEGMENTER = pathlib.Path(__file__).resolve().parent / "Segment.java"


def words(sentences, locale):
    """The words of each sentence, as ICU4J splits them for `locale`."""
    jar = fetch(f"icu4j-{ICU4J_VERSION}.jar", ICU4J, ICU4J_SHA256)
    with tempfile.TemporaryDirectory(dir=CACHE) as tmp:
        src = pathlib.Path(tmp) / "in.txt"
        src.write_text("\n".join(s.replace("\n", " ") for s in sentences), encoding="utf-8")
        out = subprocess.run(
            ["java", "-cp", str(jar), str(SEGMENTER), locale, str(src)],
            check=True, capture_output=True,
        ).stdout.decode("utf-8")
    for line in out.splitlines():
        yield json.loads(line)
