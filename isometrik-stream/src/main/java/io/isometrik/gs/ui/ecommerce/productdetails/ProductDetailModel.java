package io.isometrik.gs.ui.ecommerce.productdetails;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import io.isometrik.gs.ui.ecommerce.products.featured.FeaturedProductsModel;
import io.isometrik.gs.ui.ecommerce.products.featuring.FeaturingProductModel;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailModel {
  private String productId;
  private String productName;
  private CharSequence price;
  private String productImageUrl;
  private int unitsInStock;
  private JSONObject productMetadata;

  public ProductDetailModel(FeaturingProductModel featuringProductModel) {
    productId = featuringProductModel.getProductId();
    productName = featuringProductModel.getProductName();
    price = featuringProductModel.getPrice();
    productImageUrl = featuringProductModel.getProductImageUrl();
    unitsInStock = featuringProductModel.getUnitsInStock();
    productMetadata = featuringProductModel.getProductMetadata();
  }

  public ProductDetailModel(FeaturedProductsModel featuredProductsModel) {
    productId = featuredProductsModel.getProductId();
    productName = featuredProductsModel.getProductName();
    price = featuredProductsModel.getPrice();
    productImageUrl = featuredProductsModel.getProductImageUrl();
    unitsInStock = featuredProductsModel.getUnitsInStock();
    productMetadata = featuredProductsModel.getProductMetadata();
  }

  public ProductDetailModel(JSONObject product) {
    try {
      productId = product.getString("productId");
      productName = product.getString("productName");
      productMetadata = new JSONObject(product.getString("productMetadata"));
      productImageUrl = product.getString("productImageUrl");
      unitsInStock = product.getInt("unitsInStock");

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
    } catch (JSONException ignore) {
    }
  }

  public String getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public CharSequence getPrice() {
    return price;
  }

  public String getProductImageUrl() {
    return productImageUrl;
  }

  public int getUnitsInStock() {
    return unitsInStock;
  }

  public JSONObject getProductMetadata() {
    return productMetadata;
  }
}
