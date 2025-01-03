package io.isometrik.gs.rtcengine.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The class to manage isometrik RTC engine stats.
 */
public class IsometrikRtcStatsManager {
  private final List<Integer> mUidList = new ArrayList<>();
  private final Map<Integer, StatsData> mDataMap = new HashMap<>();
  private boolean mEnable = false;

  /**
   * Add user stats.
   *
   * @param uid the uid of the user whose stats are to be added
   * @param ifLocal whether the user whose stats are added,is local user or remote user
   */
  public void addUserStats(int uid, boolean ifLocal) {
    if (mUidList.contains(uid) && mDataMap.containsKey(uid)) {
      return;
    }

    StatsData data = ifLocal ? new LocalStatsData() : new RemoteStatsData();
    // in case 32-bit unsigned integer uid is received
    data.setUid(uid & 0xFFFFFFFFL);

    if (ifLocal) {
      mUidList.add(0, uid);
    } else {
      mUidList.add(uid);
    }

    mDataMap.put(uid, data);
  }

  /**
   * Remove user stats.
   *
   * @param uid the uid of the user whose stats are removed
   */
  public void removeUserStats(int uid) {
    if (mUidList.contains(uid) && mDataMap.containsKey(uid)) {
      mUidList.remove((Integer) uid);
      mDataMap.remove(uid);
    }
  }

  /**
   * Gets stats data.
   *
   * @param uid the uid of the user whose stats data is to be fetched
   * @return the stats data
   * @see StatsData
   */
  public StatsData getStatsData(int uid) {
    if (mUidList.contains(uid) && mDataMap.containsKey(uid)) {
      return mDataMap.get(uid);
    } else {
      return null;
    }
  }

  /**
   * Enable stats.
   *
   * @param enabled whether the stats to be enabled or disabled
   */
  public void enableStats(boolean enabled) {
    mEnable = enabled;
  }

  /**
   * Is enabled boolean.
   *
   * @return whether the stats are currently enabled or disabled
   */
  public boolean isEnabled() {
    return mEnable;
  }

  /**
   * Clear all data.
   */
  public void clearAllData() {
    mUidList.clear();
    mDataMap.clear();
  }
}
