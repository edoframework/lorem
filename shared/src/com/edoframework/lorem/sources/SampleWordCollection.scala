package com.edoframework.lorem.sources

import java.security.SecureRandom

trait SampleWordCollection:
  private val random = new SecureRandom()
  lazy val words: List[String]

  final def apply(): String = words(random.nextInt(words.length))
