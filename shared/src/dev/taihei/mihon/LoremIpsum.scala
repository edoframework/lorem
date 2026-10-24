package dev.taihei.mihon

import java.security.SecureRandom
import java.time.{LocalDate, ZoneId}
import java.util.UUID

import dev.taihei.mihon.sources.*
import dev.taihei.tsuji.Language

/**
 * Fake data for tests, seed data and mock-ups: filler text, people, contact details, addresses,
 * dates, files and ids.
 *
 * Every generator returns a fresh value drawn from a `java.security.SecureRandom`, so output is
 * not reproducible; assert on its shape, never on a value. The exceptions are [[today]],
 * [[yesterday]] and [[tomorrow]], which follow the UTC calendar date.
 *
 * {{{
 * import dev.taihei.mihon.LoremIpsum
 *
 * LoremIpsum.name  // "Olivia Hernandez"
 * LoremIpsum.email // "olivia.hernandez@brightwavehub.example"
 * }}}
 */
object LoremIpsum:
  private val random = new SecureRandom()

  /** Every language taihei-tsuji knows. */
  def languages: Array[Language] = Language.values

  /** One of [[languages]], each equally likely. */
  def language: Language =
    val values = languages
    values(random.nextInt(values.length))

  /**
   * A random int in `[1, bound]`, each value equally likely; never 0.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
   */
  def int(bound: Int): Int = 1 + random.nextInt(bound)

  /**
   * A random long in `[1, bound]`, each value equally likely; never 0.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
   */
  def long(bound: Int): Long = 1L + random.nextInt(bound)

  /**
   * A random double in `[0, bound)`.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
   */
  def double(bound: Double): Double = random.nextDouble(bound)

  /**
   * `f` applied to `1, 2, …, n` for a random `n` in `[1, bound]`: between one and `bound` items.
   *
   * {{{
   * LoremIpsum.times(10, _ => LoremIpsum.name) // one to ten names
   * }}}
   */
  def times[A](bound: Int, f: Int => A): List[A] = (1 to int(bound)).map(f.apply).toList

  /** True or false, each equally likely. */
  def bool: Boolean = random.nextBoolean()

  // A decimal digit, 0 to 9, for the numeric parts of zip codes and phone numbers.
  private def digit: Int = random.nextInt(10)

  /** One to four paragraphs of filler text, each wrapped in `<p>…</p>`. */
  def htmlParagraphs: String = times(4, _ => s"<p>$paragraph</p>").mkString

  /** One to six filler sentences. */
  def paragraph: String = times(6, _ => sentence).mkString(" ")

  /** One to four filler paragraphs, separated by a space. */
  def paragraphs: String = times(4, _ => paragraph).mkString(" ")

  /** One to three filler words, used as a street name. */
  def street: String = times(3, _ => word).mkString(" ")

  /** A one-line address: street, house number in `[1, 100]`, a four-digit zip code and a city. */
  def address: String =
    s"$street ${int(100)}, $zipCode $city"

  /** An international city name, often in its native script. */
  def city: String = Cities()

  /** Four decimal digits, each 0 to 9. */
  def zipCode: String = (1 to 4).map(_ => digit).mkString

  /** `https://` and a domain under the reserved `.example` top-level domain (RFC 2606). */
  def url: String = s"https://${DomainNames()}"

  /**
   * `first.last@domain`, the name parts reduced to ASCII letters, digits and `_+&*-`, the domain
   * under the reserved `.example` top-level domain (RFC 2606), so no mail reaches a real host.
   */
  def email: String =
    val sanitizedFirst = sanitizeForEmail(firstName)
    val sanitizedLast  = sanitizeForEmail(lastName)
    s"$sanitizedFirst.$sanitizedLast@${DomainNames()}"

  private def sanitizeForEmail(name: String): String =
    name.replaceAll("[^a-zA-Z0-9_+&*-]", "").toLowerCase

  /** One Lorem Ipsum word. */
  def word: String = Lorem()

  /** One printable character. */
  def character: String = Characters()

  /** One to ten filler words, separated by a space. */
  def words: String = times(10, _ => word).mkString(" ")

  /** Thirty random [[character]]s. */
  def password: String = (1 to 30).map(_ => character).mkString

  /** One to ten filler words, capitalized and ending in a full stop. */
  def sentence: String = times(10, _ => word).mkString(" ").capitalize + "."

  /** One to five filler sentences. */
  def sentences: String = times(5, _ => sentence).mkString(" ")

  /** Two to eleven filler words: one to ten joined by `/`, with one more appended to the last. */
  def slug: String = (1 to int(10)).map(_ => word).mkString("/") + word

  /** Filler text, the same as [[sentences]]. */
  def description: String = sentences

  /** A random UUID with `_` instead of `-`. */
  def id: String = UUID.randomUUID().toString.replace("-", "_")

  /** One to five capitalized filler words. */
  def title: String = times(5, _ => word.capitalize).mkString(" ")

  /**
   * A first name and a last name, separated by a space. The last name may itself contain spaces
   * ("De La Cruz").
   */
  def name: String = s"$firstName $lastName"

  /** A male or a female first name, each equally likely. */
  def firstName: String = if bool then MaleNames() else FemaleNames()

  /** A surname. */
  def lastName: String = Surnames()

  /** `+32` and nine decimal digits, each 0 to 9. */
  def phone: String = "+32" + (1 to 9).map(_ => digit).mkString

  /** An international country name, often in its native script. */
  def country: String = Countries()

  /** A random date: year in `[1, 3000]`, any month, day of month in `[1, 28]`. */
  def localDate: LocalDate =
    LocalDate.of(int(3000), int(12), int(28))

  /** Today's date in UTC. Not random. */
  def today: LocalDate = LocalDate.now(ZoneId.of("UTC"))

  /** The day before [[today]]. Not random. */
  def yesterday: LocalDate = today.minusDays(1)

  /** The day after [[today]]. Not random. */
  def tomorrow: LocalDate = today.plusDays(1)

  /** [[today]] plus one to a thousand weeks. */
  def futureDate: LocalDate = today.plusWeeks(long(1000))

  /** [[today]] minus one to a thousand weeks. */
  def pastDate: LocalDate = today.minusWeeks(long(1000))

  /** A file extension with its leading dot, such as `.pdf`. */
  def fileExtension: String = FileExtensions()

  /** A file extension without the dot, such as `pdf`. */
  def fileExtensionWithoutDot: String = FileExtensions().filterNot(Set('.'))

  /** A MIME type, such as `application/pdf`. */
  def mimeType: String = MimeTypes()

end LoremIpsum
