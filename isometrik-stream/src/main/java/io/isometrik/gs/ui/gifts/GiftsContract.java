package io.isometrik.gs.ui.gifts;


import io.isometrik.gs.ui.gifts.response.Gift;
import io.isometrik.gs.ui.utils.BasePresenter;


public interface GiftsContract {

  interface Presenter extends BasePresenter<View> {
    void fetchGiftCategories();

    void sendGift(String streamId, String receiverId, String receiverName,
                  Gift gift, boolean isPk, String pkId, String pkInviteId, String receiverStreamId, String receiverStreamUserId, String reciverUserType);
  }

  interface View {

    void onError(String errorMessage);


    void insufficientBalance();
  }
}
