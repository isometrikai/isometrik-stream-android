package io.isometrik.gs.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.member.AddMemberQuery;
import io.isometrik.gs.builder.member.FetchEligibleMembersQuery;
import io.isometrik.gs.builder.member.FetchMemberDetailsQuery;
import io.isometrik.gs.builder.member.FetchMembersQuery;
import io.isometrik.gs.builder.member.LeaveMemberQuery;
import io.isometrik.gs.builder.member.RemoveMemberQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.member.AddMember;
import io.isometrik.gs.models.member.FetchEligibleMembers;
import io.isometrik.gs.models.member.FetchMemberDetails;
import io.isometrik.gs.models.member.FetchMembers;
import io.isometrik.gs.models.member.LeaveMember;
import io.isometrik.gs.models.member.RemoveMember;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.member.AddMemberResult;
import io.isometrik.gs.response.member.FetchEligibleMembersResult;
import io.isometrik.gs.response.member.FetchMemberDetailsResult;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.member.LeaveMemberResult;
import io.isometrik.gs.response.member.RemoveMemberResult;

/**
 * Classes containing use cases for various members operations, allowing ui sdk to communicate with
 * the remote backend using respective model classes.
 */
public class MembersUseCases {
  /**
   * Model classes for members
   */
  private final AddMember addMember;
  private final FetchMembers fetchMembers;
  private final LeaveMember leaveMember;
  private final RemoveMember removeMember;
  private final FetchMemberDetails fetchMemberDetails;
  private final FetchEligibleMembers fetchEligibleMembers;
  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  public MembersUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    configuration = imConfiguration;
    this.retrofitManager = retrofitManager;
    this.gson = gson;
    this.baseResponse = baseResponse;

    addMember = new AddMember();
    fetchMembers = new FetchMembers();
    leaveMember = new LeaveMember();
    removeMember = new RemoveMember();
    fetchMemberDetails = new FetchMemberDetails();
    fetchEligibleMembers = new FetchEligibleMembers();
  }

  /**
   * Add member.
   *
   * @param addMemberQuery the add member query
   * @param completionHandler the completion handler
   */
  public void addMember(@NotNull AddMemberQuery addMemberQuery,
      @NotNull CompletionHandler<AddMemberResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addMember.validateParams(addMemberQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch members.
   *
   * @param fetchMembersQuery the fetch members query
   * @param completionHandler the completion handler
   */
  public void fetchMembers(@NotNull FetchMembersQuery fetchMembersQuery,
      @NotNull CompletionHandler<FetchMembersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMembers.validateParams(fetchMembersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
  /**
   * Fetch eligible members.
   *
   * @param fetchEligibleMembersQuery the fetch eligible members query
   * @param completionHandler the completion handler
   */
  public void fetchEligibleMembers(@NotNull FetchEligibleMembersQuery fetchEligibleMembersQuery,
      @NotNull CompletionHandler<FetchEligibleMembersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchEligibleMembers.validateParams(fetchEligibleMembersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
  /**
   * Leave member.
   *
   * @param leaveMemberQuery the leave member query
   * @param completionHandler the completion handler
   */
  public void leaveMember(@NotNull LeaveMemberQuery leaveMemberQuery,
      @NotNull CompletionHandler<LeaveMemberResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      leaveMember.validateParams(leaveMemberQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove member.
   *
   * @param removeMemberQuery the remove member query
   * @param completionHandler the completion handler
   */
  public void removeMember(@NotNull RemoveMemberQuery removeMemberQuery,
      @NotNull CompletionHandler<RemoveMemberResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeMember.validateParams(removeMemberQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch member details.
   *
   * @param fetchMemberDetailsQuery the fetch member details query
   * @param completionHandler the completion handler
   */
  public void fetchMemberDetails(@NotNull FetchMemberDetailsQuery fetchMemberDetailsQuery,
      @NotNull CompletionHandler<FetchMemberDetailsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMemberDetails.validateParams(fetchMemberDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
