package com.shotexa.imgedit

import java.awt.image.BufferedImage

object Implicits {
  implicit def bufferedImage2EditableImage(bi: BufferedImage): EditableImage =
    EditableImage(bi)

}
