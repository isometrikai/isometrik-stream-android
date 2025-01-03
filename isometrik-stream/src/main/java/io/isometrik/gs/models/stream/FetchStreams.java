package io.isometrik.gs.models.stream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.utils.MapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch list of streams FetchStreamsQuery{@link
 * FetchStreamsQuery} request and parse the response(both success or
 * error) to return fetch streams result
 * FetchStreamsResult{@link FetchStreamsResult}
 * or error received.
 *
 * @see FetchStreamsQuery
 * @see FetchStreamsResult
 */
public class FetchStreams {
  /**
   * Validate params.
   *
   * @param fetchStreamsQuery the fetch streams request query
   * @param completionHandler the fetch streams list request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see FetchStreamsQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull FetchStreamsQuery fetchStreamsQuery,
      @NotNull final CompletionHandler<FetchStreamsResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String userToken = fetchStreamsQuery.getUserToken();
    String searchTag = fetchStreamsQuery.getSearchTag();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (searchTag != null && searchTag.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SEARCH_TAG_INVALID_VALUE);
    } else {

      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Integer sort = fetchStreamsQuery.getSort();
      Integer limit = fetchStreamsQuery.getLimit();
      Integer skip = fetchStreamsQuery.getSkip();
      Boolean includeMembers = fetchStreamsQuery.getIncludeMembers();
      List<String> membersIncluded = fetchStreamsQuery.getMembersIncluded();
      List<String> membersExactly = fetchStreamsQuery.getMembersExactly();
      List<String> streamIds = fetchStreamsQuery.getStreamIds();
      String customType = fetchStreamsQuery.getCustomType();
      Integer membersSkip = fetchStreamsQuery.getMembersSkip();
      Integer membersLimit = fetchStreamsQuery.getMembersLimit();

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

      if (fetchStreamsQuery.getPublic() != null) {
        queryParams.put("public", fetchStreamsQuery.getPublic());
      }
      if (fetchStreamsQuery.getAudioOnly() != null) {
        queryParams.put("audioOnly", fetchStreamsQuery.getAudioOnly());
      }
      if (fetchStreamsQuery.getMultiLive() != null) {
        queryParams.put("multiLive", fetchStreamsQuery.getMultiLive());
      }
      if (fetchStreamsQuery.getRecorded() != null) {
        queryParams.put("recorded", fetchStreamsQuery.getRecorded());
      }
      if (fetchStreamsQuery.getLowLatencyMode() != null) {
        queryParams.put("lowLatencyMode", fetchStreamsQuery.getLowLatencyMode());
      }
      if (fetchStreamsQuery.getHdBroadcast() != null) {
        queryParams.put("hdBroadcast", fetchStreamsQuery.getHdBroadcast());
      }
      if (fetchStreamsQuery.getRestream() != null) {
        queryParams.put("restream", fetchStreamsQuery.getRestream());
      }
      if (fetchStreamsQuery.getProductsLinked() != null) {
        queryParams.put("productsLinked", fetchStreamsQuery.getProductsLinked());
      }
      if (fetchStreamsQuery.getCanPublish() != null) {
        queryParams.put("canPublish", fetchStreamsQuery.getCanPublish());
      }

      queryParams.put("fetchLive",true);

      Call<FetchStreamsResult> call =
          retrofitManager.getStreamService().fetchStreams(headers, queryParams);
      call.enqueue(new Callback<FetchStreamsResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchStreamsResult> call,
            @NotNull Response<FetchStreamsResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 200) {
              completionHandler.onComplete(response.body(), null);
            }else if(response.code() == 204){
              FetchStreamsResult fetchStreamsResult = new FetchStreamsResult();
              completionHandler.onComplete(fetchStreamsResult, null);
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

              case 204:
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
        public void onFailure(@NotNull Call<FetchStreamsResult> call, @NotNull Throwable t) {
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
