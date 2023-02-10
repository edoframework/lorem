package fish.genius.lorem

import com.thedeanda.lorem.LoremIpsum

import java.security.SecureRandom
import java.time.LocalDate
import java.util.{Locale, UUID}

object SampleText {
  private val random = new SecureRandom()
  private val lorem = LoremIpsum.getInstance()

  def locales: List[Locale] =
    List(Locale.ENGLISH, Locale.US, Locale.FRENCH, Locale.of("nl", "BE"))

  def locale: Locale = {
    val index = int(locales.length)
    locales(index)
  }

  def int(bound: Int): Int = {
    val theRandomInt = random.nextInt(bound)
    if (theRandomInt == 0) 1 else theRandomInt
  }

  def double(bound: Double): Double = random.nextDouble(bound)

  def times[A](bound: Int, f: Int => A): List[A] =
    (1 to int(bound)).map(f.apply).toList

  def bool: Boolean = if (int(2) % 2 == 0) true else false

  def htmlParagraphs: String = lorem.getHtmlParagraphs(1, 3)

  def paragraphs: String = lorem.getParagraphs(1, 3)

  def address: String =
    s"${lorem.getWords(2)} ${int(100)}, $zipCode $city"

  def city: String = lorem.getCity

  def zipCode: String = lorem.getZipCode

  def url: String = lorem.getUrl

  def email: String = lorem.getEmail

  def word: String = lorem.getWords(1)

  def words: String = lorem.getWords(int(10))

  def slug: String =
    (1 to int(10)).map(_ => word).mkString("/") + word

  def description: String = lorem.getWords(5, 100)

  def id: String = UUID.randomUUID().toString.replace("-", "_")

  def title: String = lorem.getTitle(1, 3)

  def name: String = s"$firstName $lastName"

  def firstName: String = lorem.getFirstName

  def lastName: String = lorem.getLastName

  def phone: String = lorem.getPhone

  def country: String = locale.getDisplayCountry

  def localDate: LocalDate =
    LocalDate.of(int(3000), int(12), int(28))
}
