package io.isometrik.gs.ui.wallet.alltransaction;


import static java.lang.Boolean.TRUE;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

import io.isometrik.gs.builder.wallet.TransactionsQuery;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.utils.Utilities;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionData;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionItemData;

/**
 * holds the data for AllTransactionsActivity
 */
public class AllTransactionsViewModel extends ViewModel {

    public MutableLiveData<Boolean> progressVisible = new MutableLiveData<>(TRUE);
    private MutableLiveData<String> mWalletData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mEmptyTransations = new MutableLiveData<>();
    private MutableLiveData<String> mMsg = new MutableLiveData<>();
    private MutableLiveData<Boolean> mBackLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsDataReceived = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>>
            mAllTransactionLiveData = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>>
            mCreditTransactionLiveData = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>>
            mDebitTransactionLiveData = new MutableLiveData<>();

    public FragmentType selectedFragmentType = FragmentType.ALL;
    boolean isAllTransaction, isCashWalletTransaction, isRewardBonusTransaction;
    String mWalletId = "", mWalletEarningId = "", mCurrency = "";


    void updateCurrency(String mCurrency){
        this.mCurrency = mCurrency;
    }
    /**
     * to get wallet balance
     */
    void fetchTransaction(String txnType) {
        Boolean txnSpecific = true;
        if (txnType.isEmpty()) {
            txnSpecific = false;
        }
        TransactionsQuery.Builder transactionsQuery = new TransactionsQuery.Builder()
                .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                .setCurrency(mCurrency)
                .setTxnSpecific(txnSpecific)
                .setTxnType(txnType);
        IsometrikStreamSdk.getInstance().getIsometrik().getRemoteUseCases()
                .getWalletUseCases()
                .fetchTransaction(transactionsQuery.build(), (var1, var2) -> {
                    if (var1 != null) {
                        isAllTransaction = true;
                        ArrayList<WalletTransactionItemData> transactionList = new ArrayList<>();
                        if (!var1.getData().isEmpty()) {
                            for (WalletTransactionData transactionData : var1.getData()) {
                                transactionList.add(
                                        new WalletTransactionItemData(
                                                transactionData.getWalletId(),
                                                transactionData.getAmount(),
                                                transactionData.getNotes(),
                                                transactionData.getTxnType(),
                                                transactionData.getTxnTimeStamp(),
                                                transactionData.getClosingBalance(),
                                                transactionData.getDescription(),
                                                null,
                                                transactionData.getOpeningBalance(),
                                                (transactionData.getTxnType() != null && transactionData.getTxnType().trim().equalsIgnoreCase("CREDIT")) ? "1" : "0",
                                                transactionData.getCurrency(),
                                                null,
                                                null,
                                                transactionData.getTransactionId()
                                        )
                                );
                            }
                            if (txnType.equals("CREDIT")) {
                                mCreditTransactionLiveData.postValue(
                                        Pair.create(var1.getTotalCount(),
                                                transactionList));
                            } else if (txnType.equals("DEBIT")) {
                                mDebitTransactionLiveData.postValue(
                                        Pair.create(var1.getTotalCount(),
                                                transactionList));
                            } else {
                                mAllTransactionLiveData.postValue(
                                        Pair.create(var1.getTotalCount(),
                                                transactionList));
                            }
                        }
                        mIsDataReceived.postValue(true);
                    } else {

                        isAllTransaction = true;
                        mIsDataReceived.postValue(TRUE);
                        Utilities.printLog("Wallet Fail" + var2.getErrorMessage());
                    }
                });
    }

    void fragmentSelected(FragmentType fragmentType){
        selectedFragmentType = fragmentType;
    }


    /**
     * notify wallet data comes
     **/
    MutableLiveData<Boolean> getEmptyTransactions() {
        return mEmptyTransations;
    }

    /**
     * notify wallet transaction data comes
     *
     * @return mutable live data of transactions
     */
    MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>> getAllTransactionLiveData() {
        return mAllTransactionLiveData;
    }

    /**
     * notify wallet transaction data comes
     *
     * @return mutable live data of transactions
     */
    MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>> getCreditTransactionLiveData() {
        return mCreditTransactionLiveData;
    }

    /**
     * notify wallet transaction data comes
     *
     * @return mutable live data of transactions
     */
    MutableLiveData<Pair<Integer, ArrayList<WalletTransactionItemData>>> getDebitTransactionLiveData() {
        return mDebitTransactionLiveData;
    }



    /**
     * get currency code
     *
     * @return String
     */
    public String getCurrency() {
        return mCurrency;
    }


}
