package io.isometrik.gs.rtcengine.ar.capture;

import android.graphics.Bitmap;

import java.io.File;

/**
 * The interface Image capture callbacks to inform on successful image capture.
 */
public interface ImageCaptureCallbacks {

  /**
   * Image captured from the ar camera.
   *
   * @param bitmap the bitmap captured
   */
  void arImageCaptured(Bitmap bitmap);

  /**
   * Image captured from the normal camera.
   *
   * @param file thefile containing data of image captured
   * @param success whether image capture from normal camera has been successfull or not
   */
  void normalImageCaptured(File file, boolean success);
}
