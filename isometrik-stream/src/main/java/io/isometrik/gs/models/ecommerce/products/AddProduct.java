package io.isometrik.gs.models.ecommerce.products;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.ecommerce.products.AddProductQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.ecommerce.products.AddProductResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the add a product AddProductQuery{@link
 * AddProductQuery} request and parse the response(both success or
 * error) to return add a produc result AddProductResult{@link AddProductResult}
 * or error received.
 *
 * @see AddProductQuery
 * @see AddProductResult
 */
public class AddProduct {
  /**
   * Validate params.
   *
   * @param addProductQuery the add a produc request query
   * @param completionHandler the add a produc request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see AddProductQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull AddProductQuery addProductQuery,
      @NotNull final CompletionHandler<AddProductResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String externalProductId = addProductQuery.getExternalProductId();
    Integer count = addProductQuery.getCount();
    String productName = addProductQuery.getProductName();
    JSONObject metadata = addProductQuery.getMetadata();

    if (externalProductId == null || externalProductId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_EXTERNAL_PRODUCTID_MISSING);
    } else if (productName == null || productName.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRODUCT_NAME_MISSING);
    } else if (metadata == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_METADATA_MISSING);
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
      body.put("externalProductId", externalProductId);
      body.put("count", count);
      body.put("productName", productName);
      body.put("metadata", metadata);

      Call<AddProductResult> call = retrofitManager.getEcommerceService().addProduct(headers, body);

      call.enqueue(new Callback<AddProductResult>() {
        @Override
        public void onResponse(@NotNull Call<AddProductResult> call,
            @NotNull Response<AddProductResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 201) {
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
              case 409:

                isometrikErrorBuilder =
                    baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, true);
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
        public void onFailure(@NotNull Call<AddProductResult> call, @NotNull Throwable t) {
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
