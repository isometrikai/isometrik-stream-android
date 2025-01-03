package io.isometrik.gs.ui.metadata;

/**
 * The interface add metadata callbacks for communication back to scrollable streams activity, on
 * click of
 * add metadata buttons.
 */
public interface AddMetadataCallback {
  /**
   * Returns metadata entered by user, which is to be added to stream
   *
   * @param message the message to be added as metadata to stream
   */
  void insertMetadataRequested(String message);
}
