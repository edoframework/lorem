package dev.taihei.mihon.sources

/**
 * Internal: one of the word lists [[dev.taihei.mihon.LoremIpsum]] draws from. Not part of the public
 * API and not covered by the compatibility promise; it becomes private in the next major release.
 * Use [[dev.taihei.mihon.LoremIpsum]] instead.
 */
object MaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/dev/taihei/mihon/sources/male-names.json")
