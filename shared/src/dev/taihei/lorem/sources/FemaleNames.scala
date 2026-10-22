package dev.taihei.lorem.sources

/**
 * Internal: one of the word lists [[dev.taihei.lorem.LoremIpsum]] draws from. Not part of the public
 * API and not covered by the compatibility promise; it becomes private in the next major release.
 * Use [[dev.taihei.lorem.LoremIpsum]] instead.
 */
object FemaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/lorem/sources/female-names.json")
