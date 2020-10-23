# ImgEdit - fluent image editing library

# Install

```scala
libraryDependencies += "com.shotexa" %% "imgedit" % "0.0.1"
```

# Usage

## EditableImage

The main utility class is `com.shotexa.imgedit.EditableImage`, through which you will access the API

```scala
import com.shotexa.imgedit.EditableImage
import java.io.{ InputStream, File } 
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

...

def stream:        InputStream = getClass.getResourceAsStream("/some-image.png")
val image:         BufferedImage = ImageIO.read(stream)
val blackAndWhite: BufferedImage = EditableImage.grayScaled(image)

ImageIO.write(blackAndWhite, "png", new File("/out.png"))

```
`EditableImage` can also be instantiated

```scala
import com.shotexa.imgedit.EditableImage
import java.io.InputStream

...

def stream:   InputStream   = getClass.getResourceAsStream("/some-image.png")
val image:    EditableImage = EditableImage(stream)
val newImage: EditableImage = image
                .grayScaled // make it black and white
                .changedBrightness(20) // make it brighter by +20%
```
`EditableImage` supports alternative constructors

```scala
import com.shotexa.imgedit.EditableImage
import java.io.InputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

...

def stream: InputStream   = getClass.getResourceAsStream("/some-image.png")
val image1: EditableImage = EditableImage(stream) // from InputStream
val image2: EditableImage = EditableImage(new File("/some-image.png")) // from File
val image3: BufferedImage = ImageIO.read(stream)
val image4: EditableImage = EditableImage(image3) // from BufferedImage

```
`BufferedImage` and `EditableImage` support implicit conversions to one another

```scala
import com.shotexa.imgedit.EditableImage
import com.shotexa.imgedit.Implicits._
import javax.imageio.ImageIO

...

def stream:    InputStream   = getClass.getResourceAsStream("/some-image.png")
val image:     BufferedImage = ImageIO.read(stream)
val sharpened: EditableImage = image.sharpen(20) // make image sharper by 20 points

ImageIO.write(sharpened, "png", new File("/out.png"))

```

It is possible to instantiate an `EditableImage` from a remote url

```scala
import com.shotexa.imgedit.EditableImage
import javax.imageio.ImageIO

...

 EditableImage
      .fromUrl(
        "https://www.petpaw.com.au/wp-content/uploads/2012/09/abyssinian-cat-3.jpg"
      )
      .map(_.grayScaled)
      .map(_.boxBlurred(10))
      .map(ImageIO.write(_, "png", new File("out.png")))


```

Before                                      |  After
:------------------------------------------:|:-------------------------:
![Before](https://i.imgur.com/Q2ss2Eu.jpg)  |![After](https://i.imgur.com/qSxD0Gg.png) 


Sometimes you might want to chain multiply operations for later execution, you can use `EditableImageView` for that

Note: <em>`EditableImageView` only supports immutable operations</em>

```scala
import com.shotexa.imgedit.EditableImage
import javax.imageio.ImageIO

val imageView = ImageIO
                .read(inPath)
                .editView // returns a view
                .grayScaled
                .boxBlurred(20)

val editedImage:  EditableImage         = imageView.run      // runs chained operations in current thread
val editedImage2: Future[EditableImage] = imageView.asFuture // runs chained operations in another thread

```


## Mutable and Immutable operations

Every operations has it's mutable and immutable version, here is the list of all possible operations

| Description                                                                                                                                   | Mutable                   | Immutable           |
|-----------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|---------------------|
| flood fills the image with the given color starting from the given coordinates                                                                | `floodFillInPlace`        | `floodFilled`       |
| Turns a colored image into a black and white one                                                                                              | `grayScaleInPlace`        | `grayScaled`        |
| Applies a box blur to an image with a given intensity                                                                                         | `boxBlurInPlace`          | `boxBlurred`        |
| Sharpens the image with a given intensity                                                                                                     | `sharpenInPlace`          | `sharpened`         |
| changes the brightness of the image based on the given percentage. the positive percentage will make the image brighter, and negative dimmer. | `changeBrightnessInPlace` | `changedBrightness` |
| Increase or decrease the contrast of the image based on the given intensity                                                                   | `changeContrastInPlace`   | `changedContrast`   |