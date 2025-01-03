package io.isometrik.gs.ui.ecommerce.products.link;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import io.isometrik.gs.response.ecommerce.util.Product;
import org.json.JSONException;
import org.json.JSONObject;

public class LinkProductsModel {

  private final String productId;
  private final String productName;
  private CharSequence price;
  private String productImageUrl;
  private final int unitsInStock;

  private boolean selected;

  /**
   * Instantiates a new link products model.
   *
   * @param product the product
   */
  LinkProductsModel(Product product) {

    productId = product.getProductId();
    productName = product.getProductName();
    unitsInStock = product.getCount();
    JSONObject productMetadata = product.getMetadata();

    try {
      if (productMetadata.has("currencySymbol") && productMetadata.has("price")) {
        price = productMetadata.getString("currencySymbol") + productMetadata.getString("price");
        if (productMetadata.has("originalPrice")) {
          try {
            int length = productMetadata.getString("originalPrice").length();
            SpannableString originalPrice = new SpannableString(
                productMetadata.getString("currencySymbol") + productMetadata.getString(
                    "originalPrice"));
            originalPrice.setSpan(new StrikethroughSpan(), 0, length + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            originalPrice.setSpan(new RelativeSizeSpan(0.8f), 0, length + 1, 0); // set size
            originalPrice.setSpan(new ForegroundColorSpan(Color.GRAY), 0, length + 1,
                0);// set color
            price = TextUtils.concat(price + "  ", originalPrice);
          } catch (Exception ignore) {
            price = "NA";
          }
        }
      } else {
        price = "NA";
      }
      if (productMetadata.has("productImageUrl")) {
        productImageUrl = productMetadata.getString("productImageUrl");
      } else {
        productImageUrl = null;
      }
    } catch (JSONException ignore) {
    }
  }

  /**
   * Gets product id.
   *
   * @return the product id
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Gets product name.
   *
   * @return the product name
   */
  String getProductName() {
    return productName;
  }

  /**
   * Gets product price.
   *
   * @return the product price
   */
  CharSequence getPrice() {
    return price;
  }

  /**
   * Gets productImageUrl.
   *
   * @return the product image url
   */
  String getProductImageUrl() {
    return productImageUrl;
  }

  /**
   * Gets unitsInStock.
   *
   * @return the number of units in stock for the given pro
   */
  int getUnitsInStock() {
    return unitsInStock;
  }

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  boolean isSelected() {
    return selected;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  void setSelected(boolean selected) {
    this.selected = selected;
  }
}
