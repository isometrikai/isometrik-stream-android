package io.isometrik.gs.ui.live;

import com.cloudinary.android.callback.ErrorInfo;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.members.select.MultiLiveSelectMembersContract;
import io.isometrik.gs.ui.members.select.MultiLiveSelectMembersModel;
import io.isometrik.gs.ui.utils.ImageUtil;
import io.isometrik.gs.ui.utils.UploadImageResult;
import io.isometrik.gs.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.StartStreamQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoLivePresenter implements GoLiveContract.Presenter {

    GoLivePresenter(GoLiveContract.View goLiveView) {
        this.goLiveView = goLiveView;
    }

    private final GoLiveContract.View goLiveView;
    private final Isometrik isometrik = IsometrikStreamSdk.getInstance().getIsometrik();

    /**
     * {@link GoLiveContract.Presenter#requestImageUpload(String)}
     */
    @Override
    public void requestImageUpload(String path) {

        UploadImageResult uploadImageResult = new UploadImageResult() {

            @Override
            public void uploadSuccess(String requestId, @SuppressWarnings("rawtypes") Map resultData) {
                goLiveView.onImageUploadResult((String) resultData.get("url"));
            }

            @Override
            public void uploadError(String requestId, ErrorInfo error) {

                goLiveView.onImageUploadError(error.getDescription());
            }
        };

        ImageUtil.requestUploadImage(path, true, null, uploadImageResult);
    }

    /**
     * {@link GoLiveContract.Presenter#deleteImage(File)}
     */
    @Override
    public void deleteImage(File imageFile) {
        if (imageFile != null && imageFile.exists()) {

            //noinspection ResultOfMethodCallIgnored
            imageFile.delete();
        }
    }

    /**
     * {@link GoLiveContract.Presenter#startBroadcast(String, String, boolean, boolean,
     * boolean, boolean, boolean, boolean, boolean, boolean, ArrayList, boolean, boolean, boolean)}
     */
    @Override
    public void startBroadcast(String streamDescription, String streamImageUrl, boolean isPublic, boolean audioOnly, boolean multiLive, boolean enableRecording, boolean hdBroadcast, boolean lowLatencyMode, boolean restream, boolean productsLinked, ArrayList<String> productIds, boolean selfHosted, boolean rtmpIngest, boolean persistRtmpIngestEndpoint) {

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();
        List<String> searchableTags = new ArrayList<>();
        searchableTags.add(streamDescription);
        searchableTags.add(IsometrikStreamSdk.getInstance().getUserSession().getUserName());

        isometrik.getRemoteUseCases().getStreamsUseCases().startStream(new StartStreamQuery.Builder().setStreamDescription(streamDescription).setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken()).setStreamImage(streamImageUrl).setPublic(isPublic).setAudioOnly(audioOnly).setMultiLive(multiLive).setMembers(new ArrayList<>()).setEnableRecording(enableRecording).setHdBroadcast(hdBroadcast).setLowLatencyMode(lowLatencyMode).setRestream(restream).setProductsLinked(productsLinked).setProductIds(productIds).setSelfHosted(selfHosted).setSearchableTags(searchableTags).setRtmpIngest(rtmpIngest).setPersistRtmpIngestEndpoint(persistRtmpIngestEndpoint).build(), (var1, var2) -> {

            if (var1 != null) {

                goLiveView.onBroadcastStarted(var1.getStreamId(), streamDescription, streamImageUrl,
                        new ArrayList<>(), var1.getStartTime(), userSession.getUserId(), audioOnly, var1.getIngestEndpoint(),
                        var1.getStreamKey(), hdBroadcast, restream, var1.getRtcToken(), var1.getRestreamEndpoints(), productsLinked,
                        productIds.size(), null);
            }
            //else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
            //  goLiveView.noRestreamChannelsFound();
            //}
            else {

                goLiveView.onError(var2.getErrorMessage());
            }
        });
    }

    /**
     * {@link MultiLiveSelectMembersContract.Presenter#startBroadcast(String, String, ArrayList,
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, ArrayList, boolean, boolean, boolean)}
     */
    @Override
    public void startBroadcast(String streamDescription, String streamImageUrl,
                               ArrayList<MultiLiveSelectMembersModel> members, boolean isPublic, boolean audioOnly,
                               boolean multiLive, boolean enableRecording, boolean hdBroadcast, boolean lowLatencyMode,
                               boolean restream, boolean productsLinked, ArrayList<String> productIds, boolean selfHosted, boolean rtmpIngest, boolean persistRtmpIngestEndpoint) {

        UserSession userSession = IsometrikStreamSdk.getInstance().getUserSession();

        ArrayList<String> memberIds = new ArrayList<>();
        int size = members.size();
        for (int i = 0; i < size; i++) {

            memberIds.add(members.get(i).getUserId());
        }
        List<String> searchableTags = new ArrayList<>();
        searchableTags.add(streamDescription);
        searchableTags.add(IsometrikStreamSdk.getInstance().getUserSession().getUserName());

        isometrik.getRemoteUseCases()
                .getStreamsUseCases()
                .startStream(new StartStreamQuery.Builder().setStreamDescription(streamDescription)
                        .setUserId(IsometrikStreamSdk.getInstance().getUserSession().getUserId())
                        .setStreamImage(streamImageUrl)
                        .setMembers(memberIds)
                        .setPublic(isPublic)
                        .setMultiLive(multiLive)
                        .setAudioOnly(audioOnly)
                        .setEnableRecording(enableRecording)
                        .setHdBroadcast(hdBroadcast)
                        .setLowLatencyMode(lowLatencyMode)
                        .setRestream(restream)
                        .setProductsLinked(productsLinked)
                        .setProductIds(productIds)
                        .setSelfHosted(selfHosted)
                        .setSearchableTags(searchableTags)
                        .setRtmpIngest(rtmpIngest)
                        .setPersistRtmpIngestEndpoint(persistRtmpIngestEndpoint)
                        .setUserToken(IsometrikStreamSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {

                    if (var1 != null) {
                        String rtmpIngestUrl = null;
                        if (selfHosted && rtmpIngest) {
                            rtmpIngestUrl = var1.getIngestEndpoint() + "/" + var1.getStreamKey();
                        }

                        goLiveView.onBroadcastStarted(var1.getStreamId(), streamDescription,
                                streamImageUrl, memberIds, var1.getStartTime(), userSession.getUserId(),
                                var1.getIngestEndpoint(), var1.getStreamKey(), hdBroadcast, restream,
                                var1.getRtcToken(), var1.getRestreamEndpoints(), productsLinked, productIds.size(), rtmpIngestUrl);
                    }
                    //else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 5) {
                    //  selectMembersView.noRestreamChannelsFound();
                    //}
                    else {

                        goLiveView.onError(var2.getErrorMessage());
                    }
                });
    }


}
