package io.isometrik.gs.ui.gifts;

import io.isometrik.gs.ui.gifts.response.Gift;

/**
 * Interface containing callback method to send a gift in a broadcast.
 */
public interface GiftsActionCallback {

  /**
   * Callback for a gift sent in a broadcast.
   *
   * @param streamId id of the stream in which gift was sent
   * @param gift gift that was sent in a broadcast
   */
  void onGiftSentSuccessfully(String streamId, Gift gift, String categoryTitle);

}
