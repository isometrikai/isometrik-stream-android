package io.isometrik.gs.ui.gifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmGiftCategoryItemBinding;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Gift categories items{@link GiftCategoryModel}.
 *
 * @see GiftCategoryModel
 */
public class GiftCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final ArrayList<GiftCategoryModel> allGiftsData;
  private final Context mContext;

  /**
   * Instantiates a new Gift categories adapter.
   *
   * @param allGiftsData the all gifts data
   * @param mContext the m context
   */
  public GiftCategoriesAdapter(ArrayList<GiftCategoryModel> allGiftsData, Context mContext) {

    this.allGiftsData = allGiftsData;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new ViewHolderGiftCategories(
        IsmGiftCategoryItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    try {
      GiftCategoriesAdapter.ViewHolderGiftCategories holder =
          (GiftCategoriesAdapter.ViewHolderGiftCategories) viewHolder;

      holder.ismGiftCategoryItemBinding.tvGiftCategoryName.setText(
          allGiftsData.get(position).getGiftCategoryName());
      holder.ismGiftCategoryItemBinding.tvGiftCategoryImage.setImageResource(
          allGiftsData.get(position).getGiftCategoryImage());

      if (allGiftsData.get(position).isSelected()) {

        holder.ismGiftCategoryItemBinding.tvGiftCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_white),
            android.graphics.PorterDuff.Mode.SRC_IN);

        holder.ismGiftCategoryItemBinding.tvGiftCategoryName.setSelected(true);
      } else {

        holder.ismGiftCategoryItemBinding.tvGiftCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_grey),
            android.graphics.PorterDuff.Mode.SRC_IN);
        holder.ismGiftCategoryItemBinding.tvGiftCategoryName.setSelected(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return allGiftsData.size();
  }

  private static class ViewHolderGiftCategories extends RecyclerView.ViewHolder {

    private final IsmGiftCategoryItemBinding ismGiftCategoryItemBinding;

    public ViewHolderGiftCategories(final IsmGiftCategoryItemBinding ismGiftCategoryItemBinding) {
      super(ismGiftCategoryItemBinding.getRoot());
      this.ismGiftCategoryItemBinding = ismGiftCategoryItemBinding;
    }
  }
}
