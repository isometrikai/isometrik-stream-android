//package io.isometrik.gs.rtcengine.ar;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.util.Size;
//
//import androidx.annotation.NonNull;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.core.content.ContextCompat;
//import androidx.lifecycle.LifecycleOwner;
//
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.util.concurrent.ExecutionException;
//
//import ai.deepar.ar.CameraResolutionPreset;
//import ai.deepar.ar.DeepAR;
//import ai.deepar.ar.DeepARImageFormat;
//
//public class ARCameraHelper {
//
//  private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//  private ByteBuffer[] buffers;
//  private int currentBuffer = 0;
//  private static final int NUMBER_OF_BUFFERS = 2;
//  private int lensFacing = CameraSelector.LENS_FACING_FRONT;
//  private final DeepAR deepAR;
//
//  public ARCameraHelper(DeepAR deepAR) {
//    this.deepAR = deepAR;
//  }
//
//
//  public void releaseCamera() {
//    ProcessCameraProvider cameraProvider;
//    try {
//      if (cameraProviderFuture != null) {
//        cameraProvider = cameraProviderFuture.get();
//        cameraProvider.unbindAll();
//      }
//    } catch (ExecutionException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void switchCamera(Context context) {
//    lensFacing = lensFacing == CameraSelector.LENS_FACING_FRONT ? CameraSelector.LENS_FACING_BACK
//        : CameraSelector.LENS_FACING_FRONT;
//    //unbind immediately to avoid mirrored frame.
//    ProcessCameraProvider cameraProvider;
//    try {
//      cameraProvider = cameraProviderFuture.get();
//      cameraProvider.unbindAll();
//    } catch (ExecutionException | InterruptedException e) {
//      e.printStackTrace();
//    }
//    setupCamera( context);
//  }
//
//  public void setupCamera(Context context) {
//
//    currentBuffer = 0;
//    cameraProviderFuture = ProcessCameraProvider.getInstance(context);
//    cameraProviderFuture.addListener(() -> {
//      try {
//        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//        //new ARCameraUtils(). bindImageAnalysis(cameraProvider,deepAR,context,null);
//         bindImageAnalysis(cameraProvider,context);
//
//      } catch (ExecutionException | InterruptedException e) {
//        e.printStackTrace();
//      }
//    }, ContextCompat.getMainExecutor(context));
//  }
//
//  public void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider,Context context) {
//    CameraResolutionPreset cameraPreset = CameraResolutionPreset.P1920x1080;
//    int width;
//    int height;
//    int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//
//    //int orientation = getScreenOrientation();
//    if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
//        || orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//      width = cameraPreset.getWidth();
//      height = cameraPreset.getHeight();
//    } else {
//      width = cameraPreset.getHeight();
//      height = cameraPreset.getWidth();
//    }
//    buffers = new ByteBuffer[NUMBER_OF_BUFFERS];
//    for (int i = 0; i < NUMBER_OF_BUFFERS; i++) {
//      buffers[i] = ByteBuffer.allocateDirect(width * height * 3);
//      buffers[i].order(ByteOrder.nativeOrder());
//      buffers[i].position(0);
//    }
//
//    ImageAnalysis imageAnalysis =
//        new ImageAnalysis.Builder().setTargetResolution(new Size(width, height))
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build();
//    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), image -> {
//      //image.getImageInfo().getTimestamp();
//      byte[] byteData;
//      ByteBuffer yBuffer = image.getPlanes()[0].getBuffer();
//      ByteBuffer uBuffer = image.getPlanes()[1].getBuffer();
//      ByteBuffer vBuffer = image.getPlanes()[2].getBuffer();
//
//      int ySize = yBuffer.remaining();
//      int uSize = uBuffer.remaining();
//      int vSize = vBuffer.remaining();
//
//      byteData = new byte[ySize + uSize + vSize];
//
//      //U and V are swapped
//      yBuffer.get(byteData, 0, ySize);
//      vBuffer.get(byteData, ySize, vSize);
//      uBuffer.get(byteData, ySize + vSize, uSize);
//
//      buffers[currentBuffer].put(byteData);
//      buffers[currentBuffer].position(0);
//
//      if (deepAR != null) {
//        deepAR.receiveFrame(buffers[currentBuffer], image.getWidth(), image.getHeight(),
//            image.getImageInfo().getRotationDegrees(),
//            lensFacing == CameraSelector.LENS_FACING_FRONT, DeepARImageFormat.YUV_420_888,
//            image.getPlanes()[1].getPixelStride());
//      }
//      currentBuffer = (currentBuffer + 1) % NUMBER_OF_BUFFERS;
//      image.close();
//    });
//
//    CameraSelector cameraSelector =
//        new CameraSelector.Builder().requireLensFacing(lensFacing).build();
//    cameraProvider.unbindAll();
//    cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis);
//  }
//}
//
