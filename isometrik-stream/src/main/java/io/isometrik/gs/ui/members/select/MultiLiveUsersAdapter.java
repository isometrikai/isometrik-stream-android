package io.isometrik.gs.ui.members.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmUsersItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Users adapter.
 */
public class MultiLiveUsersAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<MultiLiveSelectMembersModel> users;

  /**
   * Instantiates a new Users adapter.
   *
   * @param mContext the m context
   * @param users the users
   */
  MultiLiveUsersAdapter(Context mContext, ArrayList<MultiLiveSelectMembersModel> users) {
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
    IsmUsersItemBinding ismUsersItemBinding =
        IsmUsersItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new UsersViewHolder(ismUsersItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    MultiLiveUsersAdapter.UsersViewHolder holder =
        (MultiLiveUsersAdapter.UsersViewHolder) viewHolder;

    try {
      MultiLiveSelectMembersModel user = users.get(position);
      if (user != null) {
        holder.ismUsersItemBinding.tvUserName.setText(user.getUserName());
        holder.ismUsersItemBinding.tvUserIdentifier.setText(user.getUserIdentifier());

        if (user.isSelected()) {
          holder.ismUsersItemBinding.tvSelect.setText(mContext.getString(R.string.ism_remove));
        } else {
          holder.ismUsersItemBinding.tvSelect.setText(mContext.getString(R.string.ism_add));
        }
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismUsersItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismUsersItemBinding.ivUserImage, position, 20);
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

    private final IsmUsersItemBinding ismUsersItemBinding;

    public UsersViewHolder(final IsmUsersItemBinding ismUsersItemBinding) {
      super(ismUsersItemBinding.getRoot());
      this.ismUsersItemBinding = ismUsersItemBinding;
    }
  }
}