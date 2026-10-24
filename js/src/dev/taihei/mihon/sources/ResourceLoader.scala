package dev.taihei.mihon.sources

import zio.json.*

// Loads the JSON name lists. It throws on failure on purpose: the lists are compiled in from
// shared/resources (see EmbeddedLists in build.mill), so a failure here is a build error to fix
// before the application can run, not an input to handle.
private[mihon] object ResourceLoader:

  def loadStringList(resourcePath: String): List[String] =
    val json = EmbeddedLists.byPath.getOrElse(
      resourcePath,
      throw new RuntimeException(
        s"Unknown resource: $resourcePath. Embedded (JS): ${EmbeddedLists.byPath.keys.toSeq.sorted.mkString(", ")}"
      ),
    )
    json.fromJson[List[String]] match
      case Right(list) => list
      case Left(error) =>
        throw new RuntimeException(s"Failed to parse JSON from $resourcePath: $error")
