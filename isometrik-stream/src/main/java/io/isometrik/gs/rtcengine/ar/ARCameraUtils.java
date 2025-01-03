package io.isometrik.gs.rtcengine.ar;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ai.deepar.ar.CameraResolutionPreset;
import ai.deepar.ar.DeepAR;
import ai.deepar.ar.DeepARImageFormat;

public class ARCameraUtils {

  private ByteBuffer[] buffers;
  private int currentBuffer = 0;
  private static final int NUMBER_OF_BUFFERS = 2;
  private final int lensFacing = CameraSelector.LENS_FACING_FRONT;

  public int getScreenOrientation(WindowManager windowManager) {
    int rotation = windowManager.getDefaultDisplay().getRotation();
    DisplayMetrics dm = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    int orientation;
    // if the device's natural orientation is portrait:
    if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width
        || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
        && width > height) {
      switch (rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_180:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        case Surface.ROTATION_270:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        default:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
      }
    }
    // if the device's natural orientation is landscape or if the device
    // is square:
    else {
      switch (rotation) {
        case Surface.ROTATION_0:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
        case Surface.ROTATION_90:
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          break;
        case Surface.ROTATION_180:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          break;
        case Surface.ROTATION_270:
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          break;
        default:
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          break;
      }
    }

    return orientation;
  }

  public void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider, DeepAR deepAR,
      Context context, WindowManager windowManager) {

    CameraResolutionPreset cameraPreset = CameraResolutionPreset.P1920x1080;
    int width;
    int height;

    int orientation = getScreenOrientation(windowManager);
    if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
      width = cameraPreset.getWidth();
      height = cameraPreset.getHeight();
    } else {
      width = cameraPreset.getHeight();
      height = cameraPreset.getWidth();
    }
    buffers = new ByteBuffer[NUMBER_OF_BUFFERS];
    for (int i = 0; i < NUMBER_OF_BUFFERS; i++) {
      buffers[i] = ByteBuffer.allocateDirect(width * height * 3);
      buffers[i].order(ByteOrder.nativeOrder());
      buffers[i].position(0);
    }

    ImageAnalysis imageAnalysis =
        new ImageAnalysis.Builder().setTargetResolution(new Size(width, height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build();
    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), image -> {
      //image.getImageInfo().getTimestamp();
      byte[] byteData;
      ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
      ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
      ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();

      int ySize = yBuffer.remaining();
      int uSize = uBuffer.remaining();
      int vSize = vBuffer.remaining();

      byteData = new byte[ySize + uSize + vSize];

      //U and V are swapped
      yBuffer.get(byteData, 0, ySize);
      vBuffer.get(byteData, ySize, vSize);
      uBuffer.get(byteData, ySize + vSize, uSize);

      buffers[currentBuffer].put(byteData);
      buffers[currentBuffer].position(0);
      if (deepAR != null) {
        deepAR.receiveFrame(buffers[currentBuffer], image.getWidth(), image.getHeight(),
            image.getImageInfo().getRotationDegrees(),
            lensFacing == CameraSelector.LENS_FACING_FRONT, DeepARImageFormat.YUV_420_888,
            image.getPlanes()[1].getPixelStride());
      }
      currentBuffer = (currentBuffer + 1) % NUMBER_OF_BUFFERS;
      image.close();
    });

    CameraSelector cameraSelector =
        new CameraSelector.Builder().requireLensFacing(lensFacing).build();
    cameraProvider.unbindAll();
    cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis);
  }
}
