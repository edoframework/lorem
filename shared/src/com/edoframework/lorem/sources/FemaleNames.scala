package com.edoframework.lorem.sources

object FemaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/com/edoframework/lorem/sources/female-names.json")
