package io.isometrik.gs.ivs.preview;

import java.util.Objects;

public class BroadcastPreviewTextMetadataCue extends BroadcastPreviewCue {
  public final String description;
  public final String text;

  public BroadcastPreviewTextMetadataCue(long startTime, long endTime, String description,
      String text) {
    super(startTime, endTime);
    this.description = description;
    this.text = text;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o != null && this.getClass() == o.getClass()) {
      if (!super.equals(o)) {
        return false;
      } else {
        BroadcastPreviewTextMetadataCue textMetadataCue = (BroadcastPreviewTextMetadataCue) o;
        return this.description.equals(textMetadataCue.description) && this.text.equals(
            textMetadataCue.text);
      }
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(this.startTime, this.endTime, this.description, this.text);
  }
}