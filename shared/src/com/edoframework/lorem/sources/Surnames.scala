package com.edoframework.lorem.sources

object Surnames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/com/edoframework/lorem/sources/surnames.json")
