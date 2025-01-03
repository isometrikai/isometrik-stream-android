package io.isometrik.gs.ui.members.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.databinding.BottomsheetRejoinBinding;


public class RejoinFragment extends BottomSheetDialogFragment {

    public static final String TAG = RejoinFragment.class.getSimpleName();
    private BottomsheetRejoinBinding ismBottomsheerRejoinBinding;

    private ReJoinActionCallbacks reJoinActionCallbacks;

    public RejoinFragment(ReJoinActionCallbacks reJoinActionCallbacks) {
        // Required empty public constructor
        this.reJoinActionCallbacks = reJoinActionCallbacks;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheerRejoinBinding == null) {
            ismBottomsheerRejoinBinding =
                    BottomsheetRejoinBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheerRejoinBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheerRejoinBinding.getRoot().getParent()).removeView(
                        ismBottomsheerRejoinBinding.getRoot());
            }
        }

        ismBottomsheerRejoinBinding.tvAccept.setOnClickListener(
                view -> {
                    reJoinActionCallbacks.clickOnJoin();
                }
        );
        ismBottomsheerRejoinBinding.tvReject.setOnClickListener(
                view -> reJoinActionCallbacks.clickOnCancel()
        );


        return ismBottomsheerRejoinBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

   public interface ReJoinActionCallbacks {
        void clickOnJoin();
        void clickOnCancel();
    }

}
