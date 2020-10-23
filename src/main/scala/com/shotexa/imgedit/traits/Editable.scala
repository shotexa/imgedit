package com.shotexa.imgedit
package traits

import java.awt.Color

private[imgedit] trait Editable {

  protected type Self <: Editable

  /**
    * flood fills the <code>EditableImage</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    * Does not mutate the image.
    *
    * @param coordinates
    * @param fillColor
    * @return <code>Editable</code>
    */
  def floodFilled(
      coordinates: (Int, Int),
      fillColor: Color
  ): Self

  /**
    * Turns a colored <code>EditableImage</code> into a black and white one.
    *
    * @return <code>Editable</code>
    */
  def grayScaled: Self

  /**
    * Applies a box blur to a <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>EditableImage</code>
    */
  def boxBlurred(intensity: Int): Self

  /**
    * Sharpens the <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>Editable</code>
    */
  def sharpened(intensity: Int = 1): Self

  /**
    * changes the brightness of the <code>EditableImage</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    * Does not mutate the image.
    *
    * @param changePercentage
    * @return <code>Editable</code>
    */
  def changedBrightness(
      changePercentage: Int
  ): Self

  /**
    * Increase or decrease the contrast of the <code>EditableImage</code> based on the given <code>intensity</code>.
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>Editable</code>
    */
  def changedContrast(
      intensity: Int
  ): Self
}
