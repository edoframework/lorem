package genius.lorem.sources

object Surnames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/genius/lorem/sources/surnames.json")
