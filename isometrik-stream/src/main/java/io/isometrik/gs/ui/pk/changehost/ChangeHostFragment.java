package io.isometrik.gs.ui.pk.changehost;

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
import io.isometrik.gs.ui.databinding.IsmBottomsheetChangeHostBinding;
import io.isometrik.gs.ui.pk.changehost.ChangePkHostActionCallbacks;

/**
 * Bottomsheet dialog fragment to allow publisher to either stop publishing or leave a
 * broadcast.Host can end a live broadcast as well.Action callbacks are triggered using interface

 *
 */
public class ChangeHostFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ChangeHostFragment";

    private boolean isAdmin;
    private Activity activity;
    ChangePkHostActionCallbacks changePkHostActionCallbacks;

    private IsmBottomsheetChangeHostBinding ismBottomsheetChangeHostBinding;

    public ChangeHostFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetChangeHostBinding == null) {
            ismBottomsheetChangeHostBinding =
                    IsmBottomsheetChangeHostBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetChangeHostBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetChangeHostBinding.getRoot().getParent()).removeView(
                        ismBottomsheetChangeHostBinding.getRoot());
            }
        }


        ismBottomsheetChangeHostBinding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        ismBottomsheetChangeHostBinding.tvAction.setOnClickListener(view -> {
            changePkHostActionCallbacks.clickedOnChangeHost(true);
            dismiss();
        });

        return ismBottomsheetChangeHostBinding.getRoot();
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateParameters(boolean isAdmin, ChangePkHostActionCallbacks changePkHostActionCallbacks) {

        this.isAdmin = isAdmin;
        this.changePkHostActionCallbacks = changePkHostActionCallbacks;
    }
}
