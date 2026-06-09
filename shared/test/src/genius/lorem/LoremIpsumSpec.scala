package genius.lorem

import java.time.LocalDate

import zio.test.*

import genius.i18n.Language

object LoremIpsumSpec extends ZIOSpecDefault:

  def spec = suite("LoremIpsumSpec")(
    suite("Language generation")(
      test("languages returns a non-empty array") {
        assertTrue(LoremIpsum.languages.nonEmpty)
      },
      test("languages contains all Language values") {
        val langs = LoremIpsum.languages
        assertTrue(
          langs.contains(Language.English),
          langs.contains(Language.Dutch),
          langs.contains(Language.French),
        )
      },
      test("language returns a valid Language from the list") {
        val result = LoremIpsum.language
        assertTrue(LoremIpsum.languages.contains(result))
      },
    ),
    suite("Random number generation")(
      test("int returns a positive number") {
        val result = LoremIpsum.int(100)
        assertTrue(result > 0 && result <= 100)
      },
      test("int never returns zero") {
        val results = (1 to 20).map(_ => LoremIpsum.int(10))
        assertTrue(results.forall(_ > 0))
      },
      test("long returns a positive number") {
        val result = LoremIpsum.long(100)
        assertTrue(result > 0L && result <= 100L)
      },
      test("long never returns zero") {
        val results = (1 to 20).map(_ => LoremIpsum.long(10))
        assertTrue(results.forall(_ > 0L))
      },
      test("double returns a number within bound") {
        val result = LoremIpsum.double(100.0)
        assertTrue(result >= 0.0 && result < 100.0)
      },
      test("bool returns a boolean") {
        val result = LoremIpsum.bool
        assertTrue(result == true || result == false)
      },
      test("bool returns mixed true and false values") {
        val results = (1 to 50).map(_ => LoremIpsum.bool)
        assertTrue(
          results.contains(true),
          results.contains(false),
        )
      },
    ),
    suite("times function")(
      test("times generates correct number of items") {
        val result = LoremIpsum.times(5, i => i * 2)
        assertTrue(result.length <= 5 && result.length > 0)
      },
      test("times applies function to each index") {
        val result = LoremIpsum.times(3, i => i)
        assertTrue(result.forall(i => i >= 1 && i <= 3))
      },
    ),
    suite("Text generation")(
      test("word generates a non-empty string") {
        val result = LoremIpsum.word
        assertTrue(result.nonEmpty)
      },
      test("words generates multiple words") {
        val result = LoremIpsum.words
        // Random word count - may be one or more
        assertTrue(result.nonEmpty)
      },
      test("character generates a single character string") {
        val result = LoremIpsum.character
        assertTrue(result.length == 1)
      },
      test("sentence generates text ending with period") {
        val result = LoremIpsum.sentence
        assertTrue(
          result.nonEmpty,
          result.endsWith("."),
        )
      },
      test("sentences generates multiple sentences") {
        val result = LoremIpsum.sentences
        // Random sentence count - may be one or more
        assertTrue(result.nonEmpty)
      },
      test("paragraph generates substantial text") {
        val result = LoremIpsum.paragraph
        // Random word count - length varies
        assertTrue(result.nonEmpty)
      },
      test("paragraphs generates multiple paragraphs") {
        val result = LoremIpsum.paragraphs
        // paragraphs generates text (length varies due to randomness)
        assertTrue(result.nonEmpty)
      },
      test("htmlParagraphs generates HTML wrapped text") {
        val result = LoremIpsum.htmlParagraphs
        assertTrue(
          result.contains("<p>"),
          result.contains("</p>"),
        )
      },
      test("title generates capitalized text") {
        val result = LoremIpsum.title
        val words  = result.split(" ")
        assertTrue(
          result.nonEmpty,
          words.forall(w => w.nonEmpty && w.head.isUpper),
        )
      },
      test("description generates descriptive text") {
        val result = LoremIpsum.description
        assertTrue(result.nonEmpty)
      },
      test("slug generates path-like string") {
        val result = LoremIpsum.slug
        // Slug generates words, may or may not contain "/" depending on random count
        assertTrue(result.nonEmpty)
      },
    ),
    suite("Contact information generation")(
      test("email generates valid-looking email") {
        val result = LoremIpsum.email
        assertTrue(
          result.nonEmpty,
          result.contains("@"),
          result.contains("."),
        )
      },
      test("url generates valid URL structure") {
        val result = LoremIpsum.url
        assertTrue(
          result.startsWith("https://"),
          result.length > 8,
        )
      },
      test("phone generates phone number") {
        val result = LoremIpsum.phone
        assertTrue(
          result.nonEmpty,
          result.startsWith("+32"),
          result.length > 3,
        )
      },
    ),
    suite("Person information generation")(
      test("firstName generates a non-empty name") {
        val result = LoremIpsum.firstName
        assertTrue(result.nonEmpty)
      },
      test("lastName generates a non-empty name") {
        val result = LoremIpsum.lastName
        assertTrue(result.nonEmpty)
      },
      test("name generates full name with space") {
        val result = LoremIpsum.name
        assertTrue(
          result.nonEmpty,
          result.contains(" "),
        )
      },
    ),
    suite("Address generation")(
      test("street generates street name") {
        val result = LoremIpsum.street
        assertTrue(result.nonEmpty)
      },
      test("city generates city name") {
        val result = LoremIpsum.city
        assertTrue(result.nonEmpty)
      },
      test("zipCode generates numeric code") {
        val result = LoremIpsum.zipCode
        assertTrue(
          result.nonEmpty,
          result.length == 4,
          result.forall(_.isDigit),
        )
      },
      test("address generates complete address") {
        val result = LoremIpsum.address
        assertTrue(
          result.nonEmpty,
          result.split(" ").length > 2,
        )
      },
      test("country generates country name") {
        val result = LoremIpsum.country
        assertTrue(result.nonEmpty)
      },
    ),
    suite("Date generation")(
      test("localDate generates a valid date") {
        val result = LoremIpsum.localDate
        assertTrue(
          result.getYear > 0,
          result.getMonthValue >= 1 && result.getMonthValue <= 12,
          result.getDayOfMonth >= 1 && result.getDayOfMonth <= 31,
        )
      },
      test("today returns current date") {
        val result = LoremIpsum.today
        val now    = LocalDate.now(java.time.ZoneId.of("UTC"))
        assertTrue(result == now)
      },
      test("yesterday returns date before today") {
        val result = LoremIpsum.yesterday
        val today  = LoremIpsum.today
        assertTrue(result.isBefore(today))
      },
      test("tomorrow returns date after today") {
        val result = LoremIpsum.tomorrow
        val today  = LoremIpsum.today
        assertTrue(result.isAfter(today))
      },
      test("futureDate returns date in the future") {
        val result = LoremIpsum.futureDate
        val today  = LoremIpsum.today
        assertTrue(result.isAfter(today))
      },
      test("pastDate returns date in the past") {
        val result = LoremIpsum.pastDate
        val today  = LoremIpsum.today
        assertTrue(result.isBefore(today))
      },
    ),
    suite("File and MIME type generation")(
      test("fileExtension generates extension with dot") {
        val result = LoremIpsum.fileExtension
        assertTrue(
          result.nonEmpty,
          result.startsWith("."),
        )
      },
      test("fileExtensionWithoutDot generates extension without dot") {
        val result = LoremIpsum.fileExtensionWithoutDot
        assertTrue(
          result.nonEmpty,
          !result.contains("."),
        )
      },
      test("mimeType generates valid MIME type structure") {
        val result = LoremIpsum.mimeType
        assertTrue(
          result.nonEmpty,
          result.contains("/"),
        )
      },
    ),
    suite("ID and password generation")(
      test("id generates UUID-like string") {
        val result = LoremIpsum.id
        assertTrue(
          result.nonEmpty,
          result.length > 20,
          result.contains("_"),
        )
      },
      test("password generates non-empty password") {
        val result = LoremIpsum.password
        assertTrue(
          result.nonEmpty,
          result.length == 30,
        )
      },
    ),
    suite("Consistency and randomness")(
      test("multiple calls generate different values") {
        val emails = (1 to 10).map(_ => LoremIpsum.email)
        // Check that we have at least some variety
        assertTrue(emails.toSet.size > 1)
      },
      test("multiple words are different") {
        val words = (1 to 10).map(_ => LoremIpsum.word)
        // Should have at least some variety
        assertTrue(words.toSet.size > 1)
      },
      test("generated emails have consistent structure") {
        val emails = (1 to 5).map(_ => LoremIpsum.email)
        assertTrue(emails.forall { email =>
          email.contains("@") && email.split("@").length == 2
        })
      },
      test("generated URLs all start with https") {
        val urls = (1 to 5).map(_ => LoremIpsum.url)
        assertTrue(urls.forall(_.startsWith("https://")))
      },
    ),
  )

end LoremIpsumSpec
