package io.isometrik.gs.rtcengine.ar.capture;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.isometrik.gs.Isometrik;

/**
 * The Capture operations utility to allow capturing of the cover image for the live stream with AR
 * effects.
 */
public class CaptureOperations {

  private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
  private final Isometrik isometrik;
  private PreviewView previewView;
  private ImageCapture imageCapture;
  private final Executor executor = Executors.newSingleThreadExecutor();

  /**
   * Instantiates a new Capture operations.
   *
   * @param isometrik the isometrik instance
   * @param arView the AR view
   */
  public CaptureOperations(Isometrik isometrik, SurfaceView arView, PreviewView previewView,
      Context context) {
    this.isometrik = isometrik;
    if (isometrik.isARFiltersEnabled()) {
//      setupAREngine(arView, isometrik.initializeArEngine(context));
    } else {
      this.previewView = previewView;
    }
  }

  /**
   * Request image capture.
   */
  public void requestARImageCapture() {
    if (isometrik.isARFiltersEnabled()) {
//      isometrik.getAREngine().takeScreenshot();
    }
  }

  /**
   * Open camera.
   *
   * @param context the context
   * @param windowManager the window manager
   */
  public void openCamera(Context context, WindowManager windowManager) {

    cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    cameraProviderFuture.addListener(() -> {
      try {
        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
        if (isometrik.isARFiltersEnabled()) {
//          new ARCameraUtils().bindImageAnalysis(cameraProvider, isometrik.getAREngine(), context,
//              windowManager);
        } else {
          imageCapture = new NormalCameraUtils().bindPreview(cameraProvider, context, previewView,
              windowManager);
        }
      } catch (ExecutionException | InterruptedException e) {
        e.printStackTrace();
      }
    }, ContextCompat.getMainExecutor(context));
  }

//  /**
//   * Initializes surface for the AR engine
//   *
//   * @param arView the AR view
//   * @param deepAR the AR engine instance
//   */
//  private void setupAREngine(SurfaceView arView, DeepAR deepAR) {
//
//    try {
//
//      arView.getHolder().addCallback(new SurfaceHolder.Callback() {
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//          if (deepAR != null) {
//            deepAR.setRenderSurface(holder.getSurface(), width, height);
//          }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//
//          if (deepAR != null) {
//            deepAR.setRenderSurface(null, 0, 0);
//          }
//        }
//      });
//      // Surface might already be initialized, so we force the call to onSurfaceChanged
//      arView.setVisibility(View.GONE);
//      arView.setVisibility(View.VISIBLE);
//    } catch (Exception e) {
//
//      e.printStackTrace();
//    }
//  }

  public void captureNormalPhoto(File file) {
    if (imageCapture != null) {
      ImageCapture.OutputFileOptions outputFileOptions =
          new ImageCapture.OutputFileOptions.Builder(file).build();
      imageCapture.takePicture(outputFileOptions, executor,
          new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
              isometrik.normalImageCaptured(file, true);
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
              isometrik.normalImageCaptured(file, false);
            }
          });
    } else {
      isometrik.normalImageCaptured(file, false);
    }
  }

  /**
   * Cleanup the surface from the ar engine.
   */
  public void cleanup() {

    ProcessCameraProvider cameraProvider;
    try {
      if(cameraProviderFuture != null){
        cameraProvider = cameraProviderFuture.get();
        cameraProvider.unbindAll();
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    if (isometrik.isARFiltersEnabled()) {
//      isometrik.releaseArEngine();
    }
  }
}
