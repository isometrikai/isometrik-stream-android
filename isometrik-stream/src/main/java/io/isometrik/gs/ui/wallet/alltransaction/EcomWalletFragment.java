package io.isometrik.gs.ui.wallet.alltransaction;


import static io.isometrik.gs.ui.utils.Constants.ALL_WALLET;
import static io.isometrik.gs.ui.utils.Constants.ONE;
import static io.isometrik.gs.ui.utils.Constants.WALLET_CODE;
import static io.isometrik.gs.ui.utils.Constants.ZERO;
import static io.isometrik.gs.ui.utils.Utilities.isEmptyArray;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.FragmentWalletAllTransactionBinding;
import io.isometrik.gs.ui.utils.MyScrollListener;
import io.isometrik.gs.ui.utils.Utilities;
import io.isometrik.gs.ui.wallet.WalletTransactionsAdapter;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionItemData;


/**
 * fragment for wallet transactions
 */
public class EcomWalletFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
  private FragmentWalletAllTransactionBinding mWalletTransactionBinding;
  private WalletTransactionsAdapter mWalletTransactionsAdapter;
  private ArrayList<WalletTransactionItemData> mWalletTransactionItemData = new ArrayList<>();
  private ArrayList<WalletTransactionItemData> mTransactionsLog = new ArrayList<>();
  private boolean isForAllTransaction, isForCashWallet, isForRewardBonus;
  private String mCurrency = "";

  public EcomWalletFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    mWalletTransactionBinding = FragmentWalletAllTransactionBinding.inflate(inflater, container, false);
    return mWalletTransactionBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    initialize();
    mWalletTransactionBinding.rgTransactionType.setOnCheckedChangeListener(this);
  }

  /**
   * does Initialization of basic fragment resources
   */
  private void initialize() {
    if (getArguments() != null) {
      mWalletTransactionBinding.rbAllTransactions.setChecked(true);
      mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.base_color));
      mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionItemData.clear();
      int request = getArguments().getInt(WALLET_CODE);
      if (!isEmptyArray(getArguments().getParcelableArrayList(ALL_WALLET))) {
        isForAllTransaction = true;
        mWalletTransactionBinding.rgTransactionType.setVisibility(request == ZERO ? View.GONE : View.VISIBLE);
        mWalletTransactionItemData.clear();
        mTransactionsLog.clear();
        mWalletTransactionItemData.addAll(getArguments().getParcelableArrayList(ALL_WALLET));
        mCurrency = IsometrikStreamSdk.getInstance().getUserSession().getCurrency();

        mTransactionsLog.addAll(getArguments().getParcelableArrayList(ALL_WALLET));
      }/* else if (request == ONE && !isEmptyArray(getArguments().getParcelableArrayList(CASH_WALLET))) {
        isForCashWallet = true;
        mWalletTransactionBinding.rbAllTransactions.setChecked(true);
        mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.colorPrimary));
        mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
        mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
        mWalletTransactionItemData.clear();
        mTransactionsLog.clear();
        mWalletTransactionItemData.addAll(getArguments().getParcelableArrayList(CASH_WALLET));
        mCurrency = AppController.currency;
        mTransactionsLog.addAll(getArguments().getParcelableArrayList(CASH_WALLET));
      } else if (!isEmptyArray(getArguments().getParcelableArrayList(REWARD_WALLET))) {
        isForRewardBonus = true;
        mWalletTransactionBinding.rbAllTransactions.setChecked(true);
        mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.colorPrimary));
        mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
        mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
        mWalletTransactionItemData.clear();
        mTransactionsLog.clear();
        mWalletTransactionItemData.addAll(getArguments().getParcelableArrayList(REWARD_WALLET));
        mCurrency = AppController.currency;
        mTransactionsLog.addAll(getArguments().getParcelableArrayList(REWARD_WALLET));
      }*/
      Utilities.printLog("wallet Fra " + "initialize" + "mWalletTransactionItemData"
          + mTransactionsLog.size());
      mWalletTransactionBinding.vgNoTransactionsFound.setVisibility(
          mTransactionsLog.size() > ZERO ? View.GONE : View.VISIBLE);
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
      mWalletTransactionBinding.rvWalletTransaction.setLayoutManager(linearLayoutManager);

      mWalletTransactionBinding.ivEmptyTransactions.setVisibility((mTransactionsLog == null
          || mTransactionsLog.size() == 0) ? View.VISIBLE : View.GONE);
      mWalletTransactionBinding.tvNoTransactions.setVisibility((mTransactionsLog == null
          || mTransactionsLog.size() == 0) ? View.VISIBLE : View.GONE);
      if (isEmptyArray(mTransactionsLog)) {
        mWalletTransactionBinding.rgTransactionType.setVisibility(request == ZERO ? View.GONE : View.VISIBLE);
      }
      mWalletTransactionsAdapter = new WalletTransactionsAdapter(mWalletTransactionItemData);
      mWalletTransactionBinding.rvWalletTransaction.setAdapter(mWalletTransactionsAdapter);
      if (getActivity() instanceof AllTransactionsActivity) {
        AllTransactionsActivity ecomWalletActivity = (AllTransactionsActivity) getActivity();
        mWalletTransactionBinding.rvWalletTransaction.addOnScrollListener(
            new MyScrollListener(linearLayoutManager) {
              @Override
              protected void loadMoreItems() {
//                Utilities.printLog(
//                    "itemLoadingVisible" + mWalletTransactionItemData.size() + "count"
//                        + ecomWalletActivity.mAllTransactionTotalCount);
//                if (request == ZERO && (mWalletTransactionItemData.size()
//                    < ecomWalletActivity.mAllTransactionTotalCount)) {
//                  ecomWalletActivity.callMoreItemsTransactions(isForAllTransaction, isForCashWallet);
//                }
              }

              @Override
              public boolean isLastPage() {
                return ecomWalletActivity.isLoading();
              }

              @Override
              public boolean isLoading() {
                return ecomWalletActivity.isLoading();
              }
            });
      }
    }
  }

  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {
    if (mWalletTransactionsAdapter == null) return;
    if (checkedId == R.id.rbAllTransactions) {
      ArrayList<WalletTransactionItemData> allList = new ArrayList<>(mTransactionsLog);
      mWalletTransactionBinding.tvNoTransactions.setVisibility(isEmptyArray(allList) ? View.VISIBLE : View.GONE);
      mWalletTransactionBinding.ivEmptyTransactions.setVisibility(isEmptyArray(allList) ? View.VISIBLE : View.GONE);
      mWalletTransactionsAdapter.updateList(allList);
      mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.base_color));
      mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
    } else if (checkedId == R.id.rbDebitTransactions) {
      ArrayList<WalletTransactionItemData> debitList = new ArrayList<>();
      for (WalletTransactionItemData itemData : mTransactionsLog) {
        if (itemData.getTxntype() != null && Integer.parseInt(itemData.getTxntype()) == ONE) ;
        else {
          debitList.add(itemData);
        }
      }
      mWalletTransactionBinding.tvNoTransactions.setVisibility(isEmptyArray(debitList) ? View.VISIBLE : View.GONE);
      mWalletTransactionBinding.ivEmptyTransactions.setVisibility(isEmptyArray(debitList) ? View.VISIBLE : View.GONE);
      mWalletTransactionsAdapter.updateList(debitList);
      mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.base_color));
      mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
    } else if (checkedId == R.id.rbCreditTransactions) {
      ArrayList<WalletTransactionItemData> creditList = new ArrayList<>();
      for (WalletTransactionItemData itemData : mTransactionsLog) {
        if (itemData.getTxntype() != null && Integer.parseInt(itemData.getTxntype()) == ONE) {
          creditList.add(itemData);
        }
      }
      mWalletTransactionBinding.tvNoTransactions.setVisibility(isEmptyArray(creditList) ? View.VISIBLE : View.GONE);
      mWalletTransactionBinding.ivEmptyTransactions.setVisibility(isEmptyArray(creditList) ? View.VISIBLE : View.GONE);
      mWalletTransactionsAdapter.updateList(creditList);
      mWalletTransactionBinding.rbAllTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionBinding.rbDebitTransactions.setTextColor(getResources().getColor(R.color.allCadetBlue));
      mWalletTransactionBinding.rbCreditTransactions.setTextColor(getResources().getColor(R.color.base_color));
    }
  }
}
