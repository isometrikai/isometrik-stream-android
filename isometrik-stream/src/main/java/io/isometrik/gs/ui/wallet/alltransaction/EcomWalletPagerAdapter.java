package io.isometrik.gs.ui.wallet.alltransaction;



import static io.isometrik.gs.ui.utils.Constants.ZERO;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * pager adapter for the  wallet
 */
public class EcomWalletPagerAdapter extends FragmentPagerAdapter {
  private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
  private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

  public EcomWalletPagerAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList != null ? mFragmentList.size() : ZERO;
  }

  /**
   * <P>This method is used to add the fragment to the tab layout </P>
   * this method will internally called by the fragment pager adapter
   *
   * @param fragment is a variable of type fragment
   * @param frag1    is a variable of type string
   */
  public void addFragment(Fragment fragment, String frag1) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(frag1);
  }

  /**
   * <P>This method is used to getProductList the title of the fragment</P>
   *
   * @param position is a variable of type int
   * @return it will returns the title of the fragment
   */
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }
}
