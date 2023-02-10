import sbt._

object Dependencies {
  object Testing {
    private val _version = "3.2.14"
    final val * = Seq(
      "org.scalactic" %% "scalactic" % _version,
      "org.scalatest" %% "scalatest" % _version % Test
    )
  }
  object Lorem {
    private val _version = "2.1"
    final val * = Seq("com.thedeanda" % "lorem" % _version)
  }
  object GeniusFish {
    final val config = "fish.genius" %% "config" % "1.0.6"
    final val logging = "fish.genius" %% "logging" % "1.0.3"
    final val io = "fish.genius" %% "io" % "1.5.0"
    final val * = Seq(config, logging, io)
  }
}
