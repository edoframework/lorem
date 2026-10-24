package dev.taihei.mihon

import java.security.SecureRandom
import java.time.{Clock, LocalDate}
import java.util.{Random, UUID}

import dev.taihei.mihon.sources.*
import dev.taihei.tsuji.Language

/**
 * Fake data for tests, seed data and mock-ups: filler text, people, contact details, addresses,
 * dates, files and ids.
 *
 * A generator draws every value from its own random number generator and reads today's date from
 * its own clock; there is no global generator and no wall clock. Create one with
 * [[LoremIpsum.seeded]] for values that repeat from run to run, or with [[LoremIpsum.secure]] for
 * values that do not.
 *
 * One instance may be shared across threads: it stays thread-safe, but which thread gets which
 * value then depends on scheduling, so the values are no longer reproducible.
 *
 * {{{
 * import dev.taihei.mihon.LoremIpsum
 *
 * val lorem = LoremIpsum.secure()
 * lorem.name  // "Olivia Hernandez"
 * lorem.email // "olivia.hernandez@brightwavehub.example"
 *
 * val fixed = LoremIpsum.seeded(42L)
 * fixed.name  // the same name on every run, on the JVM and on Scala.js
 * }}}
 */
final class LoremIpsum private (random: Random, clock: Clock):

  /**
   * One of the languages taihei-tsuji knows (`Language.values`), each equally likely. A
   * taihei-tsuji release with another language changes the values a seed gives from here on.
   */
  def language: Language =
    val values = Language.values
    values(random.nextInt(values.length))

  /**
   * A random int in `[1, bound]`, each value equally likely; never 0.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
   */
  def int(bound: Int): Int = 1 + random.nextInt(bound)

  /**
   * A random long in `[1, bound]`, each value equally likely; never 0. The bound is an `Int`, so
   * the result is at most `Int.MaxValue`.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
   */
  def long(bound: Int): Long = 1L + random.nextInt(bound)

  /**
   * A random double in `[0, bound)`.
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive and finite
   */
  def double(bound: Double): Double =
    if !(bound > 0.0 && bound < Double.PositiveInfinity) then
      throw new IllegalArgumentException(s"bound must be positive and finite: $bound")
    // nextDouble() alone: its algorithm is specified, so a seed gives the same value on every
    // platform; the bounded variant of newer JDKs is not.
    val r = random.nextDouble() * bound
    if r < bound then r else Math.nextDown(bound)

  /**
   * `f` applied to `1, 2, …, n` for a random `n` in `[1, bound]`: between one and `bound` items.
   *
   * {{{
   * lorem.times(10, _ => lorem.name) // one to ten names
   * }}}
   *
   * @throws IllegalArgumentException
   *   when `bound` is not positive
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
  def city: String = Cities(random)

  /** Four decimal digits, each 0 to 9. */
  def zipCode: String = (1 to 4).map(_ => digit).mkString

  /** `https://` and a domain under the reserved `.example` top-level domain (RFC 2606). */
  def url: String = s"https://${DomainNames(random)}"

  /**
   * `first.last@domain`, the name parts reduced to ASCII letters, digits and `_+&*-`, the domain
   * under the reserved `.example` top-level domain (RFC 2606), so no mail reaches a real host.
   */
  def email: String =
    val sanitizedFirst = sanitizeForEmail(firstName)
    val sanitizedLast  = sanitizeForEmail(lastName)
    s"$sanitizedFirst.$sanitizedLast@${DomainNames(random)}"

  private def sanitizeForEmail(name: String): String =
    name.replaceAll("[^a-zA-Z0-9_+&*-]", "").toLowerCase(java.util.Locale.ROOT)

  /** One Lorem Ipsum word. */
  def word: String = Lorem(random)

  /** One printable ASCII character: a letter, a digit or one of `!@#$%^&*()_+-=[]{}|;:,.<>?`. */
  def character: String = Characters(random)

  /** One to ten filler words, separated by a space. */
  def words: String = times(10, _ => word).mkString(" ")

  /**
   * Thirty random [[character]]s. Fake data, not a credential: from a seeded generator it is
   * predictable.
   */
  def password: String = (1 to 30).map(_ => character).mkString

  /** One to ten filler words, capitalized and ending in a full stop. */
  def sentence: String = times(10, _ => word).mkString(" ").capitalize + "."

  /** One to five filler sentences. */
  def sentences: String = times(5, _ => sentence).mkString(" ")

  /** Two to eleven filler words: one to ten joined by `/`, with one more appended to the last. */
  def slug: String = (1 to int(10)).map(_ => word).mkString("/") + word

  /** Filler text, the same as [[sentences]]. */
  def description: String = sentences

  /**
   * A random version-4 UUID with `_` instead of `-`, drawn from this generator, so a seeded one is
   * reproducible. Not unique across generators with the same seed.
   */
  def id: String =
    // Version 4 (random) and the IETF variant, as UUID.randomUUID sets them.
    val msb = (random.nextLong() & ~0xf000L) | 0x4000L
    val lsb = (random.nextLong() & 0x3fffffffffffffffL) | Long.MinValue
    new UUID(msb, lsb).toString.replace("-", "_")

  /** One to five capitalized filler words. */
  def title: String = times(5, _ => word.capitalize).mkString(" ")

  /**
   * A first name and a last name, separated by a space. The last name may itself contain spaces
   * ("De La Cruz").
   */
  def name: String = s"$firstName $lastName"

  /** A male or a female first name, each equally likely. */
  def firstName: String = if bool then MaleNames(random) else FemaleNames(random)

  /** A surname. */
  def lastName: String = Surnames(random)

  /** `+32` and nine decimal digits, each 0 to 9. */
  def phone: String = "+32" + (1 to 9).map(_ => digit).mkString

  /** An international country name, often in its native script. */
  def country: String = Countries(random)

  /** A random date: year in `[1, 3000]`, any month, day of month in `[1, 28]`. */
  def localDate: LocalDate =
    LocalDate.of(int(3000), int(12), int(28))

  /** Today's date by this generator's clock, in the clock's time zone. Not random. */
  def today: LocalDate = LocalDate.now(clock)

  /** The day before [[today]]. Not random. */
  def yesterday: LocalDate = today.minusDays(1)

  /** The day after [[today]]. Not random. */
  def tomorrow: LocalDate = today.plusDays(1)

  /** [[today]] plus one to a thousand weeks. */
  def futureDate: LocalDate = today.plusWeeks(long(1000))

  /** [[today]] minus one to a thousand weeks. */
  def pastDate: LocalDate = today.minusWeeks(long(1000))

  /** A file extension with its leading dot, such as `.pdf`. */
  def fileExtension: String = FileExtensions(random)

  /** A file extension without the dot, such as `pdf`. */
  def fileExtensionWithoutDot: String = FileExtensions(random).filterNot(Set('.'))

  /** A MIME type, such as `application/pdf`. */
  def mimeType: String = MimeTypes(random)

