package com.shotexa.imgedit

import java.awt.image.BufferedImage
import java.net.URL
import scala.concurrent.Future
import java.io.File
import javax.imageio.ImageIO
import java.io.InputStream
import traits.{Editable, EditableMut}
import java.awt.Color
import collection.mutable.{Set => MutSet, ArrayDeque}
import java.awt.image.ConvolveOp
import java.awt.image.Kernel

class EditableImage(image: BufferedImage) {

  def this(file: File) = {
    this(ImageIO.read(file))
  }

  def this(stream: InputStream) = {
    this(ImageIO.read(stream))
  }

  /**
    * flood fills the <code>EditableImage</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    * Does not mutate the image.
    *
    * @param coordinates
    * @param fillColor
    * @return <code>BufferedImage</code>
    */
  def floodFilled(coordinates: (Int, Int), fillColor: Color): BufferedImage =
    EditableImage.floodFilled(coordinates, image, fillColor)

  /**
    * flood fills the <code>EditableImage</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    *
    * @param coordinates
    * @param fillColor
    * @return <code>BufferedImage</code>
    */
  def floodFillInPlace(
      coordinates: (Int, Int),
      fillColor: Color
  ): BufferedImage =
    EditableImage.floodFillInPlace(coordinates, image, fillColor)

  /**
    * Turns a colored <code>EditableImage</code> into a black and white one.
    *
    * @return <code>BufferedImage</code>
    */
  def grayScaled: BufferedImage = EditableImage.grayScaled(image)

  /**
    * Turns a colored <code>EditableImage</code> into a black and white one.
    *
    * @return <code>BufferedImage</code>
    */
  def grayScaleInPlace: BufferedImage = EditableImage.grayScaleInPlace(image)

  /**
    * Applies a box blur to a <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def boxBlurred(intensity: Int): BufferedImage =
    EditableImage.boxBlurred(image, intensity)

  /**
    * Applies a box blur to a <code>EditableImage</code> with a given <code>intensity</code>
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def boxBlurInPlace(intensity: Int): BufferedImage =
    EditableImage.boxBlurInPlace(image, intensity)

  /**
    * Sharpens the <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def sharpened(intensity: Int): BufferedImage =
    EditableImage.sharpened(image, intensity)

  /**
    * Sharpens the <code>EditableImage</code> with a given <code>intensity</code>
    *
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def sharpenInPlace(intensity: Int): BufferedImage =
    EditableImage.sharpenInPlace(image, intensity)

  /**
    * changes the brightness of the <code>EditableImage</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    * Does not mutate the image.
    *
    * @param changePercentage
    * @return <code>BufferedImage</code>
    */
  def changedBrightness(
      changePercentage: Int
  ): BufferedImage = {
    val newImage = copyImage(image)
    EditableImage.changedBrightness(image, changePercentage)
  }

  /**
    * changes the brightness of the <code>EditableImage</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    *
    * @param changePercentage
    * @return <code>BufferedImage</code>
    */
  def changeBrightnessInPlace(
      changePercentage: Int
  ): BufferedImage =
    EditableImage.changeBrightnessInPlace(image, changePercentage)

  /**
    * Increase or decrease the contrast of the <code>EditableImage</code> based on the given <code>intensity</code>.
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def changedContrast(
      intensity: Int
  ): BufferedImage = EditableImage.changedContrast(image, intensity)

  /**
    * Increase or decrease the contrast of the <code>EditableImage</code> based on the given <code>intensity</code>.
    *
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def changeContrastInPlace(
      intensity: Int
  ): BufferedImage = EditableImage.changeContrastInPlace(image, intensity)
}

object EditableImage extends Editable with EditableMut {

  def apply(image: BufferedImage) = new EditableImage(image)
  def apply(image: File)          = new EditableImage(image)
  def apply(image: InputStream)   = new EditableImage(image)

  def fromUrl(url: URL): Future[EditableImage] = ???

  /**
    * flood fills the <code>image</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    * Does not mutate the image.
    *
    * @param coordinates
    * @param image
    * @param fillColor
    * @return <code>BufferedImage</code>
    */
  def floodFilled(
      coordinates: (Int, Int),
      image: BufferedImage,
      fillColor: Color
  ): BufferedImage = {
    val imgCopy = copyImage(image)
    floodFill(coordinates, imgCopy, fillColor)
  }

