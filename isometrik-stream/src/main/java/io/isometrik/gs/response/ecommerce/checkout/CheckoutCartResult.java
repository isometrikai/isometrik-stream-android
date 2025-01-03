package io.isometrik.gs.response.ecommerce.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.isometrik.gs.builder.ecommerce.checkout.CheckoutCartQuery;

/**
 * The class to parse checkout cart result of checking out a cart query
 * CheckoutCartQuery{@link CheckoutCartQuery}.
 *
 * @see CheckoutCartQuery
 */
public class CheckoutCartResult implements Serializable {

  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("checkoutSecret")
  @Expose
  private String checkoutSecret;

  @SerializedName("checkoutDetails")
  @Expose
  private CheckoutDetails checkoutDetails;

  public static class CheckoutDetails {
    @SerializedName("externalCheckoutLink")
    @Expose
    private String externalCheckoutLink;

    @SerializedName("checkoutLinkBearerToken")
    @Expose
    private String checkoutLinkBearerToken;

    @SerializedName("checkoutLinkBasicUsername")
    @Expose
    private String checkoutLinkBasicUsername;

    @SerializedName("checkoutLinkBasicPassword")
    @Expose
    private String checkoutLinkBasicPassword;

    @SerializedName("checkoutLinkApiKey")
    @Expose
    private String checkoutLinkApiKey;

    @SerializedName("checkoutLinkApiValue")
    @Expose
    private String checkoutLinkApiValue;

    @SerializedName("checkoutLinkAuthType")
    @Expose
    private int checkoutLinkAuthType;

    @SerializedName("checkoutLinkApiAddTo")
    @Expose
    private Integer checkoutLinkApiAddTo;

    /**
     * Gets externalCheckoutLink.
     *
     * @return the remote page url to be redirected to when a cart checkout is requested
     */
    public String getExternalCheckoutLink() {
      return externalCheckoutLink;
    }

    /**
     * Gets checkoutLinkBearerToken.
     *
     * @return the token to be used for external checkout redirect if authentication type is bearer token
     */
    public String getCheckoutLinkBearerToken() {
      return checkoutLinkBearerToken;
    }

    /**
     * Gets checkoutLinkBasicUsername.
     *
     * @return the username to be used for external checkout redirect if authentication type is basic auth
     */
    public String getCheckoutLinkBasicUsername() {
      return checkoutLinkBasicUsername;
    }

    /**
     * Gets checkoutLinkBasicPassword.
     *
     * @return the password to be used for external checkout redirect if authentication type is basic auth
     */
    public String getCheckoutLinkBasicPassword() {
      return checkoutLinkBasicPassword;
    }

    /**
     * Gets checkoutLinkApiKey.
     *
     * @return the key name to be used for external checkout redirect if authentication type is api-key
     */
    public String getCheckoutLinkApiKey() {
      return checkoutLinkApiKey;
    }

    /**
     * Gets checkoutLinkApiValue.
     *
     * @return the data to be used to specify value of key, for external checkout redirect if
     * authentication type is api-key
     */
    public String getCheckoutLinkApiValue() {
      return checkoutLinkApiValue;
    }

    /**
     * Gets checkoutLinkAuthType.
     *
     * @return the Auth type to be used for redirecting to external checkout page.(0=None, 1-Basic
     * auth, 2- Bearer Token, 3- Api-key)
     */
    public int getCheckoutLinkAuthType() {
      return checkoutLinkAuthType;
    }

    /**
     * Gets checkoutLinkApiAddTo.
     *
     * @return where to send key-value pair for external checkout redirect if authentication type is
     * api-key.(0- header/1- query params)
     */
    public Integer getCheckoutLinkApiAddTo() {
      return checkoutLinkApiAddTo;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets checkoutSecret.
   *
   * @return the secret to be used for checkout on external website
   */
  public String getCheckoutSecret() {
    return checkoutSecret;
  }

  /**
   * Gets checkoutDetails.
   *
   * @return the checkout details to be used for verifying checkout request on external website
   */
  public CheckoutDetails getCheckoutDetails() {
    return checkoutDetails;
  }
}