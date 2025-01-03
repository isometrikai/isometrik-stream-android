package io.isometrik.gs.services;

import io.isometrik.gs.response.message.AddMetadataResult;
import io.isometrik.gs.response.message.DeleteMessageReplyResult;
import io.isometrik.gs.response.message.DeleteMessageResult;
import io.isometrik.gs.response.message.FetchMessageRepliesResult;
import io.isometrik.gs.response.message.FetchMessagesCountResult;
import io.isometrik.gs.response.message.FetchMessagesResult;
import io.isometrik.gs.response.message.SendMessageReplyResult;
import io.isometrik.gs.response.message.SendMessageResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface message service to send or remove message in a stream group,fetch messages sent in
 * a stream group.
 */
public interface MessageService {

  /**
   * Send message in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<SendMessageResult>
   * @see SendMessageResult
   */
  @POST("/streaming/v2/message")
  Call<SendMessageResult> sendMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove message from a stream group call.
   *
   * @param headers the headers
   * @return the Call<DeleteMessageResult>
   * @see DeleteMessageResult
   */
  @DELETE("/streaming/v2/message")
  Call<DeleteMessageResult> removeMessage(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch messages in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchMessagesResult>
   * @see FetchMessagesResult
   */
  @GET("/streaming/v2/messages")
  Call<FetchMessagesResult> fetchMessages(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch messages count in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchMessagesCountResult>
   * @see FetchMessagesCountResult
   */
  @GET("/streaming/v2/messages/count")
  Call<FetchMessagesCountResult> fetchMessagesCount(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Add metadata in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddMetadataResult>
   * @see AddMetadataResult
   */
  @POST("/streaming/v2/message/ivs/metadata")
  Call<AddMetadataResult> addMetadata(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Send message reply in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<SendMessageReplyResult>
   * @see SendMessageReplyResult
   */
  @POST("/streaming/v2/message/reply")
  Call<SendMessageReplyResult> sendMessageReply(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove message reply from a stream group call.
   *
   * @param headers the headers
   * @return the Call<DeleteMessageReplyResult>
   * @see DeleteMessageReplyResult
   */
  @DELETE("/streaming/v2/message/reply")
  Call<DeleteMessageReplyResult> removeMessageReply(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch message replies in a stream group call.
   *
   * @param headers the headers
   * @return the Call<FetchMessageRepliesResult>
   * @see FetchMessageRepliesResult
   */
  @GET("/streaming/v2/message/replies")
  Call<FetchMessageRepliesResult> fetchMessageReplies(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
