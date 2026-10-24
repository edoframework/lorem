package dev.taihei.mihon.sources

// Reads a data file. Scala.js has no classpath, so the build compiles the files in from
// shared/resources (EmbeddedData in build.mill), after checking each one. It throws on an
// unknown path on purpose: that is a build error to fix before the application can run, not an
// input to handle.
private[mihon] object ResourceLoader:

  def load(resourcePath: String): String =
    EmbeddedData.get(resourcePath).getOrElse(
      throw new IllegalStateException(s"Resource not found: $resourcePath")
    )
