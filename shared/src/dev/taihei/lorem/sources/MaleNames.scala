package dev.taihei.lorem.sources

object MaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/lorem/sources/male-names.json")
