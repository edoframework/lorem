package dev.taihei.mihon.sources

import java.security.SecureRandom

// A list of words of which `apply()` returns one at random.
private[mihon] trait SampleWordCollection:
  private val random = new SecureRandom()
  lazy val words: List[String]

  final def apply(): String = words(random.nextInt(words.length))
