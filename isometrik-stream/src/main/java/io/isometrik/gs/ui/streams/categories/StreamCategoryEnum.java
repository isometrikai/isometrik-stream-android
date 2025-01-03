package io.isometrik.gs.ui.streams.categories;

public enum StreamCategoryEnum {

  All(0),
  AudioOnly(1),
  Multilive(2),
  Private(3),
  Ecommerce(4),
  Restream(5),
  Hd(6),
  Recorded(7),
  PK(11);

  private final int value;

  StreamCategoryEnum(int value) {
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