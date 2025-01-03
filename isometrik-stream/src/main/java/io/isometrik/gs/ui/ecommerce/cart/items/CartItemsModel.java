package io.isometrik.gs.ui.ecommerce.cart.items;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import io.isometrik.gs.ui.utils.DateUtil;
import io.isometrik.gs.response.ecommerce.util.CartProduct;
import org.json.JSONException;
import org.json.JSONObject;

public class CartItemsModel {
  private final String productId;
  private final String productName;
  private CharSequence price;
  private String productImageUrl;
  private String unitsInCart;
  private final String addedToCartAt;

  /**
   * Instantiates a new cart product item model.
   *
   * @param product the product
   */
  CartItemsModel(CartProduct product) {

    productId = product.getProductId();
    productName = product.getProductName();
    unitsInCart = String.valueOf(product.getQuantity());
    addedToCartAt = DateUtil.getDate(product.getTimestamp());

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
  public String getProductName() {
    return productName;
  }

  /**
   * Gets product price.
   *
   * @return the product price
   */
  public CharSequence getPrice() {
    return price;
  }

  /**
   * Gets productImageUrl.
   *
   * @return the product image url
   */
  public String getProductImageUrl() {
    return productImageUrl;
  }

  /**
   * Gets unitsInStock.
   *
   * @return the number of units in cart for the given product
   */
  public String getUnitsInCart() {
    return unitsInCart;
  }

  /**
   * Gets addedToCartAt.
   *
   * @return the time at which product was added to cart
   */
  public String getAddedToCartAt() {
    return addedToCartAt;
  }

  /**
   * Sets unitsInCart.
   *
   * @param unitsInCart the number of units in cart for the product
   */
  public void setUnitsInCart(int unitsInCart) {
    this.unitsInCart = String.valueOf(unitsInCart);
  }
}
