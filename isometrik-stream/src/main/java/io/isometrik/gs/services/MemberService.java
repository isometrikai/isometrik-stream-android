package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.member.AddMemberResult;
import io.isometrik.gs.response.member.FetchEligibleMembersResult;
import io.isometrik.gs.response.member.FetchMemberDetailsResult;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.member.LeaveMemberResult;
import io.isometrik.gs.response.member.RemoveMemberResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface member service to add,remove member,fetch members in stream group and leave a
 * stream group by a member.
 */
public interface MemberService {

  /**
   * Add member to a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddMemberResult>
   * @see AddMemberResult
   */
  @POST("/streaming/v2/member")
  Call<AddMemberResult> addMember(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove member from a stream group call.
   *
   * @param headers the headers
   * @return the Call<RemoveMemberResult>
   * @see RemoveMemberResult
   */
  @DELETE("/streaming/v2/member")
  Call<RemoveMemberResult> removeMember(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch members in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchMembersResult>
   * @see FetchMembersResult
   */
  @GET("/streaming/v2/members")
  Call<FetchMembersResult> fetchMembers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Leave stream group by a member call.
   *
   * @param headers the headers
   * @return Call<LeaveMemberResult>
   * @see LeaveMemberResult
   */
  @DELETE("/streaming/v2/member/leave")
  Call<LeaveMemberResult> leaveMember(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch member details by uid call.
   *
   * @param headers the headers
   * @return the Call<FetchMemberDetailsResult>
   * @see FetchMemberDetailsResult
   */
  @GET("/streaming/v2/member")
  Call<FetchMemberDetailsResult> fetchMemberDetails(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch eligible members to add to a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchEligibleMembersResult>
   * @see FetchEligibleMembersResult
   */
  @GET("/live/v1/members/eligible")
  Call<FetchEligibleMembersResult> fetchEligibleMembers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

}
