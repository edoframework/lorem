# Data sources

Every value Mihon draws from a language's data comes from the sources below. The data files are
under `shared/resources/dev/taihei/mihon/data/`; the scripts in `scripts/data/` derive them. The
build never runs the scripts and needs nothing but Maven Central; the raw downloads stay in a
cache outside the repository (`MIHON_DATA_CACHE`, `~/.cache/taihei-mihon-data` by default) and
never enter it. `scripts/data/sources.lock.json` records the URL, size, retrieval date and SHA-256
of each download; the table at the end of this page is generated from it. Tatoeba publishes
its exports weekly under the same URL, so a later download differs: the date and SHA-256 identify
the file the data was built from.

Each source's licence was checked before its data was committed. Only single words and names are
taken; no sentence, and no span of more than one word, from any source is reproduced.

## Sources

| What | Source | Licence |
| --- | --- | --- |
| Country names in each language, quotation marks, the default country of each language (likely subtags) | [Unicode CLDR](https://cldr.unicode.org) 47.0.0, the `cldr-localenames-full`, `cldr-misc-full` and `cldr-core` JSON packages | [Unicode License v3](https://www.unicode.org/license.txt), in `LICENSE-UNICODE` |
| Words of each language's text (except Latin) | Word frequencies counted from the per-language sentence exports of the [Tatoeba Project](https://tatoeba.org), written by the Tatoeba contributors | [CC BY 2.0 FR](https://creativecommons.org/licenses/by/2.0/fr/), attributed in `NOTICE` |
| Latin text | The words of Lorem Ipsum, derived from Cicero, *De finibus bonorum et malorum* (45 BC), in `scripts/data/lorem.txt` | public domain |
| United States given names and family names | [U.S. Census Bureau](https://www.census.gov/topics/population/genealogy/data.html): the 1990 first-name lists (`dist.female.first`, `dist.male.first`) and the 2010 surname list | U.S. Government work, 17 U.S.C. 105 |
| Latin given and family names | Classical Roman praenomina and nomina gentilicia, written in `scripts/data/names.py` | historical facts, written for this library (Apache 2.0) |
| Cities, their regions, Latin country names, and the given and family names of the other languages | [Wikidata](https://www.wikidata.org), through its Query Service; the queries are in `scripts/data/` | [CC0 1.0](https://www.wikidata.org/wiki/Wikidata:Licensing) |
| Address formats, postal code patterns, street patterns, phone patterns | Written for this library in `scripts/data/formats.py`, after the address metadata of [Google libaddressinput](https://github.com/google/libaddressinput); UPU material was used as a reference only | written for this library (Apache 2.0), based on data under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/), attributed in `NOTICE` |

## Filters, used by the scripts and never shipped

- The words of each language are checked against the [LDNOOBW](https://github.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words)
  lists (CC BY 4.0) at commit `5faf2ba42d7b1c0977169ec3611df25a3c08eb13`, and every word on them is
  removed. The lists are downloaded into the cache, never committed. Six of the 25 languages
  have no list there: Vietnamese, Ukrainian, Bulgarian, Serbian, Greek and Hebrew.
- In scripts with case, a word that is capitalised wherever it stands inside a sentence is a
  proper noun and is removed (German keeps it: it capitalises nouns). The names of the language,
  and the recurring characters of Tatoeba's sentences, are removed in every script.
- Chinese, Japanese and Thai, which write no space between words, are split into words with
  [ICU4J](https://github.com/unicode-org/icu) 77.1 from Maven Central (Unicode License v3), run by
  `scripts/data/segment.py` only, never by the build or the library.

## Telephone numbers

Mihon's phone numbers are in a range reserved for fiction only where the country reserves one and
the reservation is cited here; elsewhere a number may be real.

| Country | Range | Primary source |
| --- | --- | --- |
| United States (NANP) | 555-0100 to 555-0199, any area code | [NANPA: 555 line numbers](https://www.nanpa.com/numbering/555-line-numbers) |

## Processing

- **Words.** Tatoeba's sentences of a language are split into words; a word must consist of
  letters of the language's script. The 1500 most frequent words that pass the filters are kept,
  each weighted by the square root of its count, so that the commonest words do not crowd out
  the rest.
- **Capitals.** For a script with case, the data holds the capital of each initial its words use,
  computed by the script (`str.upper`, with Turkish `i` to `İ` and Dutch `ij` to `IJ`), so that no
  platform case mapping runs at run time and the JVM and Scala.js cannot differ.
- **Names.** The Census lists keep their top 200 given names per gender and 300 surnames,
  weighted by frequency.
- **Cities.** The settlements of the country with the largest populations on Wikidata (an urban
  or metropolitan area is not a city), named in the language, weighted by population in
  thousands, with their first-level region where the address format names one.

## Downloads

<!-- generated by scripts/data/docs.py from scripts/data/sources.lock.json -->

| File | Retrieved | Bytes | SHA-256 |
| --- | --- | --- | --- |
| [census/2010surnames-names.zip](https://www2.census.gov/topics/genealogy/2010surnames/names.zip) | 2026-10-24 | 12874389 | `117c41cb4668727b7627b2845b6df3f83eb2a22a1813f42c0ff4bdcab86de135` |
| [census/dist.female.first](https://www2.census.gov/topics/genealogy/1990surnames/dist.female.first) | 2026-10-24 | 149625 | `bd2f310fc4e5d5e5ea122c9d4342c9821145823118eb20db1647f305ec77b358` |
| [census/dist.male.first](https://www2.census.gov/topics/genealogy/1990surnames/dist.male.first) | 2026-10-24 | 42665 | `0a5078ef6effe3b483d15b0f7f95047662126c9bfb624ecd5e5b978fc0f2470b` |
| [cldr-47.0.0/cldr-core/LICENSE](https://cdn.jsdelivr.net/npm/cldr-core@47.0.0/LICENSE) | 2026-10-24 | 2033 | `b4c0ae8ef04f7059f96ce5bbe0467f9fe6f6d81bbe13517701dfeb961fb4d0b6` |
| [cldr-47.0.0/cldr-core/supplemental/likelySubtags.json](https://cdn.jsdelivr.net/npm/cldr-core@47.0.0/supplemental/likelySubtags.json) | 2026-10-24 | 218772 | `b0f791c37b7f1dedb57f6d0542b904ab0ceee7a476f5194c797f53e87511b9db` |
| [cldr-47.0.0/cldr-localenames-full/main/en/territories.json](https://cdn.jsdelivr.net/npm/cldr-localenames-full@47.0.0/main/en/territories.json) | 2026-10-24 | 9956 | `158c1d575308f7e46912edbeda435c8fe2ef5dad280798231f3a432e406b1807` |
| [cldr-47.0.0/cldr-misc-full/main/ar/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/ar/delimiters.json) | 2026-10-24 | 267 | `3d602c24023aa1bd998335ebe3f497390a17093b74791ae6c797a974813d6de2` |
| [cldr-47.0.0/cldr-misc-full/main/bg/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/bg/delimiters.json) | 2026-10-24 | 267 | `f37a1033935efbb023767f3aa2dbe0565d00241431983babbe0d54fde2839ae8` |
| [cldr-47.0.0/cldr-misc-full/main/de/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/de/delimiters.json) | 2026-10-24 | 267 | `8885bcb7f04cc611a0904583a708396ecfc273cfaf69935441b1124f9cdc9557` |
| [cldr-47.0.0/cldr-misc-full/main/el/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/el/delimiters.json) | 2026-10-24 | 265 | `24cf80d4638d234949f9eb2830905691fc61e58c1b68e929f30fbef77a7888bd` |
| [cldr-47.0.0/cldr-misc-full/main/en/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/en/delimiters.json) | 2026-10-24 | 267 | `7dba09b7aba3f8c98292df1e6fee27cdc950d3542139472799ca350d877909bb` |
| [cldr-47.0.0/cldr-misc-full/main/es/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/es/delimiters.json) | 2026-10-24 | 267 | `72a69bb3fcaf42c08fd937c34a943b2654d522a3a11e350dec6e3c003e388ea9` |
| [cldr-47.0.0/cldr-misc-full/main/fr/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/fr/delimiters.json) | 2026-10-24 | 263 | `f77ccca4fe6749dcd5801f8dce618beea0318c4cfbff7934287e3d968d96c095` |
| [cldr-47.0.0/cldr-misc-full/main/he/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/he/delimiters.json) | 2026-10-24 | 267 | `52a6099efc608e956574551aff671f1ef03004ac0a55482eca1a69a6016c8888` |
| [cldr-47.0.0/cldr-misc-full/main/hi/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/hi/delimiters.json) | 2026-10-24 | 267 | `6036e1d19f25bbef2683d3135d1ce89cf5a226d85f40bae62c68aee950d1c15e` |
| [cldr-47.0.0/cldr-misc-full/main/hu/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/hu/delimiters.json) | 2026-10-24 | 265 | `1ec4b8d7ea4952da39ad30c6de0302d05a68614d925c82433e91f59927e10ece` |
| [cldr-47.0.0/cldr-misc-full/main/it/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/it/delimiters.json) | 2026-10-24 | 265 | `d016f3b3aa7cf9740195b32eec0192d1fb13a04185284e1faddf45705d242d8c` |
| [cldr-47.0.0/cldr-misc-full/main/ja/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/ja/delimiters.json) | 2026-10-24 | 267 | `b5381ed238e92f3bc63f94a24b12ad192a297dc58e15afd8cf17da5b239e0a6b` |
| [cldr-47.0.0/cldr-misc-full/main/ko/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/ko/delimiters.json) | 2026-10-24 | 267 | `4257ab3c82bb486a5c20182fbe2f7e8db575e6c1f3a18d06495670820210ce88` |
| [cldr-47.0.0/cldr-misc-full/main/nl/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/nl/delimiters.json) | 2026-10-24 | 267 | `17471b5aedd3812c47c066569bad5d850602a162538c7837e832451b499b94ef` |
| [cldr-47.0.0/cldr-misc-full/main/pl/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/pl/delimiters.json) | 2026-10-24 | 265 | `66361f409d3daa467fd243c1becdde63e061c16dfc6409208e7ae4520f3f254a` |
| [cldr-47.0.0/cldr-misc-full/main/pt/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/pt/delimiters.json) | 2026-10-24 | 267 | `9b00615b6df751390908e2cf64f8bd42b59f3adc005a217774da7f8199c84c96` |
| [cldr-47.0.0/cldr-misc-full/main/ru/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/ru/delimiters.json) | 2026-10-24 | 265 | `3cea69c5a49b41db4e30f6f09adb3c95648afaad2124f69d697fee43af9d0475` |
| [cldr-47.0.0/cldr-misc-full/main/sr/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/sr/delimiters.json) | 2026-10-24 | 267 | `8470817dd5544daa06c5f030cfb31dd9842b52fc44825844153f9d6e2b883266` |
| [cldr-47.0.0/cldr-misc-full/main/th/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/th/delimiters.json) | 2026-10-24 | 267 | `0023ed0aa9d41c0466ebaf0cc4267c11ae8912f274143d6e7c6489ef048b32fe` |
| [cldr-47.0.0/cldr-misc-full/main/tr/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/tr/delimiters.json) | 2026-10-24 | 267 | `468d8c8b30fd987d857bdba0bcda2ca30b3a17e4f60dde75af480376fa159d64` |
| [cldr-47.0.0/cldr-misc-full/main/uk/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/uk/delimiters.json) | 2026-10-24 | 265 | `9779da602c2fa68078607fe2a2d13f4b792f12cc0dd9dc32d09c3ccfe803c656` |
| [cldr-47.0.0/cldr-misc-full/main/vi/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/vi/delimiters.json) | 2026-10-24 | 267 | `2c74ef507b4874108962d74eae5e386f84b6895f4aa7bf48830f2ce361f2f05d` |
| [cldr-47.0.0/cldr-misc-full/main/zh-Hant/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/zh-Hant/delimiters.json) | 2026-10-24 | 298 | `08aa133e037b7e7fc6ddb7f3607e8481ccd8b20de041c47e79eafe2dbe6540c9` |
| [cldr-47.0.0/cldr-misc-full/main/zh/delimiters.json](https://cdn.jsdelivr.net/npm/cldr-misc-full@47.0.0/main/zh/delimiters.json) | 2026-10-24 | 267 | `70d8c955b62e4c94f36aafd9d015a0154fad3a11bf7fc4e82b241ecb8876cb16` |
| [ldnoobw-5faf2ba42d7b/en](https://raw.githubusercontent.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words/5faf2ba42d7b1c0977169ec3611df25a3c08eb13/en) | 2026-10-24 | 3777 | `af851ecef1d5f212caba17339b12ac39cc2fef7d78c74876f67237644fcee8bd` |
| [tatoeba/eng_sentences.tsv.bz2](https://downloads.tatoeba.org/exports/per_language/eng/eng_sentences.tsv.bz2) | 2026-10-24 | 24918676 | `f634eaaf26623c2a2e170be2dae54b18388479311ae8619d04889df4e5d021f3` |
| wikidata/cities-US-100000-5dc2183a6c63.json (Wikidata Query Service) | 2026-10-24 | 92466 | `8f1b00feb15e705b8e4d902889993213c1fcb3a072b20ce339db47b6f8ed3c91` |
| wikidata/city-labels-US-en-612bee56effd.json (Wikidata Query Service) | 2026-10-24 | 119226 | `93d484df82180cf0dd916ca68d99e747f7f0ca23b8d9f165c93e53fbdf506a4c` |
| wikidata/countries-la-c7530040f26b.json (Wikidata Query Service) | 2026-10-24 | 49396 | `7713e175339f68f3b240a8bc7b6a57884afaec221f5eb054cf3447fc79a9b32e` |
