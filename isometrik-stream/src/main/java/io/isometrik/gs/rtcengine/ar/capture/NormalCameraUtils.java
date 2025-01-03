package io.isometrik.gs.rtcengine.ar.capture;

import android.content.Context;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LifecycleOwner;

public class NormalCameraUtils {


  ImageCapture bindPreview(@NonNull ProcessCameraProvider cameraProvider, Context context,
      PreviewView previewView, WindowManager windowManager) {
    Preview preview = new Preview.Builder().build();

    CameraSelector cameraSelector =
        new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

    preview.setSurfaceProvider(previewView.getSurfaceProvider());

    ImageCapture.Builder imageCaptureBuilder = new ImageCapture.Builder();
    final ImageCapture imageCapture =
        imageCaptureBuilder.setTargetRotation(windowManager.getDefaultDisplay().getRotation())
            .build();
//this.imageCapture=imageCapture;
    //Camera camera =
        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview,
            imageCapture);
        return  imageCapture;
  }


}
