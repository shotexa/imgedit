package com.shotexa

import java.awt.image.BufferedImage
import java.awt.Color

package object imgedit {

  private[imgedit] def copyImage(image: BufferedImage): BufferedImage = {
    val b = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    val g = b.createGraphics
    g.drawImage(image, 0, 0, null)
    g.dispose()
    b
  }

  private[imgedit] def isInBound(
      coordinates: (Int, Int),
      image: BufferedImage
  ): Boolean = {
    val (x, y)   = coordinates
    val xInBound = x >= 0 && x < image.getWidth
    val yInBound = y >= 0 && y < image.getHeight

    xInBound && yInBound
  }

  private[imgedit] def disassembleRgb(rgb: Int): ARGB = {
    val color = new Color(rgb)
    ARGB(color.getAlpha, color.getRed, color.getGreen, color.getBlue)
  }

  private[imgedit] def assembleRgb(rgb: ARGB): Int = {
    val ARGB(a, r, g, b) = rgb
    // Making Int out of 4 bytes, for example 255, 255, 255, 255 -> 11111111 11111111 11111111 11111111 -> 4294967295
    List(r, g, b).foldLeft(a) { _ << 8 | _ }
  }

}
