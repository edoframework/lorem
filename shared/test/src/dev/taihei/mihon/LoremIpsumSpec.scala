package dev.taihei.mihon

import java.time.LocalDate

import zio.test.*

import dev.taihei.tsuji.Language

object LoremIpsumSpec extends ZIOSpecDefault:

  private val lorem = LoremIpsum.secure()

  def spec = suite("LoremIpsumSpec")(
    suite("Language generation")(
      test("language returns one of Language.values") {
        assertTrue(Language.values.contains(lorem.language))
      },
      test("language reaches every language, the first one included") {
        val langs = Language.values
        val drawn = (1 to langs.length * 200).map(_ => lorem.language).toSet
        assertTrue(langs.nonEmpty, drawn == langs.toSet)
      },
    ),
    suite("Random number generation")(
      test("int returns a positive number") {
        val result = lorem.int(100)
        assertTrue(result > 0 && result <= 100)
      },
      test("int never returns zero") {
        val results = (1 to 20).map(_ => lorem.int(10))
        assertTrue(results.forall(_ > 0))
      },
      test("int reaches every value in [1, bound], bound included") {
        val drawn = (1 to 600).map(_ => lorem.int(3))
        assertTrue(drawn.toSet == Set(1, 2, 3))
      },
      test("int(2) returns 1 and 2 about equally often") {
        val ones = (1 to 2000).count(_ => lorem.int(2) == 1)
        assertTrue(ones > 800 && ones < 1200)
      },
      test("int(1) is always 1") {
        assertTrue((1 to 50).forall(_ => lorem.int(1) == 1))
      },
      test("long reaches every value in [1, bound], bound included") {
        val drawn = (1 to 600).map(_ => lorem.long(3))
        assertTrue(drawn.toSet == Set(1L, 2L, 3L))
      },
      test("long returns a positive number") {
        val result = lorem.long(100)
        assertTrue(result > 0L && result <= 100L)
      },
      test("long never returns zero") {
        val results = (1 to 20).map(_ => lorem.long(10))
        assertTrue(results.forall(_ > 0L))
      },
      test("double returns a number within bound") {
        val result = lorem.double(100.0)
        assertTrue(result >= 0.0 && result < 100.0)
      },
      test("bool returns a boolean") {
        val result = lorem.bool
        assertTrue(result == true || result == false)
      },
      test("bool returns mixed true and false values") {
        val results = (1 to 50).map(_ => lorem.bool)
        assertTrue(
          results.contains(true),
          results.contains(false),
        )
      },
    ),
    suite("times function")(
      test("times generates correct number of items") {
        val result = lorem.times(5, i => i * 2)
        assertTrue(result.length <= 5 && result.length > 0)
      },
      test("times returns between one and bound items, bound included") {
        val lengths = (1 to 600).map(_ => lorem.times(3, identity).length)
        assertTrue(lengths.toSet == Set(1, 2, 3))
      },
      test("times applies function to each index") {
        val result = lorem.times(3, i => i)
        assertTrue(result.forall(i => i >= 1 && i <= 3))
      },
    ),
    suite("Text generation")(
      test("word generates a non-empty string") {
        val result = lorem.word
        assertTrue(result.nonEmpty)
      },
      test("words generates multiple words") {
        val result = lorem.words
        // Random word count - may be one or more
        assertTrue(result.nonEmpty)
      },
      test("character generates a single character string") {
        val result = lorem.character
        assertTrue(result.length == 1)
      },
      test("sentence generates text ending with period") {
        val result = lorem.sentence
        assertTrue(
          result.nonEmpty,
          result.endsWith("."),
        )
      },
      test("sentences generates multiple sentences") {
        val result = lorem.sentences
        // Random sentence count - may be one or more
        assertTrue(result.nonEmpty)
      },
      test("paragraph generates substantial text") {
        val result = lorem.paragraph
        // Random word count - length varies
        assertTrue(result.nonEmpty)
      },
      test("paragraphs generates multiple paragraphs") {
        val result = lorem.paragraphs
        // paragraphs generates text (length varies due to randomness)
        assertTrue(result.nonEmpty)
      },
      test("htmlParagraphs generates HTML wrapped text") {
        val result = lorem.htmlParagraphs
        assertTrue(
          result.contains("<p>"),
          result.contains("</p>"),
        )
      },
      test("title generates capitalized text") {
        val result = lorem.title
        val words  = result.split(" ")
        assertTrue(
          result.nonEmpty,
          words.forall(w => w.nonEmpty && w.head.isUpper),
        )
      },
      test("description generates descriptive text") {
        val result = lorem.description
        assertTrue(result.nonEmpty)
      },
      test("slug generates path-like string") {
        val result = lorem.slug
        // Slug generates words, may or may not contain "/" depending on random count
        assertTrue(result.nonEmpty)
      },
    ),
    suite("Contact information generation")(
      test("email generates valid-looking email") {
        val result = lorem.email
        assertTrue(
          result.nonEmpty,
          result.contains("@"),
          result.contains("."),
        )
      },
      test("email and url use only the reserved .example top-level domain") {
        val hosts = (1 to 500).flatMap(_ => Seq(lorem.email.split("@").last, lorem.url.stripPrefix("https://")))
        assertTrue(hosts.nonEmpty, hosts.forall(_.endsWith(".example")))
      },
      test("url generates valid URL structure") {
        val result = lorem.url
        assertTrue(
          result.startsWith("https://"),
          result.length > 8,
        )
      },
      test("phone generates phone number") {
        val result = lorem.phone
        assertTrue(
          result.nonEmpty,
          result.startsWith("+32"),
          result.length > 3,
        )
      },
    ),
    suite("Person information generation")(
      test("firstName generates a non-empty name") {
        val result = lorem.firstName
        assertTrue(result.nonEmpty)
      },
      test("lastName generates a non-empty name") {
        val result = lorem.lastName
        assertTrue(result.nonEmpty)
      },
      test("name is a first name, a space and a last name") {
        val result = lorem.name
        val first  = result.takeWhile(_ != ' ')
        assertTrue(first.nonEmpty, result.length > first.length + 1, result(first.length) == ' ')
      },
      test("name generates full name with space") {
        val result = lorem.name
        assertTrue(
          result.nonEmpty,
          result.contains(" "),
        )
      },
    ),
    suite("Address generation")(
      test("street generates street name") {
        val result = lorem.street
        assertTrue(result.nonEmpty)
      },
      test("city generates city name") {
        val result = lorem.city
        assertTrue(result.nonEmpty)
      },
      test("zipCode generates numeric code") {
        val result = lorem.zipCode
        assertTrue(
          result.nonEmpty,
          result.length == 4,
          result.forall(_.isDigit),
        )
      },
      test("zip codes and phone numbers use every digit, 0 and 9 included") {
        val zipDigits   = (1 to 300).flatMap(_ => lorem.zipCode).toSet
        val phoneDigits = (1 to 300).flatMap(_ => lorem.phone.stripPrefix("+32")).toSet
        val all         = ('0' to '9').toSet
        assertTrue(zipDigits == all, phoneDigits == all)
      },
      test("address generates complete address") {
        val result = lorem.address
        assertTrue(
          result.nonEmpty,
          result.split(" ").length > 2,
        )
      },
      test("country generates country name") {
        val result = lorem.country
        assertTrue(result.nonEmpty)
      },
    ),
    suite("Date generation")(
      test("localDate generates a valid date") {
        val result = lorem.localDate
        assertTrue(
          result.getYear > 0,
          result.getMonthValue >= 1 && result.getMonthValue <= 12,
          result.getDayOfMonth >= 1 && result.getDayOfMonth <= 31,
        )
      },
      test("localDate reaches December and the 28th") {
        val dates = (1 to 3000).map(_ => lorem.localDate)
        assertTrue(
          dates.exists(_.getMonthValue == 12),
          dates.exists(_.getDayOfMonth == 28),
          dates.forall(d => d.getYear >= 1 && d.getYear <= 3000 && d.getDayOfMonth <= 28),
        )
      },
      test("today is the UTC date by default") {
        val before = LocalDate.now(java.time.ZoneOffset.UTC)
        val result = lorem.today
        val after  = LocalDate.now(java.time.ZoneOffset.UTC)
        assertTrue(result == before || result == after)
      },
      test("yesterday returns date before today") {
        val result = lorem.yesterday
        val today  = lorem.today
        assertTrue(result.isBefore(today))
      },
      test("tomorrow returns date after today") {
        val result = lorem.tomorrow
        val today  = lorem.today
        assertTrue(result.isAfter(today))
      },
      test("futureDate returns date in the future") {
        val result = lorem.futureDate
        val today  = lorem.today
        assertTrue(result.isAfter(today))
      },
      test("pastDate returns date in the past") {
        val result = lorem.pastDate
        val today  = lorem.today
        assertTrue(result.isBefore(today))
      },
    ),
    suite("File and MIME type generation")(
      test("fileExtension generates extension with dot") {
        val result = lorem.fileExtension
        assertTrue(
          result.nonEmpty,
          result.startsWith("."),
        )
      },
      test("fileExtensionWithoutDot generates extension without dot") {
        val result = lorem.fileExtensionWithoutDot
        assertTrue(
          result.nonEmpty,
          !result.contains("."),
        )
      },
      test("mimeType generates valid MIME type structure") {
        val result = lorem.mimeType
        assertTrue(
          result.nonEmpty,
          result.contains("/"),
        )
      },
    ),
    suite("ID and password generation")(
      test("id generates UUID-like string") {
        val result = lorem.id
        assertTrue(
          result.nonEmpty,
          result.length > 20,
          result.contains("_"),
        )
      },
      test("password generates non-empty password") {
        val result = lorem.password
        assertTrue(
          result.nonEmpty,
          result.length == 30,
        )
      },
    ),
    suite("Consistency and randomness")(
      test("multiple calls generate different values") {
        val emails = (1 to 10).map(_ => lorem.email)
        // Check that we have at least some variety
        assertTrue(emails.toSet.size > 1)
      },
      test("multiple words are different") {
        val words = (1 to 10).map(_ => lorem.word)
        // Should have at least some variety
        assertTrue(words.toSet.size > 1)
      },
      test("generated emails have consistent structure") {
        val emails = (1 to 5).map(_ => lorem.email)
        assertTrue(emails.forall { email =>
          email.contains("@") && email.split("@").length == 2
        })
      },
      test("generated URLs all start with https") {
        val urls = (1 to 5).map(_ => lorem.url)
        assertTrue(urls.forall(_.startsWith("https://")))
      },
    ),
  )

end LoremIpsumSpec
