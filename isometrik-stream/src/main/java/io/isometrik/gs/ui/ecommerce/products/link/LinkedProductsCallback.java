package io.isometrik.gs.ui.ecommerce.products.link;

import java.util.ArrayList;

/**
 * The interface linked products callback for communication back to golive activity, on click of
 * confirm button.
 */
public interface LinkedProductsCallback {

  /**
   * Call to pass products selected on click of confirm button.
   */
  void onProductsSelected(ArrayList<String> productIds);
}
