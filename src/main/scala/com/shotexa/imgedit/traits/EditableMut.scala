package com.shotexa.imgedit
package traits

import java.awt.image.BufferedImage
import java.awt.Color

private[imgedit] trait EditableMut {
    
  def floodFillInPlace(
      coordinates: (Int, Int),
      image: BufferedImage,
      fillColor: Color
  ): BufferedImage

  def grayScaleInPlace(image: BufferedImage): BufferedImage

  def boxBlurInPlace(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage

  def sharpenInPlace(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage

  def changeBrightnessInPlace(
      image: BufferedImage,
      changePercentage: Int
  ): BufferedImage

  def changeContrastInPlace(
      image: BufferedImage,
      intensity: Int
  ): BufferedImage

}
