package dev.taihei.lorem.sources

import java.security.SecureRandom

/**
 * Internal: a list of words of which `apply()` returns one at random. Not part of the public API
 * and not covered by the compatibility promise; it becomes private in the next major release. Use
 * [[dev.taihei.lorem.LoremIpsum]] instead.
 */
trait SampleWordCollection:
  private val random = new SecureRandom()
  lazy val words: List[String]

  final def apply(): String = words(random.nextInt(words.length))
