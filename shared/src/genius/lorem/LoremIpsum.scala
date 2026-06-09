package genius.lorem

import java.security.SecureRandom
import java.time.{LocalDate, ZoneId}
import java.util.UUID

import genius.i18n.Language

import genius.lorem.sources.*

object LoremIpsum:
  private val random = new SecureRandom()

  def languages: Array[Language] = Language.values

  def language: Language =
    val values = languages
    values(int(values.length))

  def int(bound: Int): Int =
    val theRandomInt = random.nextInt(bound)
    if theRandomInt == 0 then 1 else theRandomInt

  def long(bound: Int): Long =
    val theRandomLong = random.nextInt(bound).toLong
    if theRandomLong == 0 then 1 else theRandomLong

  def double(bound: Double): Double = random.nextDouble(bound)

  def times[A](bound: Int, f: Int => A): List[A] = (1 to int(bound)).map(f.apply).toList

  def bool: Boolean = if int(100) % 2 == 0 then true else false

  def htmlParagraphs: String = times(4, _ => s"<p>$paragraph</p>").mkString

  def paragraph: String  = times(6, _ => sentence).mkString(" ")
  def paragraphs: String = times(4, _ => paragraph).mkString(" ")

  def street: String = times(3, _ => word).mkString(" ")

  def address: String =
    s"$street ${int(100)}, $zipCode $city"

  def city: String = Cities()

  def zipCode: String = (1 to 4).map(_ => int(9)).mkString

  def url: String = s"https://${DomainNames()}"

  def email: String =
    val sanitizedFirst = sanitizeForEmail(firstName)
    val sanitizedLast  = sanitizeForEmail(lastName)
    s"$sanitizedFirst.$sanitizedLast@${DomainNames()}"

  private def sanitizeForEmail(name: String): String =
    name.replaceAll("[^a-zA-Z0-9_+&*-]", "").toLowerCase

  def word: String = Lorem()

  def character: String = Characters()

  def words: String = times(10, _ => word).mkString(" ")

  def password: String = (1 to 30).map(_ => character).mkString

  def sentence: String = times(10, _ => word).mkString(" ").capitalize + "."

  def sentences: String = times(5, _ => sentence).mkString(" ")

  def slug: String = (1 to int(10)).map(_ => word).mkString("/") + word

  def description: String = sentences

  def id: String = UUID.randomUUID().toString.replace("-", "_")

  def title: String = times(5, _ => word.capitalize).mkString(" ")

  def name: String = s"$firstName $lastName"

  def firstName: String = if bool then MaleNames() else FemaleNames()

  def lastName: String = Surnames()

  def phone: String = "+32" + (1 to 9).map(_ => int(9)).mkString

  def country: String = Countries()

  def localDate: LocalDate =
    LocalDate.of(int(3000), int(12), int(28))

  def today: LocalDate      = LocalDate.now(ZoneId.of("UTC"))
  def yesterday: LocalDate  = today.minusDays(1)
  def tomorrow: LocalDate   = today.plusDays(1)
  def futureDate: LocalDate = today.plusWeeks(long(1000))
  def pastDate: LocalDate   = today.minusWeeks(long(1000))

  def fileExtension: String           = FileExtensions()
  def fileExtensionWithoutDot: String = FileExtensions().filterNot(Set('.'))
  def mimeType: String                = MimeTypes()

end LoremIpsum
