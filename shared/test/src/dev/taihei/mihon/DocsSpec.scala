package dev.taihei.mihon

import zio.test.*

import dev.taihei.tsuji.Language

// The seeded values the README and the Scaladoc show. A change to the data changes them: update
// the documentation in the same change.
object DocsSpec extends ZIOSpecDefault:

  private def seeded(tag: String) = Mihon.seeded(1L, Language(tag))

  def spec = suite("DocsSpec")(
    test("the people of the README and the Person Scaladoc") {
      val ja = seeded("ja").person
      assertTrue(
        ja.fullName == "三浦さくら",
        ja.latin == "Miura Sakura",
        ja.reading == Some("ミウラ サクラ"),
        seeded("ru").person.fullName == "Ольга Емельянова",
        seeded("ru").person.latin == "Olga Emelianova",
        seeded("ar").person.fullName == "أمينة الداود",
        seeded("zh-Hans").person.fullName == "姜慧",
        seeded("zh-Hans").person.latin == "Jiang Hui",
      )
    },
    test("the sentence and the addresses of the README and the Address Scaladoc") {
      assertTrue(
        seeded("ru").sentence == "Время перед а знаю быстрее человека права неделю почти перевести.",
        seeded("ja").address.lines == List("日本", "〒873-4446", "京都市中央29-20-14"),
        seeded("en").address.lines == List("7 Lincoln Avenue", "New York City, New York 87344", "United States"),
      )
    },
  )

end DocsSpec
