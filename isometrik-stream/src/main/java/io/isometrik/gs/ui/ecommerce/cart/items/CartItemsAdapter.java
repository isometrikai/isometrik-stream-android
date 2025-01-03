package io.isometrik.gs.ui.ecommerce.cart.items;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmCartProductItemBinding;
import io.isometrik.gs.ui.utils.GlideApp;
import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter {
  private final Context mContext;
  private final ArrayList<CartItemsModel> products;
  private final CartItemsFragment cartItemsFragment;
  private final int cornerRadiusInPixels;

  /**
   * Instantiates a new cart items adapter.
   *
   * @param mContext the context
   * @param products the products in cart
   * @param cartItemsFragment the instance of cart items fragment for update quantity and remove
   * product from cart callback
   */
  CartItemsAdapter(Context mContext, ArrayList<CartItemsModel> products,
      CartItemsFragment cartItemsFragment) {
    this.mContext = mContext;
    this.products = products;
    this.cartItemsFragment = cartItemsFragment;
    cornerRadiusInPixels = (int) (Resources.getSystem().getDisplayMetrics().density * 6);
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmCartProductItemBinding ismCartProductItemBinding =
        IsmCartProductItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new CartItemsViewHolder(ismCartProductItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    CartItemsViewHolder holder = (CartItemsViewHolder) viewHolder;

    try {
      CartItemsModel product = products.get(position);
      if (product != null) {
        holder.ismCartProductItemBinding.tvProductName.setText(product.getProductName());
        holder.ismCartProductItemBinding.tvPrice.setText(product.getPrice());
        holder.ismCartProductItemBinding.tvAddedAt.setText(product.getAddedToCartAt());
        holder.ismCartProductItemBinding.tvQuantity.setText(product.getUnitsInCart());

        holder.ismCartProductItemBinding.ivRemoveProduct.setOnClickListener(
            v -> cartItemsFragment.removeProductFromCart(product.getProductId()));

        holder.ismCartProductItemBinding.tvIncrement.setOnClickListener(
            v -> cartItemsFragment.updateProductQuantityInCart(product.getProductId(),
                product.getProductName(), true, Integer.parseInt(product.getUnitsInCart())));

        holder.ismCartProductItemBinding.tvDecrement.setOnClickListener(
            v -> cartItemsFragment.updateProductQuantityInCart(product.getProductId(),
                product.getProductName(), false, Integer.parseInt(product.getUnitsInCart())));
        try {
          GlideApp.with(mContext)
              .load(product.getProductImageUrl())
              .placeholder(R.drawable.ism_ic_product_default_image)
              .transform(new CenterCrop(), new RoundedCorners(cornerRadiusInPixels))
              .into(holder.ismCartProductItemBinding.ivProductImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type cart items view holder.
   */
  static class CartItemsViewHolder extends RecyclerView.ViewHolder {

    private final IsmCartProductItemBinding ismCartProductItemBinding;

    public CartItemsViewHolder(final IsmCartProductItemBinding ismCartProductItemBinding) {
      super(ismCartProductItemBinding.getRoot());
      this.ismCartProductItemBinding = ismCartProductItemBinding;
    }
  }
}
