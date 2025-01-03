package io.isometrik.gs.ui.pk.challengsSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.databinding.BottomsheetPkChallengeSettingsBinding;


/**
 * The PK Challenge Settings Screen And Start
 */
public class PkChallengeSettingsFragment extends BottomSheetDialogFragment {

    public static final String TAG = "PkChallengeSettingsFragment";

    private PkSettingsActionCallbacks pkSettingsActionCallbacks;

    private String userImageUrl, userName;
    private int selectedMin = 1;

    private BottomsheetPkChallengeSettingsBinding bottomsheetPkChallengeSettingsBinding;


    public PkChallengeSettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (bottomsheetPkChallengeSettingsBinding == null) {
            bottomsheetPkChallengeSettingsBinding =
                    BottomsheetPkChallengeSettingsBinding.inflate(inflater, container, false);
        } else {

            if (bottomsheetPkChallengeSettingsBinding.getRoot().getParent() != null) {
                ((ViewGroup) bottomsheetPkChallengeSettingsBinding.getRoot().getParent()).removeView(
                        bottomsheetPkChallengeSettingsBinding.getRoot());
            }
        }

        bottomsheetPkChallengeSettingsBinding.toggle1Min.setChecked(true);

        bottomsheetPkChallengeSettingsBinding.btStartPublish.setOnClickListener(
                view -> pkSettingsActionCallbacks.clickOnPkStart(selectedMin));

        bottomsheetPkChallengeSettingsBinding.toggle1Min.setOnClickListener(view -> {
            selectedMin = 1;
            clearSelection();
            bottomsheetPkChallengeSettingsBinding.toggle1Min.setChecked(true);
        });
        bottomsheetPkChallengeSettingsBinding.toggle3Min.setOnClickListener(view -> {
            selectedMin = 3;
            clearSelection();
            bottomsheetPkChallengeSettingsBinding.toggle3Min.setChecked(true);
        });
        bottomsheetPkChallengeSettingsBinding.toggle5Min.setOnClickListener(view -> {
            selectedMin = 5;
            clearSelection();
            bottomsheetPkChallengeSettingsBinding.toggle5Min.setChecked(true);
        });
        bottomsheetPkChallengeSettingsBinding.toggle10Min.setOnClickListener(view -> {
            selectedMin = 10;
            clearSelection();
            bottomsheetPkChallengeSettingsBinding.toggle10Min.setChecked(true);
        });
        return bottomsheetPkChallengeSettingsBinding.getRoot();
    }

    /**
     * Update parameters.
     *
     * @param userImageUrl             the user image url
     * @param userName                 the user name
     * @param pkSettingsActionCallbacks the copublish action callbacks
     */
    public void updateParameters(String userImageUrl, String userName,
                                 PkSettingsActionCallbacks pkSettingsActionCallbacks) {
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.pkSettingsActionCallbacks = pkSettingsActionCallbacks;
    }

    public void clearSelection() {
        bottomsheetPkChallengeSettingsBinding.toggle1Min.setChecked(false);
        bottomsheetPkChallengeSettingsBinding.toggle3Min.setChecked(false);
        bottomsheetPkChallengeSettingsBinding.toggle5Min.setChecked(false);
        bottomsheetPkChallengeSettingsBinding.toggle10Min.setChecked(false);

    }


}
