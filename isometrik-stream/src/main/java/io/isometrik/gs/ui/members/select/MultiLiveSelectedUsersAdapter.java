package io.isometrik.gs.ui.members.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmSelectedMembersItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type selected users adapter for the multi live.
 */
public class MultiLiveSelectedUsersAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<MultiLiveSelectMembersModel> users;

  /**
   * Instantiates a new Selected users adapter for the multi live.
   *
   * @param mContext the m context
   * @param users the users
   */
  MultiLiveSelectedUsersAdapter(Context mContext, ArrayList<MultiLiveSelectMembersModel> users) {
    this.mContext = mContext;
    this.users = users;
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new UsersViewHolder(
        IsmSelectedMembersItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      MultiLiveSelectMembersModel user = users.get(position);
      if (user != null) {
        holder.ismSelectedMembersItemBinding.tvUserName.setText(user.getUserName());

        holder.ismSelectedMembersItemBinding.ivRemoveUser.setOnClickListener(
            v -> ((MultiLiveSelectMembersActivity) mContext).removeUser(user.getUserId()));
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismSelectedMembersItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismSelectedMembersItemBinding.ivUserImage, position, 20);
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

    private final IsmSelectedMembersItemBinding ismSelectedMembersItemBinding;

    public UsersViewHolder(final IsmSelectedMembersItemBinding ismSelectedMembersItemBinding) {
      super(ismSelectedMembersItemBinding.getRoot());
      this.ismSelectedMembersItemBinding = ismSelectedMembersItemBinding;
    }
  }
}