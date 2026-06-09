package genius.lorem.sources

object FemaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/genius/lorem/sources/female-names.json")
