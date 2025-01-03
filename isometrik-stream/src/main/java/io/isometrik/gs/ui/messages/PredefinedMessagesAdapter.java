package io.isometrik.gs.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.databinding.IsmPresetMessageItemBinding;
import io.isometrik.gs.ui.scrollable.webrtc.MultiliveStreamsActivity;

/**
 * The type Predefined messages adapter.
 */
public class PredefinedMessagesAdapter extends RecyclerView.Adapter {

  private final String[] presetMessages;
  private final Context context;

  /**
   * Instantiates a new Predefined messages adapter.
   *
   * @param presetMessages the preset messages
   * @param context the context
   */
  public PredefinedMessagesAdapter(String[] presetMessages, Context context) {
    this.presetMessages = presetMessages;
    this.context = context;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new PresetMessagesViewHolder(
        IsmPresetMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    try {

      PresetMessagesViewHolder presetMessagesViewHolder = (PresetMessagesViewHolder) viewHolder;
      presetMessagesViewHolder.ismPresetMessageItemBinding.tvPresetMessage.setText(
          presetMessages[position]);
      presetMessagesViewHolder.ismPresetMessageItemBinding.tvPresetMessage.setOnClickListener(v -> {
    if (context instanceof MultiliveStreamsActivity) {
          ((MultiliveStreamsActivity) context).sendPresetMessage(presetMessages[position]);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return (presetMessages != null) ? presetMessages.length : 0;
  }

  /**
   * The type Preset messages view holder.
   */
  static class PresetMessagesViewHolder extends RecyclerView.ViewHolder {
    private final IsmPresetMessageItemBinding ismPresetMessageItemBinding;

    public PresetMessagesViewHolder(final IsmPresetMessageItemBinding ismPresetMessageItemBinding) {
      super(ismPresetMessageItemBinding.getRoot());
      this.ismPresetMessageItemBinding = ismPresetMessageItemBinding;
    }
  }
}
