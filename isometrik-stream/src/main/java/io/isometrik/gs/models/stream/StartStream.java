package io.isometrik.gs.models.stream;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.stream.StartStreamResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the create stream group StartStreamQuery{@link
 * StartStreamQuery} request and parse the response(both success or
 * error) to return start stream result
 * StartStreamResult{@link StartStreamResult}
 * or error received.
 *
 * @see StartStreamQuery
 * @see StartStreamResult
 */
public class StartStream {

    /**
     * Validate params.
     *
     * @param startStreamQuery  the start stream request query
     * @param completionHandler the create stream group request completion handler
     * @param retrofitManager   the retrofit manager to make remote api calls
     * @param imConfiguration   the isometrik configuration instance
     * @param baseResponse      the base response instance to handle non 200 http response code messages
     * @param gson              the gson instance
     * @see StartStreamQuery
     * @see CompletionHandler
     * @see RetrofitManager
     * @see IMConfiguration
     * @see BaseResponse
     * @see com.google.gson.Gson
     */
    public void validateParams(@NotNull StartStreamQuery startStreamQuery,
                               @NotNull final CompletionHandler<StartStreamResult> completionHandler,
                               @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
                               @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

        String streamImage = startStreamQuery.getStreamImage();
        String streamDescription = startStreamQuery.getStreamDescription();
        Boolean isPublic = startStreamQuery.isPublic();
        Boolean audioOnly = startStreamQuery.getAudioOnly();
        Boolean multiLive = startStreamQuery.getMultiLive();

        Boolean enableRecording = startStreamQuery.getEnableRecording();
        Boolean lowLatencyMode = startStreamQuery.getLowLatencyMode();
        Boolean hdBroadcast = startStreamQuery.getHdBroadcast();
        Boolean restream = startStreamQuery.getRestream();
        Boolean productsLinked = startStreamQuery.getProductsLinked();
        Boolean selfHosted = startStreamQuery.getSelfHosted();
        Boolean rtmpIngest = startStreamQuery.getRtmpIngest();
        Boolean persistRtmpIngestEndpoint = startStreamQuery.getPersistRtmpIngestEndpoint();

        List<String> members = startStreamQuery.getMembers();
        List<String> productIds = startStreamQuery.getProductIds();

        String customType = startStreamQuery.getCustomType();
        JSONObject metaData = startStreamQuery.getMetaData();
        List<String> searchableTags = startStreamQuery.getSearchableTags();

        String userToken = startStreamQuery.getUserToken();
        String userId = startStreamQuery.getUserId();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else if (userId == null || userId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_REQUEST_BY_USERID_MISSING);
        } else if (streamImage == null || streamImage.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_IMAGE_MISSING);
        } else if (streamDescription == null || streamDescription.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_DESCRIPTION_MISSING);
        } else if (members == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_MEMBERS_MISSING);
        } else if (isPublic == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_IS_PUBLIC_MISSING);
        } else if (audioOnly == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_AUDIO_ONLY_MISSING);
        } else if (multiLive == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MULTI_LIVE_MISSING);
        } else if (enableRecording == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_ENABLE_RECORDING_MISSING);
        } else if (lowLatencyMode == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_LOW_LATENCY_MODE_MISSING);
        } else if (hdBroadcast == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_HD_BROADCAST_MISSING);
        } else if (restream == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_RESTREAM_MISSING);
        } else if (selfHosted == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SELF_HOSTED_MISSING);
        } else if (productsLinked == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRODUCTS_LINKED_MISSING);
        } else if (productsLinked && (productIds == null || productIds.isEmpty())) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRODUCT_IDS_MISSING);
        } else if (rtmpIngest == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_RTMP_INGEST_MISSING);
        } else if (persistRtmpIngestEndpoint == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PERSIST_RTMP_INGEST_ENDPOINT_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> body = new HashMap<>();
            if (metaData != null) body.put("metaData", JsonParser.parseString(metaData.toString()));
            if (customType != null) body.put("customType", customType);
            if (searchableTags != null) body.put("searchableTags", searchableTags);

            body.put("userId", userId);
            body.put("streamImage", streamImage);
            body.put("streamDescription", streamDescription);
            body.put("members", members);
            body.put("isPublic", isPublic);
            body.put("audioOnly", audioOnly);
            body.put("multiLive", multiLive);
            body.put("isGroupStream", true);
            body.put("enableRecording", enableRecording);
            body.put("lowLatencyMode", lowLatencyMode);
            body.put("hdBroadcast", hdBroadcast);
            body.put("restream", restream);
            body.put("productsLinked", productsLinked);
            body.put("products", productIds);
            body.put("isSelfHosted", selfHosted);
            body.put("rtmpIngest", rtmpIngest);
            body.put("persistRtmpIngestEndpoint", persistRtmpIngestEndpoint);

            Call<StartStreamResult> call = retrofitManager.getStreamService().startStream(headers, body);
            call.enqueue(new Callback<StartStreamResult>() {
                @Override
                public void onResponse(@NotNull Call<StartStreamResult> call,
                                       @NotNull Response<StartStreamResult> response) {

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
                public void onFailure(@NotNull Call<StartStreamResult> call, @NotNull Throwable t) {
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
