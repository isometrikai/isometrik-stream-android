package io.isometrik.gs.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.AuthenticateUserQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.user.AuthenticateUserResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the authenticate user request and expose the
 * parsed successful or error response.
 */
public class AuthenticateUser {
  /**
   * Validate params.
   *
   * @param authenticateUserQuery the authenticate user query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(@NotNull AuthenticateUserQuery authenticateUserQuery,
      @NotNull final CompletionHandler<AuthenticateUserResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String userIdentifier = authenticateUserQuery.getUserIdentifier();
    String password = authenticateUserQuery.getPassword();
    if (userIdentifier == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_IDENTIFIER_MISSING);
    } else if (password == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_PASSWORD_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userSecret", imConfiguration.getUserOrProductSecret());

      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("userIdentifier", userIdentifier);
      bodyParams.put("password", password);

      Call<AuthenticateUserResult> call =
          retrofitManager.getUserService().authenticateUser(headers, bodyParams);

      call.enqueue(new Callback<AuthenticateUserResult>() {
        @Override
        public void onResponse(@NotNull Call<AuthenticateUserResult> call,
            @NotNull Response<AuthenticateUserResult> response) {

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
                    baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse, true);
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
        public void onFailure(@NotNull Call<AuthenticateUserResult> call, @NotNull Throwable t) {
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
