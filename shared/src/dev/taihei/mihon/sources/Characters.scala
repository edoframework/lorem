package dev.taihei.mihon.sources

/**
 * Internal: one of the word lists [[dev.taihei.mihon.LoremIpsum]] draws from. Not part of the public
 * API and not covered by the compatibility promise; it becomes private in the next major release.
 * Use [[dev.taihei.mihon.LoremIpsum]] instead.
 */
object Characters extends SampleWordCollection:
  private val upperCaseLetters = ('A' to 'Z').toArray
  private val lowerCaseLetters = ('a' to 'z').toArray
  private val numbers          = ('0' to '9').toArray
  private val symbols          = "!@#$%^&*()_+-=[]{}|;:,.<>?".toCharArray
  private val chars            = upperCaseLetters ++ lowerCaseLetters ++ numbers ++ symbols

  override lazy val words: List[String] = chars.map(_.toString).toList
