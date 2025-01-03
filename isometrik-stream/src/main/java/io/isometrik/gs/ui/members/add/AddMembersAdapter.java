package io.isometrik.gs.ui.members.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmAddUserItemBinding;
import io.isometrik.gs.ui.databinding.IsmAddViewerItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Members adapter.
 */
public class AddMembersAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<AddMembersModel> addMembersModels;
  private final AddMembersFragment addMembersFragment;
  private final int NORMAL_USER = 0;
  private final int VIEWER = 1;

  /**
   * Instantiates a new Add member adapter.
   *
   * @param mContext the m context
   * @param addMembersModels the users
   */
  AddMembersAdapter(Context mContext, ArrayList<AddMembersModel> addMembersModels,
      AddMembersFragment addMembersFragment) {
    this.mContext = mContext;
    this.addMembersModels = addMembersModels;
    this.addMembersFragment = addMembersFragment;
  }

  @Override
  public int getItemViewType(int position) {

    if (addMembersModels.get(position).isNormaUser()) {
      return NORMAL_USER;
    } else {
      return VIEWER;
    }
  }

  @Override
  public int getItemCount() {
    return addMembersModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    if (viewType == NORMAL_USER) {

      return new UsersViewHolder(
          IsmAddUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
              false));
    } else {

      return new ViewersViewHolder(
          IsmAddViewerItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
              false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    if (viewHolder.getItemViewType() == NORMAL_USER) {

      configureViewHolderUser((UsersViewHolder) viewHolder, position);
    } else {

      configureViewHolderViewer((ViewersViewHolder) viewHolder, position);
    }
  }

  public void configureViewHolderUser(@NonNull UsersViewHolder holder, int position) {

    try {
      AddMembersModel addMembersModel = addMembersModels.get(position);
      if (addMembersModel != null) {
        holder.ismAddUserItemBinding.tvUserName.setText(addMembersModel.getUserName());
        holder.ismAddUserItemBinding.tvUserIdentifier.setText(addMembersModel.getUserIdentifier());

        holder.ismAddUserItemBinding.rlAdd.setOnClickListener(
            v -> addMembersFragment.addMember(addMembersModel.getUserId()));

        if (PlaceholderUtils.isValidImageUrl(addMembersModel.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(addMembersModel.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismAddUserItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, addMembersModel.getUserName(),
              holder.ismAddUserItemBinding.ivUserImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void configureViewHolderViewer(@NonNull ViewersViewHolder holder, int position) {

    try {
      AddMembersModel addMembersModel = addMembersModels.get(position);
      if (addMembersModel != null) {
        holder.ismAddViewerItemBinding.tvUserName.setText(addMembersModel.getUserName());
        holder.ismAddViewerItemBinding.tvUserIdentifier.setText(
            addMembersModel.getUserIdentifier());

        holder.ismAddViewerItemBinding.tvJoinTime.setText(addMembersModel.getJoinTime());
        if (addMembersModel.isMember()) {
          holder.ismAddViewerItemBinding.rlAdd.setVisibility(View.INVISIBLE);
          holder.ismAddViewerItemBinding.ivMember.setVisibility(View.VISIBLE);
        } else {
          holder.ismAddViewerItemBinding.rlAdd.setVisibility(View.VISIBLE);
          holder.ismAddViewerItemBinding.ivMember.setVisibility(View.GONE);
          holder.ismAddViewerItemBinding.rlAdd.setOnClickListener(
              v -> addMembersFragment.addMember(addMembersModel.getUserId()));
        }
        if (PlaceholderUtils.isValidImageUrl(addMembersModel.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(addMembersModel.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismAddViewerItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, addMembersModel.getUserName(),
              holder.ismAddViewerItemBinding.ivUserImage, position, 20);
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

  static class ViewersViewHolder extends RecyclerView.ViewHolder {

    private final IsmAddViewerItemBinding ismAddViewerItemBinding;

    public ViewersViewHolder(final IsmAddViewerItemBinding ismAddViewerItemBinding) {
      super(ismAddViewerItemBinding.getRoot());
      this.ismAddViewerItemBinding = ismAddViewerItemBinding;
    }
  }
}
