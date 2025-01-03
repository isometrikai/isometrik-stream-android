package io.isometrik.gs.ui.ecommerce.productdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexboxLayout;
import io.isometrik.gs.ui.R;
import io.isometrik.gs.ui.databinding.IsmProductAttributesListItemBinding;
import java.util.ArrayList;
import org.json.JSONArray;

public class ProductAttributesAdapter extends RecyclerView.Adapter {

  private final ArrayList<ProductAttributesModel> productAttributes;
  private final LayoutInflater mInflater;
  private final FlexboxLayout.LayoutParams layoutParams;

  /**
   * Instantiates a new featured product attributes adapter.
   *
   * @param mContext the context
   * @param productAttributes the product attributes
   */
  ProductAttributesAdapter(Context mContext, ArrayList<ProductAttributesModel> productAttributes) {

    this.productAttributes = productAttributes;
    mInflater = LayoutInflater.from(mContext);
    layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);

    layoutParams.setMargins(0, 0, (int) (13 * mContext.getResources().getDisplayMetrics().density),
        0);
  }

  @Override
  public int getItemCount() {
    return productAttributes.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmProductAttributesListItemBinding ismProductAttributesListItemBinding =
        IsmProductAttributesListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new ProductAttributesViewHolder(ismProductAttributesListItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ProductAttributesViewHolder holder = (ProductAttributesViewHolder) viewHolder;

    try {
      ProductAttributesModel productAttribute = productAttributes.get(position);
      if (productAttribute != null) {
        holder.ismProductAttributesListItemBinding.tvAttributeName.setText(
            productAttribute.getAttributeName());

        FlexboxLayout attributesLayout =
            holder.ismProductAttributesListItemBinding.flAttributeValues;
        attributesLayout.removeAllViews();

        JSONArray attributes = productAttribute.getAttributeValues();
        for (int i = 0; i < attributes.length(); i++) {
          @SuppressLint("InflateParams")
          View mView = mInflater.inflate(R.layout.ism_product_attribute_item, null, false);
          TextView tvAttributeName = mView.findViewById(R.id.tvAttributeName);
          tvAttributeName.setText((String) attributes.get(i));
          tvAttributeName.setSelected(i == 0);
          RelativeLayout rlAttributeBackground = mView.findViewById(R.id.rlAttributeBackground);
          rlAttributeBackground.setSelected(i == 0);

          attributesLayout.addView(mView, i, layoutParams);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type product attributes view holder.
   */
  static class ProductAttributesViewHolder extends RecyclerView.ViewHolder {

    private final IsmProductAttributesListItemBinding ismProductAttributesListItemBinding;

    public ProductAttributesViewHolder(
        final IsmProductAttributesListItemBinding ismProductAttributesListItemBinding) {
      super(ismProductAttributesListItemBinding.getRoot());
      this.ismProductAttributesListItemBinding = ismProductAttributesListItemBinding;
    }
  }
}
