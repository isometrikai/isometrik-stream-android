package io.isometrik.gs.ui.action.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.action.CopublishActionCallbacks;
import io.isometrik.gs.ui.databinding.IsmBottomsheetCopublishRequestStatusBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;

/**
 * The Copublish request status bottomsheet dialog fragment to fetch the status of the copublish
 * request by a user for
 * a stream group.
 */
public class CopublishRequestStatusFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CopublishRequestStatusFragment";

    private CopublishActionCallbacks copublishActionCallbacks;

    private String userImageUrl, userName, initiatorName, initiatorImageUrl;
    private boolean pending, accepted;

    private IsmBottomsheetCopublishRequestStatusBinding ismBottomsheetCopublishRequestStatusBinding;

    /**
     * Instantiates a new Copublish request status fragment.
     */
    public CopublishRequestStatusFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetCopublishRequestStatusBinding == null) {
            ismBottomsheetCopublishRequestStatusBinding =
                    IsmBottomsheetCopublishRequestStatusBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetCopublishRequestStatusBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetCopublishRequestStatusBinding.getRoot().getParent()).removeView(
                        ismBottomsheetCopublishRequestStatusBinding.getRoot());
            }
        }

        if (pending) {
            ismBottomsheetCopublishRequestStatusBinding.rlPending.setVisibility(View.VISIBLE);
            ismBottomsheetCopublishRequestStatusBinding.rlAccepted.setVisibility(View.GONE);
            ismBottomsheetCopublishRequestStatusBinding.rlRejected.setVisibility(View.GONE);
            ismBottomsheetCopublishRequestStatusBinding.tvActionDescription.setText(
                    getString(R.string.ism_copublish_request_pending_description, initiatorName));
            if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
                try {
                    GlideApp.with(this)
                            .load(initiatorImageUrl)
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(new CircleCrop())
                            .into(ismBottomsheetCopublishRequestStatusBinding.ivInitiatorImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(),
                        initiatorName, ismBottomsheetCopublishRequestStatusBinding.ivInitiatorImage, 16);
            }
            if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {
                try {
                    GlideApp.with(this)
                            .load(userImageUrl)
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(new CircleCrop())
                            .into(ismBottomsheetCopublishRequestStatusBinding.ivPendingUserImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), userName,
                        ismBottomsheetCopublishRequestStatusBinding.ivPendingUserImage, 16);
            }
        } else {
            ismBottomsheetCopublishRequestStatusBinding.rlPending.setVisibility(View.GONE);
            if (accepted) {
                ismBottomsheetCopublishRequestStatusBinding.rlAccepted.setVisibility(View.VISIBLE);
                ismBottomsheetCopublishRequestStatusBinding.rlRejected.setVisibility(View.GONE);
                ismBottomsheetCopublishRequestStatusBinding.tvAcceptedHeading.setText(
                        getString(R.string.ism_copublish_previously_accepted_heading, initiatorName));
                if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
                    try {
                        GlideApp.with(this)
                                .load(initiatorImageUrl)
                                .placeholder(R.drawable.ism_default_profile_image)
                                .transform(new CircleCrop())
                                .into(ismBottomsheetCopublishRequestStatusBinding.ivAcceptedUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {

                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(),
                            initiatorName, ismBottomsheetCopublishRequestStatusBinding.ivAcceptedUserImage, 33);
                }
            } else {
                ismBottomsheetCopublishRequestStatusBinding.rlAccepted.setVisibility(View.GONE);
                ismBottomsheetCopublishRequestStatusBinding.rlRejected.setVisibility(View.VISIBLE);
                ismBottomsheetCopublishRequestStatusBinding.tvRejectedHeading.setText(
                        getString(R.string.ism_copublish_previously_rejected_heading, initiatorName));
                if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
                    try {
                        GlideApp.with(this)
                                .load(initiatorImageUrl)
                                .placeholder(R.drawable.ism_default_profile_image)
                                .transform(new CircleCrop())
                                .into(ismBottomsheetCopublishRequestStatusBinding.ivRejectedUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {

                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(),
                            initiatorName, ismBottomsheetCopublishRequestStatusBinding.ivRejectedUserImage, 33);
                }
            }
        }
        /*
         * Exit on previously accepted request but no longer member
         */
        ismBottomsheetCopublishRequestStatusBinding.btExit.setOnClickListener(
                view -> copublishActionCallbacks.exitOnNoLongerBeingAMember());
        /*
         * Delete copublish request.
         */
        ismBottomsheetCopublishRequestStatusBinding.tvDelete.setOnClickListener(
                view -> copublishActionCallbacks.deleteCopublishRequest());
        /*
         * Exit on copublish request rejected.
         */
        ismBottomsheetCopublishRequestStatusBinding.tvExit.setOnClickListener(
                view -> copublishActionCallbacks.exitOnCopublishRequestRejected());
        /*
         * Continue watching on copublish request accepted.
         */
        ismBottomsheetCopublishRequestStatusBinding.tvContinueAccept.setOnClickListener(
                view -> copublishActionCallbacks.continueWatching());
        /*
         * Continue watching on copublish request rejected.
         */
        ismBottomsheetCopublishRequestStatusBinding.tvContinueReject.setOnClickListener(
                view -> copublishActionCallbacks.continueWatching());
        ismBottomsheetCopublishRequestStatusBinding.tvContinue.setOnClickListener(
                view -> copublishActionCallbacks.continueWatching());

        return ismBottomsheetCopublishRequestStatusBinding.getRoot();
    }

    /**
     * Update parameters.
     *
     * @param initiatorImageUrl        the initiator image url
     * @param userImageUrl             the user image url
     * @param initiatorName            the initiator name
     * @param pending                  the pending state of copublish request
     * @param accepted                 the accepted state of copublish request
     * @param copublishActionCallbacks the copublish action callbacks
     */
    public void updateParameters(String initiatorImageUrl, String userImageUrl, String initiatorName,
                                 boolean pending, boolean accepted, CopublishActionCallbacks copublishActionCallbacks,
                                 String userName) {
        this.initiatorImageUrl = initiatorImageUrl;
        this.userImageUrl = userImageUrl;
        this.initiatorName = initiatorName;
        this.copublishActionCallbacks = copublishActionCallbacks;
        this.pending = pending;
        this.accepted = accepted;
        this.userName = userName;
    }
}
