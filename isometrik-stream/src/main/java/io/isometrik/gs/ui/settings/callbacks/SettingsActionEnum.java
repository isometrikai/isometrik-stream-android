package io.isometrik.gs.ui.settings.callbacks;

public enum SettingsActionEnum {

  /**
   * Chat messages visibility toggles action type enum.
   */
  ChatMessagesVisibilityToggleAction(0),
  /**
   * Control buttons visibility toggles action type enum.
   */
  ControlButtonsVisibilityToggleAction(1),

  NetworkQualityVisibilityToggleAction(2);


  private final int value;

  SettingsActionEnum(int value) {
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