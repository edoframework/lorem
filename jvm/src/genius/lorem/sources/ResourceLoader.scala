package genius.lorem.sources

import scala.io.Source

import zio.json.*

// Throws on failure intentionally: this is fail-fast initialization code.
// Resources are bundled at compile time; failure here indicates a build/packaging error
// that must be fixed before the application can run.
object ResourceLoader:

  def loadStringList(resourcePath: String): List[String] =
    val stream = getClass.getResourceAsStream(resourcePath)
    if stream == null then
      throw new RuntimeException(s"Resource not found: $resourcePath")
    try
      val jsonString = Source.fromInputStream(stream).mkString
      jsonString.fromJson[List[String]] match
        case Right(list) => list
        case Left(error) =>
          throw new RuntimeException(s"Failed to parse JSON from $resourcePath: $error")
    finally
      stream.close()
