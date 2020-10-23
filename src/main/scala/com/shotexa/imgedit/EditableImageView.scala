package com.shotexa.imgedit

import traits.{Editable}
import java.awt.Color
import Implicits._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

private[imgedit] class EditableImageView(image: EditableImage)
    extends Editable {

  override protected type Self = EditableImageView
  private[imgedit] var function: EditableImage => EditableImage = ei => ei
  private val self                                     = this

  /**
    * flood fills the <code>EditableImage</code> with the given <code>fillColor</code>
    * starting from the <code>coordinates</code>.
    * Does not mutate the image.
    *
    * @param coordinates
    * @param fillColor
    * @return <code>EditableImageView</code>
    */
  override def floodFilled(
      coordinates: (Int, Int),
      fillColor: Color
  ): Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) =>
            EditableImage
              .floodFilled(coordinates, ei, fillColor)
              .toEditableImage
      ) compose self.function
    }

  /**
    * Turns a colored <code>EditableImage</code> into a black and white one.
    *
    * @return <code>EditableImageView</code>
    */
  override def grayScaled: Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) => EditableImage.grayScaled(ei).toEditableImage
      ) compose self.function
    }

  /**
    * Applies a box blur to a <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>EditableImageView</code>
    */
  override def boxBlurred(intensity: Int): Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) =>
            EditableImage.boxBlurred(ei, intensity).toEditableImage
      ) compose self.function
    }

  /**
    * Sharpens the <code>EditableImage</code> with a given <code>intensity</code>
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>EditableImageView</code>
    */
  override def sharpened(intensity: Int): Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) =>
            EditableImage.sharpened(ei, intensity).toEditableImage
      ) compose self.function
    }

  /**
    * changes the brightness of the <code>EditableImage</code> based on the given <code>percentage</code>
    * positive percentage will make the image brighter, and negative dimmer.
    * Does not mutate the image.
    *
    * @param changePercentage
    * @return <code>EditableImageView</code>
    */
  override def changedBrightness(changePercentage: Int): Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) =>
            EditableImage
              .changedBrightness(ei, changePercentage)
              .toEditableImage
      ) compose self.function
    }

  /**
    * Increase or decrease the contrast of the <code>EditableImage</code> based on the given <code>intensity</code>.
    * Does not mutate the image
    *
    * @param intensity
    * @return <code>EditableImageView</code>
    */
  override def changedContrast(intensity: Int): Self =
    new EditableImageView(image) {
      function = (
          (ei: EditableImage) =>
            EditableImage
              .changedContrast(ei, intensity)
              .toEditableImage
      ) compose self.function
    }

  /**
    * run a computation inside a <code>Future</code>
    * @return
    */
  def asFuture: Future[EditableImage] = Future { function(image) }

  /**
    * run a computation
    *
    * @return
    */
  def run: EditableImage = function(image)

}
