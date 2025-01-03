package io.isometrik.gs.services;

import java.util.Map;

import io.isometrik.gs.response.recording.FetchRecordingsResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

/**
 * The interface recording service to fetch user's stream recordings.
 */
public interface RecordingService {

  /**
   * Fetch streams call.
   *
   * @param headers the headers
   * @return the Call<FetchRecordingsResult>
   * @see FetchRecordingsResult
   */
  @GET("/streaming/v1/stream/recordings")
  Call<FetchRecordingsResult> fetchRecordings(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
