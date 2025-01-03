package io.isometrik.gs.models.ecommerce.featuring;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.ecommerce.featuring.UpdateFeaturingProductStatusQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.ecommerce.featuring.UpdateFeaturingProductStatusResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the update featuring product status in a broadcast
 * UpdateFeaturingProductStatusQuery{@link
 * UpdateFeaturingProductStatusQuery} request and parse the response(both success or
 * error) to return update featuring product status in a broadcast result
 * UpdateFeaturingProductStatusResult{@link
 * UpdateFeaturingProductStatusResult}
 * or error received.
 *
 * @see UpdateFeaturingProductStatusQuery
 * @see UpdateFeaturingProductStatusResult
 */
public class UpdateFeaturingProduct {

  /**
   * Validate params.
   *
   * @param updateFeaturingProductQuery the update featuring product status in a broadcast query
   * @param completionHandler the update featuring product status in a broadcast request completion
   * handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see UpdateFeaturingProductStatusQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull UpdateFeaturingProductStatusQuery updateFeaturingProductQuery,
      @NotNull final CompletionHandler<UpdateFeaturingProductStatusResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = updateFeaturingProductQuery.getStreamId();
    String productId = updateFeaturingProductQuery.getProductId();
    Boolean startFeaturing = updateFeaturingProductQuery.getStartFeaturing();

    String userToken = updateFeaturingProductQuery.getUserToken();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (productId == null || productId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRODUCTID_MISSING);
    } else if (startFeaturing == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_START_FEATURING_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> body = new HashMap<>();
      body.put("streamId", streamId);
      body.put("productId", productId);
      body.put("startFeaturing", startFeaturing);

      Call<UpdateFeaturingProductStatusResult> call =
          retrofitManager.getEcommerceService().updateFeaturingProductStatus(headers, body);

      call.enqueue(new Callback<UpdateFeaturingProductStatusResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateFeaturingProductStatusResult> call,
            @NotNull Response<UpdateFeaturingProductStatusResult> response) {

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
              case 400:

                isometrikErrorBuilder =
                    baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, true);
                break;

              case 401:

                isometrikErrorBuilder =
                    baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse, false);
                break;
              case 403:

                isometrikErrorBuilder =
                    baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 404:

                isometrikErrorBuilder =
                    baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
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
        public void onFailure(@NotNull Call<UpdateFeaturingProductStatusResult> call,
            @NotNull Throwable t) {
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
