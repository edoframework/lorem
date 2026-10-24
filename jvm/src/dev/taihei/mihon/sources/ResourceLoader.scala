package dev.taihei.mihon.sources

import scala.io.{Codec, Source}

// Reads a data file from the classpath. It throws on failure on purpose: the files are bundled
// at build time and checked by the build, so a failure here is a packaging error to fix before
// the application can run, not an input to handle.
private[mihon] object ResourceLoader:

  def load(resourcePath: String): String =
    val stream = getClass.getResourceAsStream(resourcePath)
    if stream == null then throw new IllegalStateException(s"Resource not found: $resourcePath")
    try Source.fromInputStream(stream)(
        using Codec.UTF8
      ).mkString
    finally stream.close()
