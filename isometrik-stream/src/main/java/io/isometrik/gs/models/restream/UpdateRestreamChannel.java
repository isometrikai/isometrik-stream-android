package io.isometrik.gs.models.restream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.restream.UpdateRestreamChannelQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.restream.UpdateRestreamChannelResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the update restream channel UpdateRestreamChannelQuery{@link
 * UpdateRestreamChannelQuery} request and parse the response(both
 * success or
 * error) to return update restream channel result UpdateRestreamChannelResult{@link
 * UpdateRestreamChannelResult}
 * or error received.
 *
 * @see UpdateRestreamChannelQuery
 * @see UpdateRestreamChannelResult
 */
public class UpdateRestreamChannel {
  /**
   * Validate params.
   *
   * @param updateRestreamChannelQuery the update restream channel request query
   * @param completionHandler the update restream channel request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see UpdateRestreamChannelQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull UpdateRestreamChannelQuery updateRestreamChannelQuery,
      @NotNull final CompletionHandler<UpdateRestreamChannelResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String channelName = updateRestreamChannelQuery.getChannelName();
    Boolean enabled = updateRestreamChannelQuery.getEnabled();
    String ingestUrl = updateRestreamChannelQuery.getIngestUrl();
    Integer channelType = updateRestreamChannelQuery.getChannelType();
    String channelId = updateRestreamChannelQuery.getChannelId();
    String userToken = updateRestreamChannelQuery.getUserToken();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (channelId == null || channelId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CHANNELID_MISSING);
    } else if (channelName != null && channelName.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CHANNEL_NAME_INVALID_VALUE);
    } else if (ingestUrl != null && ingestUrl.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_INGEST_URL_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> body = new HashMap<>();
      body.put("channelId", channelId);

      if (ingestUrl != null) {
        body.put("ingestUrl", ingestUrl);
      }
      if (channelName != null) {
        body.put("channelName", channelName);
      }
      if (channelType != null) {
        body.put("channelType", channelType);
      }
      if (enabled != null) {
        body.put("enabled", enabled);
      }
      Call<UpdateRestreamChannelResult> call =
          retrofitManager.getRestreamService().updateRestreamChannel(headers, body);
      call.enqueue(new Callback<UpdateRestreamChannelResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateRestreamChannelResult> call,
            @NotNull Response<UpdateRestreamChannelResult> response) {

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
        public void onFailure(@NotNull Call<UpdateRestreamChannelResult> call,
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
