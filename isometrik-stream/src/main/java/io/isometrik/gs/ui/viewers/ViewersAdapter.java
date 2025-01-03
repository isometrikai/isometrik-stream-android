package io.isometrik.gs.ui.viewers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmViewersItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import io.isometrik.gs.ui.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The type Viewers adapter.
 */
public class ViewersAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<ViewersModel> viewers;
  private final ViewersFragment viewersBottomSheetFragment;

  /**
   * Instantiates a new Viewers adapter.
   *
   * @param mContext the m context
   * @param viewers the viewers
   * @param viewersBottomSheetFragment the viewersBottomSheetFragment fragment
   */
  ViewersAdapter(Context mContext, ArrayList<ViewersModel> viewers,
      ViewersFragment viewersBottomSheetFragment) {
    this.mContext = mContext;
    this.viewers = viewers;
    this.viewersBottomSheetFragment = viewersBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return viewers.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmViewersItemBinding ismViewersItemBinding =
        IsmViewersItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ViewersViewHolder(ismViewersItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ViewersAdapter.ViewersViewHolder holder = (ViewersAdapter.ViewersViewHolder) viewHolder;

    try {
      ViewersModel viewer = viewers.get(position);
      if (viewer != null) {
        holder.ismViewersItemBinding.tvViewerName.setText(viewer.getViewerName());
        holder.ismViewersItemBinding.tvViewerIdentifier.setText(viewer.getViewerIdentifier());
        holder.ismViewersItemBinding.tvJoinTime.setText(viewer.getJoinTime());

        if (viewer.isMember()) {

          holder.ismViewersItemBinding.ivMember.setVisibility(View.VISIBLE);
        } else {

          holder.ismViewersItemBinding.ivMember.setVisibility(View.GONE);
        }

        if (viewer.isCanRemoveViewer()) {

          holder.ismViewersItemBinding.rlRemoveViewer.setVisibility(View.VISIBLE);

          holder.ismViewersItemBinding.rlRemoveViewer.setOnClickListener(
              v -> (viewersBottomSheetFragment).removeViewer(viewer.getViewerId()));
        } else {

          holder.ismViewersItemBinding.rlRemoveViewer.setVisibility(View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(viewer.getViewerProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(viewer.getViewerProfilePic())
                .placeholder(R.drawable.ism_default_profile_image)
                .transform(new CircleCrop())
                .into(holder.ismViewersItemBinding.ivViewerImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {

          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, viewer.getViewerName(),
              holder.ismViewersItemBinding.ivViewerImage, position, 20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Viewers view holder.
   */
  static class ViewersViewHolder extends RecyclerView.ViewHolder {

    private final IsmViewersItemBinding ismViewersItemBinding;

    public ViewersViewHolder(final IsmViewersItemBinding ismViewersItemBinding) {
      super(ismViewersItemBinding.getRoot());
      this.ismViewersItemBinding = ismViewersItemBinding;
    }
  }
}
