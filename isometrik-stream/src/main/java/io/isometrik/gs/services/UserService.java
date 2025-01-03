package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.user.AuthenticateUserResult;
import io.isometrik.gs.response.user.CreateUserResult;
import io.isometrik.gs.response.user.DeleteUserResult;
import io.isometrik.gs.response.user.FetchCreateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUpdateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUserDetailsResult;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.user.UpdateUserResult;
import io.isometrik.gs.ui.gifts.response.GiftSentResponse;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartResponse;
import io.isometrik.gs.ui.pk.invitationList.LiveUsersListResponse;
import io.isometrik.gs.ui.pk.invitesent.CommonResponse;
import io.isometrik.gs.ui.pk.response.PkBattleWinnerResponse;
import io.isometrik.gs.ui.pk.response.PkStatsResponse;
import io.isometrik.gs.ui.pk.response.PkStreamStopResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface user service to create, update or delete a user and fetch list of users.
 */
public interface UserService {

  /**
   * Create user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddUserResult>
   * @see CreateUserResult
   */
  @POST("/streaming/v2/user")
  Call<CreateUserResult> createUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateUserResult>
   * @see UpdateUserResult
   */
  @PATCH("/streaming/v2/user")
  Call<UpdateUserResult> updateUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete user call.
   *
   * @param headers the headers
   * @return the Call<DeleteUserResult>
   * @see DeleteUserResult
   */
  @DELETE("/streaming/v2/user")
  Call<DeleteUserResult> deleteUser(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch users list call.
   *
   * @param headers the headers
   * @return the Call<FetchUsersResult>
   * @see FetchUsersResult
   */
  @GET("/streaming/v2/users")
  Call<FetchUsersResult> fetchUsers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch user details call.
   *
   * @param headers the headers
   * @return the Call<FetchUserDetailsResult>
   * @see FetchUserDetailsResult
   */
  @GET("/streaming/v2/user/details")
  Call<FetchUserDetailsResult> fetchUserDetails(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Authenticate user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/streaming/v2/user/authenticate")
  Call<AuthenticateUserResult> authenticateUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Fetch create user presigned url call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/streaming/v2/user/presignedurl/create")
  Call<FetchCreateUserPresignedUrlResult> fetchCreateUserPresignedUrl(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch update user presigned url call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/streaming/v2/user/presignedurl/update")
  Call<FetchUpdateUserPresignedUrlResult> fetchUpdateUserPresignedUrl(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch live list call.
   *
   * @param headers the headers
   * @return the Call<FetchUsersResult>
   * @see LiveUsersListResponse
   */
  @GET("/live/v1/invite/users")
  Call<LiveUsersListResponse> liveUsers(@HeaderMap Map<String, String> headers,
                                        @QueryMap Map<String, Object> queryParams);

  /**
   * send invitation to user for pk.
   *
   * @param headers the headers
   * @return the Call<FetchUsersResult>
   * @see CommonResponse
   */
  @POST("/live/v1/invite/users")
  Call<CommonResponse> sendInvitationToUserForPK(@HeaderMap Map<String, String> headers,
                                                 @Body Map<String, Object> queryParams);

  /**
   *  use accept and reject pk invite
   *
   * @param headers the headers
   * @return the Call<FetchUsersResult>
   * @see CommonResponse
   */
  @POST("/live/v1/invites")
  Call<CommonResponse> acceptRejectPKInvite(@HeaderMap Map<String, String> headers,
                                            @Body Map<String, Object> queryParams);

  /**
   *  use to get pk-stream stat
   *
   * @param headers the headers
   * @see PkStatsResponse
   */
  @GET("/live/v1/pk/stream/stats")
  Call<PkStatsResponse> fetchPkStreamStat(@HeaderMap Map<String, String> headers,
                                          @QueryMap Map<String, Object> queryParams);

  /**
   *  use to start pk-battle
   *
   * @param headers the headers
   * @see PkBattleStartResponse
   */
  @POST("/live/v1/pk/start")
  Call<PkBattleStartResponse> startPkBattle(@HeaderMap Map<String, String> headers,
                                            @Body Map<String, Object> queryParams);

  /**
   *  use to stop pk-battle
   *
   * @param headers the headers
   * @see PkStreamStopResponse
   */
  @POST("/live/v1/pk/stop")
  Call<PkStreamStopResponse> stopPkBattle(@HeaderMap Map<String, String> headers,
                                          @Body Map<String, Object> queryParams);

  /**
   *  use to end pk-battle
   *
   * @param headers the headers
   * @see CommonResponse
   */
  @POST("/live/v1/pk/end")
  Call<CommonResponse> endPkBattle(@HeaderMap Map<String, String> headers,
                                            @Body Map<String, Object> queryParams);

  /**
   *  use to get pk-battle-winners
   *
   * @param headers the headers
   * @see PkBattleWinnerResponse
   */
  @GET("/live/v1/pk/winner")
  Call<PkBattleWinnerResponse> getPkBattleWinners(@HeaderMap Map<String, String> headers,
                                                  @QueryMap Map<String, Object> queryParams);

  /**
   *  use to for send gift
   *
   * @param headers the headers
   * @see GiftSentResponse
   */
  @POST("/live/v1/giftTransfer")
  Call<GiftSentResponse> sendGiftToStreamer(@HeaderMap Map<String, String> headers,
                                            @Body Map<String, Object> bodyParams);

}
