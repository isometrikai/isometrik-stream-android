package io.isometrik.gs.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.builder.user.LiveUsersQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.ui.pk.invitationList.LiveUsersListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the fetch list of users FetchUsersQuery{@link
 * FetchUsersQuery} request and parse the response(both success or
 * error) to return fetch users result
 * InviteUsersResponse{@link LiveUsersListResponse}
 * or error received.
 *
 * @see FetchUsersQuery
 * @see LiveUsersListResponse
 */
public class LiveUsers {

  /**
   * Validate params.
   *
   * @param liveUsersQuery the fetch users request query
   * @param completionHandler the fetch users list request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see FetchUsersQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see Gson
   */
  public void validateParams(@NotNull LiveUsersQuery liveUsersQuery,
      @NotNull final CompletionHandler<LiveUsersListResponse> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
    String searchTag = liveUsersQuery.getSearchTag();

    if (searchTag != null && searchTag.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SEARCH_TAG_INVALID_VALUE);
    } else {
      String userToken = liveUsersQuery.getUserToken();
      Integer skip = liveUsersQuery.getSkip();
      Integer limit = liveUsersQuery.getLimit();

      if (userToken == null || userToken.isEmpty()) {
        completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
      }else{
        Map<String, String> headers = new HashMap<>();
        headers.put("licenseKey", imConfiguration.getLicenseKey());
        headers.put("appSecret", imConfiguration.getAppSecret());
        headers.put("userSecret", imConfiguration.getUserOrProductSecret());
        headers.put("userToken", userToken);

        Map<String, Object> queryParams = new HashMap<>();
        if (skip != null) queryParams.put("skip", skip);
        if (limit != null) queryParams.put("limit", limit);
        if (searchTag != null) queryParams.put("q", searchTag);

        Call<LiveUsersListResponse> call =
                retrofitManager.getUserService().liveUsers(headers, queryParams);

        call.enqueue(new Callback<LiveUsersListResponse>() {
          @Override
          public void onResponse(@NotNull Call<LiveUsersListResponse> call,
                                 @NotNull Response<LiveUsersListResponse> response) {

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
          public void onFailure(@NotNull Call<LiveUsersListResponse> call, @NotNull Throwable t) {
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
}
