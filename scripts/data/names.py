"""Given and family names per language.

- en: the U.S. Census Bureau's 1990 first-name lists and 2010 surname list (17 U.S.C. 105).
- la: classical Roman praenomina and nomina, historical facts written here.
- the other languages: Wikidata (CC0)."""

import csv
import io
import zipfile

from common import fetch

CENSUS_FIRST = "https://www2.census.gov/topics/genealogy/1990surnames/dist.{}.first"
CENSUS_SURNAMES = "https://www2.census.gov/topics/genealogy/2010surnames/names.zip"


# The Census lists drop apostrophes: OBRIEN is O'Brien.
APOSTROPHE = {"OBRIEN": "O'Brien", "OCONNOR": "O'Connor", "ONEILL": "O'Neill", "ONEAL": "O'Neal",
              "ODONNELL": "O'Donnell", "OHARA": "O'Hara", "OKEEFE": "O'Keefe"}


def _title(upper):
    if upper in APOSTROPHE:
        return APOSTROPHE[upper]
    name = upper[:1] + upper[1:].lower()
    if name.startswith("Mc") and len(name) > 2:
        name = "Mc" + name[2].upper() + name[3:]
    return name


def census(limit_given=200, limit_family=300):
    def first(sex):
        path = fetch(f"census/dist.{sex}.first", CENSUS_FIRST.format(sex))
        out = []
        for line in path.read_text(encoding="ascii").splitlines()[:limit_given]:
            name, freq = line.split()[:2]
            out.append({"name": _title(name), "weight": max(1, round(float(freq) * 1000))})
        return out

    path = fetch("census/2010surnames-names.zip", CENSUS_SURNAMES)
    with zipfile.ZipFile(path) as z:
        member = next(n for n in z.namelist() if n.lower().endswith(".csv"))
        rows = list(csv.DictReader(io.TextIOWrapper(z.open(member), encoding="ascii")))
    family = []
    for row in rows:
        name = row["name"]
        if not name.isalpha():
            continue  # "ALL OTHER NAMES"
        title = _title(name)
        entry = {"name": title, "weight": max(1, int(row["count"]) // 1000)}
        if "'" in title:
            entry["ascii"] = title.replace("'", "")
        family.append(entry)
        if len(family) == limit_family:
            break
    return {"female": first("female"), "male": first("male"), "family": family}


# Praenomina, the given names of Romans, in their male and female forms.
LATIN_MALE = ["Gaius", "Lucius", "Marcus", "Publius", "Quintus", "Titus", "Tiberius", "Sextus",
              "Aulus", "Decimus", "Gnaeus", "Spurius", "Manius", "Servius", "Appius", "Numerius",
              "Kaeso", "Mamercus", "Vibius", "Postumus"]
LATIN_FEMALE = ["Gaia", "Lucia", "Marcia", "Publia", "Quinta", "Tita", "Tiberia", "Sexta", "Aula",
                "Decima", "Prima", "Secunda", "Tertia", "Maxima", "Paulla", "Vibia", "Postuma",
                "Caesula", "Rutila", "Numeria"]
# Nomina gentilicia, the family names, which a woman bore in the feminine.
LATIN_NOMINA = ["Iulius", "Cornelius", "Claudius", "Aemilius", "Valerius", "Fabius", "Tullius",
                "Antonius", "Octavius", "Livius", "Flavius", "Aurelius", "Domitius", "Iunius",
                "Licinius", "Sempronius", "Caecilius", "Pompeius", "Sulpicius", "Servilius",
                "Horatius", "Calpurnius", "Manlius", "Porcius", "Terentius", "Vipsanius", "Atilius",
                "Furius", "Marius", "Fulvius"]


def latin():
    return {
        "female": [{"name": n, "weight": 1} for n in LATIN_FEMALE],
        "male": [{"name": n, "weight": 1} for n in LATIN_MALE],
        "family": [{"name": n, "weight": 1, "female": {"name": n[:-2] + "a"}} for n in LATIN_NOMINA],
    }
