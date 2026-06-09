package genius.lorem.sources

object MaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/genius/lorem/sources/male-names.json")
