package fish.genius.lorem

import fish.genius.io.FileUtils

import java.io.File
import java.nio.file.Files
import java.security.cert.Extension

object SampleImages {
  private val baseUrl = "https://picsum.photos/"
  private val targetDirectory = FileUtils.temporaryDirectory
  private val imagesDirectory = new File(targetDirectory, "lorem")

  private def download(
      width: Int,
      height: Int,
      extension: String = "webp",
      grayscale: Boolean = false,
      blur: Boolean = false
  ): Option[File] = FileUtils.copyUrlToFile(
    s"$baseUrl$width/$height.$extension${urlSuffix(grayscale, blur)}",
    new File(imagesDirectory, s"${SampleText.id}.$extension")
  )

  private def urlSuffix(grayscale: Boolean, blur: Boolean): String =
    if (grayscale && blur) "/?grayscale&blur=5"
    else if (grayscale) "/?grayscale"
    else if (blur) "/?blur=5"
    else ""

  def image(
      width: Int,
      height: Int,
      extension: String = "webp",
      grayscale: Boolean = false,
      blur: Boolean = false
  ): Option[File] =
    download(width, height, extension, grayscale, blur)

  def square(
      width: Int,
      extension: String = "webp",
      grayscale: Boolean = false,
      blur: Boolean = false
  ): Option[File] =
    download(width, width, extension, grayscale, blur)

}
