package dev.taihei.mihon

import java.time.*

import zio.test.*

import dev.taihei.tsuji.Language

object SeededSpec extends ZIOSpecDefault:

  private val noon  = Instant.parse("2026-10-24T12:00:00Z")
  private val clock = Clock.fixed(noon, ZoneOffset.UTC)

  // A run of draws in one language, so a test compares whole sequences and not one value.
  private def run(mihon: Mihon): List[String] =
    val p = mihon.person
    List(
      mihon.sentence,
      p.fullName,
      p.latin,
      p.reading.getOrElse("-"),
      mihon.email,
      mihon.address.singleLine,
      mihon.phone,
      mihon.title,
      mihon.word,
    )

  // The values of seed 42 in each language in this version. They are the same on the JVM and on
  // Scala.js, which this test proves by running on both. A change to the data or to how a
  // generator draws changes them: update them in that change, and say so in the release notes.
  private val golden: Map[String, List[String]] = Map(
    "en" -> List(
      "Date still as somebody extremely other pick brother go this clothes life?",
      "Virginia Wood",
      "Virginia Wood",
      "-",
      "charles.white@lunarbytefusion.example",
      "180 Cedar Lane, Chicago, Illinois 77233, United States",
      "+1 737 555-0154",
      "Father City Soup",
      "and",
    ),
    "la" -> List(
      "Quas rutrum ferri amet eloquentiam sanctus lorem mollis referrentur habitasse graecis alterum?",
      "Publia Sulpicia",
      "Publia Sulpicia",
      "-",
      "gnaeus.tullius@lunarbytefusion.example",
      "Clivus Pompeia 13, 00120 Civitas Vaticana, Civitas Vaticana",
      "+39 06 69833495",
      "Suavitate vituperata noluisse efficiantur",
      "inceptos",
    ),
  )

  def spec = suite("SeededSpec")(
    test("every language has its golden run") {
      assertTrue(golden.keySet == Mihon.languages.map(_.tag).toSet)
    },
    suite("seed 42 gives these values, on every platform")(
      golden.toSeq.sortBy(_._1).map((tag, values) =>
        test(tag)(assertTrue(run(Mihon.seeded(42L, Language(tag), clock)) == values))
      )*
    ),
    test("the same seed and language give the same values") {
      assertTrue(run(Mihon.seeded(7L, Language("la"))) == run(Mihon.seeded(7L, Language("la"))))
    },
    test("another seed gives other values") {
      assertTrue(run(Mihon.seeded(7L)) != run(Mihon.seeded(8L)))
    },
    test("secure generators do not repeat each other") {
      assertTrue(run(Mihon.secure()) != run(Mihon.secure()))
    },
    test("withLanguage shares the generator: the views draw one sequence between them") {
      val one   = Mihon.seeded(5L, clock = clock)
      val other = one.withLanguage(Language("la"))
      val mixed = List(one.int(1000), other.int(1000), one.int(1000))
      val plain = Mihon.seeded(5L, clock = clock)
      assertTrue(
        other.language == Language("la"),
        one.language == Language.English,
        other.today == one.today,
        mixed == List(plain.int(1000), plain.int(1000), plain.int(1000)),
      )
    },
    test("a seeded run does not depend on which language the generator started in") {
      val a = Mihon.seeded(9L).withLanguage(Language("la"))
      val b = Mihon.seeded(9L, Language("la"))
      assertTrue(run(a) == run(b))
    },
  )

end SeededSpec
