package dev.taihei.mihon.data

import zio.json.*

// The JSON files under dev/taihei/mihon/data/, as they are written. build.mill checks the same
// shape when it builds (EmbeddedData.check), so a decoding failure here is a packaging bug.
// Fields are documented in docs/skill/reference/internals.md and scripts/data/README.md.

// languages.json
final private[mihon] case class LanguageEntry(
  tag: String,
  script: String,
  country: String)

final private[mihon] case class LanguagesFile(
  languages: List[LanguageEntry],
  aliases: List[(String, String)])

// <tag>/text.json
final private[mihon] case class TextFile(
  wordSeparator: String,
  sentenceSeparator: String,
  fullStop: String,
  questionOpen: String,
  questionMark: String,
  exclamationOpen: String,
  exclamationMark: String,
  comma: String,
  quoteOpen: String,
  quoteClose: String,
  cased: Boolean,
  titleCase: Boolean,
  capitals: List[(String, String)],
  wordsPerSentence: (Int, Int),
  words: List[(String, Int)])

// <tag>/names.json: a name in its own script, romanised (`latin`, absent when the name is in
// Latin script), reduced to ASCII for e-mail addresses (`ascii`, absent when `latin` is ASCII
// already), and its kana reading (`reading`, Japanese only).
final private[mihon] case class FormEntry(
  name: String,
  latin: Option[String],
  ascii: Option[String],
  reading: Option[String])

final private[mihon] case class GivenEntry(
  name: String,
  weight: Int,
  latin: Option[String],
  ascii: Option[String],
  reading: Option[String])

final private[mihon] case class FamilyEntry(
  name: String,
  weight: Int,
  latin: Option[String],
  ascii: Option[String],
  reading: Option[String],
  female: Option[FormEntry])

final private[mihon] case class NamesFile(
  order: String,
  separator: String,
  female: List[GivenEntry],
  male: List[GivenEntry],
  family: List[FamilyEntry])

// <tag>/country-<CC>.json
final private[mihon] case class CityEntry(
  name: String,
  weight: Int,
  region: Option[String])

final private[mihon] case class CountryFile(
  code: String,
  lines: List[String],
  separator: String,
  postalCode: String,
  regions: List[(String, Int)],
  cities: List[CityEntry],
  streets: List[String],
  phone: String,
  fictionalPhone: Boolean)

private[mihon] object Files:
  given JsonDecoder[LanguageEntry] = DeriveJsonDecoder.gen[LanguageEntry]
  given JsonDecoder[LanguagesFile] = DeriveJsonDecoder.gen[LanguagesFile]
  given JsonDecoder[TextFile] = DeriveJsonDecoder.gen[TextFile]
  given JsonDecoder[FormEntry] = DeriveJsonDecoder.gen[FormEntry]
  given JsonDecoder[GivenEntry] = DeriveJsonDecoder.gen[GivenEntry]
  given JsonDecoder[FamilyEntry] = DeriveJsonDecoder.gen[FamilyEntry]
  given JsonDecoder[NamesFile] = DeriveJsonDecoder.gen[NamesFile]
  given JsonDecoder[CityEntry] = DeriveJsonDecoder.gen[CityEntry]
  given JsonDecoder[CountryFile] = DeriveJsonDecoder.gen[CountryFile]

  // Countries: [code, name] pairs, sorted by code.
  type CountriesFile = List[(String, String)]
