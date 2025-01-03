package io.isometrik.gs.models.recording;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.recording.FetchRecordingsQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.recording.FetchRecordingsResult;
import io.isometrik.gs.utils.MapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch list of recordings FetchRecordingsQuery{@link
 * FetchRecordingsQuery} request and parse the response(both success or
 * error) to return fetch recordings result FetchRecordingsResult{@link FetchRecordingsResult}
 * or error received.
 *
 * @see FetchRecordingsQuery
 * @see FetchRecordingsResult
 */
public class FetchRecordings {
  /**
   * Validate params.
   *
   * @param fetchRecordingsQuery the fetch recordings request query
   * @param completionHandler the fetch recordings list request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see FetchRecordingsQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull FetchRecordingsQuery fetchRecordingsQuery,
      @NotNull final CompletionHandler<FetchRecordingsResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String userToken = fetchRecordingsQuery.getUserToken();
    String searchTag = fetchRecordingsQuery.getSearchTag();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (searchTag != null && searchTag.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SEARCH_TAG_INVALID_VALUE);
    } else {

      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Integer sort = fetchRecordingsQuery.getSort();
      Integer limit = fetchRecordingsQuery.getLimit();
      Integer skip = fetchRecordingsQuery.getSkip();
      Boolean includeMembers = fetchRecordingsQuery.getIncludeMembers();
      List<String> membersIncluded = fetchRecordingsQuery.getMembersIncluded();
      List<String> membersExactly = fetchRecordingsQuery.getMembersExactly();
      List<String> streamIds = fetchRecordingsQuery.getStreamIds();
      String customType = fetchRecordingsQuery.getCustomType();
      Integer membersSkip = fetchRecordingsQuery.getMembersSkip();
      Integer membersLimit = fetchRecordingsQuery.getMembersLimit();

      Map<String, Object> queryParams = new HashMap<>();
      if (membersIncluded != null) {
        queryParams.put("membersIncluded", MapUtils.stringListToCsv(membersIncluded));
      }
      if (membersExactly != null) {
        queryParams.put("membersExactly", MapUtils.stringListToCsv(membersExactly));
      }
      if (streamIds != null) {
        queryParams.put("ids", MapUtils.stringListToCsv(streamIds));
      }
      if (customType != null) queryParams.put("customType", customType);
      if (searchTag != null) queryParams.put("searchTag", searchTag);

      if (includeMembers != null) {
        queryParams.put("includeMembers", includeMembers);
        if (includeMembers) {
          if (membersSkip != null) queryParams.put("membersSkip", membersSkip);
          if (membersLimit != null) queryParams.put("membersLimit", membersLimit);
        }
      }

      if (skip != null) queryParams.put("skip", skip);
      if (limit != null) queryParams.put("limit", limit);
      if (sort != null) queryParams.put("sort", sort);

      if (fetchRecordingsQuery.getPublic() != null) {
        queryParams.put("public", fetchRecordingsQuery.getPublic());
      }
      if (fetchRecordingsQuery.getAudioOnly() != null) {
        queryParams.put("audioOnly", fetchRecordingsQuery.getAudioOnly());
      }
      if (fetchRecordingsQuery.getMultiLive() != null) {
        queryParams.put("multiLive", fetchRecordingsQuery.getMultiLive());
      }

      if (fetchRecordingsQuery.getLowLatencyMode() != null) {
        queryParams.put("lowLatencyMode", fetchRecordingsQuery.getLowLatencyMode());
      }
      if (fetchRecordingsQuery.getHdBroadcast() != null) {
        queryParams.put("hdBroadcast", fetchRecordingsQuery.getHdBroadcast());
      }
      if (fetchRecordingsQuery.getRestream() != null) {
        queryParams.put("restream", fetchRecordingsQuery.getRestream());
      }
      if (fetchRecordingsQuery.getProductsLinked() != null) {
        queryParams.put("productsLinked", fetchRecordingsQuery.getProductsLinked());
      }

      Call<FetchRecordingsResult> call =
          retrofitManager.getRecordingService().fetchRecordings(headers, queryParams);
      call.enqueue(new Callback<FetchRecordingsResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchRecordingsResult> call,
            @NotNull Response<FetchRecordingsResult> response) {

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
        public void onFailure(@NotNull Call<FetchRecordingsResult> call, @NotNull Throwable t) {
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
