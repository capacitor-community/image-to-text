package com.capacitorcommunity.CapacitorOcr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import java.io.File;
import java.io.IOException;

@NativePlugin
public class CapacitorOcr extends Plugin {

    @PluginMethod
    public void detectText(PluginCall call) throws IOException {
        String orientation = call.getString("orientation");

        if (orientation == null) {
            orientation = "UP";
        }

        int rotation = this.orientationToRotation(orientation);

        String filename = call.getString("filename");
        String base64 = call.getString("base64");
        Bitmap bitmap;

        if (filename != null) {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), Uri.parse(filename));
            if (bitmap == null) {
                call.reject("Could not load image from path");
                return;
            }
        } else if (base64 != null) {
            base64 = base64.substring(base64.indexOf(",") + 1);
            byte[] imageData = Base64.decode(base64, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            if (bitmap == null) {
                call.reject("Could not load image from base64");
                return;
            }
        } else {
            call.reject("Invalid image input");
            return;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate((float) rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        TextDetector td = new TextDetector();
        td.detectText(call, rotatedBitmap);
    }

    private int orientationToRotation(String orientation) {
        switch (orientation) {
            case "UP":
                return 0;
            case "RIGHT":
                return 90;
            case "DOWN":
                return 180;
            case "LEFT":
                return 270;
            default:
                return 0;
        }
    }
}
