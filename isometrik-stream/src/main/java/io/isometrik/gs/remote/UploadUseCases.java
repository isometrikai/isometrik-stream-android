package io.isometrik.gs.remote;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.gs.builder.upload.UploadUserImageQuery;
import io.isometrik.gs.managers.MediaTransferManager;
import io.isometrik.gs.models.upload.UploadUserImage;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.upload.CancelUserImageUploadResult;
import io.isometrik.gs.response.upload.UploadUserImageResult;

/**
 * The remote use case class containing methods for api calls for upload operations-
 * UploadMedia, UploadConversationImage and UploadUserImage.
 */
public class UploadUseCases {

  private final UploadUserImage uploadUserImage;

  private final MediaTransferManager mediaTransferManager;
  private final BaseResponse baseResponse;

  /**
   * Instantiates a new Upload use cases.
   *
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   */
  public UploadUseCases(MediaTransferManager mediaTransferManager, BaseResponse baseResponse) {

    this.mediaTransferManager = mediaTransferManager;
    this.baseResponse = baseResponse;

    uploadUserImage = new UploadUserImage();
  }

  /**
   * Upload user image.
   *
   * @param uploadUserImageQuery the upload user image query
   * @param completionHandler the completion handler
   */
  public void uploadUserImage(@NotNull UploadUserImageQuery uploadUserImageQuery,
      @NotNull CompletionHandler<UploadUserImageResult> completionHandler) {

    uploadUserImage.uploadUserImage(uploadUserImageQuery, completionHandler, mediaTransferManager,
        baseResponse);
  }

  /**
   * Cancel user image upload.
   *
   * @param cancelUserImageUploadQuery the cancel user image upload query
   * @param completionHandler the completion handler
   */
  public void cancelUserImageUpload(@NotNull CancelUserImageUploadQuery cancelUserImageUploadQuery,
      @NotNull CompletionHandler<CancelUserImageUploadResult> completionHandler) {

    uploadUserImage.cancelUserImageUpload(cancelUserImageUploadQuery, completionHandler);
  }
}
