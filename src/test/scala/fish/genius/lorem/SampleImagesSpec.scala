package fish.genius.lorem

import org.scalatest.flatspec.AnyFlatSpec

class SampleImagesSpec extends AnyFlatSpec {
  it should "download sample images" in {
    SampleImages.image(500, 300)
    SampleImages.square(400)
    SampleImages.image(1000, 500, "jpg", true, true)
    SampleImages.image(500, 1000, "jpg", true, false)
    SampleImages.image(1000, 1000, "jpg", false, true)
    SampleImages.image(500, 500, "jpg", false, false)
    (1 to 10).foreach(i => {
      val image = SampleImages.square(400, filename = "virginie")
      println(image)
    })
  }
}
