package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.AuthenticateUserQuery;
import io.isometrik.gs.builder.user.CreateUserQuery;
import io.isometrik.gs.builder.user.DeleteUserQuery;
import io.isometrik.gs.builder.user.FetchCreateUserPresignedUrlQuery;
import io.isometrik.gs.builder.user.FetchUpdateUserPresignedUrlQuery;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;
import io.isometrik.gs.builder.user.LiveUsersQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.user.AuthenticateUser;
import io.isometrik.gs.models.user.CreateUser;
import io.isometrik.gs.models.user.DeleteUser;
import io.isometrik.gs.models.user.FetchCreateUserPresignedUrl;
import io.isometrik.gs.models.user.FetchUpdateUserPresignedUrl;
import io.isometrik.gs.models.user.FetchUserDetails;
import io.isometrik.gs.models.user.LiveUsers;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.user.AuthenticateUserResult;
import io.isometrik.gs.response.user.CreateUserResult;
import io.isometrik.gs.response.user.DeleteUserResult;
import io.isometrik.gs.response.user.FetchCreateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUpdateUserPresignedUrlResult;
import io.isometrik.gs.response.user.FetchUserDetailsResult;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleEnd;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleEndQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStart;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStartResponse;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStop;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleStopQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleWinners;
import io.isometrik.gs.ui.pk.challengsSettings.PkBattleWinnersQuery;
import io.isometrik.gs.ui.pk.challengsSettings.PkStreamStat;
import io.isometrik.gs.ui.pk.challengsSettings.PkStreamStatQuery;
import io.isometrik.gs.ui.pk.invitationList.LiveUsersListResponse;
import io.isometrik.gs.ui.pk.invitesent.PkInviteAcceptReject;
import io.isometrik.gs.ui.pk.invitesent.PkInviteAcceptRejectQuery;
import io.isometrik.gs.ui.pk.invitesent.PkInviteSent;
import io.isometrik.gs.ui.pk.invitesent.PkInviteSentQuery;
import io.isometrik.gs.ui.pk.invitesent.CommonResponse;
import io.isometrik.gs.ui.pk.response.PkBattleWinnerResponse;
import io.isometrik.gs.ui.pk.response.PkStats;
import io.isometrik.gs.ui.pk.response.PkStatsResponse;
import io.isometrik.gs.ui.pk.response.PkStreamStopResponse;

/**
 * Classes containing use cases for various users operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class PkUseCases {

  /**
   * Model classes for user
   */
  private final PkBattleStart pkBattleStart;
  private final PkBattleStop pkBattleStop;
  private final PkBattleEnd pkBattleEnd;
  private final PkStreamStat pkStreamStat;
  private final LiveUsers liveUsers;
  private final PkInviteSent pkInviteSent;
  private final PkBattleWinners pkBattleWinners;
  private final PkInviteAcceptReject pkInviteAcceptReject;
  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public PkUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
                    BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    pkBattleStart = new PkBattleStart();
    pkBattleStop = new PkBattleStop();
    pkBattleEnd = new PkBattleEnd();
    pkStreamStat = new PkStreamStat();
    liveUsers = new LiveUsers();
    pkInviteSent = new PkInviteSent();
    pkInviteAcceptReject = new PkInviteAcceptReject();
    pkBattleWinners = new PkBattleWinners();
  }

  /**
   * Fetch live users.
   *
   * @param liveUsersQuery the fetch users query
   * @param completionHandler the completion handler
   */
  public void liveUsers(@NotNull LiveUsersQuery liveUsersQuery,
                         @NotNull CompletionHandler<LiveUsersListResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
    if (isometrikError == null) {
      liveUsers.validateParams(liveUsersQuery, completionHandler, retrofitManager, configuration,
              baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }


  public void sendInvitationToUserForPK(PkInviteSentQuery params, CompletionHandler<CommonResponse> completionHandler){
    pkInviteSent.validateParams(params,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }


  public void clickOnPKRequestAccept(PkInviteAcceptRejectQuery pkInviteAcceptRejectQuery, CompletionHandler<CommonResponse> completionHandler){
    pkInviteAcceptReject.validateParams(pkInviteAcceptRejectQuery,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }


  public void startPkBattle(PkBattleStartQuery pkBattleStartQuery, CompletionHandler<PkBattleStartResponse> completionHandler){
    pkBattleStart.validateParams(pkBattleStartQuery,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }

  public void stopPkBattle(PkBattleStopQuery pkBattleStopQuery, CompletionHandler<PkStreamStopResponse> completionHandler){
    pkBattleStop.validateParams(pkBattleStopQuery,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }

  public void endPkBattle(PkBattleEndQuery pkBattleEndQuery, CompletionHandler<CommonResponse> completionHandler){
    pkBattleEnd.validateParams(pkBattleEndQuery,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }


  public void getPkBattleWinners(PkBattleWinnersQuery pkBattleWinnersQuery, CompletionHandler<PkBattleWinnerResponse> completionHandler){
    pkBattleWinners.validateParams(pkBattleWinnersQuery,completionHandler,retrofitManager,configuration,baseResponse,gson);
  }

  /**
   * Fetch pk stat details.
   *
   * @param pkStreamStatQuery the fetch user details query
   * @param completionHandler the completion handler
   */
  public void fetchPkStatDetails(@NotNull PkStreamStatQuery pkStreamStatQuery,
      @NotNull CompletionHandler<PkStatsResponse> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      pkStreamStat.validateParams(pkStreamStatQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

}
