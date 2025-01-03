package io.isometrik.gs.ui.metadata;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetAddMetadataBinding;

/**
 * Bottomsheet dialog fragment to allow publisher to add metadata to a
 * broadcast.Metadata callbacks are triggered using interface
 * AddMetadataCallback{@link AddMetadataCallback}
 *
 * @see AddMetadataCallback
 */
public class AddMetadataFragment extends BottomSheetDialogFragment {

  public static final String TAG = "AddMetadataFragment";

  private AddMetadataCallback metadataCallback;

  private IsmBottomsheetAddMetadataBinding ismBottomsheetAddMetadataBinding;

  /**
   * Instantiates a new add metadata fragment.
   */
  public AddMetadataFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (ismBottomsheetAddMetadataBinding == null) {
      ismBottomsheetAddMetadataBinding =
          IsmBottomsheetAddMetadataBinding.inflate(inflater, container, false);
    } else {

      if (ismBottomsheetAddMetadataBinding.getRoot().getParent() != null) {
        ((ViewGroup) ismBottomsheetAddMetadataBinding.getRoot().getParent()).removeView(
            ismBottomsheetAddMetadataBinding.getRoot());
      }
    }
    ismBottomsheetAddMetadataBinding.etMetadata.setText(getString(R.string.ism_metadata_sample));
    ismBottomsheetAddMetadataBinding.etMetadata.requestFocus();

    if (getActivity() != null) {
      InputMethodManager imm =
          (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      if (imm != null) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
      }
    }

    ismBottomsheetAddMetadataBinding.btAddMetadata.setOnClickListener(view -> {
      if (ismBottomsheetAddMetadataBinding.etMetadata.getText().toString().isEmpty()) {
        if (getActivity() != null) {
          Toast.makeText(getActivity(), getString(R.string.ism_metadata_cannot_be_empty),
              Toast.LENGTH_SHORT).show();
        }
      } else {
        metadataCallback.insertMetadataRequested(
            ismBottomsheetAddMetadataBinding.etMetadata.getText().toString());
      }
    });

    return ismBottomsheetAddMetadataBinding.getRoot();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    try {
      dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
        BottomSheetDialog d = (BottomSheetDialog) dialog1;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        @SuppressWarnings("rawtypes")
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }, 0));
    } catch (Exception ignore) {
    }
    return dialog;
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {

    if (getActivity() != null) {
      getActivity().getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    super.onDismiss(dialog);
  }

  /**
   * Update parameters.
   *
   * @param metadataCallback the metadata callback
   */
  public void updateParameters(AddMetadataCallback metadataCallback) {

    this.metadataCallback = metadataCallback;
  }
}