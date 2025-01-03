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
import io.isometrik.gs.ui.databinding.IsmLinkProductsItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import java.util.ArrayList;

public class LinkProductsAdapter extends RecyclerView.Adapter {

  private final Context mContext;
  private final ArrayList<LinkProductsModel> products;
  private final int cornerRadiusInPixels;

  /**
   * Instantiates a new link product adapter.
   *
   * @param mContext the context
   * @param products the products to be linked
   */
  LinkProductsAdapter(Context mContext, ArrayList<LinkProductsModel> products) {
    this.mContext = mContext;
    this.products = products;
    cornerRadiusInPixels = (int) (Resources.getSystem().getDisplayMetrics().density * 6);
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmLinkProductsItemBinding ismLinkProductsItemBinding =
        IsmLinkProductsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ProductsViewHolder(ismLinkProductsItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ProductsViewHolder holder = (ProductsViewHolder) viewHolder;

    try {
      LinkProductsModel product = products.get(position);
      if (product != null) {
        holder.ismLinkProductsItemBinding.tvProductName.setText(product.getProductName());
        holder.ismLinkProductsItemBinding.tvProductPrice.setText(product.getPrice());
        holder.ismLinkProductsItemBinding.tvUnitsInStock.setText(
            mContext.getString(R.string.ism_units_in_stock, product.getUnitsInStock()));
        if (product.isSelected()) {
          holder.ismLinkProductsItemBinding.tvLink.setText(mContext.getString(R.string.ism_unlink));
        } else {
          holder.ismLinkProductsItemBinding.tvLink.setText(mContext.getString(R.string.ism_link));
        }

        try {
          GlideApp.with(mContext)
              .load(product.getProductImageUrl())
              .placeholder(R.drawable.ism_ic_product_default_image)
              .transform(new CenterCrop(), new RoundedCorners(cornerRadiusInPixels))
              .into(holder.ismLinkProductsItemBinding.ivProductImage);
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

    private final IsmLinkProductsItemBinding ismLinkProductsItemBinding;

    public ProductsViewHolder(final IsmLinkProductsItemBinding ismLinkProductsItemBinding) {
      super(ismLinkProductsItemBinding.getRoot());
      this.ismLinkProductsItemBinding = ismLinkProductsItemBinding;
    }
  }
}
