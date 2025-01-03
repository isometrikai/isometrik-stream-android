package io.isometrik.gs.ui.gifts.giftCategories;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.gift.GiftSendQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.gifts.response.GiftSentResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch list of users FetchUsersQuery{@link
 * FetchUsersQuery} request and parse the response(both success or
 * error) to return fetch users result
 * InviteUsersResponse{@link GiftSentResponse}
 * or error received.
 *
 * @see FetchUsersQuery
 * @see GiftSentResponse
 */
public class GiftsSend {

    /**
     * Validate params.
     *
     * @param giftRequest       the fetch users request query
     * @param completionHandler the fetch users list request completion handler
     * @param retrofitManager   the retrofit manager to make remote api calls
     * @param imConfiguration   the isometrik configuration instance
     * @param baseResponse      the base response instance to handle non 200 http response code messages
     * @param gson              the gson instance
     * @see FetchUsersQuery
     * @see CompletionHandler
     * @see RetrofitManager
     * @see IMConfiguration
     * @see BaseResponse
     * @see Gson
     */
    public void validateParams(@NotNull GiftSendQuery giftRequest,
                               @NotNull final CompletionHandler<GiftSentResponse> completionHandler,
                               @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
                               @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {


        String userToken = giftRequest.getIsometricToken();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userSecret", imConfiguration.getUserOrProductSecret());
            headers.put("userToken", userToken);

            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("messageStreamId", giftRequest.getMessageStreamId());
            bodyParam.put("senderId", giftRequest.getSenderId());
            bodyParam.put("giftThumbnailUrl", giftRequest.getGiftThumbnailUrl());
            bodyParam.put("amount", giftRequest.getAmount());
            bodyParam.put("giftId", giftRequest.getGiftId());
            bodyParam.put("deviceId", giftRequest.getDeviceId());
            bodyParam.put("giftUrl", giftRequest.getGiftUrl());
            bodyParam.put("receiverUserId", giftRequest.getReceiverUserId());
            bodyParam.put("reciverUserType", giftRequest.getReciverUserType());
            bodyParam.put("receiverName", giftRequest.getReceiverName());
            bodyParam.put("receiverStreamId", giftRequest.getReceiverStreamId());
            bodyParam.put("receiverCurrency", giftRequest.getReceiverCurrency());
            bodyParam.put("isometricToken", giftRequest.getIsometricToken());
            bodyParam.put("giftTitle", giftRequest.getGiftTitle());
            bodyParam.put("isGiftVideo", giftRequest.isGiftVideo());
            bodyParam.put("currency", giftRequest.getCurrency());

//            if (giftRequest.isPk()) {
                bodyParam.put("isPk", giftRequest.isPk());
                bodyParam.put("pkId", giftRequest.getPkId());
//            }


            Call<GiftSentResponse> call =
                    retrofitManager.getUserService().sendGiftToStreamer(headers, bodyParam);

            call.enqueue(new Callback<GiftSentResponse>() {
                @Override
                public void onResponse(@NotNull Call<GiftSentResponse> call,
                                       @NotNull Response<GiftSentResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            completionHandler.onComplete(response.body(), null);
                        }
                    } else {

                        ErrorResponse errorResponse;
                        IsometrikError.Builder isometrikErrorBuilder;
                        try {

                            if (response.errorBody() != null) {
                                errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            } else {
                                errorResponse = new ErrorResponse();
                            }
                        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                            // handle failure to read error
                            errorResponse = new ErrorResponse();
                        }

                        isometrikErrorBuilder =
                                new IsometrikError.Builder().setHttpResponseCode(response.code())
                                        .setRemoteError(true);

                        switch (response.code()) {

                            case 401:

                                isometrikErrorBuilder =
                                        baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse, false);
                                break;
                            case 403:

                                isometrikErrorBuilder =
                                        baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                                break;
                            case 404:

                                isometrikErrorBuilder.setErrorMessage("insufficient balance");

                                break;

                            case 409:
                                try {
                                    if (response.errorBody() != null) {
                                        GiftSentResponse giftSentResponse = new Gson().fromJson(response.errorBody().string(), GiftSentResponse.class);
                                        if (giftSentResponse.getErrorCode() == 1) {
                                            isometrikErrorBuilder.setErrorMessage(IsometrikStreamSdk.getInstance().getContext().getString(R.string.gift_send_msg_after_pk_end));
                                        } else if (giftSentResponse.getErrorCode() == 0) {
                                            isometrikErrorBuilder.setErrorMessage(IsometrikStreamSdk.getInstance().getContext().getString(R.string.gift_send_msg_before_pk_start));
                                        } else {
                                            isometrikErrorBuilder.setErrorMessage(
                                                    IsometrikStreamSdk.getInstance().getContext().getString(R.string.ism_send_gift_failed));
                                        }
                                    }
                                } catch (IOException | IllegalStateException |
                                         JsonSyntaxException e) {

                                }
                                break;

                            case 422:

                                isometrikErrorBuilder =
                                        baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 502:

                                isometrikErrorBuilder = baseResponse.handle502responseCode(isometrikErrorBuilder);
                                break;

                            case 503:
                                isometrikErrorBuilder =
                                        baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            default:
                                //500 response code
                                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
                        }

                        completionHandler.onComplete(null, isometrikErrorBuilder.build());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GiftSentResponse> call, @NotNull Throwable t) {
                    if (t instanceof IOException) {
                        // Network failure
                        completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
                    } else {
                        // Parsing error
                        completionHandler.onComplete(null, baseResponse.handleParsingError(t));
                    }
                }
            });
        }
    }

}
