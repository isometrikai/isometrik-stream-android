package io.isometrik.gs.ui.dominantspeaker

import java.util.Random

class Utils {
  companion object {
    /**
     * get Random Number
     */
    @JvmStatic
    fun getRandomNumber(min: Int, max: Int): Int {
      return Random().nextInt(max - min + 1) + min
    }

  }
}
