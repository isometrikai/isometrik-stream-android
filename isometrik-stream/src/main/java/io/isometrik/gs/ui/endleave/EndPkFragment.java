package io.isometrik.gs.ui.endleave;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetEndPkBinding;
import io.isometrik.gs.ui.settings.callbacks.SettingsActionCallback;


/**
 * Bottomsheet dialog fragment to allow publisher to either stop publishing or leave a
 * broadcast.Host can end a live broadcast as well.Action callbacks are triggered using interface
 * SettingsActionCallback{@link SettingsActionCallback}
 *
 * @see SettingsActionCallback
 */
public class EndPkFragment extends BottomSheetDialogFragment {

    public static final String TAG = "EndPkFragment";

    private SettingsActionCallback actionCallback;
    private Activity activity;
    private boolean isPKOnGoing = false;
    private IsmBottomsheetEndPkBinding ismBottomsheetEndPkBinding;

    public EndPkFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetEndPkBinding == null) {
            ismBottomsheetEndPkBinding =
                    IsmBottomsheetEndPkBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetEndPkBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetEndPkBinding.getRoot().getParent()).removeView(
                        ismBottomsheetEndPkBinding.getRoot());
            }
        }

        if (isPKOnGoing) {
            ismBottomsheetEndPkBinding.tvAction.setText(getString(R.string.ism_stop_pk));
            ismBottomsheetEndPkBinding.tvWarning.setText(getString(R.string.ism_warning_stop_pk));
        } else {
            ismBottomsheetEndPkBinding.tvAction.setText(getString(R.string.ism_end_pk));
            ismBottomsheetEndPkBinding.tvWarning.setText(getString(R.string.ism_warning_end_pk));
        }

        ismBottomsheetEndPkBinding.tvCancel.setOnClickListener(view -> {

            if (activity != null && !activity.isFinishing()) dismiss();

        });

        ismBottomsheetEndPkBinding.tvAction.setOnClickListener(view -> {
            if (actionCallback != null) {
                actionCallback.endPKRequested();
                if (activity != null && !activity.isFinishing()) dismiss();

            }
        });

        return ismBottomsheetEndPkBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        if (context instanceof SettingsActionCallback) {
            actionCallback = (SettingsActionCallback) context;
        }
    }

    public void updateText(boolean isPKOnGoing) {
        this.isPKOnGoing = isPKOnGoing;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionCallback = null;
    }

}
