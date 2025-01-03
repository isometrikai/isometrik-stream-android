package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.ui.gifts.response.GiftCategoriesResponse;
import io.isometrik.gs.ui.gifts.response.GiftSentResponse;
import io.isometrik.gs.ui.gifts.response.GiftsByCategoryResponse;
import io.isometrik.gs.ui.wallet.response.WalletInAppPurchaseDataResponse;
import io.isometrik.gs.ui.wallet.response.WalletResponseData;
import io.isometrik.gs.ui.wallet.transaction.Model.WalletTransactionResponseData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

/**
 * The interface WalletService to fetch wallet balance and transactions
 */
public interface WalletService {

    /**
     * Fetch currency Plans.
     *
     * @param headers the headers
     * @see WalletInAppPurchaseDataResponse
     */
    @GET("/v1/currencyPlan/isometrikAuth")
    Call<WalletInAppPurchaseDataResponse> fetchCurrencyPlans(@HeaderMap Map<String, String> headers);

    /**
     * Fetch wallet balance.
     *
     * @param headers the headers
     * @see WalletResponseData
     */
    @GET("/v1/wallet/user")
    Call<WalletResponseData> getWalletBalance(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

    /**
     * Fetch transactions.
     *
     * @param headers the headers
     * @see WalletResponseData
     */
    @GET("/v1/transaction/user")
    Call<WalletTransactionResponseData> getTransactions(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

}
