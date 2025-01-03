package io.isometrik.gs.ui.requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmRequestsItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Requests adapter.
 */
public class RequestsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<RequestsModel> requests;
  private final RequestsFragment requestsBottomSheetFragment;

  /**
   * Instantiates a new Requests adapter.
   *
   * @param mContext the m context
   * @param requests the requests
   * @param requestsBottomSheetFragment the requestsBottomSheetFragment fragment
   */
  RequestsAdapter(Context mContext, ArrayList<RequestsModel> requests,
      RequestsFragment requestsBottomSheetFragment) {
    this.mContext = mContext;
    this.requests = requests;
    this.requestsBottomSheetFragment = requestsBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return requests.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new RequestsViewHolder(
        IsmRequestsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    RequestsViewHolder holder = (RequestsViewHolder) viewHolder;

    try {
      RequestsModel request = requests.get(position);
      if (request != null) {
        holder.ismRequestsItemBinding.tvUserName.setText(request.getUserName());
        holder.ismRequestsItemBinding.tvUserIdentifier.setText(request.getUserIdentifier());

        if (request.isPending()) {
          holder.ismRequestsItemBinding.tvRequestTime.setText(request.getRequestTime());
          holder.ismRequestsItemBinding.tvRequestTime.setTextColor(
              ContextCompat.getColor(mContext, R.color.ism_jointime_text_white));

          if (request.isInitiator()) {

            holder.ismRequestsItemBinding.rlAccept.setVisibility(View.VISIBLE);
            holder.ismRequestsItemBinding.rlDecline.setVisibility(View.VISIBLE);

            holder.ismRequestsItemBinding.rlAccept.setOnClickListener(
                v -> (requestsBottomSheetFragment).acceptCopublishRequest(request.getUserId()));

            holder.ismRequestsItemBinding.rlDecline.setOnClickListener(
                v -> (requestsBottomSheetFragment).declineCopublishRequest(request.getUserId()));
          } else {

            holder.ismRequestsItemBinding.rlAccept.setVisibility(View.GONE);
            holder.ismRequestsItemBinding.rlDecline.setVisibility(View.GONE);
          }
        } else {

          if (request.isAccepted()) {
            holder.ismRequestsItemBinding.tvRequestTime.setText(
                mContext.getString(R.string.ism_accepted));
            holder.ismRequestsItemBinding.tvRequestTime.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_accept_green));
          } else {
            holder.ismRequestsItemBinding.tvRequestTime.setText(
                mContext.getString(R.string.ism_declined));
            holder.ismRequestsItemBinding.tvRequestTime.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_decline_red));
          }
          holder.ismRequestsItemBinding.rlAccept.setVisibility(View.GONE);
          holder.ismRequestsItemBinding.rlDecline.setVisibility(View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(request.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(request.getUserProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismRequestsItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, request.getUserName(),
              holder.ismRequestsItemBinding.ivUserImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Requests view holder.
   */
  static class RequestsViewHolder extends RecyclerView.ViewHolder {

    private final IsmRequestsItemBinding ismRequestsItemBinding;

    public RequestsViewHolder(final IsmRequestsItemBinding ismRequestsItemBinding) {
      super(ismRequestsItemBinding.getRoot());
      this.ismRequestsItemBinding = ismRequestsItemBinding;
    }
  }
}
