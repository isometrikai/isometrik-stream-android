package io.isometrik.gs.ui.pk.invitationList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;

import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.pk.invitationList.InviteUserModel;
import io.isometrik.gs.ui.utils.PlaceholderUtils;

/**
 * The type Members adapter.
 */
public class InviteUserAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private ArrayList<InviteUserModel> addMembersModels;
    private final InviteUserFragment addMembersFragment;
    public static final int ONLINE = 0;
    public static final int INVITED = 1;
    private boolean forOnline = true;
    private InviteClickListener clickListener;

    /**
     * Instantiates a new Add member adapter.
     *
     * @param mContext         the m context
     * @param addMembersModels the users
     */
    InviteUserAdapter(Context mContext, ArrayList<InviteUserModel> addMembersModels,
                      InviteUserFragment inviteUserFragment, boolean forOnline, InviteClickListener clickListener) {
        this.mContext = mContext;
        this.addMembersModels = addMembersModels;
        this.addMembersFragment = inviteUserFragment;
        this.forOnline = forOnline;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (!forOnline) {
            return INVITED;
        } else {
            return ONLINE;
        }
    }

    @Override
    public int getItemCount() {
        return addMembersModels.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

//        if (viewType == ONLINE) {

        return new OnlineViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gs_ism_invite_user_item, viewGroup, false));
//        } else {
//
//            return new OnlineViewHolder(LayoutInflater.from(viewGroup.getContext())
//                    .inflate(R.layout.gs_ism_invite_user_item, viewGroup, false));
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == ONLINE) {

            configureViewHolderOnline((OnlineViewHolder) viewHolder, position);
        } else {

            configureViewHolderInvite((OnlineViewHolder) viewHolder, position);
        }
    }

    public void configureViewHolderOnline(@NonNull OnlineViewHolder holder, int position) {

        try {
            InviteUserModel addMembersModel = addMembersModels.get(position);
            if (addMembersModel != null) {
                holder.rlInvite.setVisibility(View.VISIBLE);
                holder.rlAccept.setVisibility(View.GONE);
                holder.tvUserName.setText("@" + addMembersModel.getUserName());
                holder.tvUserIdentifier.setText(addMembersModel.getViewerCount() + "");

                if (PlaceholderUtils.isValidImageUrl(addMembersModel.getProfilePic())) {
                    try {
                        Glide.with(mContext)
                                .load(addMembersModel.getProfilePic())
                                .placeholder(R.drawable.ism_default_profile_image)
                                .transform(new CircleCrop())
                                .into(holder.ivUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {

                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(mContext, addMembersModel.getUserName(),
                            holder.ivUserImage, position, 20);
                }
                if (addMembersModel.isInvited()) {
                    holder.rlInvite.setBackground(mContext.getDrawable(R.drawable.ism_cancel_button));
                    holder.tvAccept.setText(mContext.getString(R.string.pking));
                    holder.rlInvite.setEnabled(false);
                } else {
                    holder.rlInvite.setBackground(mContext.getDrawable(R.drawable.ism_request_button));
                    holder.tvAccept.setText(mContext.getString(R.string.invite));
                    holder.rlInvite.setEnabled(true);

                }

                holder.rlInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.inviteButtonClick(addMembersModel, position);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configureViewHolderInvite(@NonNull OnlineViewHolder holder, int position) {

        try {
            InviteUserModel addMembersModel = addMembersModels.get(position);
            if (addMembersModel != null) {

                holder.rlInvite.setVisibility(View.GONE);
                holder.rlAccept.setVisibility(View.VISIBLE);

                holder.tvUserName.setText("@" + addMembersModel.getUserName());
                holder.tvUserIdentifier.setText(addMembersModel.getViewerCount() + "");

                if (PlaceholderUtils.isValidImageUrl(addMembersModel.getProfilePic())) {
                    try {
                        Glide.with(mContext)
                                .load(addMembersModel.getProfilePic())
                                .placeholder(R.drawable.ism_default_profile_image)
                                .transform(new CircleCrop())
                                .into(holder.ivUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {

                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(mContext, addMembersModel.getUserName(),
                            holder.ivUserImage, position, 20);
                }

//                holder.rlAccept.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        clickListener.acceptButtonClick(addMembersModel);
//                    }
//                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Online User holder.
     */
    static class OnlineViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvUserIdentifier, tvAccept, tvCoinsCount;
        ImageView ivUserImage, ivMember, ivFollower, ivFollowStatus;

        RelativeLayout rlInvite, rlAccept;

        /**
         * Instantiates a new Users view holder.
         *
         * @param itemView the item view
         */
        OnlineViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserIdentifier = itemView.findViewById(R.id.tvUserIdentifier);
            tvAccept = itemView.findViewById(R.id.tv_accept);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
//            tvJoinTime = itemView.findViewById(R.id.tvJoinTime);
            rlInvite = itemView.findViewById(R.id.rlInvite);
            rlAccept = itemView.findViewById(R.id.rlAccept);
//            ivMember = itemView.findViewById(R.id.ivMember);
//            tvCoinsCount = itemView.findViewById(R.id.tvCoinsCount);
//            ivFollowStatus = itemView.findViewById(R.id.ivFollowStatus);
        }
    }

    public void updateData(ArrayList<InviteUserModel> list) {
        addMembersModels = list;
        notifyDataSetChanged();

    }

    public void updateItem(boolean isInvited, int position){
        addMembersModels.get(position).setInvited(isInvited);
        notifyItemChanged(position);
    }

    interface InviteClickListener {

        void inviteButtonClick(InviteUserModel inviteUserModel, int position);

    }
}
