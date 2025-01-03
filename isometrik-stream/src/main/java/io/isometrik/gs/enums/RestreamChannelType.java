package io.isometrik.gs.enums;

public enum RestreamChannelType {

  Facebook(0),
  Youtube(1),
  Twitch(2),
  Twitter(3),
  LinkedIn(4),
  CustomRtmp(5);

  private final int value;

  RestreamChannelType(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final int getValue() {
    return this.value;
  }
}
