package dev.taihei.mihon.sources

import scala.io.{Codec, Source}

import zio.json.*

// Loads the JSON name lists. It throws on failure on purpose: the lists are bundled at build
// time, so a failure here is a packaging error to fix before the application can run, not an
// input to handle.
private[mihon] object ResourceLoader:

  def loadStringList(resourcePath: String): List[String] =
    val stream = getClass.getResourceAsStream(resourcePath)
    if stream == null then
      throw new RuntimeException(s"Resource not found: $resourcePath")
    try
      val jsonString = Source.fromInputStream(stream)(
        using Codec.UTF8
      ).mkString
      jsonString.fromJson[List[String]] match
        case Right(list) => list
        case Left(error) =>
          throw new RuntimeException(s"Failed to parse JSON from $resourcePath: $error")
    finally
      stream.close()

end ResourceLoader
