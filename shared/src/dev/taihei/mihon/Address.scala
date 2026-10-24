package dev.taihei.mihon

/**
 * A fake postal address in the format of one country, the default country of the generator's
 * language. Made by [[Mihon.address]]; not a case class, so that a later minor release can add a
 * field. The street is made up from the language's names and words; the city is a real one.
 *
 * {{{
 * val us = Mihon.seeded(1L).address
 * us.lines      // List("1600 Wilson Street", "Houston, Texas 77001", "United States")
 * us.singleLine // "1600 Wilson Street, Houston, Texas 77001, United States"
 * }}}
 */
final class Address private[mihon] (
  /** The lines of the address, top to bottom, in the country's format. */
  val lines: List[String],
  /** The lines on one line, joined by the separator the language uses for that. */
  val singleLine: String,
  /** The street and the house number, or, in Japan, the block and lot. */
  val street: String,
  /** The postal code, in the country's pattern, with ASCII digits. */
  val postalCode: String,
  /** The city. */
  val city: String,
  /** The state or province, where the country's format names one (US, Brazil, India). */
  val region: Option[String],
  /** The country's name, in the generator's language. */
  val country: String,
  /** The country's ISO 3166-1 alpha-2 code, such as `US`. */
  val countryCode: String):

  /** The [[singleLine]]. */
  override def toString: String = singleLine

end Address