  /**
    * flood fills the <code>image</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    *
    * @param coordinates
    * @param image
    * @param fillColor
    * @return <code>BufferedImage</code>
    */
  def floodFillInPlace(
      coordinates: (Int, Int),
      image: BufferedImage,
      fillColor: Color
  ): BufferedImage = floodFill(coordinates, image, fillColor)

  private def floodFill(
      coordinates: (Int, Int),
      image: BufferedImage,
      fillColor: Color
      // TODO: add tolerance argument to pixels that are of a "similar" color
  ): BufferedImage = {
    // Implemented using BFS approach
    val (cordX, cordY) = coordinates
    val initialColor   = image.getRGB(cordX, cordY)
    val queue          = ArrayDeque[(Int, Int)](cordX -> cordY)
    val queued         = MutSet[(Int, Int)](cordX -> cordY)

    while (queue.nonEmpty) {
      val (currX, currY) = queue.removeHead()
      image.setRGB(currX, currY, fillColor.getRGB)
      val neighbors = List(
        currX + 1 -> currY,
        currX - 1 -> currY,
        currX     -> (currY + 1),
        currX     -> (currY - 1)
      )

      for ((x, y) <- neighbors if isInBound(x -> y, image)) {
        if (image.getRGB(x, y) == initialColor && !queued.contains(x -> y)) {
          queue.append(x -> y)
          queued += x    -> y
        }
      }
    }

    image
  }

  /**
    * Turns a colored <code>image</code> into a black and white one.
    *
    * @param image
    * @return <code>BufferedImage</code>
    */
  def grayScaleInPlace(image: BufferedImage): BufferedImage = grayScale(image)

  /**
    * Turns a colored <code>image</code> into a black and white one.
    * Does not mutate the image
    *
    * @param image
    * @return <code>BufferedImage</code>
    */
  def grayScaled(image: BufferedImage): BufferedImage = {
    val imgCopy = copyImage(image)
    grayScale(imgCopy)
  }

  private def grayScale(image: BufferedImage): BufferedImage = {

    for {
      x <- Range(0, image.getWidth)
      y <- Range(0, image.getHeight)
    } {
      val ARGB(a, r, g, b) = disassembleRgb(image.getRGB(x, y))

      val gray =
        (r * 0.3 + g * 0.59 + b * 0.11).toInt // slightly modified version of (r + g + b) / 3 grayScaling algorithm that is more pleasant to the human eye

      val grayColor = assembleRgb(ARGB(a, gray, gray, gray))
      image.setRGB(x, y, grayColor)

    }

    image
  }

  private def boxBlur(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage = {

    val newImage = new BufferedImage(
      image.getWidth,
      image.getHeight,
      BufferedImage.TYPE_INT_ARGB
    )

    val kernelDimension = intensity + 2
    val matrixSize      = kernelDimension * kernelDimension

    val matrix: Array[Float] = Array.fill(matrixSize)(1f / matrixSize)
    val convolution = new ConvolveOp(
      new Kernel(kernelDimension, kernelDimension, matrix)
    )

    convolution.filter(image, newImage)

    newImage

  }

  /**
    * Applies an box blur to a <code>image</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def boxBlurred(image: BufferedImage, intensity: Int): BufferedImage =
    boxBlur(image, intensity)

  /**
    * Applies an box blur to an <code>image</code> with a given <code>intensity</code>
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def boxBlurInPlace(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage = {
    val newImage = boxBlur(image, intensity)
    val g        = image.createGraphics
    g.drawImage(newImage, 0, 0, null)
    g.dispose()

    image
  }

  private def sharpen(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage = {

    val newImage = new BufferedImage(
      image.getWidth,
      image.getHeight,
      BufferedImage.TYPE_INT_ARGB
    )

    val matrixSize  = 3 * 3
    val sideValue   = Math.abs(intensity / (intensity.toFloat + 10f)) * -1
    val centerValue = 1 - (sideValue * 8)
    val halfMatrix  = Array.fill(matrixSize / 2)(sideValue)

    /**
      * sharpening using high pass filter convolution, with matrix 3 x 3
      *
      * for example: intensity with limit to Infinity
      * [-1 -1 -1]
      * [-1  9 -1]
      * [-1 -1 -1]
      * sum of all values should be 1 to not change brightness of the image
      * reducing or increasing sharpening is performed by reducing or increasing intensity
      * which changes the distribution in the matrix towards lesser central value and higher side values
      * for example: intensity of 10 gives
      *
      * [-0.5 -0.5 -0.5]
      * [-0.5  5.0 -0.5]
      * [-0.5 -0.5 -0.5]
      */

    val matrix = halfMatrix ++ Array(centerValue) ++ halfMatrix

    val convolution = new ConvolveOp(
      new Kernel(3, 3, matrix)
    )

    convolution.filter(image, newImage)

    newImage
  }

