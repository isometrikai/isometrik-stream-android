package io.isometrik.gs.ui.members.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmMembersItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Members adapter.
 */
public class MembersAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<MembersModel> members;
  private final MembersFragment membersBottomSheetFragment;

  /**
   * Instantiates a new Members adapter.
   *
   * @param mContext the m context
   * @param members the members
   * @param membersBottomSheetFragment the membersBottomSheetFragment fragment
   */
  MembersAdapter(Context mContext, ArrayList<MembersModel> members,
      MembersFragment membersBottomSheetFragment) {
    this.mContext = mContext;
    this.members = members;
    this.membersBottomSheetFragment = membersBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return members.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new MembersViewHolder(
        IsmMembersItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    MembersAdapter.MembersViewHolder holder = (MembersAdapter.MembersViewHolder) viewHolder;

    try {
      MembersModel member = members.get(position);
      if (member != null) {
        holder.ismMembersItemBinding.tvMemberName.setText(member.getMemberName());
        holder.ismMembersItemBinding.tvMemberIdentifier.setText(member.getMemberIdentifier());
        holder.ismMembersItemBinding.tvJoinTime.setText(member.getJoinTime());

        if (member.isAdmin()) {

          holder.ismMembersItemBinding.tvAdmin.setText(mContext.getString(R.string.ism_host));
          holder.ismMembersItemBinding.rlAdmin.setVisibility(View.VISIBLE);
          holder.ismMembersItemBinding.rlAdmin.setBackground(
              ContextCompat.getDrawable(mContext, R.drawable.ism_copublish_button));
          holder.ismMembersItemBinding.rlAdmin.setOnClickListener(null);
        } else {

          if (member.isCanRemoveMember()) {
            holder.ismMembersItemBinding.tvAdmin.setText(mContext.getString(R.string.ism_kickout));
            holder.ismMembersItemBinding.rlAdmin.setVisibility(View.VISIBLE);
            holder.ismMembersItemBinding.rlAdmin.setBackground(
                ContextCompat.getDrawable(mContext, R.drawable.ism_request_button));

            holder.ismMembersItemBinding.rlAdmin.setOnClickListener(
                v -> (membersBottomSheetFragment).requestRemoveMember(member.getMemberId()));
          } else {

            holder.ismMembersItemBinding.rlAdmin.setVisibility(View.GONE);
          }
        }

        if (member.isPublishing()) {

          holder.ismMembersItemBinding.ivLiveStatus.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_green_dark));
        } else {

          holder.ismMembersItemBinding.ivLiveStatus.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_red_dark));
        }
        if (PlaceholderUtils.isValidImageUrl(member.getMemberProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(member.getMemberProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismMembersItemBinding.ivMemberImage);
          } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, member.getMemberName(),
              holder.ismMembersItemBinding.ivMemberImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Members view holder.
   */
  static class MembersViewHolder extends RecyclerView.ViewHolder {

    private final IsmMembersItemBinding ismMembersItemBinding;

    public MembersViewHolder(final IsmMembersItemBinding ismMembersItemBinding) {
      super(ismMembersItemBinding.getRoot());
      this.ismMembersItemBinding = ismMembersItemBinding;
    }
  }
}
