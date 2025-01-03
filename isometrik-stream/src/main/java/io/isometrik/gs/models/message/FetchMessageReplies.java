package io.isometrik.gs.models.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.message.FetchMessageRepliesQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.message.FetchMessageRepliesResult;
import io.isometrik.gs.utils.MapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch list of message replies in a stream group
 * FetchMessageRepliesQuery{@link
 * FetchMessageRepliesQuery} request and parse the response(both success
 * or error) to return fetch message replies result FetchMessageRepliesResult{@link
 * FetchMessageRepliesResult} or error received.
 *
 * @see FetchMessageRepliesQuery
 * @see FetchMessageRepliesResult
 */
public class FetchMessageReplies {
  /**
   * Validate params.
   *
   * @param fetchMessageRepliesQuery the fetch message replies request query
   * @param completionHandler the fetch message replies list request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see FetchMessageRepliesQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull FetchMessageRepliesQuery fetchMessageRepliesQuery,
      @NotNull final CompletionHandler<FetchMessageRepliesResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = fetchMessageRepliesQuery.getStreamId();
    String parentMessageId = fetchMessageRepliesQuery.getParentMessageId();
    String userToken = fetchMessageRepliesQuery.getUserToken();

    Integer sort = fetchMessageRepliesQuery.getSort();
    Integer limit = fetchMessageRepliesQuery.getLimit();
    Integer skip = fetchMessageRepliesQuery.getSkip();
    Long lastMessageTimestamp = fetchMessageRepliesQuery.getLastMessageTimestamp();
    List<String> messageIds = fetchMessageRepliesQuery.getMessageIds();
    List<Integer> messageTypes = fetchMessageRepliesQuery.getMessageTypes();
    List<String> customTypes = fetchMessageRepliesQuery.getCustomTypes();
    List<String> senderIds = fetchMessageRepliesQuery.getSenderIds();
    String searchTag = fetchMessageRepliesQuery.getSearchTag();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (parentMessageId == null || parentMessageId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PARENT_MESSAGE_ID_MISSING);
    } else if (searchTag != null && searchTag.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SEARCH_TAG_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> queryParams = new HashMap<>();
      queryParams.put("streamId", streamId);
      queryParams.put("parentMessageId", parentMessageId);
      if (skip != null) queryParams.put("skip", skip);
      if (limit != null) queryParams.put("limit", limit);
      if (sort != null) queryParams.put("sort", sort);

      if (lastMessageTimestamp != null) {
        queryParams.put("lastMessageTimestamp", lastMessageTimestamp);
      }

      if (messageIds != null) queryParams.put("ids", MapUtils.stringListToCsv(messageIds));
      if (messageTypes != null) {
        queryParams.put("messageTypes", MapUtils.integerListToCsv(messageTypes));
      }
      if (customTypes != null) {
        queryParams.put("customTypes", MapUtils.stringListToCsv(customTypes));
      }
      if (senderIds != null) queryParams.put("senderIds", MapUtils.stringListToCsv(senderIds));
      if (searchTag != null) queryParams.put("searchTag", searchTag);
      if (fetchMessageRepliesQuery.getSenderIdsExclusive() != null) {
        queryParams.put("senderIdsExclusive", fetchMessageRepliesQuery.getSenderIdsExclusive());
      }
      Call<FetchMessageRepliesResult> call =
          retrofitManager.getMessageService().fetchMessageReplies(headers, queryParams);
      call.enqueue(new Callback<FetchMessageRepliesResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchMessageRepliesResult> call,
            @NotNull Response<FetchMessageRepliesResult> response) {

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
                    baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, false);
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
        public void onFailure(@NotNull Call<FetchMessageRepliesResult> call, @NotNull Throwable t) {
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
