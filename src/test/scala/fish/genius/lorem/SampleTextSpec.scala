package fish.genius.lorem

import org.scalatest.flatspec.AnyFlatSpec

class SampleTextSpec extends AnyFlatSpec {
  it should "generate random values" in {
    println(SampleText.locale)
    println(SampleText.phone)
    println(SampleText.address)
    println(SampleText.name)
  }
}
