package com.capacitorcommunity.CapacitorOcr

import android.graphics.Bitmap
import android.util.NoSuchPropertyException
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import org.json.JSONArray


class TextDetector {
  fun detectText(call: PluginCall, bitmap: Bitmap) {
    val image: FirebaseVisionImage
    val detectedText = ArrayList<Any>()

    try {
      image = FirebaseVisionImage.fromBitmap(bitmap)
      val width = bitmap.width
      val height = bitmap.height

      val textDetector: FirebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

      textDetector.processImage(image)
        .addOnSuccessListener { detectedBlocks ->
          for (block in detectedBlocks.textBlocks) {
            for (line in block.lines) {
              // Gets the four corner points in clockwise direction starting with top-left.
              val cornerPoints = line.cornerPoints ?: throw NoSuchPropertyException("FirebaseVisionTextRecognizer.processImage: could not get bounding coordinates")
              val topLeft = cornerPoints[0]
              val topRight = cornerPoints[1]
              val bottomRight = cornerPoints[2]
              val bottomLeft = cornerPoints[3]

              val textDetection = mapOf(
                // normalizing coordinates
                "topLeft" to listOf<Double?>((topLeft.x).toDouble()/width, (height - topLeft.y).toDouble()/height),
                "topRight" to listOf<Double?>((topRight.x).toDouble()/width, (height - topRight.y).toDouble()/height),
                "bottomLeft" to listOf<Double?>((bottomLeft.x).toDouble()/width, (height - bottomLeft.y).toDouble()/height),
                "bottomRight" to listOf<Double?>((bottomRight.x).toDouble()/width, (height - bottomRight.y).toDouble()/height),
                "text" to line.text
              )
              detectedText.add(textDetection)
            }
          }
          call.resolve(JSObject().put("textDetections", JSONArray(detectedText)))
        }
        .addOnFailureListener { e ->
          call.reject("FirebaseVisionTextRecognizer couldn't process the given image", e)
        }
    } catch (e: Exception) {
      e.printStackTrace();
      call.reject(e.localizedMessage, e)
    }
  }
}
