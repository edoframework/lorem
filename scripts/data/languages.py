"""The 25 languages of Mihon 5.0.0 and the conventions of each, written for this library.

Punctuation, word and sentence separators and name order are facts of each writing system; the
quotation marks come from CLDR (cldr.py) unless `quotes` overrides them. `country` is CLDR's
likely region for the language (likelySubtags)."""

NNBSP = " "  # narrow no-break space, French typography

# Wikidata items of the countries.
COUNTRY_QID = {
    "US": "Q30", "NL": "Q55", "FR": "Q142", "DE": "Q183", "ES": "Q29", "IT": "Q38", "BR": "Q155",
    "PL": "Q36", "TR": "Q43", "VN": "Q881", "HU": "Q28", "VA": "Q237", "RU": "Q159", "UA": "Q212",
    "BG": "Q219", "RS": "Q403", "GR": "Q41", "EG": "Q79", "IL": "Q801", "IN": "Q668", "TH": "Q869",
    "CN": "Q148", "TW": "Q865", "JP": "Q17", "KR": "Q884",
}

DEFAULT = dict(
    word_sep=" ", sentence_sep=" ", full_stop=".", question_open="", question="?",
    exclamation_open="", exclamation="!", comma=",", quotes=None, title_case=False,
    words_per_sentence=[4, 12], order="given-family", name_sep=" ",
)

CJK = dict(word_sep="", sentence_sep="", full_stop="。", question="？", exclamation="！",
           comma="，", words_per_sentence=[6, 16], order="family-given", name_sep="")

LANGUAGES = {
    "en": dict(script="Latn", country="US", tatoeba="eng", wikidata="en", title_case=True),
    "nl": dict(script="Latn", country="NL", tatoeba="nld", wikidata="nl"),
    "fr": dict(script="Latn", country="FR", tatoeba="fra", wikidata="fr",
               question=NNBSP + "?", exclamation=NNBSP + "!", quotes=["«" + NNBSP, NNBSP + "»"]),
    "de": dict(script="Latn", country="DE", tatoeba="deu", wikidata="de"),
    "es": dict(script="Latn", country="ES", tatoeba="spa", wikidata="es", question_open="¿",
               exclamation_open="¡"),
    "it": dict(script="Latn", country="IT", tatoeba="ita", wikidata="it"),
    "pt": dict(script="Latn", country="BR", tatoeba="por", wikidata="pt"),
    "pl": dict(script="Latn", country="PL", tatoeba="pol", wikidata="pl"),
    "tr": dict(script="Latn", country="TR", tatoeba="tur", wikidata="tr"),
    "vi": dict(script="Latn", country="VN", tatoeba="vie", wikidata="vi", order="family-given"),
    "hu": dict(script="Latn", country="HU", tatoeba="hun", wikidata="hu", order="family-given"),
    "la": dict(script="Latn", country="VA", tatoeba=None, wikidata="la", quotes=["“", "”"]),
    "ru": dict(script="Cyrl", country="RU", tatoeba="rus", wikidata="ru"),
    "uk": dict(script="Cyrl", country="UA", tatoeba="ukr", wikidata="uk"),
    "bg": dict(script="Cyrl", country="BG", tatoeba="bul", wikidata="bg"),
    "sr-Cyrl": dict(script="Cyrl", country="RS", tatoeba="srp", wikidata="sr", cldr="sr"),
    "el": dict(script="Grek", country="GR", tatoeba="ell", wikidata="el", question=";"),
    "ar": dict(script="Arab", country="EG", tatoeba="ara", wikidata="ar", question="؟", comma="،"),
    "he": dict(script="Hebr", country="IL", tatoeba="heb", wikidata="he"),
    "hi": dict(script="Deva", country="IN", tatoeba="hin", wikidata="hi", full_stop="।"),
    # Thai writes no space between words and no final stop, and a space between sentences; it has
    # no comma of its own.
    "th": dict(script="Thai", country="TH", tatoeba="tha", wikidata="th", full_stop="",
               question="", exclamation="", comma="", word_sep="", words_per_sentence=[6, 16]),
    "zh-Hans": dict(CJK, script="Hans", country="CN", tatoeba="cmn", wikidata="zh-hans", cldr="zh"),
    "zh-Hant": dict(CJK, script="Hant", country="TW", tatoeba="cmn", wikidata="zh-hant", cldr="zh-Hant"),
    "ja": dict(CJK, script="Jpan", country="JP", tatoeba="jpn", wikidata="ja", comma="、"),
    # Korean separates words with spaces, family name first and no space within the name.
    "ko": dict(script="Kore", country="KR", tatoeba="kor", wikidata="ko", order="family-given", name_sep=""),
}

ALIASES = [
    ["sr", "sr-Cyrl"], ["sr-RS", "sr-Cyrl"],
    ["zh", "zh-Hans"], ["zh-CN", "zh-Hans"], ["zh-SG", "zh-Hans"],
    ["zh-HK", "zh-Hant"], ["zh-MO", "zh-Hant"], ["zh-TW", "zh-Hant"],
]


def conf(tag):
    c = dict(DEFAULT)
    c.update(LANGUAGES[tag])
    c.setdefault("cldr", tag)
    return c
