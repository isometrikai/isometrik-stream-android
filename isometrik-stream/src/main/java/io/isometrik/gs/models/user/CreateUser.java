package io.isometrik.gs.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.CreateUserQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.user.CreateUserResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the create a new user AddUserQuery{@link
 * CreateUserQuery} request and parse the response(both success or error)
 * to return add user result AddUserResult{@link CreateUserResult} or
 * error received.
 *
 * @see CreateUserQuery
 * @see CreateUserResult
 */
public class CreateUser {

  /**
   * Validate params.
   *
   * @param createUserQuery the create user request query
   * @param completionHandler the create new user request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see CreateUserQuery
   * @see CompletionHandler
   * @see RetrofitManager
   * @see IMConfiguration
   * @see BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull CreateUserQuery createUserQuery,
      @NotNull final CompletionHandler<CreateUserResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String userIdentifier = createUserQuery.getUserIdentifier();
    String password = createUserQuery.getPassword();
    String userProfileImageUrl = createUserQuery.getUserProfileImageUrl();
    String userName = createUserQuery.getUserName();
    JSONObject metadata = createUserQuery.getMetadata();

    if (userIdentifier == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_IDENTIFIER_MISSING);
    } else if (password == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_PASSWORD_MISSING);
    } else if (userProfileImageUrl == null) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_USER_PROFILE_IMAGEURL_MISSING);
    } else if (userName == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USERNAME_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userSecret", imConfiguration.getUserOrProductSecret());

      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("userIdentifier", userIdentifier);
      bodyParams.put("password", password);
      bodyParams.put("userProfileImageUrl", userProfileImageUrl);
      bodyParams.put("userName", userName);

      if (metadata != null) {
        bodyParams.put("metaData", JsonParser.parseString(metadata.toString()));
      }
      Call<CreateUserResult> call =
          retrofitManager.getUserService().createUser(headers, bodyParams);

      call.enqueue(new Callback<CreateUserResult>() {
        @Override
        public void onResponse(@NotNull Call<CreateUserResult> call,
            @NotNull Response<CreateUserResult> response) {

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
                    baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, false);
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
        public void onFailure(@NotNull Call<CreateUserResult> call, @NotNull Throwable t) {
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
