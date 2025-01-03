package io.isometrik.gs.ivs.preview;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class BroadcastPreviewQuality {
  private final String name;
  private final String codecs;
  private final int bitrate;
  private final int width;
  private final int height;
  private final float framerate;

  public BroadcastPreviewQuality(String name, String codecs, int bitrate, int width, int height,
      float framerate) {
    this.name = name;
    this.codecs = codecs;
    this.bitrate = bitrate;
    this.width = width;
    this.height = height;
    this.framerate = framerate;
  }

  public @NotNull String toString() {
    return String.format(Locale.ROOT,
        "name: %s, codecs %s, bitrate %d, resolution %d x %d framerate %.2f", this.name,
        this.codecs, this.bitrate, this.width, this.height, this.framerate);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o != null && this.getClass() == o.getClass()) {
      BroadcastPreviewQuality quality = (BroadcastPreviewQuality) o;
      return this.bitrate == quality.bitrate
          && this.width == quality.width
          && this.height == quality.height
          && Float.compare(quality.framerate, this.framerate) == 0
          && this.name.equals(quality.name)
          && this.codecs.equals(quality.codecs);
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(this.name, this.codecs, this.bitrate, this.width, this.height,
        this.framerate);
  }

  @NotNull
  public String getName() {
    return this.name;
  }

  @NotNull
  public String getCodecs() {
    return this.codecs;
  }

  public int getBitrate() {
    return this.bitrate;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public float getFramerate() {
    return this.framerate;
  }
}