end LoremIpsum

/** Creates [[LoremIpsum]] generators. */
object LoremIpsum:

  /**
   * A generator whose values repeat: the same seed, clock, mihon version and taihei-tsuji version
   * give the same values, in the same order, on the JVM and on Scala.js. A minor release of either
   * may change the values for a seed (a new word, a changed list, a new language for
   * [[LoremIpsum.language]]); assert on the values of a seed only with both versions pinned.
   *
   * {{{
   * import java.time.{Clock, Instant, ZoneOffset}
   *
   * val clock = Clock.fixed(Instant.parse("2026-10-24T12:00:00Z"), ZoneOffset.UTC)
   * val lorem = LoremIpsum.seeded(42L, clock)
   * lorem.today // 2026-10-24
   * }}}
   *
   * @param seed
   *   the seed of the generator's `java.util.Random`
   * @param clock
   *   where [[LoremIpsum.today]] and the dates relative to it come from; the system clock in UTC by
   *   default
   */
  def seeded(seed: Long, clock: Clock = Clock.systemUTC()): LoremIpsum =
    new LoremIpsum(new Random(seed), clock)

  /**
   * A generator whose values do not repeat, drawn from a `java.security.SecureRandom`.
   *
   * @param clock
   *   where [[LoremIpsum.today]] and the dates relative to it come from; the system clock in UTC by
   *   default
   */
  def secure(clock: Clock = Clock.systemUTC()): LoremIpsum =
    new LoremIpsum(new SecureRandom(), clock)

end LoremIpsum
