package io.isometrik.gs.ui.streams.categories;

import android.content.Context;
import io.isometrik.gs.ui.R;

public class StreamCategoriesUtil {

  public static String getStreamCategory(Context context, int position) {

    switch (position) {

      case 0:
        return context.getString(R.string.ism_all);
      case 1:
        return context.getString(R.string.ism_audio_only);
      case 2:
        return context.getString(R.string.ism_multilive);
      case 3:
        return context.getString(R.string.ism_private_streams);
      case 4:
        return context.getString(R.string.ism_ecommerce);
      case 5:
        return context.getString(R.string.ism_restream);
      case 6:
        return context.getString(R.string.ism_hd);
      case 7:
        return context.getString(R.string.ism_recorded);
      default:
        return "";
    }
  }
}