package dev.taihei.lorem.sources

object FemaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/lorem/sources/female-names.json")
