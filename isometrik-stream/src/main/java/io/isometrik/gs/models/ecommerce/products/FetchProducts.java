package io.isometrik.gs.models.ecommerce.products;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.ecommerce.products.FetchProductsQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.ecommerce.products.FetchProductsResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch products FetchProductsQuery{@link
 * FetchProductsQuery} request and parse the response(both success or
 * error) to return fetch products result FetchProductsResult{@link FetchProductsResult}
 * or error received.
 *
 * @see FetchProductsQuery
 * @see FetchProductsResult
 */
public class FetchProducts {
  /**
   * Validate params.
   *
   * @param fetchProductsQuery the fetch products request query
   * @param completionHandler the fetch products request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see FetchProductsQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull FetchProductsQuery fetchProductsQuery,
      @NotNull final CompletionHandler<FetchProductsResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    Integer skip = fetchProductsQuery.getSkip();
    Integer limit = fetchProductsQuery.getLimit();

    Map<String, String> headers = new HashMap<>();
    headers.put("licenseKey", imConfiguration.getLicenseKey());
    headers.put("appSecret", imConfiguration.getAppSecret());

    Map<String, Object> queryParams = new HashMap<>();
    if (skip != null) queryParams.put("skip",  skip);
    if (limit != null) queryParams.put("limit", limit);

    Call<FetchProductsResult> call =
        retrofitManager.getEcommerceService().fetchProducts(headers, queryParams);

    call.enqueue(new Callback<FetchProductsResult>() {
      @Override
      public void onResponse(@NotNull Call<FetchProductsResult> call,
          @NotNull Response<FetchProductsResult> response) {

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

          isometrikErrorBuilder = new IsometrikError.Builder().setHttpResponseCode(response.code())
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
      public void onFailure(@NotNull Call<FetchProductsResult> call, @NotNull Throwable t) {
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
