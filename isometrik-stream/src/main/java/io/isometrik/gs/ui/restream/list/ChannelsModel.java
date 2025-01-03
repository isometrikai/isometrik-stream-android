package io.isometrik.gs.ui.restream.list;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.enums.RestreamChannelType;
import io.isometrik.gs.response.restream.FetchRestreamChannelsResult;

public class ChannelsModel {

  private final String channelName, ingestUrl, channelId;
  private final int channelTypeDrawable, channelType;
  private boolean enabled;

  public ChannelsModel(FetchRestreamChannelsResult.RestreamChannel channel) {
    channelName = channel.getChannelName();
    ingestUrl = channel.getIngestUrl();
    channelId = channel.getChannelId();
    int channelType = channel.getChannelType();
    this.channelType = channelType;
    enabled = channel.isEnabled();
    if (channelType == RestreamChannelType.Facebook.getValue()) {
      channelTypeDrawable = R.drawable.ism_restream_facebook_preview;
    } else if (channelType == RestreamChannelType.Youtube.getValue()) {
      channelTypeDrawable = R.drawable.ism_restream_youtube_preview;
    } else if (channelType == RestreamChannelType.Twitch.getValue()) {
      channelTypeDrawable = R.drawable.ism_restream_twitch_preview;
    } else if (channelType == RestreamChannelType.Twitter.getValue()) {
      channelTypeDrawable = R.drawable.ism_restream_twitter_preview;
    } else if (channelType == RestreamChannelType.LinkedIn.getValue()) {
      channelTypeDrawable = R.drawable.ism_restream_linkedin_preview;
    } else {
      channelTypeDrawable = R.drawable.ism_restream_custom_rtmp_preview;
    }
  }

  public String getChannelName() {
    return channelName;
  }

  public String getIngestUrl() {
    return ingestUrl;
  }

  public int getChannelTypeDrawable() {
    return channelTypeDrawable;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public int getChannelType() {
    return channelType;
  }

  public boolean isEnabled() {
    return enabled;
  }
}
