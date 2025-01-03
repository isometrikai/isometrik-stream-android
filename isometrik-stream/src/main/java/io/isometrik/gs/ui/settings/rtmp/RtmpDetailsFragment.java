package io.isometrik.gs.ui.settings.rtmp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmBottomsheetAcceptedCopublishBinding;
import io.isometrik.gs.ui.databinding.IsmBottomsheetRtmpDetailsBinding;

/**
 * The Rtmp details bottomsheet dialog fragment to show the bottomsheet for rtmp details to broadcast into a stream.
 */
public class RtmpDetailsFragment extends BottomSheetDialogFragment {

    public static final String TAG = "RtmpDetailsFragment";


    private String rtmpIngestUrl;

    private IsmBottomsheetRtmpDetailsBinding ismBottomsheetRtmpDetailsBinding;

    /**
     * Instantiates a new rtmp details fragment.
     */
    public RtmpDetailsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetRtmpDetailsBinding == null) {
            ismBottomsheetRtmpDetailsBinding =
                    IsmBottomsheetRtmpDetailsBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetRtmpDetailsBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetRtmpDetailsBinding.getRoot().getParent()).removeView(
                        ismBottomsheetRtmpDetailsBinding.getRoot());
            }
        }
        ismBottomsheetRtmpDetailsBinding.tvRtmpIngestUrl.setText(
                rtmpIngestUrl);

        ismBottomsheetRtmpDetailsBinding.imgCopy.setOnClickListener(v -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("RTMP URL", ismBottomsheetRtmpDetailsBinding.tvRtmpIngestUrl.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Copied", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }


        });

        return ismBottomsheetRtmpDetailsBinding.getRoot();
    }

    /**
     * Update parameters.
     *
     * @param rtmpIngestUrl the rtmp ingest url
     */
    public void updateParameters(String rtmpIngestUrl) {
        this.rtmpIngestUrl = rtmpIngestUrl;
    }
}
