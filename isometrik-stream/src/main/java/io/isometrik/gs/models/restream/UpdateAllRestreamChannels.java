package io.isometrik.gs.models.restream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.restream.UpdateAllRestreamChannelsQuery;
import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.restream.UpdateAllRestreamChannelsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the update all restream channels UpdateAllRestreamChannelsQuery{@link
 * UpdateAllRestreamChannelsQuery} request and parse the response(both
 * success or
 * error) to return update all restream channels result UpdateAllRestreamChannelsResult{@link
 * UpdateAllRestreamChannelsResult}
 * or error received.
 *
 * @see UpdateAllRestreamChannelsQuery
 * @see UpdateAllRestreamChannelsResult
 */
public class UpdateAllRestreamChannels {
  /**
   * Validate params.
   *
   * @param updateAllRestreamChannelQuery the update all restream channels request query
   * @param completionHandler the update all restream channels request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see UpdateRestreamChannelQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull UpdateAllRestreamChannelsQuery updateAllRestreamChannelQuery,
      @NotNull final CompletionHandler<UpdateAllRestreamChannelsResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    Boolean enabled = updateAllRestreamChannelQuery.getEnabled();
    String userToken = updateAllRestreamChannelQuery.getUserToken();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (enabled == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_RESTREAM_ENABLED_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> body = new HashMap<>();
      body.put("enabled", enabled);

      Call<UpdateAllRestreamChannelsResult> call =
          retrofitManager.getRestreamService().updateAllRestreamChannels(headers, body);
      call.enqueue(new Callback<UpdateAllRestreamChannelsResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateAllRestreamChannelsResult> call,
            @NotNull Response<UpdateAllRestreamChannelsResult> response) {

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
        public void onFailure(@NotNull Call<UpdateAllRestreamChannelsResult> call,
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
