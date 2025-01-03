package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.wallet.CurrencyPlansQuery;
import io.isometrik.gs.builder.wallet.TransactionsQuery;
import io.isometrik.gs.builder.wallet.WalletBalanceQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.ui.wallet.CurrencyPlans;
import io.isometrik.gs.ui.wallet.Transactions;
import io.isometrik.gs.ui.wallet.WalletBalance;
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseDataResponse;
import io.isometrik.gs.ui.wallet.response.WalletResponseData;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionResponseData;

/**
 * Classes containing use cases for various users operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class WalletUseCases {

  /**
   * Model classes for user
   */
  private final CurrencyPlans currencyPlans;
  private final WalletBalance walletBalance;
  private final Transactions transactions;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public WalletUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
                        BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    currencyPlans = new CurrencyPlans();
    walletBalance = new WalletBalance();
    transactions = new Transactions();
  }

  /**
   * Fetch currency plans.
   *
   * @param currencyPlansQuery the fetch users query
   * @param completionHandler the completion handler
   */
  public void fetchCurrencyPlans(@NotNull CurrencyPlansQuery currencyPlansQuery,
                         @NotNull CompletionHandler<WalletInAppPurchaseDataResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      currencyPlans.validateParams(currencyPlansQuery, completionHandler, retrofitManager, configuration,
              baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch currency plans.
   *
   * @param walletBalanceQuery the fetch wallet data
   * @param completionHandler the completion handler
   */
  public void fetchWalletBalance(@NotNull WalletBalanceQuery walletBalanceQuery,
                                 @NotNull CompletionHandler<WalletResponseData> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      walletBalance.validateParams(walletBalanceQuery, completionHandler, retrofitManager, configuration,
              baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch currency plans.
   *
   * @param transactionsQuery the fetch wallet data
   * @param completionHandler the completion handler
   */
  public void fetchTransaction(@NotNull TransactionsQuery transactionsQuery,
                                 @NotNull CompletionHandler<WalletTransactionResponseData> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      transactions.validateParams(transactionsQuery, completionHandler, retrofitManager, configuration,
              baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }


}
