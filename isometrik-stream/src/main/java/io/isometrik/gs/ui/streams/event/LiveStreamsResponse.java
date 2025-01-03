package io.isometrik.gs.ui.streams.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveStreamsResponse implements Serializable {

  @SerializedName("streams")
  @Expose
  private ArrayList<Stream> streams;

  @SerializedName("message")
  @Expose
  private String message;

  public static class Stream {
    @SerializedName("alreadyPaid")
    @Expose
    private boolean alreadyPaid;
    @SerializedName("audioOnly")
    @Expose
    private boolean audioOnly;
    @SerializedName("coinsCount")
    @Expose
    private Integer coinsCount= 0;
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
    private UserDetails userDetails;

    @SerializedName("firstUserDetails")
    @Expose
    private UserDetails firstUserDetails;

    @SerializedName("secondUserDetails")
    @Expose
    private UserDetails secondUserDetails;

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

      @SerializedName(value = "walletUserId", alternate = "userId")
      @Expose
      private String userId;
      @SerializedName("firstName")
      @Expose
      private String firstName;
      @SerializedName("lastName")
      @Expose
      private String lastName;
      @SerializedName("userName")
      @Expose
      private String userName;
      @SerializedName(value = "userProfile", alternate = "profilePic")
      @Expose
      private String userProfile;
      @SerializedName("isFollow")
      @Expose
      private int followStatus;
      @SerializedName("isStar")
      @Expose
      private boolean isStar;
      @SerializedName("streamImage")
      @Expose
      private String streamImage;
      @SerializedName(value = "streamUserId", alternate = "isometrikUserId")
      @Expose
      private String streamUserId;
      @SerializedName("streamId")
      @Expose
      private String streamId;
      @SerializedName("isomatricChatUserId")
      @Expose
      private String isomatricChatUserId;

      @SerializedName("metaData")
      @Expose
      private UserMetaData userMetaData;

      public UserMetaData getUserMetaData() {
        return userMetaData;
      }

      public void setUserMetaData(UserMetaData userMetaData) {
        this.userMetaData = userMetaData;
      }

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

      public String getUserId() {
        return userId;
      }

      public String getStreamImage() {
        return streamImage;
      }

      public void setStreamImage(String streamImage) {
        this.streamImage = streamImage;
      }

      public void setUserId(String userId) {
        this.userId = userId;
      }

      public String getStreamUserId() {
        return streamUserId;
      }

      public void setStreamUserId(String streamUserId) {
        this.streamUserId = streamUserId;
      }

      public String getStreamId() {
        return streamId;
      }

      public void setStreamId(String streamId) {
        this.streamId = streamId;
      }

      public String getIsomatricChatUserId() {
        return isomatricChatUserId;
      }
    }

    public static class UserMetaData{
      @SerializedName("userId")
      @Expose
      private String userId;

      public String getUserId() {
        return userId;
      }

      public void setUserId(String userId) {
        this.userId = userId;
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

    public UserDetails getUserDetails() {
      return userDetails;
    }

    public UserDetails getFirstUserDetails() {
      return firstUserDetails;
    }

    public void setFirstUserDetails(UserDetails firstUserDetails) {
      this.firstUserDetails = firstUserDetails;
    }

    public UserDetails getSecondUserDetails() {
      return secondUserDetails;
    }

    public void setSecondUserDetails(UserDetails secondUserDetails) {
      this.secondUserDetails = secondUserDetails;
    }

    public boolean isHdBroadcast() {
      return hdBroadcast;
    }

    public boolean isRestream() {
      return restream;
    }

    public boolean isProductsLinked() {
      return productsLinked;
    }

    public int getProductsCount() {
      return productsCount;
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

    public boolean getSelfHosted() {
      return selfHosted;
    }

    public void setSelfHosted( boolean selfHosted) {
      this.selfHosted = selfHosted;
    }

    public boolean isRtmpIngest() {
      return rtmpIngest;
    }

    public void setRtmpIngest(boolean rtmpIngest) {
      this.rtmpIngest = rtmpIngest;
    }
  }

  public ArrayList<Stream> getStreams() {
    return streams;
  }

  public String getMessage() {
    return message;
  }




}
