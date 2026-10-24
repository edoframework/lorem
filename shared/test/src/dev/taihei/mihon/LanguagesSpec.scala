package dev.taihei.mihon

import zio.test.*

import dev.taihei.tsuji.Language

// The conventions of each language Mihon has data for: its script, punctuation, name order,
// address and phone number. Every language in Mihon.languages must have an entry, so a language
// cannot be added without its tests.
object LanguagesSpec extends ZIOSpecDefault:

  // Code point ranges, inclusive, of the scripts. Explicit, so the check does not depend on a
  // platform's Unicode tables (Character.UnicodeScript is not on Scala.js).
  private val Latin = Seq(0x41 -> 0x5a, 0x61 -> 0x7a, 0xc0 -> 0x24f, 0x300 -> 0x36f, 0x1e00 -> 0x1eff)

  // What a language's output may hold.
  final case class Conventions(
    letters: Seq[(Int, Int)],
    // The marks a sentence may hold besides letters, and which of them must show up.
    marks: Set[String],
    required: Set[String],
    spaced: Boolean,
    familyFirst: Boolean,
    nameSeparator: String,
    country: String,
    postalCode: String,
    phone: String)

  private val west = Set(".", "?", "!", ",")

  private def latin(
    country: String,
    postal: String,
    phone: String,
    quotes: Set[String],
    familyFirst: Boolean = false,
  ) =
    Conventions(Latin, west ++ quotes, west ++ quotes, true, familyFirst, " ", country, postal, phone)

  private val conventions: Map[String, Conventions] = Map(
    "en" -> latin("US", "[0-9]{5}", "\\+1 [1-9][0-9]{2} 555-01[0-9]{2}", Set("“", "”")),
    "la" -> latin("VA", "00120", "\\+39 06 698[0-9]{5}", Set("“", "”")),
  )

  private def codePoints(s: String): Seq[Int] =
    var i   = 0
    val out = Seq.newBuilder[Int]
    while i < s.length do
      val cp = s.codePointAt(i)
      out += cp
      i += Character.charCount(cp)
    out.result()

  private def inRanges(cp: Int, ranges: Seq[(Int, Int)]) = ranges.exists((lo, hi) => cp >= lo && cp <= hi)

  // Every code point a letter of the script, or one of the marks, or a space where the language
  // writes one.
  private def written(
    s: String,
    c: Conventions,
    extra: Set[String] = Set(),
  ) =
    val allowed = (c.marks ++ extra).flatMap(codePoints)
    codePoints(s).forall(cp => inRanges(cp, c.letters) || allowed(cp) || (cp == ' ' && c.spaced))

  private def generator(tag: String) = Mihon.seeded(2026L, Language(tag))

  private def perLanguage(tag: String, c: Conventions) =
    suite(tag)(
      test("sentences are in the script, with the language's marks, each of which shows up") {
        val mihon     = generator(tag)
        val sentences = (1 to 600).map(_ => mihon.sentence)
        val all       = sentences.mkString
        assertTrue(
          sentences.forall(s => written(s, c)),
          c.required.forall(all.contains),
          sentences.forall(_.contains(' ') == c.spaced),
        )
      },
      test("names are in the script, in the language's order") {
        val mihon  = generator(tag)
        val people = (1 to 300).map(_ => mihon.person)
        assertTrue(
          people.forall { p =>
            val ordered =
              if c.familyFirst then s"${p.familyName}${c.nameSeparator}${p.givenName}"
              else s"${p.givenName}${c.nameSeparator}${p.familyName}"
            p.fullName == ordered && written(p.fullName, c, Set(" ", "-", "'", "’"))
          },
          people.forall(p => p.latin.nonEmpty && p.latin.forall(ch => ch < 0x2000)),
        )
      },
      test("e-mail addresses are ASCII") {
        val mihon = generator(tag)
        assertTrue((1 to 200).forall(_ => mihon.email.matches("[a-z0-9._-]+@[a-z0-9-]+\\.example")))
      },
      test("addresses are those of the default country, and phone numbers too") {
        val mihon     = generator(tag)
        val addresses = (1 to 100).map(_ => mihon.address)
        assertTrue(
          addresses.forall(_.countryCode == c.country),
          addresses.forall(_.postalCode.matches(c.postalCode)),
          addresses.forall(a => a.lines.nonEmpty && a.lines.forall(_.nonEmpty)),
          addresses.forall(a => a.lines.exists(_.contains(a.city)) && a.lines.exists(_.contains(a.street))),
          (1 to 100).forall(_ => mihon.phone.matches(c.phone)),
        )
      },
    )

  def spec = suite("LanguagesSpec")(
    test("every language has its conventions here") {
      val tags = Mihon.languages.map(_.tag)
      assertTrue(tags.nonEmpty, tags.toSet == conventions.keySet)
    },
    test("languages are sorted by tag") {
      val tags = Mihon.languages.map(_.tag)
      assertTrue(tags == tags.sorted)
    },
    test("a tag with a region is served by its language") {
      assertTrue(
        Mihon.resolve(Language("en-GB")) == Some(Language.English),
        Mihon.resolve(Language("la-VA")) == Some(Language("la")),
        Mihon.seeded(1L, Language("en-US")).language == Language.English,
        Mihon.resolve(Language("en-u-ca-gregory")) == Some(Language.English),
      )
    },
    test("a language without data is refused, by resolve and by the factories, naming the languages") {
      val refused = scala.util.Try(Mihon.seeded(1L, Language("sw"))).failed.toOption
      assertTrue(
        Mihon.resolve(Language("sw")).isEmpty,
        refused.exists(_.isInstanceOf[IllegalArgumentException]),
        refused.exists(e => e.getMessage.contains("sw") && e.getMessage.contains("en, ")),
        scala.util.Try(Mihon.secure(Language("sw"))).isFailure,
        scala.util.Try(Mihon.seeded(1L).withLanguage(Language("sw"))).isFailure,
      )
    },
    test("a tag that names another script is refused") {
      assertTrue(Mihon.resolve(Language("en-Cyrl")).isEmpty, Mihon.resolve(Language("la-Grek")).isEmpty)
    },
    test("English is the default and never refused") {
      assertTrue(Mihon.seeded(1L).language == Language.English, Mihon.secure().language == Language.English)
    },
    test("anyLanguage reaches every language") {
      val mihon = Mihon.seeded(3L)
      val drawn = (1 to 2000).map(_ => mihon.anyLanguage).toSet
      assertTrue(drawn == Mihon.languages.toSet)
    },
    suite("per language")(conventions.toSeq.sortBy(_._1).map(perLanguage)*),
  )

end LanguagesSpec
