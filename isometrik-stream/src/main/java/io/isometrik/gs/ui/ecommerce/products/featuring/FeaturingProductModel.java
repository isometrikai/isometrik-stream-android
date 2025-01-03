package io.isometrik.gs.ui.ecommerce.products.featuring;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import io.isometrik.gs.response.ecommerce.featuring.FetchFeaturingProductResult;
import org.json.JSONException;
import org.json.JSONObject;

public class FeaturingProductModel {
  private final String productId;
  private final String productName;
  private CharSequence price;
  private String productImageUrl;
  private final int unitsInStock;
  private final JSONObject productMetadata;

  private final boolean productAlreadyInCart;
  private final Integer quantity;
  private final Long timestamp;

  /**
   * Instantiates a new featured products model.
   *
   * @param product the product
   */
  public FeaturingProductModel(FetchFeaturingProductResult.FeaturingProduct product) {

    productId = product.getProductId();
    productName = product.getProductName();
    unitsInStock = product.getCount();
    productMetadata = product.getMetadata();

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

    productAlreadyInCart = product.isProductAlreadyInCart();
    quantity = product.getQuantity();
    timestamp = product.getTimestamp();
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
   * @return the number of units in stock for the given product
   */
  public int getUnitsInStock() {
    return unitsInStock;
  }

  /**
   * Gets the metadata json object of the product
   *
   * @return the metadata json object of the product
   */
  public JSONObject getProductMetadata() {
    return productMetadata;
  }

  /**
   * Gets productAlreadyInCart.
   *
   * @return Whether product has already been added to cart by the given user
   */
  public boolean isProductAlreadyInCart() {
    return productAlreadyInCart;
  }

  /**
   * Gets quantity.
   *
   * @return the number of units added to cart, if product is already added to cart by the given user
   */
  public Integer getQuantity() {
    return quantity;
  }

  /**
   * Gets timestamp.
   *
   * @return the time at which product was added to cart, if product is already added to cart by
   * the given user
   */
  public Long getTimestamp() {
    return timestamp;
  }
}
