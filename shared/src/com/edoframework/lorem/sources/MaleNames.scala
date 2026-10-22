package com.edoframework.lorem.sources

object MaleNames extends SampleWordCollection:

  override lazy val words: List[String] =
    ResourceLoader.loadStringList("/com/edoframework/lorem/sources/male-names.json")
