package dev.taihei.mihon

import zio.test.*

import dev.taihei.mihon.sources.ResourceLoader

// The JVM reads the name lists from the classpath, Scala.js from lists compiled in by the build.
// Both must give exactly the files in shared/resources. The size and String.hashCode of each
// list (both specified, and the same on the two platforms) are pinned here: a change to a list
// updates them, and a platform that loads anything else fails.
object NameListsSpec extends ZIOSpecDefault:

  private def load(file: String) = ResourceLoader.loadStringList(s"/dev/taihei/mihon/sources/$file")

  private def fingerprint(list: List[String]) = (list.size, list.mkString("\n").hashCode)

  def spec = suite("NameListsSpec")(
    test("female-names.json loads as the file holds it") {
      assertTrue(fingerprint(load("female-names.json")) == (999, -1347248677))
    },
    test("male-names.json loads as the file holds it") {
      val names = load("male-names.json")
      assertTrue(fingerprint(names) == (898, -1224206217), names.contains("Abdul"), !names.contains("Abigail"))
    },
    test("surnames.json loads as the file holds it") {
      assertTrue(fingerprint(load("surnames.json")) == (998, 1992490761))
    },
    test("an unknown list is a packaging error, not an empty list") {
      assertTrue(scala.util.Try(load("no-such-list.json")).isFailure)
    },
  )

end NameListsSpec
