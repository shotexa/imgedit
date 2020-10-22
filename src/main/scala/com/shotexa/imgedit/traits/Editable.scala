package com.shotexa.imgedit
package traits

import java.awt.image.BufferedImage
import java.awt.Color

private[imgedit] trait Editable {

  def floodFilled(
      coordinates: (Int, Int),
      image: BufferedImage,
      fillColor: Color
  ): BufferedImage

  def grayScaled(image: BufferedImage): BufferedImage

  def boxBlurred(image: BufferedImage, intensity: Int): BufferedImage

  def sharpened(image: BufferedImage, intensity: Int = 1): BufferedImage

  def changedBrightness(
      image: BufferedImage,
      changePercentage: Int
  ): BufferedImage

  def changedContrast(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage
}
