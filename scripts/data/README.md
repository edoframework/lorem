# Data scripts

These scripts build the language data in `shared/resources/dev/taihei/mihon/data/`. The build
never runs them; they need Python 3.10 or later with nothing beyond its standard library, and a
JDK for segmenting Chinese, Japanese and Thai (`segment.py`, ICU4J from Maven Central).

```sh
python3 scripts/data/build.py en la     # rebuild some languages (all when none is named)
python3 scripts/data/docs.py            # the Downloads table of docs/data-sources.md
```

Downloads and Wikidata query results are cached outside the repository, in `MIHON_DATA_CACHE`
(`~/.cache/taihei-mihon-data` by default), and recorded in `sources.lock.json` with their URL,
size, retrieval date and SHA-256; a cached file that no longer matches its record stops the run.
A Wikidata result is cached under a hash of its query, so a changed query asks again. A download
that is not in the lock file yet is refused unless `MIHON_DATA_RECORD=1` is set, so that a new
source is a deliberate change to the lock file; ICU4J, which the scripts run, is checked against
a SHA-256 pinned in `segment.py`. The cache must not be writable by group or others, and a
download is at most 256 MB.

| Script | Builds |
| --- | --- |
| `languages.py` | the languages, their default countries, punctuation, separators and name order |
| `text.py` | `text.json`: words from Tatoeba, filtered, with weights; the capitals of the initials |
| `names.py` | `names.json`: given and family names with their romanised and ASCII forms |
| `cldr.py` | `countries.json` and the quotation marks |
| `places.py`, `formats.py` | `country-<CC>.json`: cities and regions, and the hand-written address formats |

## The files

- `languages.json`: `languages` (each `tag`, `script` and default `country`) and `aliases`
  (`[alias, tag]`).
- `<tag>/text.json`: `wordSeparator`, `sentenceSeparator`, `fullStop`, `questionOpen`,
  `questionMark`, `exclamationOpen`, `exclamationMark`, `comma`, `quoteOpen`, `quoteClose`;
  `cased` and `titleCase`; `capitals` (`[initial, capital]`); `wordsPerSentence` (`[min, max]`);
  `words` (`[word, weight]`, at most 2000).
- `<tag>/names.json`: `order` (`given-family` or `family-given`), `separator`, and the lists
  `female`, `male` and `family` of `{name, weight, latin?, ascii?, reading?}`; a family name may
  have a `female` form `{name, latin?, ascii?, reading?}`. `latin` is absent when the name is in
  Latin script; `ascii`, the form an e-mail address is built from, is absent when it equals
  `latin`.
- `<tag>/countries.json`: `[code, name]` for every country, sorted by code.
- `<tag>/country-<CC>.json`: `code`, `lines` (templates with `{street}`, `{city}`, `{region}`,
  `{postalCode}`, `{country}`), `separator`, `postalCode` (a pattern), `regions` (`[name, weight]`),
  `cities` (`{name, weight, region?}`), `streets` (templates, see `formats.py`), `phone` (a
  pattern) and `fictionalPhone`.

The build checks every file against this schema (`EmbeddedData.check` in `build.mill`) and holds
each language to 80 KB; `scripts/test-data-check.sh` proves that the check refuses what it must.
