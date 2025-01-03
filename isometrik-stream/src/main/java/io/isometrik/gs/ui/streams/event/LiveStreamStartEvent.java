package io.isometrik.gs.ui.streams.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.isometrik.gs.ui.streams.categories.StreamCategoryEnum;

public class LiveStreamStartEvent implements Serializable {

  @SerializedName("streamTypes")
  @Expose
  private ArrayList<Integer> streamTypes;

  @SerializedName("alreadyPaid")
  @Expose
  private boolean alreadyPaid;
  @SerializedName("audioOnly")
  @Expose
  private boolean audioOnly;
  @SerializedName("coinsCount")
  @Expose
  private Integer coinsCount;
  @SerializedName("country")
  @Expose
  private String country;
  @SerializedName("duration")
  @Expose
  private Long duration;
  @SerializedName("isGroupStream")
  @Expose
  private boolean isGroupStream;
  @SerializedName("isPaid")
  @Expose
  private boolean isPaid;
  @SerializedName("isPublicStream")
  @Expose
  private boolean isPublicStream;
  @SerializedName("isRecorded")
  @Expose
  private boolean isRecorded;
  @SerializedName("isScheduledStream")
  @Expose
  private boolean isScheduledStream;
  @SerializedName("amount")
  @Expose
  private Integer paymentAmount;
  @SerializedName("paymentType")
  @Expose
  private Integer paymentType;
  @SerializedName("paymentCurrencyCode")
  @Expose
  private String paymentCurrencyCode;
  @SerializedName("recordUrl")
  @Expose
  private String recordUrl;
  @SerializedName("startDateTime")
  @Expose
  private long startDateTime;
  @SerializedName("streamDescription")
  @Expose
  private String streamDescription;
  @SerializedName("streamId")
  @Expose
  private String streamId;
  @SerializedName("streamImage")
  @Expose
  private String streamImage;
  @SerializedName("streamTags")
  @Expose
  private ArrayList<String> streamTags;
  @SerializedName("members")
  @Expose
  private ArrayList<String> members;
  @SerializedName("streamTitle")
  @Expose
  private String streamTitle;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;
  @SerializedName("userType")
  @Expose
  private int userType;

  @SerializedName("hdBroadcast")
  @Expose
  private boolean hdBroadcast;

  @SerializedName("restream")
  @Expose
  private boolean restream;

  @SerializedName("productsLinked")
  @Expose
  private boolean productsLinked;

  @SerializedName("productsCount")
  @Expose
  private int productsCount;

  @SerializedName("userDetails")
  @Expose
  private LiveStreamsResponse.Stream.UserDetails userDetails;

  @SerializedName("firstUserDetails")
  @Expose
  private LiveStreamsResponse.Stream.UserDetails firstUserDetails;

  @SerializedName("secondUserDetails")
  @Expose
  private LiveStreamsResponse.Stream.UserDetails secondUserDetails;


  @SerializedName("isPkChallenge")
  @Expose
  private boolean isPkChallenge;
  @SerializedName("pkId")
  @Expose
  private String pkId;

  @SerializedName("selfHosted")
  @Expose
  private boolean selfHosted;

  @SerializedName("rtmpIngest")
  @Expose
  private boolean rtmpIngest;

  public static class UserDetails {

    @SerializedName("walletUserId")
    @Expose
    private String walletUserId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userProfile")
    @Expose
    private String userProfile;
    @SerializedName("isFollow")
    @Expose
    private int followStatus;
    @SerializedName("isStar")
    @Expose
    private boolean isStar;

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public String getUserName() {
      return userName;
    }

    public String getUserProfile() {
      return userProfile;
    }

    public int getFollowStatus() {
      return followStatus;
    }

    public boolean isStar() {
      return isStar;
    }

    public String getWalletUserId() {
      return walletUserId;
    }
  }

  public boolean isAudioOnly() {
    return audioOnly;
  }

  public Integer getCoinsCount() {
    return coinsCount;
  }

  public String getCountry() {
    return country;
  }

  public Long getDuration() {
    return duration;
  }

  public boolean isGroupStream() {
    return isGroupStream;
  }

  public boolean isPaid() {
    return isPaid;
  }

  public boolean isPublicStream() {
    return isPublicStream;
  }

  public boolean isRecorded() {
    return isRecorded;
  }

  public boolean isScheduledStream() {
    return isScheduledStream;
  }

  public Integer getPaymentAmount() {
    return paymentAmount;
  }

  public boolean isAlreadyPaid() {
    return alreadyPaid;
  }

  public Integer getPaymentType() {
    return paymentType;
  }

  public String getPaymentCurrencyCode() {
    return paymentCurrencyCode;
  }

  public String getRecordUrl() {
    return recordUrl;
  }

  public long getStartDateTime() {
    return startDateTime;
  }

  public String getStreamDescription() {
    return streamDescription;
  }

  public String getStreamId() {
    return streamId;
  }

  public String getStreamImage() {
    return streamImage;
  }

  public ArrayList<String> getStreamTags() {
    return streamTags;
  }

  public ArrayList<String> getMembers() {
    return members;
  }

  public String getStreamTitle() {
    return streamTitle;
  }

  public String getUserId() {
    return userId;
  }

  public int getViewersCount() {
    return viewersCount;
  }

  public int getUserType() {
    return userType;
  }

  public boolean isHdBroadcast() {
    return hdBroadcast;
  }

  public boolean isRestream() {
    return restream;
  }

  public boolean isPkChallenge() {
    return isPkChallenge;
  }

  public void setPkChallenge(boolean pkChallenge) {
    isPkChallenge = pkChallenge;
  }

  public String getPkId() {
    return pkId;
  }

  public void setPkId(String pkId) {
    this.pkId = pkId;
  }

  public boolean isProductsLinked() {
    return productsLinked;
  }

  public int getProductsCount() {
    return productsCount;
  }

  public ArrayList<Integer> getStreamTypes() {
    if (streamTypes == null) {
      streamTypes = new ArrayList<>();
      streamTypes.add(StreamCategoryEnum.All.getValue());
    }
    return streamTypes;
  }

  public LiveStreamsResponse.Stream.UserDetails getUserDetails() {
    return userDetails;
  }

  public LiveStreamsResponse.Stream.UserDetails getFirstUserDetails() {
    return firstUserDetails;
  }

  public void setFirstUserDetails(LiveStreamsResponse.Stream.UserDetails firstUserDetails) {
    this.firstUserDetails = firstUserDetails;
  }

  public LiveStreamsResponse.Stream.UserDetails getSecondUserDetails() {
    return secondUserDetails;
  }

  public void setSecondUserDetails(LiveStreamsResponse.Stream.UserDetails secondUserDetails) {
    this.secondUserDetails = secondUserDetails;
  }

  public boolean getSelfHosted() {
    return selfHosted;
  }

  public void setSelfHosted(boolean selfHosted) {
    this.selfHosted = selfHosted;
  }

  public boolean isRtmpIngest() {
    return rtmpIngest;
  }

  public void setRtmpIngest(boolean rtmpIngest) {
    this.rtmpIngest = rtmpIngest;
  }
}
