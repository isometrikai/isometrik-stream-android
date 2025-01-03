package io.isometrik.gs.ui.wallet;


import static io.isometrik.gs.ui.utils.Constants.ONE;
import static io.isometrik.gs.ui.utils.Constants.SIX;
import static io.isometrik.gs.ui.utils.Constants.ZERO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.ItemTransactionBinding;
import io.isometrik.gs.ui.utils.DateAndTimeUtil;
import io.isometrik.gs.ui.utils.Utilities;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionItemData;

/**
 * adapter class for the  wallet transactions
 */
public class WalletTransactionsAdapter extends
    RecyclerView.Adapter<WalletTransactionsAdapter.WalletViewHolder> {
  private ArrayList<WalletTransactionItemData> mWalletTransactionItemData;
  private Context mContext;

  public WalletTransactionsAdapter(
      ArrayList<WalletTransactionItemData> walletTransactionItemData) {
    mWalletTransactionItemData = walletTransactionItemData;
  }

  @NonNull
  @Override
  public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    mContext = parent.getContext();
    ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

    return new WalletViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull WalletViewHolder holder,
      int position) {
    WalletTransactionItemData walletTransactionItemData = mWalletTransactionItemData.get(position);
    holder.mBinding.tvTransactionId.setText(
        String.format("%s %s", mContext.getResources().getString(R.string.transactionId), walletTransactionItemData.getTxnid().length() > ZERO ?
                getMorphedTxnId(walletTransactionItemData.getTxnid()) : walletTransactionItemData.getTxnid()));
    holder.mBinding.tvDescription.setText(walletTransactionItemData.getDescription());
    try {
      holder.mBinding.tvTransactionDate.setText(
              DateAndTimeUtil.getTransactionTime(walletTransactionItemData.getTxntimestamp()));
    } catch (Exception e){
      try {
        holder.mBinding.tvTransactionDate.setText(
                Utilities.convertTimeStamptoString(
                        Long.parseLong(walletTransactionItemData.getTxntimestamp()),
                        "dd/MMMM/yyyy HH:mm"
                ));
      } catch (Exception exp){
        holder.mBinding.tvTransactionDate.setText("");
      }
    }
    holder.mBinding.tvAmount.setText(
        String.format("%s", Utilities.groupPriceFormatter(walletTransactionItemData.getCurrency(), Utilities.convertPriceInToDecimalIfDecimalPresent(walletTransactionItemData.getAmount()))));
    holder.mBinding.ivMark.setImageDrawable(
        walletTransactionItemData.getTxntype() != null
            && Integer.parseInt(walletTransactionItemData.getTxntype()) == ONE
            ? mContext.getResources().getDrawable(R.drawable.ic_arraow_credit)
            : mContext.getResources().getDrawable(R.drawable.ic_arrow_debit));
  }

  /**
   * return only last six digit of transaction Id hidden everything else by ****
   * @param transactionId String
   * @return String
   */
  private String getMorphedTxnId(String transactionId) {
    String stars = "*****";
    StringBuilder morphedId = new StringBuilder(stars);
    morphedId.append((transactionId.length() > SIX
            ? transactionId.substring(transactionId.length() - SIX) : transactionId));
    return morphedId.toString();
  }

  @Override
  public int getItemCount() {
    return mWalletTransactionItemData != null ? mWalletTransactionItemData.size() : ZERO;
  }

  /**
   * view holder class for the wallet item
   */
  class WalletViewHolder extends RecyclerView.ViewHolder {
    private ItemTransactionBinding mBinding;

    WalletViewHolder(@NonNull ItemTransactionBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }

  /**
   * update the data list in case of different transaction type is selected
   * @param data ArrayList
   */
  public void updateList(ArrayList<WalletTransactionItemData> data) {
    mWalletTransactionItemData.clear();
    mWalletTransactionItemData.addAll(data);
    notifyDataSetChanged();
  }
}
