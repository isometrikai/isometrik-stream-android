package io.isometrik.gs.ivs.preview;


import java.util.Objects;

public  abstract class BroadcastPreviewCue {
  public final long startTime;
  public final long endTime;

  public BroadcastPreviewCue(long startTime, long endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o != null && this.getClass() == o.getClass()) {
      BroadcastPreviewCue cue = (BroadcastPreviewCue) o;
      return this.startTime == cue.startTime && this.endTime == cue.endTime;
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(this.startTime, this.endTime);
  }
}
