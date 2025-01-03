package io.isometrik.gs.rtcengine.rtc.webrtc;

/**
 * Properties of the audio volume information.Object containing the user ID and volume information
 * for each speaker.
 */
public class RemoteUsersAudioLevelInfo {

  private final int memberUid;
  private final String memberId;
  private final float audioLevel;
  private final boolean isSpeaking;

  public RemoteUsersAudioLevelInfo(String memberId, int memberUid, float audioLevel,
      boolean isSpeaking) {
    this.memberId = memberId;
    this.memberUid = memberUid;
    this.audioLevel = audioLevel;
    this.isSpeaking = isSpeaking;
  }

  public int getMemberUid() {
    return memberUid;
  }

  public float getAudioLevel() {
    return audioLevel;
  }

  public String getMemberId() {
    return memberId;
  }

  public boolean isSpeaking() {
    return isSpeaking;
  }
}
