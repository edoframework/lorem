package dev.taihei.mihon.budget

import dev.taihei.mihon.Mihon

// The size budget's fixture (mihon.budget in build.mill): a program that uses every member that
// reads language data, in every language, so its full-linked size is what Mihon can add to a
// browser bundle at most. It is never published.
object Main:

  def main(args: Array[String]): Unit =
    Mihon.languages.foreach { language =>
      val mihon = Mihon.seeded(1L, language)
      println(
        Seq(
          mihon.paragraphs,
          mihon.title,
          mihon.slug,
          mihon.person.latin,
          mihon.email,
          mihon.address.singleLine,
          mihon.phone,
          mihon.country,
          mihon.anyLanguage.tag,
          mihon.password,
          mihon.id,
          mihon.mimeType,
        )
          .mkString(" | ")
      )
    }

end Main