  /**
    * Sharpens the <code>image</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def sharpened(image: BufferedImage, intensity: Int): BufferedImage =
    sharpen(image, intensity)

  /**
    * Sharpens the <code>image</code> with a given <code>intensity</code>
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def sharpenInPlace(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage = {
    val newImage = sharpen(image, intensity)
    val g        = image.createGraphics
    g.drawImage(newImage, 0, 0, null)
    g.dispose()

    image
  }

  private def changeBrightness(
      image: BufferedImage,
      changePercentage: Int
  ): BufferedImage = {
    for {
      x <- Range(0, image.getWidth)
      y <- Range(0, image.getHeight)
    } {
      val ARGB(a, r, g, b) = disassembleRgb(image.getRGB(x, y))

      val List(newR, newG, newB) = List(r, g, b).view
        .map { x =>
          // here we increase the intensity of the color by changePercentage variable
          (x + x.toFloat * (changePercentage.toFloat / 100f)).toInt
        }
        .map(x => if (x > 255) 255 else x) // avoid byte overflows
        .map(x => if (x < 0) 0 else x)
        .to(List)

      val newColor = assembleRgb(ARGB(a, newR, newG, newB))
      image.setRGB(x, y, newColor)

    }

    image

  }

  /**
    * changes the brightness of the <code>image</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    * Does not mutate the image.
    *
    * @param image
    * @param changePercentage
    * @return <code>BufferedImage</code>
    */
  def changedBrightness(
      image: BufferedImage,
      changePercentage: Int
  ): BufferedImage = {
    val newImage = copyImage(image)
    changeBrightness(image, changePercentage)
  }

  /**
    * changes brightness of the <code>image</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    *
    * @param image
    * @param changePercentage
    * @return <code>BufferedImage</code>
    */
  def changeBrightnessInPlace(
      image: BufferedImage,
      changePercentage: Int
  ): BufferedImage = changeBrightness(image, changePercentage)

  private def changeContrast(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage = {

    // https://math.stackexchange.com/questions/906240/algorithms-to-increase-or-decrease-the-contrast-of-an-image
    val correctionFactor: Float =
      (259f * (intensity + 255f)) / (255f * (259f - intensity))

    for {
      x <- Range(0, image.getWidth)
      y <- Range(0, image.getHeight)
    } {
      val ARGB(a, r, g, b) = disassembleRgb(image.getRGB(x, y))

      val List(newR, newG, newB) = List(r, g, b).view
        .map { x => (correctionFactor * (x - 128) + 128).toInt }
        .map(x => if (x > 255) 255 else x) // avoid byte overflows
        .map(x => if (x < 0) 0 else x)
        .to(List)

      val newColor = assembleRgb(ARGB(a, newR, newG, newB))
      image.setRGB(x, y, newColor)
    }

    image
  }

  /**
    * Increase or decrease the contrast of the <code>image</code> based on the given <code>intensity</code>.
    * Does not mutate the image
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def changedContrast(
      image: BufferedImage,
      intensity: Int = 1
  ): BufferedImage = {
    val newImage = copyImage(image)
    changeContrast(image, intensity)
  }

  /**
    * Increase or decrease the contrast of the <code>image</code> based on the given <code>intensity</code>.
    *
    * @param image
    * @param intensity
    * @return <code>BufferedImage</code>
    */
  def changeContrastInPlace(
      image: BufferedImage,
      intensity: Int = 1
  ): BufferedImage = changeContrast(image, intensity)

}
