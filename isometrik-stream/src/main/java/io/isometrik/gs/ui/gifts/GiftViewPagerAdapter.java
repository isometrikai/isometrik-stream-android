package io.isometrik.gs.ui.gifts;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import io.isometrik.gs.ui.gifts.response.GiftCategoriesResponse;
import io.isometrik.gs.ui.gifts.response.GiftCategory;


public class GiftViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private Fragment fragment = null;
    private List<GiftCategory> categories;
    private GiftAdapter.GiftListener listener;

    public GiftViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<GiftCategory> categories,  GiftAdapter.GiftListener listener) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        for (int i = 0; i < mNumOfTabs; i++) {
            if (i == position) {
                fragment =
                    GiftFragment.newInstance(i, categories.get(i), listener);
                break;
            }
        }
        return fragment;
    }

    //@Override
    //public void restoreState(Parcelable state, ClassLoader loader) {
    //    try {
    //        super.restoreState(state, loader);
    //    } catch (Exception e) {
    //        Log.e("TAG", "Error Restore State of Fragment : " + e.getMessage(), e);
    //    }
    //}
    @Override
    public Parcelable saveState() {
        return null;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getGiftTitle();
    }
}
