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
  lineList: List[String],
  separator: String,
  streetName: String,
  code: String,
  cityName: String,
  regionName: Option[String],
  countryName: String,
  isoCode: String):

  /** The lines of the address, top to bottom, in the country's format. */
  def lines: List[String] = lineList

  /** The lines on one line, joined by the separator the language uses for that. */
  def singleLine: String = lineList.mkString(separator)

  /** The street and the house number, or, in Japan, the block and lot. */
  def street: String = streetName

  /** The postal code, in the country's pattern, with ASCII digits. */
  def postalCode: String = code

  /** The city. */
  def city: String = cityName

  /** The state or province, where the country's format names one (US, Brazil, India). */
  def region: Option[String] = regionName

  /** The country's name, in the generator's language. */
  def country: String = countryName

  /** The country's ISO 3166-1 alpha-2 code, such as `US`. */
  def countryCode: String = isoCode

  /** The [[singleLine]]. */
  override def toString: String = singleLine

end Address
