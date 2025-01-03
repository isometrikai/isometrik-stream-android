package io.isometrik.gs.ui.ecommerce.productdetails;

import org.json.JSONArray;

public class ProductAttributesModel {

  private final String attributeName;
  private final JSONArray attributeValues;

  public ProductAttributesModel(String attributeName, JSONArray attributeValues) {
    this.attributeName = attributeName;
    this.attributeValues = attributeValues;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public JSONArray getAttributeValues() {
    return attributeValues;
  }
}
