package io.isometrik.gs.ui.streams.list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.gs.ui.streams.categories.StreamCategoryEnum;

public class StreamsPagerAdapter extends FragmentStateAdapter {

  private static final int STREAM_CATEGORIES_COUNT = 8;

  StreamsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = new StreamsFragment();
    Bundle args = new Bundle();
    switch (position) {
      case 0:

        //All
        args.putInt("streamType", StreamCategoryEnum.All.getValue());
        fragment.setArguments(args);
        return fragment;

      case 1:

        //audio-only
        args.putInt("streamType", StreamCategoryEnum.AudioOnly.getValue());
        fragment.setArguments(args);
        return fragment;

      case 2:

        //multilive
        args.putInt("streamType", StreamCategoryEnum.Multilive.getValue());
        fragment.setArguments(args);
        return fragment;

      case 3:

        //private
        args.putInt("streamType", StreamCategoryEnum.Private.getValue());
        fragment.setArguments(args);
        return fragment;

      case 4:

        //ecommerce
        args.putInt("streamType", StreamCategoryEnum.Ecommerce.getValue());
        fragment.setArguments(args);
        return fragment;

      case 5:

        //restream
        args.putInt("streamType", StreamCategoryEnum.Restream.getValue());
        fragment.setArguments(args);
        return fragment;

      case 6:

        //hd
        args.putInt("streamType", StreamCategoryEnum.Hd.getValue());
        fragment.setArguments(args);
        return fragment;
      case 7:

        //recorded
        args.putInt("streamType", StreamCategoryEnum.Recorded.getValue());
        fragment.setArguments(args);
        return fragment;
      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return STREAM_CATEGORIES_COUNT;
  }
}