package io.isometrik.gs.ui.pk.invitesent;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import io.isometrik.gs.ui.IsometrikStreamSdk;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetPkInviteBinding;
import io.isometrik.gs.ui.pk.invitationList.InviteUserContract;
import io.isometrik.gs.ui.pk.invitationList.InviteUserModel;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import io.isometrik.gs.ui.pk.invitationList.InviteUserPresenter;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;

/**
 * The Request Pk bottomsheet dialog fragment .
 */
public class PkInviteFragment extends BottomSheetDialogFragment {

    public static final String TAG = "PkInviteFragment";

    private String initiatorImageUrl, userImageUrl, userName, initiatorUserName, invitedId, initiatorStreamId;
    private boolean isInvited;

    private PkInviteActionCallbacks pkInviteActionCallbacks;
    private IsmBottomsheetPkInviteBinding ismBottomsheetPkInviteBinding;
    private CountDownTimer countDownTimer;
    private PkInviteRelatedEvent pkInviteRelatedEvent;
    private InviteUserContract.Presenter inviteUserPresenter;
    private InviteUserModel inviteUserModel;


    /**
     * Instantiates a new Request copublish fragment.
     */
    public PkInviteFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetPkInviteBinding == null) {
            ismBottomsheetPkInviteBinding =
                    IsmBottomsheetPkInviteBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetPkInviteBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetPkInviteBinding.getRoot().getParent()).removeView(
                        ismBottomsheetPkInviteBinding.getRoot());
            }
        }

        if (inviteUserModel != null) {
            ismBottomsheetPkInviteBinding.tvClose.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.ivInitiatorImage.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.ivClip.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.llBottomButtons.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.VISIBLE);
            ismBottomsheetPkInviteBinding.tvActionHeading.setText(inviteUserModel.getFirstName() + " " + inviteUserModel.getLastName());
            ismBottomsheetPkInviteBinding.tvActionDescription.setText("Requesting to Link with @" + inviteUserModel.getUserName() + ".");

            if (PlaceholderUtils.isValidImageUrl(inviteUserModel.getProfilePic())) {
                try {
                    GlideApp.with(this)
                            .load(inviteUserModel.getProfilePic())
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(new CircleCrop())
                            .into(ismBottomsheetPkInviteBinding.ivUserImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), inviteUserModel.getUserName(),
                        ismBottomsheetPkInviteBinding.ivUserImage, 16);
            }
        } else {
            ismBottomsheetPkInviteBinding.tvClose.setVisibility(View.GONE);

            if (isInvited) {
                ismBottomsheetPkInviteBinding.llBottomButtons.setVisibility(View.VISIBLE);
                ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.GONE);

            } else {
                ismBottomsheetPkInviteBinding.llBottomButtons.setVisibility(View.GONE);
                ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.VISIBLE);
            }
            if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {
                try {
                    GlideApp.with(this)
                            .load(initiatorImageUrl)
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(new CircleCrop())
                            .into(ismBottomsheetPkInviteBinding.ivInitiatorImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), initiatorUserName,
                        ismBottomsheetPkInviteBinding.ivInitiatorImage, 16);
            }
            if (PlaceholderUtils.isValidImageUrl(userImageUrl)) {
                try {
                    GlideApp.with(this)
                            .load(userImageUrl)
                            .placeholder(R.drawable.ism_default_profile_image)
                            .transform(new CircleCrop())
                            .into(ismBottomsheetPkInviteBinding.ivUserImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {

                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(IsometrikStreamSdk.getInstance().getContext(), userName,
                        ismBottomsheetPkInviteBinding.ivUserImage, 16);
            }

            ismBottomsheetPkInviteBinding.tvActionHeading.setText("@" + initiatorUserName + " invite you to link");
            ismBottomsheetPkInviteBinding.tvActionDescription.setText("You have received an invitation from @" + initiatorUserName + " for the PK challenge. Do you want to continue?");

        }

        ismBottomsheetPkInviteBinding.tvAccept.setOnClickListener(
                view -> {
                    countDownTimer.cancel();
                    updateUi(true);
                    pkInviteActionCallbacks.clickOnPkAccept(invitedId, initiatorStreamId, pkInviteRelatedEvent);
                }
        );
        ismBottomsheetPkInviteBinding.tvReject.setOnClickListener(
                view -> pkInviteActionCallbacks.clickOnPkReject(invitedId, initiatorStreamId)
        );

        ismBottomsheetPkInviteBinding.tvClose.setOnClickListener(view -> dismiss());
        if (inviteUserModel != null) {
            startCountDown(true);

        } else {
            startCountDown(false);

        }

        return ismBottomsheetPkInviteBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inviteUserPresenter = new InviteUserPresenter();

    }

    /**
     * Update parameters.
     *
     * @param initiatorImageUrl       the initiator image url
     * @param userImageUrl            the user image url
     * @param pkInviteActionCallbacks the pkInviteActionCallbacks action callbacks
     */
    public void updateParameters(String initiatorImageUrl, String userImageUrl,
                                 PkInviteActionCallbacks pkInviteActionCallbacks, String userName,
                                 String initiatorName, boolean isInvited, String invitedId,
                                 String initiatorStreamId, PkInviteRelatedEvent pkInviteRelatedEvent) {
        this.initiatorImageUrl = initiatorImageUrl;
        this.userImageUrl = userImageUrl;
        this.pkInviteActionCallbacks = pkInviteActionCallbacks;
        this.userName = userName;
        this.initiatorUserName = initiatorName;
        this.isInvited = isInvited;
        this.invitedId = invitedId;
        this.initiatorStreamId = initiatorStreamId;
        this.pkInviteRelatedEvent = pkInviteRelatedEvent;
        this.inviteUserModel = null;
    }

    public void updateParameters(InviteUserModel inviteUserModel) {
        this.inviteUserModel = inviteUserModel;

    }

    private void startCountDown(boolean forInvitationSent) {
        countDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (!forInvitationSent) {
                    ismBottomsheetPkInviteBinding.tvReject.setText("Reject (" + (millisUntilFinished / 1000) + "s)");
                }
            }

            public void onFinish() {
                if (forInvitationSent) {
                    ismBottomsheetPkInviteBinding.tvClose.setVisibility(View.VISIBLE);
                    ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.GONE);
                    ismBottomsheetPkInviteBinding.tvActionDescription.setText(inviteUserModel.getUserName() + " refused to link.");
                } else {
                    pkInviteActionCallbacks.clickOnPkReject(invitedId, initiatorStreamId);
                }
            }

        }.start();
    }

    public void finishTimer(){
        try {
            if(countDownTimer != null){
                countDownTimer.onFinish();
                countDownTimer = null;
            }
            ismBottomsheetPkInviteBinding.tvClose.setVisibility(View.VISIBLE);
            ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.tvActionDescription.setText(inviteUserModel.getUserName() + " refused to link.");
        }catch (Exception e){}

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    public void updateUi(Boolean requestAccepted) {
        if (requestAccepted) {
            ismBottomsheetPkInviteBinding.tvActionHeading.setText(R.string.linking__);
            ismBottomsheetPkInviteBinding.llBottomButtons.setVisibility(View.GONE);
            ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.VISIBLE);
        } else {

            ismBottomsheetPkInviteBinding.tvActionHeading.setText("@" + initiatorUserName + " invite you to link");
            ismBottomsheetPkInviteBinding.llBottomButtons.setVisibility(View.VISIBLE);
            ismBottomsheetPkInviteBinding.progressbar.setVisibility(View.GONE);
        }
    }
}
