package io.isometrik.gs.models.ecommerce.products;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.ecommerce.products.UpdateProductInventoryQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.ecommerce.products.UpdateProductInventoryResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the update product inventory
 * UpdateProductInventoryQuery{@link
 * UpdateProductInventoryQuery} request and parse the response(both success or
 * error) to return update product inventory result UpdateProductInventoryResult{@link
 * UpdateProductInventoryResult}
 * or error received.
 *
 * @see UpdateProductInventoryQuery
 * @see UpdateProductInventoryResult
 */
public class UpdateProductInventory {
  /**
   * Validate params.
   *
   * @param updateProductInventoryQuery the update product inventory request query
   * @param completionHandler the update product inventory request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see UpdateProductInventoryQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull UpdateProductInventoryQuery updateProductInventoryQuery,
      @NotNull final CompletionHandler<UpdateProductInventoryResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String productId = updateProductInventoryQuery.getProductId();
    Integer count = updateProductInventoryQuery.getCount();

    if (productId == null || productId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRODUCTID_MISSING);
    } else if (count == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_COUNT_MISSING);
    } else if (count < 0) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_COUNT_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("productSecret", imConfiguration.getUserOrProductSecret());

      Map<String, Object> body = new HashMap<>();
      body.put("count", count);
      body.put("productId", productId);

      Call<UpdateProductInventoryResult> call =
          retrofitManager.getEcommerceService().updateProductInventory(headers, body);

      call.enqueue(new Callback<UpdateProductInventoryResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateProductInventoryResult> call,
            @NotNull Response<UpdateProductInventoryResult> response) {

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
        public void onFailure(@NotNull Call<UpdateProductInventoryResult> call,
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
