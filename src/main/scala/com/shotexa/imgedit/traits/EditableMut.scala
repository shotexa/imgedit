package com.shotexa.imgedit
package traits

import java.awt.Color

private[imgedit] trait EditableMut {

  protected type Self <: EditableMut

  /**
    * flood fills the <code>EditableImage</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    *
    * @param coordinates
    * @param fillColor
    * @return <code>EditableMut</code>
    */
  def floodFillInPlace(
      coordinates: (Int, Int),
      fillColor: Color
  ): Self

  /**
    * Turns a colored <code>EditableImage</code> into a black and white one.
    *
    * @return <code>EditableMut</code>
    */
  def grayScaleInPlace: Self

  /**
    * Applies a box blur to a <code>EditableImage</code> with a given <code>intensity</code>
    *
    * @param image
    * @param intensity
    * @return <code>EditableMut</code>
    */
  def boxBlurInPlace(intensity: Int): Self

  /**
    * Sharpens the <code>EditableImage</code> with a given <code>intensity</code>
    *
    * @param intensity
    * @return <code>EditableMut</code>
    */
  def sharpenInPlace(intensity: Int): Self

  /**
    * changes the brightness of the <code>EditableImage</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    *
    * @param changePercentage
    * @return <code>EditableMut</code>
    */
  def changeBrightnessInPlace(changePercentage: Int): Self

  /**
    * Increase or decrease the contrast of the <code>EditableImage</code> based on the given <code>intensity</code>.
    *
    * @param intensity
    * @return <code>EditableMut</code>
    */
  def changeContrastInPlace(intensity: Int): Self

}
