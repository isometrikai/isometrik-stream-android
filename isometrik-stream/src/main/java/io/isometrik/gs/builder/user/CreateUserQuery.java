package io.isometrik.gs.builder.user;

import org.json.JSONObject;

/**
 * Query builder class for creating the request for creating a new user.
 */
public class CreateUserQuery {

  private final String userIdentifier;
  private final String password;
  private final String userProfileImageUrl;
  private final String userName;
  private final JSONObject metadata;

  private CreateUserQuery(Builder builder) {
    this.userIdentifier = builder.userIdentifier;
    this.password = builder.password;
    this.userProfileImageUrl = builder.userProfileImageUrl;
    this.userName = builder.userName;
    this.metadata = builder.metadata;
  }

  /**
   * The Builder class for the AddUserQuery.
   */
  public static class Builder {
    private String userIdentifier;
    private String password;
    private String userProfileImageUrl;
    private String userName;
    private JSONObject metadata;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the identifier of the new user to be created
     * @return the AddUserQuery.Builder{@link Builder} instance
     * @see Builder
     */
    public Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
      return this;
    }

    /**
     * Sets user name.
     *
     * @param userName the name of the new user to be created
     * @return the AddUserQuery.Builder{@link Builder} instance
     * @see Builder
     */
    public Builder setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    /**
     * Sets password.
     *
     * @param password the password
     * @return the password
     */
    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    /**
     * Sets user profile image url.
     *
     * @param userProfileImageUrl the user profile image url
     * @return the user profile image url
     */
    public Builder setUserProfileImageUrl(String userProfileImageUrl) {
      this.userProfileImageUrl = userProfileImageUrl;
      return this;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     * @return the metadata
     */
    public Builder setMetadata(JSONObject metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Build add user query.
     *
     * @return the AddUserQuery{@link CreateUserQuery} instance
     * @see CreateUserQuery
     */
    public CreateUserQuery build() {
      return new CreateUserQuery(this);
    }
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Gets metadata.
   *
   * @return the metadata
   */
  public JSONObject getMetadata() {
    return metadata;
  }
}
