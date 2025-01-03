package io.isometrik.gs.managers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.services.CopublishService;
import io.isometrik.gs.services.EcommerceService;
import io.isometrik.gs.services.GiftService;
import io.isometrik.gs.services.MemberService;
import io.isometrik.gs.services.MessageService;
import io.isometrik.gs.services.ModeratorService;
import io.isometrik.gs.services.RecordingService;
import io.isometrik.gs.services.RestreamService;
import io.isometrik.gs.services.StreamService;
import io.isometrik.gs.services.SubscriptionService;
import io.isometrik.gs.services.UserService;
import io.isometrik.gs.services.ViewerService;
import io.isometrik.gs.services.WalletService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The manager class for Retrofit and OkHttpClient client.
 */
public class RetrofitManager {

  private final Isometrik isometrik;

  private final OkHttpClient transactionClientInstance;

  private final MemberService memberService;
  private final MessageService messageService;
  private final StreamService streamService;
  private final UserService userService;
  private final ViewerService viewerService;
  private final SubscriptionService subscriptionService;
  private final CopublishService copublishService;
  private final RecordingService recordingService;
  private final RestreamService restreamService;
  private final ModeratorService moderatorService;
  private final EcommerceService ecommerceService;
  private final GiftService giftService;
  private final WalletService walletService;

  /**
   * Instantiates a new Retrofit manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see Isometrik
   */
  public RetrofitManager(Isometrik isometrikInstance) {
    this.isometrik = isometrikInstance;
    this.transactionClientInstance =
        createOkHttpClient(this.isometrik.getConfiguration().getRequestTimeout(),
            this.isometrik.getConfiguration().getConnectTimeout());
    Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);
    Retrofit transactionInstanceGift = createRetrofitGift(this.transactionClientInstance);
    Retrofit transactionInstanceWallet = createRetrofitWallet(this.transactionClientInstance);

    this.memberService = transactionInstance.create(MemberService.class);
    this.messageService = transactionInstance.create(MessageService.class);
    this.streamService = transactionInstance.create(StreamService.class);
    this.userService = transactionInstance.create(UserService.class);
    this.viewerService = transactionInstance.create(ViewerService.class);
    this.subscriptionService = transactionInstance.create(SubscriptionService.class);
    this.copublishService = transactionInstance.create(CopublishService.class);
    this.recordingService = transactionInstance.create(RecordingService.class);
    this.restreamService = transactionInstance.create(RestreamService.class);
    this.moderatorService = transactionInstance.create(ModeratorService.class);
    this.ecommerceService = transactionInstance.create(EcommerceService.class);
    this.giftService = transactionInstanceGift.create(GiftService.class);
    this.walletService = transactionInstanceWallet.create(WalletService.class);
  }

  /**
   * Create OkHttpClient instance.
   *
   * @return OkHttpClient instance
   * @see OkHttpClient
   */
  private OkHttpClient createOkHttpClient(int requestTimeout, int connectTimeOut) {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.retryOnConnectionFailure(false);
    httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
    httpClient.connectTimeout(connectTimeOut, TimeUnit.SECONDS);

    if (isometrik.getConfiguration().getLogVerbosity() == IMLogVerbosity.BODY) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.level(HttpLoggingInterceptor.Level.BODY);
      httpClient.addInterceptor(logging);
    }

    return httpClient.build();
  }

  /**
   * Create retrofit instance.
   *
   * @return retrofit instance
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  /**
   * Create retrofit instance for gift.
   *
   * @return retrofit instance for gift
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofitGift(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getGiftBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
  }

  /**
   * Create retrofit instance for wallet.
   *
   * @return retrofit instance for wallet
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofitWallet(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getWalletBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
  }

  /**
   * Gets member service.
   *
   * @return the member service
   * @see MemberService
   */
  public MemberService getMemberService() {
    return memberService;
  }

  /**
   * Gets copublish service.
   *
   * @return the copublish service
   * @see CopublishService
   */
  public CopublishService getCopublishService() {
    return copublishService;
  }

  /**
   * Gets message service.
   *
   * @return the message service
   * @see MessageService
   */
  public MessageService getMessageService() {
    return messageService;
  }

  /**
   * Gets stream service.
   *
   * @return the stream service
   * @see StreamService
   */
  public StreamService getStreamService() {
    return streamService;
  }

  /**
   * Gets user service.
   *
   * @return the user service
   * @see UserService
   */
  public UserService getUserService() {
    return userService;
  }

  /**
   * Gets viewer service.
   *
   * @return the viewer service
   * io.isometrik.gs.services.ViewerService
   */
  public ViewerService getViewerService() {
    return viewerService;
  }

  /**
   * Gets subscription service.
   *
   * @return the subscription service
   * @see SubscriptionService
   */
  public SubscriptionService getSubscriptionService() {
    return subscriptionService;
  }

  /**
   * Gets recording service.
   *
   * @return the recording service
   * @see RecordingService
   */
  public RecordingService getRecordingService() {
    return recordingService;
  }

  /**
   * Gets restream service.
   *
   * @return the restream service
   * @see RestreamService
   */
  public RestreamService getRestreamService() {
    return restreamService;
  }

  /**
   * Gets moderator service.
   *
   * @return the moderator service
   * @see ModeratorService
   */
  public ModeratorService getModeratorService() {
    return moderatorService;
  }

  /**
   * Gets ecommerce service.
   *
   * @return the ecommerce service
   * @see EcommerceService
   */
  public EcommerceService getEcommerceService() {
    return ecommerceService;
  }

  /**
   * Gets gift service.
   *
   * @return the gift service
   * @see GiftService
   *
   */

  public GiftService getGiftService() {
    return giftService;
  }


  /**
   * Gets wallet service.
   *
   * @return the wallet service
   * @see WalletService
   *
   */

  public WalletService getWalletService() {
    return walletService;
  }


  /**
   * Destroy.
   *
   * @param force whether to destroy forcibly
   */
  public void destroy(boolean force) {
    if (this.transactionClientInstance != null) {
      closeExecutor(this.transactionClientInstance, force);
    }
  }

  /**
   * Closes the OkHttpClient execute
   *
   * @param client OkHttpClient whose requests are to be canceled
   * @param force whether to forcibly shutdown the OkHttpClient and evict all connection pool
   */
  private void closeExecutor(OkHttpClient client, boolean force) {
    try {
      new Thread(() -> {
        client.dispatcher().cancelAll();
        if (force) {

          ExecutorService executorService = client.dispatcher().executorService();
          executorService.shutdown();
          client.connectionPool().evictAll();
        }
      }).start();
    } catch (Exception ignore) {
    }
  }
}

