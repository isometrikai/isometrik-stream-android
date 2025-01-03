package io.isometrik.gs.rtcengine.ar;

import android.graphics.Bitmap;
import android.media.Image;

import ai.deepar.ar.ARErrorType;
import ai.deepar.ar.AREventListener;
import io.isometrik.gs.Isometrik;

/**
 * The listener to listen for AR engine events when AR engine is initialized, face visibility change
 * ,error in apply effect etc.
 */
public class ARInitializeListener implements AREventListener {

  private final Isometrik isometrik;

  /**
   * Instantiates a new Ar initialize listener.
   *
   * @param isometrik the isometrik instance
   * @see Isometrik
   */
  public ARInitializeListener(Isometrik isometrik) {
    this.isometrik = isometrik;
  }

  @Override
  public void screenshotTaken(Bitmap bitmap) {

    isometrik.arImageCaptured(bitmap);
  }

  @Override
  public void videoRecordingStarted() {

  }

  @Override
  public void videoRecordingFinished() {

  }

  @Override
  public void videoRecordingFailed() {

  }

  @Override
  public void videoRecordingPrepared() {

  }

  @Override
  public void shutdownFinished() {

  }

  @Override
  public void initialized() {
    isometrik.setAREngineInitialized();
  }

  @Override
  public void faceVisibilityChanged(boolean b) {

  }

  @Override
  public void imageVisibilityChanged(String s, boolean b) {

  }

  @Override
  public void error(ARErrorType arErrorType, String s) {
    //isometrik.setARFiltersEnabled(false);
  }

  @Override
  public void frameAvailable(Image image) {

  }

  @Override
  public void effectSwitched(String s) {

  }
}
