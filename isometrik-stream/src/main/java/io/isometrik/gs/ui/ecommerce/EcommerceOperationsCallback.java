package io.isometrik.gs.ui.ecommerce;

import io.isometrik.gs.ui.ecommerce.productdetails.ProductDetailModel;

public interface EcommerceOperationsCallback {
  /**
   * The interface featured products callback for communication back to scrollable streams activity,
   * on click of view cart button.
   */
  void viewCartRequested();

  /**
   * The interface featured products, product added to cart alert & product details) callback for
   * communication back to scrollable streams activity, on click of a particular product.
   */
  void viewProductDetails(ProductDetailModel product);

  /**
   * The interface product details callback for communication back to scrollable streams activity,
   * on product being successfully added to cart.
   */
  void productAddedToCartSuccessfully(String productName, CharSequence productPrice,
      String productImageUrl, int unitsAddedToCart);

  /**
   * The interface cart item callback for communication back to scrollable streams activity, on
   * checkout request.
   */
  void checkoutRequested(String checkoutUrl);
}
