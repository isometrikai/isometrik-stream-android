package io.isometrik.gs.ui.ecommerce.products.link;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmLinkedProductsItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import java.util.ArrayList;

public class SelectedProductsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<LinkProductsModel> products;
  private final LinkProductsFragment linkProductsFragment;
  private final int cornerRadiusInPixels;

  /**
   * Instantiates a new selected product adapter.
   *
   * @param mContext the context
   * @param products the products selected
   * @param linkProductsFragment the instance of LinkProductsFragment for remove product callback
   */
  SelectedProductsAdapter(Context mContext, ArrayList<LinkProductsModel> products,
      LinkProductsFragment linkProductsFragment) {
    this.mContext = mContext;
    this.products = products;
    this.linkProductsFragment = linkProductsFragment;
    cornerRadiusInPixels = (int) (Resources.getSystem().getDisplayMetrics().density * 10);
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ProductsViewHolder(
        IsmLinkedProductsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ProductsViewHolder holder = (ProductsViewHolder) viewHolder;

    try {
      LinkProductsModel product = products.get(position);
      if (product != null) {
        holder.ismLinkedProductsItemBinding.tvProductName.setText(product.getProductName());

        holder.ismLinkedProductsItemBinding.ivRemoveProduct.setOnClickListener(
            v -> linkProductsFragment.removeProduct(product.getProductId()));

        try {
          GlideApp.with(mContext)
              .load(product.getProductImageUrl())
              .placeholder(R.drawable.ism_ic_product_default_image)
              .transform(new CenterCrop(), new RoundedCorners(cornerRadiusInPixels))
              .into(holder.ismLinkedProductsItemBinding.ivProductImage);
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

    private final IsmLinkedProductsItemBinding ismLinkedProductsItemBinding;

    public ProductsViewHolder(final IsmLinkedProductsItemBinding ismLinkedProductsItemBinding) {
      super(ismLinkedProductsItemBinding.getRoot());
      this.ismLinkedProductsItemBinding = ismLinkedProductsItemBinding;
    }
  }
}