package io.isometrik.gs.ui.recordings.dialogs;

public class PlaybackOptionsItem {

  private final String option;
  private final boolean selected;

  public PlaybackOptionsItem(String option, boolean selected) {
    this.option = option;
    this.selected = selected;
  }

  public String getOption() {
    return option;
  }

  public boolean isSelected() {
    return selected;
  }
}
