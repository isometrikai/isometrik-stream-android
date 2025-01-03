package io.isometrik.gs.ui.ecommerce.products.featured;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmFeaturedProductItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import java.util.ArrayList;

public class FeaturedProductsAdapter extends RecyclerView.Adapter {
  private final Context mContext;
  private final ArrayList<FeaturedProductsModel> products;
  private final boolean isAdmin;
  private final FeaturedProductsFragment featuredProductsFragment;
  private final int cornerRadiusInPixels;

  /**
   * Instantiates a new featured products adapter.
   *
   * @param mContext the context
   * @param products the products that are linked
   * @param isAdmin whether given user is admin of broadcast or not
   * @param featuredProductsFragment reference to featuredProductsFragment for callback to
   * start/stop featuring a product
   */
  FeaturedProductsAdapter(Context mContext, ArrayList<FeaturedProductsModel> products,
      boolean isAdmin, FeaturedProductsFragment featuredProductsFragment) {
    this.mContext = mContext;
    this.products = products;
    this.isAdmin = isAdmin;
    this.featuredProductsFragment = featuredProductsFragment;
    cornerRadiusInPixels = (int) (Resources.getSystem().getDisplayMetrics().density * 6);
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmFeaturedProductItemBinding ismFeaturedProductItemBinding =
        IsmFeaturedProductItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new ProductsViewHolder(ismFeaturedProductItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ProductsViewHolder holder = (ProductsViewHolder) viewHolder;

    try {
      FeaturedProductsModel product = products.get(position);
      if (product != null) {

        if (isAdmin) {
          if (product.isFeaturing()) {
            holder.ismFeaturedProductItemBinding.tvFeaturingStatus.setText(
                mContext.getString(R.string.ism_stop_featuring));
          } else {
            holder.ismFeaturedProductItemBinding.tvFeaturingStatus.setText(
                mContext.getString(R.string.ism_start_featuring));
          }
          holder.ismFeaturedProductItemBinding.rlUpdateFeaturingStatus.setVisibility(View.VISIBLE);
          holder.ismFeaturedProductItemBinding.rlUpdateFeaturingStatus.setOnClickListener(
              v -> featuredProductsFragment.updateFeaturingProductStatus(product));
        } else {
          holder.ismFeaturedProductItemBinding.rlUpdateFeaturingStatus.setVisibility(View.GONE);
        }
        holder.ismFeaturedProductItemBinding.getRoot()
            .setOnClickListener(v -> featuredProductsFragment.viewProductDetails(product));

        holder.ismFeaturedProductItemBinding.tvProductName.setText(product.getProductName());
        holder.ismFeaturedProductItemBinding.tvPrice.setText(product.getPrice());
        if (product.isFeaturing()) {
          holder.ismFeaturedProductItemBinding.tvFeaturing.setVisibility(View.VISIBLE);
        } else {
          holder.ismFeaturedProductItemBinding.tvFeaturing.setVisibility(View.GONE);
        }
        try {
          GlideApp.with(mContext)
              .load(product.getProductImageUrl())
              .placeholder(R.drawable.ism_ic_product_default_image)
              .transform(new CenterCrop(), new RoundedCorners(cornerRadiusInPixels))
              .into(holder.ismFeaturedProductItemBinding.ivProductImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type products view holder.
   */
  static class ProductsViewHolder extends RecyclerView.ViewHolder {

    private final IsmFeaturedProductItemBinding ismFeaturedProductItemBinding;

    public ProductsViewHolder(final IsmFeaturedProductItemBinding ismFeaturedProductItemBinding) {
      super(ismFeaturedProductItemBinding.getRoot());
      this.ismFeaturedProductItemBinding = ismFeaturedProductItemBinding;
    }
  }
}
