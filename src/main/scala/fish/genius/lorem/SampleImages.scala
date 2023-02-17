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
      blur: Boolean = false,
      filename: String
  ): Option[File] = {
    val imageFile = new File(
      imagesDirectory,
      s"${filename}_${width}x${height}_${if (grayscale) "gray_" else "color_"}${if (blur)
          "blur"
        else "sharp"}.${extension}"
    )
    if (imageFile.canRead) Some(imageFile)
    else
      FileUtils.copyUrlToFile(
        s"$baseUrl$width/$height.$extension${urlSuffix(grayscale, blur)}",
        imageFile
      )
  }

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
      blur: Boolean = false,
      filename: String = "image"
  ): Option[File] =
    download(width, height, extension, grayscale, blur, filename)

  def square(
      width: Int,
      extension: String = "webp",
      grayscale: Boolean = false,
      blur: Boolean = false,
      filename: String = "image"
  ): Option[File] =
    download(width, width, extension, grayscale, blur, filename)

}
