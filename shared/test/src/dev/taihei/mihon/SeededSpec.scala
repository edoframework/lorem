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
    "ar" -> List(
      "بإمكانها يحاول كيف أنتَ سيارته تبقى جيدة منهم الصباح البشر وحده ذاك؟",
      "جانا حسين",
      "Jana Hussein",
      "-",
      "muhammad.shaheen@lunarbytefusion.example",
      "180 شارع الثورة، السويس، 77233، مصر",
      "+20 15 3754 7573",
      "ما تكن بلد حياتي",
      "خطر",
    ),
    "en" -> List(
      "Pretty has dangerous themselves color knew manager whether everything world their week?",
      "Virginia Wood",
      "Virginia Wood",
      "-",
      "charles.white@lunarbytefusion.example",
      "180 Cedar Lane, Greensboro, North Carolina 77233, United States",
      "+1 737 555-0154",
      "Safe Into Many",
      "of",
    ),
    "ja" -> List(
      "よけたと国民そう人口はなるちょうど着く去年。",
      "鈴木さちこ",
      "Suzuki Sachiko",
      "スズキ サチコ",
      "sakamoto.nao@lunarbytefusion.example",
      "日本 〒057-7233 市原市栄町30-26",
      "+81 90-3754-7573",
      "警察両親成長なる",
      "立ち",
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
    "ru" -> List(
      "Ты все моём об умер зовут окно надо будет несмотря мог ли?",
      "Марина Щербакова",
      "Marina Shcherbakova",
      "-",
      "aleksandr.abramov@lunarbytefusion.example",
      "ул. Лесная, д. 26, Сочи, 372334, Россия",
      "+7 937 547-57-33",
      "Рядом",
      "туда",
    ),
    "th" -> List(
      "ธรรมชาติหิมะตรวจสนามที่ผ้าแทนพื้นครับแล้วใบ",
      "วิไลลักษณ์ อำพันสุวรรณ",
      "Vilailak Ampunsuwan",
      "-",
      "chayanit.saengsila@lunarbytefusion.example",
      "114 ถนนรัชดาภิเษก นนทบุรี 35772 ไทย",
      "+66 84 953 7547",
      "นะ",
      "กว่า",
    ),
    "zh-Hans" -> List(
      "信什么蛋糕吧放在的总二物迷路过。",
      "高璇",
      "Gao Xuan",
      "-",
      "li.shuzhen@lunarbytefusion.example",
      "中国 357723 无锡市 中山路165号",
      "+86 195 3754 7573",
      "什么早饭不在外语",
      "变",
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
      assertTrue(run(Mihon.seeded(7L, Language("ja"))) == run(Mihon.seeded(7L, Language("ja"))))
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
