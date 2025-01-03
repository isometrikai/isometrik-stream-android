package io.isometrik.gs.ui.restream;

public interface RestreamChannelsActionCallback {

  void editChannel(String channelId, String channelName, String ingestUrl, boolean enabled,
      int channelType);

  void addChannel();
}
