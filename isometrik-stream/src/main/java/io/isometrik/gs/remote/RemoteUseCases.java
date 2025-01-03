package io.isometrik.gs.remote;

import com.google.gson.Gson;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.managers.MediaTransferManager;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.error.BaseResponse;

public class RemoteUseCases {

  private final CopublishUseCases copublishUseCases;
  private final EcommerceUseCases ecommerceUseCases;
  private final MembersUseCases membersUseCases;
  private final MessagesUseCases messagesUseCases;
  private final ModeratorsUseCases moderatorsUseCases;
  private final RecordingsUseCases recordingsUseCases;
  private final RestreamingUseCases restreamingUseCases;
  private final StreamsUseCases streamsUseCases;
  private final SubscriptionsUseCases subscriptionsUseCases;
  private final UsersUseCases usersUseCases;
  private final ViewersUseCases viewersUseCases;
  private final UploadUseCases uploadUseCases;
  private final PkUseCases pkUseCases;
  private final GiftUseCases giftUseCases;
  private final WalletUseCases walletUseCases;

  public RemoteUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson,
      MediaTransferManager mediaTransferManager) {

    copublishUseCases = new CopublishUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    ecommerceUseCases = new EcommerceUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    membersUseCases = new MembersUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    messagesUseCases = new MessagesUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    moderatorsUseCases =
        new ModeratorsUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    recordingsUseCases =
        new RecordingsUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    restreamingUseCases =
        new RestreamingUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    streamsUseCases = new StreamsUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    subscriptionsUseCases =
        new SubscriptionsUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    usersUseCases = new UsersUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    viewersUseCases = new ViewersUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    uploadUseCases = new UploadUseCases(mediaTransferManager, baseResponse);
    pkUseCases = new PkUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    giftUseCases = new GiftUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    walletUseCases = new WalletUseCases(imConfiguration, retrofitManager, baseResponse, gson);
  }

  public CopublishUseCases getCopublishUseCases() {
    return copublishUseCases;
  }

  public EcommerceUseCases getEcommerceUseCases() {
    return ecommerceUseCases;
  }

  public MembersUseCases getMembersUseCases() {
    return membersUseCases;
  }

  public MessagesUseCases getMessagesUseCases() {
    return messagesUseCases;
  }

  public ModeratorsUseCases getModeratorsUseCases() {
    return moderatorsUseCases;
  }

  public RecordingsUseCases getRecordingsUseCases() {
    return recordingsUseCases;
  }

  public RestreamingUseCases getRestreamingUseCases() {
    return restreamingUseCases;
  }

  public StreamsUseCases getStreamsUseCases() {
    return streamsUseCases;
  }

  public SubscriptionsUseCases getSubscriptionsUseCases() {
    return subscriptionsUseCases;
  }

  public UsersUseCases getUsersUseCases() {
    return usersUseCases;
  }

  public ViewersUseCases getViewersUseCases() {
    return viewersUseCases;
  }

  /**
   * Gets upload use cases.
   *
   * @return the upload use cases
   */
  public UploadUseCases getUploadUseCases() {
    return uploadUseCases;
  }

  public PkUseCases getPkUseCases() {
    return pkUseCases;
  }

  public GiftUseCases getGiftUseCases() {
    return giftUseCases;
  }

  public WalletUseCases getWalletUseCases() {
    return walletUseCases;
  }
}
