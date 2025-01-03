package io.isometrik.gs.ui.moderators.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmModeratorsItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Moderators adapter.
 */
public class ModeratorsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<ModeratorsModel> moderators;
  private final ModeratorsFragment moderatorsBottomSheetFragment;

  /**
   * Instantiates a new Moderators adapter.
   *
   * @param mContext the m context
   * @param moderators the moderators
   * @param moderatorsBottomSheetFragment the moderatorsBottomSheetFragment fragment
   */
  ModeratorsAdapter(Context mContext, ArrayList<ModeratorsModel> moderators,
      ModeratorsFragment moderatorsBottomSheetFragment) {
    this.mContext = mContext;
    this.moderators = moderators;
    this.moderatorsBottomSheetFragment = moderatorsBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return moderators.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ModeratorsViewHolder(
        IsmModeratorsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ModeratorsViewHolder holder = (ModeratorsViewHolder) viewHolder;

    try {
      ModeratorsModel moderator = moderators.get(position);
      if (moderator != null) {
        holder.ismModeratorsItemBinding.tvModeratorName.setText(moderator.getModeratorName());
        holder.ismModeratorsItemBinding.tvModeratorIdentifier.setText(
            moderator.getModeratorIdentifier());

        if (moderator.isAdmin()) {

          holder.ismModeratorsItemBinding.tvAdmin.setText(mContext.getString(R.string.ism_host));
          holder.ismModeratorsItemBinding.rlAdmin.setVisibility(View.VISIBLE);
          holder.ismModeratorsItemBinding.rlAdmin.setBackground(
              ContextCompat.getDrawable(mContext, R.drawable.ism_copublish_button));
        } else {

          if (moderator.isCanRemoveModerator()) {
            holder.ismModeratorsItemBinding.tvAdmin.setText(
                mContext.getString(R.string.ism_kickout));
            holder.ismModeratorsItemBinding.rlAdmin.setVisibility(View.VISIBLE);
            holder.ismModeratorsItemBinding.rlAdmin.setBackground(
                ContextCompat.getDrawable(mContext, R.drawable.ism_request_button));

            holder.ismModeratorsItemBinding.rlAdmin.setOnClickListener(
                v -> (moderatorsBottomSheetFragment).requestRemoveModerator(
                    moderator.getModeratorId()));
          } else {

            holder.ismModeratorsItemBinding.rlAdmin.setVisibility(View.GONE);
          }
        }
        if (PlaceholderUtils.isValidImageUrl(moderator.getModeratorProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(moderator.getModeratorProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismModeratorsItemBinding.ivModeratorImage);
          } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, moderator.getModeratorName(),
              holder.ismModeratorsItemBinding.ivModeratorImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Moderators view holder.
   */
  static class ModeratorsViewHolder extends RecyclerView.ViewHolder {

    private final IsmModeratorsItemBinding ismModeratorsItemBinding;

    public ModeratorsViewHolder(final IsmModeratorsItemBinding ismModeratorsItemBinding) {
      super(ismModeratorsItemBinding.getRoot());
      this.ismModeratorsItemBinding = ismModeratorsItemBinding;
    }
  }
}
