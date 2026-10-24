"""Builds the data files of the given languages (all when none is given):

    python3 scripts/data/build.py [tag ...]

It writes shared/resources/dev/taihei/mihon/data/<tag>/ and languages.json, and records every
download in scripts/data/sources.lock.json. The build never runs it."""

import pathlib
import sys

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parent))

import cldr  # noqa: E402
import formats  # noqa: E402
import languages  # noqa: E402
import names  # noqa: E402
import places  # noqa: E402
import text  # noqa: E402
import unihan  # noqa: E402
from common import DATA, write_json  # noqa: E402

HERE = pathlib.Path(__file__).resolve().parent


# Tatoeba files Mandarin in both standards, and Serbian in both scripts: the sentences a language
# counts its words in.
SENTENCES = {
    "zh-Hans": unihan.is_simplified,
    "zh-Hant": unihan.is_traditional,
    "sr-Cyrl": lambda s: any("\u0400" <= c <= "\u04ff" for c in s) and not any("a" <= c.lower() <= "z" for c in s),
}


def lorem_words():
    lines = (HERE / "lorem.txt").read_text(encoding="utf-8").splitlines()
    return [[w, 1] for w in lines if w and not w.startswith("#")]


# Wikidata names some Thai cities by their municipality ("city municipality of"), and lists
# districts and provinces among them.
CITY_PREFIXES = {"th": ["เทศบาลนคร", "เทศบาลเมือง"]}
NOT_CITIES = {"th": ["เขต", "จังหวัด", "อำเภอ", "แขวง"]}
# And some Japanese metropolitan areas and regions, which the settlement filter lets through, and
# the prefecture of Tokyo besides the city.
NOT_CITY_ENDINGS = {"ja": ["圏", "地域", "東京都"]}


def city_names(tag, cities):
    out, seen = [], set()
    for c in cities:
        name = c["name"]
        if any(name.startswith(p) for p in NOT_CITIES.get(tag, [])):
            continue
        if any(name.endswith(e) for e in NOT_CITY_ENDINGS.get(tag, [])):
            continue
        for p in CITY_PREFIXES.get(tag, []):
            if name.startswith(p) and len(name) > len(p):
                name = name[len(p):]
        if name not in seen:
            seen.add(name)
            out.append(dict(c, name=name))
    return out


def city_ok(script):
    def ok(label):
        if any(c in label for c in "–()0123456789,/"):
            return False
        letters = label.replace(" ", "").replace("-", "").replace("'", "").replace("’", "").replace(".", "")
        return bool(letters) and text.in_script(letters, script if script != "Latn" else "Latn")
    return ok


def build_names(tag, conf):
    if tag == "en":
        return names.census()
    if tag == "la":
        return names.latin()
    import wikinames
    return wikinames.names(tag, conf)


def build(tag):
    conf = languages.conf(tag)
    out = DATA / tag
    country = conf["country"]

    n = build_names(tag, conf)
    all_names = {e["name"] for e in n["female"] + n["male"]} | {e["name"] for e in n["family"]} | \
        {e["female"]["name"] for e in n["family"] if "female" in e}
    write_json(out / "names.json", {
        "order": conf["order"], "separator": conf["name_sep"],
        "female": n["female"], "male": n["male"], "family": n["family"],
    })

    words = lorem_words() if tag == "la" else text.words(tag, conf, all_names, SENTENCES.get(tag))
    cased = conf["script"] in text.CASED
    quotes = conf["quotes"] or cldr.quotes(conf["cldr"])
    write_json(out / "text.json", {
        "wordSeparator": conf["word_sep"], "sentenceSeparator": conf["sentence_sep"],
        "fullStop": conf["full_stop"], "questionOpen": conf["question_open"], "questionMark": conf["question"],
        "exclamationOpen": conf["exclamation_open"], "exclamationMark": conf["exclamation"],
        "comma": conf["comma"], "quoteOpen": quotes[0], "quoteClose": quotes[1],
        "cased": cased, "titleCase": conf["title_case"],
        "capitals": text.capitals(tag, [w for w, _ in words]) if cased else [],
        "wordsPerSentence": conf["words_per_sentence"], "words": words,
    })

    en_codes = cldr.territories("en")
    if tag == "la":
        names_by_code = cldr.latin_territories(set(en_codes))
    else:
        names_by_code = cldr.territories(conf["cldr"])
    write_json(out / "countries.json", [[c, names_by_code[c]] for c in sorted(names_by_code)])

    if tag == "la":
        cities = [{"name": "Civitas Vaticana", "weight": 1}]
    else:
        cities = city_names(tag, places.cities(country, languages.COUNTRY_QID[country], conf["wikidata"],
                                               city_ok(conf["script"]), limit=140))[:100]
    fmt = formats.COUNTRIES[country]
    write_json(out / f"country-{country}.json", {
        "code": country, "lines": fmt["lines"], "separator": fmt["separator"],
        "postalCode": fmt["postalCode"], "regions": places.regions(cities),
        "cities": cities, "streets": fmt["streets"], "phone": fmt["phone"],
        "fictionalPhone": fmt["fictionalPhone"],
    })
    sizes = sum(p.stat().st_size for p in out.iterdir())
    print(f"{tag}: {len(words)} words, {len(n['female'])}+{len(n['male'])} given, {len(n['family'])} family, "
          f"{len(cities)} cities, {sizes} bytes")


def main(tags):
    for tag in tags or list(languages.LANGUAGES):
        build(tag)
    present = sorted(t for t in languages.LANGUAGES if (DATA / t).is_dir())
    write_json(DATA / "languages.json", {
        "languages": [{"tag": t, "script": languages.conf(t)["script"], "country": languages.conf(t)["country"]}
                      for t in present],
        "aliases": [a for a in languages.ALIASES if a[1] in present],
    })


if __name__ == "__main__":
    main(sys.argv[1:])
