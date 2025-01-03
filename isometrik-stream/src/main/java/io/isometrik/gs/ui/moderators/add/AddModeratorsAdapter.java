package io.isometrik.gs.ui.moderators.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmAddUserItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Moderators adapter.
 */
public class AddModeratorsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<AddModeratorsModel> addModeratorsModels;
  private final AddModeratorsFragment addModeratorsFragment;

  /**
   * Instantiates a new Add moderator adapter.
   *
   * @param mContext the m context
   * @param addModeratorsModels the users
   */
  AddModeratorsAdapter(Context mContext, ArrayList<AddModeratorsModel> addModeratorsModels,
      AddModeratorsFragment addModeratorsFragment) {
    this.mContext = mContext;
    this.addModeratorsModels = addModeratorsModels;
    this.addModeratorsFragment = addModeratorsFragment;
  }

  @Override
  public int getItemCount() {
    return addModeratorsModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    return new UsersViewHolder(
        IsmAddUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    UsersViewHolder holder = (UsersViewHolder) viewHolder;
    try {
      AddModeratorsModel addModeratorsModel = addModeratorsModels.get(position);
      if (addModeratorsModel != null) {
        holder.ismAddUserItemBinding.tvUserName.setText(addModeratorsModel.getUserName());
        holder.ismAddUserItemBinding.tvUserIdentifier.setText(
            addModeratorsModel.getUserIdentifier());

        holder.ismAddUserItemBinding.rlAdd.setOnClickListener(
            v -> addModeratorsFragment.addModerator(addModeratorsModel.getUserId()));
        if (PlaceholderUtils.isValidImageUrl(addModeratorsModel.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(addModeratorsModel.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismAddUserItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, addModeratorsModel.getUserName(),
              holder.ismAddUserItemBinding.ivUserImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Users view holder.
   */
  static class UsersViewHolder extends RecyclerView.ViewHolder {

    private final IsmAddUserItemBinding ismAddUserItemBinding;

    public UsersViewHolder(final IsmAddUserItemBinding ismAddUserItemBinding) {
      super(ismAddUserItemBinding.getRoot());
      this.ismAddUserItemBinding = ismAddUserItemBinding;
    }
  }
}
