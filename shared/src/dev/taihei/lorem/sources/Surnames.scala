package dev.taihei.lorem.sources

object Surnames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/lorem/sources/surnames.json")
