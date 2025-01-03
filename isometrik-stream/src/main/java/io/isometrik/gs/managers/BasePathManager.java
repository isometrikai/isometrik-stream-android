package io.isometrik.gs.managers;

import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.ui.BuildConfig;

/**
 * A stateful manager to support base path construction, proxying and cache busting.
 */
public class BasePathManager {

  /**
   * Isometrik configuration storage.
   */
  private final IMConfiguration config;
  /**
   * for cache busting, the current subdomain number used.
   */
  private int currentSubdomain;

  /**
   * if using cache busting, this is the max number of subdomains that are supported.
   */
  private static final int MAX_SUBDOMAIN = 20;
  /**
   * default subdomain used if cache busting is disabled.
   */
  private static final String DEFAULT_SUBDOMAIN = BuildConfig.DEFAULT_SUBDOMAIN;
  /**
   * default base path if a custom one is not provided.
   */
  private static final String DEFAULT_BASE_PATH = BuildConfig.DEFAULT_BASE_PATH;

  private static final String CONNECTIONS_BASE_PATH = BuildConfig.CONNECTIONS_BASE_PATH;

  private static final String CONNECTIONS_DEFAULT_SUBDOMAIN =
      BuildConfig.CONNECTIONS_DEFAULT_SUBDOMAIN;

  private static final String GIFT_BASE_PATH = BuildConfig.GIFT_BASE_PATH;
  private static final String WALLET_BASE_PATH = BuildConfig.WALLET_BASE_PATH;

  /**
   * Initialize the path management.
   *
   * @param initialConfig configuration object
   * @see IMConfiguration
   */
  public BasePathManager(IMConfiguration initialConfig) {
    this.config = initialConfig;
    currentSubdomain = 1;
  }

  /**
   * Prepares a next usable base url.
   *
   * @return usable base url.
   */
  public String getBasePath() {
    StringBuilder constructedUrl = new StringBuilder("http");

    if (config.isSecure()) {
      constructedUrl.append("s");
    }

    constructedUrl.append("://");

    if (config.isCacheBusting()) {
      constructedUrl.append("apis").append(currentSubdomain).append(".").append(DEFAULT_BASE_PATH);

      if (currentSubdomain == MAX_SUBDOMAIN) {
        currentSubdomain = 1;
      } else {
        currentSubdomain += 1;
      }
    } else {
      constructedUrl.append(DEFAULT_SUBDOMAIN).append(".").append(DEFAULT_BASE_PATH);
    }

    return constructedUrl.toString();

    //return "http://45.77.1.74:4001";
  }

  public String getGiftBasePath(){
    return GIFT_BASE_PATH;
  }

  public String getWalletBasePath(){
    return WALLET_BASE_PATH;
  }

  /**
   * Prepares a connections base url.
   *
   * @return usable connections base url.
   */
  public String getConnectionsBasePath() {
    StringBuilder constructedUrl = new StringBuilder("tcp");

    if (config.isSecure()) {
      //constructedUrl=new StringBuilder("ssl");
      constructedUrl = new StringBuilder("tcp");
    }

    constructedUrl.append("://");

    constructedUrl.append(CONNECTIONS_DEFAULT_SUBDOMAIN).append(".").append(CONNECTIONS_BASE_PATH);

    return constructedUrl.toString();
  }
}