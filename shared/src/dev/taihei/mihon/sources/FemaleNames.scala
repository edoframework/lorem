package dev.taihei.mihon.sources

// One of the word lists LoremIpsum draws from.
private[mihon] object FemaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/mihon/sources/female-names.json")
