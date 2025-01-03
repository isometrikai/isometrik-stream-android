package io.isometrik.gs.models.message;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.message.SendMessageReplyQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.message.SendMessageReplyResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the send a message reply in a stream group
 * SendMessageReplyQuery{@link
 * SendMessageReplyQuery} request and parse the response(both success or
 * error) to return send message reply result SendMessageReplyResult{@link SendMessageReplyResult}
 * or error received.
 *
 * @see SendMessageReplyQuery
 * @see SendMessageReplyResult
 */
public class AddMessageReply {

  /**
   * Validate params.
   *
   * @param sendMessageReplyQuery the send message reply request query
   * @param completionHandler the send message reply request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see SendMessageReplyQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull SendMessageReplyQuery sendMessageReplyQuery,
      @NotNull final CompletionHandler<SendMessageReplyResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = sendMessageReplyQuery.getStreamId();
    String parentMessageId = sendMessageReplyQuery.getParentMessageId();
    String userToken = sendMessageReplyQuery.getUserToken();
    Integer messageType = sendMessageReplyQuery.getMessageType();
    String body = sendMessageReplyQuery.getBody();
    String deviceId = sendMessageReplyQuery.getDeviceId();
    String customType = sendMessageReplyQuery.getCustomType();
    JSONObject metaData = sendMessageReplyQuery.getMetaData();
    List<String> searchableTags = sendMessageReplyQuery.getSearchableTags();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (parentMessageId == null || parentMessageId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PARENT_MESSAGE_ID_MISSING);
    } else if (deviceId == null || deviceId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_DEVICEID_MISSING);
    } else if (messageType == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_TYPE_MISSING);
    } else if (body == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_BODY_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("streamId", streamId);
      bodyParams.put("messageType", messageType);
      bodyParams.put("body", body);
      bodyParams.put("deviceId", deviceId);
      bodyParams.put("parentMessageId", parentMessageId);

      if (customType != null) bodyParams.put("customType", customType);
      if (metaData != null) bodyParams.put("metaData", JsonParser.parseString(metaData.toString()));
      if (searchableTags != null) bodyParams.put("searchableTags", searchableTags);

      Call<SendMessageReplyResult> call =
          retrofitManager.getMessageService().sendMessageReply(headers, bodyParams);
      call.enqueue(new Callback<SendMessageReplyResult>() {
        @Override
        public void onResponse(@NotNull Call<SendMessageReplyResult> call,
            @NotNull Response<SendMessageReplyResult> response) {

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
        public void onFailure(@NotNull Call<SendMessageReplyResult> call, @NotNull Throwable t) {
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
