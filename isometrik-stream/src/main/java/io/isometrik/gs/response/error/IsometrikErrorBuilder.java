package io.isometrik.gs.response.error;

/**
 * The type Isometrik error builder.
 */
public final class IsometrikErrorBuilder {

    // Error Codes

    /**
     * Account id missing
     */
    private static final int IMERR_ACCOUNTID_MISSING = 101;

    /**
     * The constant IMERROBJ_ACCOUNTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_ACCOUNTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_ACCOUNTID_MISSING).setErrorMessage("Account id not configured.").setRemoteError(false).build();

    /**
     * Project id missing
     */
    private static final int IMERR_PROJECTID_MISSING = 102;

    /**
     * The constant IMERROBJ_PROJECTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_PROJECTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PROJECTID_MISSING).setErrorMessage("Project id not configured.").setRemoteError(false).build();
    /**
     * Keyset id missing
     */
    private static final int IMERR_KEYSETID_MISSING = 103;

    /**
     * The constant IMERROBJ_KEYSETID_MISSING.
     */
    public static final IsometrikError IMERROBJ_KEYSETID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_KEYSETID_MISSING).setErrorMessage("Keyset id not configured.").setRemoteError(false).build();
    /**
     * License key missing
     */
    private static final int IMERR_LICENSE_KEY_MISSING = 104;

    /**
     * The constant IMERROBJ_LICENSE_KEY_MISSING.
     */
    public static final IsometrikError IMERROBJ_LICENSE_KEY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LICENSE_KEY_MISSING).setErrorMessage("License key not configured.").setRemoteError(false).build();

    /**
     * Stream id missing
     */
    private static final int IMERR_STREAMID_MISSING = 105;

    /**
     * The constant IMERROBJ_STREAMID_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAMID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAMID_MISSING).setErrorMessage("Stream id is missing.").setRemoteError(false).build();

    /**
     * Member id missing
     */
    private static final int IMERR_MEMBERID_MISSING = 106;

    /**
     * The constant IMERROBJ_MEMBERID_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEMBERID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERID_MISSING).setErrorMessage("Member id is missing.").setRemoteError(false).build();

    /**
     * Viewer id missing
     */
    private static final int IMERR_VIEWERID_MISSING = 107;

    /**
     * The constant IMERROBJ_VIEWERID_MISSING.
     */
    public static final IsometrikError IMERROBJ_VIEWERID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_VIEWERID_MISSING).setErrorMessage("Viewer id is missing.").setRemoteError(false).build();

    /**
     * Initiator id missing
     */
    private static final int IMERR_INITIATORID_MISSING = 108;

    /**
     * The constant IMERROBJ_INITIATORID_MISSING.
     */
    public static final IsometrikError IMERROBJ_INITIATORID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_INITIATORID_MISSING).setErrorMessage("Initiator id is missing.").setRemoteError(false).build();

    /**
     * User id missing
     */
    private static final int IMERR_USERID_MISSING = 109;

    /**
     * The constant IMERROBJ_USERID_MISSING.
     */
    public static final IsometrikError IMERROBJ_USERID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERID_MISSING).setErrorMessage("User id is missing.").setRemoteError(false).build();

    /**
     * Sender id missing
     */
    private static final int IMERR_SENDERID_MISSING = 110;

    /**
     * The constant IMERROBJ_SENDERID_MISSING.
     */
    public static final IsometrikError IMERROBJ_SENDERID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDERID_MISSING).setErrorMessage("Sender id is missing.").setRemoteError(false).build();

    /**
     * Sender name missing
     */
    private static final int IMERR_SENDER_NAME_MISSING = 111;

    /**
     * The constant IMERROBJ_SENDER_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_SENDER_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_NAME_MISSING).setErrorMessage("Sender name is missing.").setRemoteError(false).build();

    /**
     * Sender identifier missing
     */
    private static final int IMERR_SENDER_IDENTIFIER_MISSING = 112;

    /**
     * The constant IMERROBJ_SENDER_IDENTIFIER_MISSING.
     */
    public static final IsometrikError IMERROBJ_SENDER_IDENTIFIER_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_IDENTIFIER_MISSING).setErrorMessage("Sender identifier is missing.").setRemoteError(false).build();

    /**
     * Sender image missing
     */
    private static final int IMERR_SENDER_IMAGE_MISSING = 113;

    /**
     * The constant IMERROBJ_SENDER_IMAGE_MISSING.
     */
    public static final IsometrikError IMERROBJ_SENDER_IMAGE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SENDER_IMAGE_MISSING).setErrorMessage("Sender image is missing.").setRemoteError(false).build();

    /**
     * Message missing
     */
    private static final int IMERR_MESSAGE_MISSING = 114;

    /**
     * The constant IMERROBJ_MESSAGE_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_MISSING).setErrorMessage("Message is missing.").setRemoteError(false).build();

    /**
     * Message type invalid value
     */
    private static final int IMERR_MESSAGE_TYPE_INVALID_VALUE = 115;

    /**
     * The constant IMERROBJ_MESSAGE_TYPE_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_TYPE_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_TYPE_INVALID_VALUE).setErrorMessage("Message type invalid value.").setRemoteError(false).build();

    /**
     * Created by missing
     */
    private static final int IMERR_CREATED_BY_MISSING = 116;

    /**
     * The constant IMERROBJ_CREATED_BY_MISSING.
     */
    public static final IsometrikError IMERROBJ_CREATED_BY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CREATED_BY_MISSING).setErrorMessage("Created by is missing.").setRemoteError(false).build();

    /**
     * Stream image is missing
     */
    private static final int IMERR_STREAM_IMAGE_MISSING = 117;

    /**
     * The constant IMERROBJ_STREAM_IMAGE_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_IMAGE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_IMAGE_MISSING).setErrorMessage("Stream image is missing.").setRemoteError(false).build();

    /**
     * Stream description missing
     */
    private static final int IMERR_STREAM_DESCRIPTION_MISSING = 118;

    /**
     * The constant IMERROBJ_STREAM_DESCRIPTION_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_DESCRIPTION_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_DESCRIPTION_MISSING).setErrorMessage("Stream description is missing.").setRemoteError(false).build();

    /**
     * Stream members missing
     */
    private static final int IMERR_STREAM_MEMBERS_MISSING = 119;

    /**
     * The constant IMERROBJ_STREAM_MEMBERS_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_MEMBERS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_MEMBERS_MISSING).setErrorMessage("Stream members are missing.").setRemoteError(false).build();

    /**
     * User name missing
     */
    private static final int IMERR_USERNAME_MISSING = 120;

    /**
     * The constant IMERROBJ_USERNAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_USERNAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERNAME_MISSING).setErrorMessage("Username is missing.").setRemoteError(false).build();

    /**
     * User profile pic missing
     */
    private static final int IMERR_USER_PROFILEPIC_MISSING = 121;

    /**
     * The constant IMERROBJ_USER_PROFILEPIC_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_PROFILEPIC_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILEPIC_MISSING).setErrorMessage("User profile pic is missing.").setRemoteError(false).build();

    /**
     * User identifier missing
     */
    private static final int IMERR_USER_IDENTIFIER_MISSING = 122;

    /**
     * The constant IMERROBJ_USER_IDENTIFIER_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_IDENTIFIER_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDENTIFIER_MISSING).setErrorMessage("User identifier is missing.").setRemoteError(false).build();

    /**
     * Count invalid value
     */
    private static final int IMERR_COUNT_INVALID_VALUE = 123;

    /**
     * The constant IMERROBJ_COUNT_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_COUNT_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_COUNT_INVALID_VALUE).setErrorMessage("Count has invalid value.").setRemoteError(false).build();

    /**
     * Page token invalid value
     */
    private static final int IMERR_PAGE_TOKEN_INVALID_VALUE = 124;

    /**
     * The constant IMERROBJ_PAGE_TOKEN_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_PAGE_TOKEN_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PAGE_TOKEN_INVALID_VALUE).setErrorMessage("Page token has invalid value.").setRemoteError(false).build();

    /**
     * User name invalid value
     */
    private static final int IMERR_USERNAME_INVALID_VALUE = 125;

    /**
     * The constant IMERROBJ_USERNAME_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_USERNAME_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USERNAME_INVALID_VALUE).setErrorMessage("Username has invalid value.").setRemoteError(false).build();

    /**
     * User profile invalid value
     */
    private static final int IMERR_USER_PROFILEPIC_INVALID_VALUE = 126;

    /**
     * The constant IMERROBJ_USER_PROFILEPIC_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_USER_PROFILEPIC_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILEPIC_INVALID_VALUE).setErrorMessage("User profile pic has invalid value.").setRemoteError(false).build();

    /**
     * User identifier invalid value
     */
    private static final int IMERR_USER_IDENTIFIER_INVALID_VALUE = 127;

    /**
     * The constant IMERROBJ_USER_IDENTIFIER_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_USER_IDENTIFIER_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDENTIFIER_INVALID_VALUE).setErrorMessage("User identifier has invalid value.").setRemoteError(false).build();

    /**
     * Not Found
     */
    private static final int IMERR_NOT_FOUND = 128;

    /**
     * Forbidden
     */
    private static final int IMERR_FORBIDDEN = 129;

    /**
     * Service unavailable
     */
    private static final int IMERR_SERVICE_UNAVAILABLE = 130;

    /**
     * Conflict
     */
    private static final int IMERR_CONFLICT = 131;

    /**
     * Unprocessable entity
     */
    private static final int IMERR_UNPROCESSABLE_ENTITY = 132;

    /**
     * Bad gateway
     */
    private static final int IMERR_BAD_GATEWAY = 133;

    /**
     * Internal server error
     */

    private static final int IMERR_INTERNAL_SERVER_ERROR = 134;

    /**
     * Parsing error
     */

    private static final int IMERR_PARSING_ERROR = 135;

    /**
     * Network error
     */

    private static final int IMERR_NETWORK_ERROR = 136;

    /**
     * Bad Request error
     */

    private static final int IMERR_BAD_REQUEST_ERROR = 137;
    private static final int IMERR_UNAUTHORIZED_ERROR = 138;

    /**
     * Is public missing
     */
    private static final int IMERR_IS_PUBLIC_MISSING = 139;

    /**
     * The constant IMERROBJ_IS_PUBLIC_MISSING.
     */
    public static final IsometrikError IMERROBJ_IS_PUBLIC_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_IS_PUBLIC_MISSING).setErrorMessage("Is public stream missing.").setRemoteError(false).build();

    /**
     * Invalid value of streamType
     */
    private static final int IMERR_STREAM_TYPE_INVALID_VALUE = 140;

    /**
     * The constant IMERROBJ_STREAM_TYPE_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_STREAM_TYPE_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_TYPE_INVALID_VALUE).setErrorMessage("Is public stream missing.").setRemoteError(false).build();

    /**
     * Missing value of streamType
     */
    private static final int IMERR_STREAM_TYPE_MISSING = 141;

    /**
     * The constant IMERROBJ_STREAM_TYPE_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_TYPE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_TYPE_MISSING).setErrorMessage("Stream type missing.").setRemoteError(false).build();

    /**
     * Missing value of audioOnly
     */
    private static final int IMERR_AUDIO_ONLY_MISSING = 142;
    /**
     * The constant IMERROBJ_AUDIO_ONLY_MISSING.
     */
    public static final IsometrikError IMERROBJ_AUDIO_ONLY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_AUDIO_ONLY_MISSING).setErrorMessage("Audio only stream missing.").setRemoteError(false).build();
    /**
     * User uid missing
     */
    private static final int IMERR_UID_MISSING = 143;

    /**
     * The constant IMERROBJ_UID_MISSING.
     */
    public static final IsometrikError IMERROBJ_UID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UID_MISSING).setErrorMessage("User uid is missing.").setRemoteError(false).build();

    /**
     * Missing value of multiLive
     */
    private static final int IMERR_MULTI_LIVE_MISSING = 144;
    /**
     * The constant IMERROBJ_MULTI_LIVE_MISSING.
     */
    public static final IsometrikError IMERROBJ_MULTI_LIVE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MULTI_LIVE_MISSING).setErrorMessage("Multi live stream missing.").setRemoteError(false).build();

    /**
     * Missing value of enableRecording
     */
    private static final int IMERR_ENABLE_RECORDING_MISSING = 145;
    /**
     * The constant IMERROBJ_ENABLE_RECORDING_MISSING.
     */
    public static final IsometrikError IMERROBJ_ENABLE_RECORDING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_ENABLE_RECORDING_MISSING).setErrorMessage("Enable recording missing.").setRemoteError(false).build();

    /**
     * Missing value of lowLatencyMode
     */
    private static final int IMERR_LOW_LATENCY_MODE_MISSING = 146;
    /**
     * The constant IMERROBJ_LOW_LATENCY_MODE_MISSING.
     */
    public static final IsometrikError IMERROBJ_LOW_LATENCY_MODE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LOW_LATENCY_MODE_MISSING).setErrorMessage("Low latency mode missing.").setRemoteError(false).build();

    /**
     * Missing value of hdBroadcast
     */
    private static final int IMERR_HD_BROADCAST_MISSING = 147;
    /**
     * The constant IMERROBJ_HD_BROADCAST_MISSING.
     */
    public static final IsometrikError IMERROBJ_HD_BROADCAST_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_HD_BROADCAST_MISSING).setErrorMessage("Hd broadcast missing.").setRemoteError(false).build();

    /**
     * Missing value of metadata
     */
    private static final int IMERR_METADATA_MISSING = 148;
    /**
     * The constant IMERROBJ_METADATA_MISSING.
     */
    public static final IsometrikError IMERROBJ_METADATA_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_METADATA_MISSING).setErrorMessage("Metadata missing.").setRemoteError(false).build();

    /**
     * Missing value of restream
     */
    private static final int IMERR_RESTREAM_MISSING = 149;
    /**
     * The constant IMERROBJ_RESTREAM_MISSING.
     */
    public static final IsometrikError IMERROBJ_RESTREAM_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_RESTREAM_MISSING).setErrorMessage("Restream missing.").setRemoteError(false).build();

    /**
     * Channel id missing
     */
    private static final int IMERR_CHANNELID_MISSING = 150;

    /**
     * The constant IMERROBJ_CHANNELID_MISSING.
     */
    public static final IsometrikError IMERROBJ_CHANNELID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CHANNELID_MISSING).setErrorMessage("Channel id is missing.").setRemoteError(false).build();

    /**
     * Channel name missing
     */
    private static final int IMERR_CHANNEL_NAME_MISSING = 151;

    /**
     * The constant IMERROBJ_CHANNEL_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_CHANNEL_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CHANNEL_NAME_MISSING).setErrorMessage("Channel name is missing.").setRemoteError(false).build();
    /**
     * Channel type missing
     */
    private static final int IMERR_CHANNEL_TYPE_MISSING = 152;

    /**
     * The constant IMERROBJ_CHANNEL_TYPE_MISSING.
     */
    public static final IsometrikError IMERROBJ_CHANNEL_TYPE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CHANNEL_TYPE_MISSING).setErrorMessage("Channel type is missing.").setRemoteError(false).build();
    /**
     * Ingest url missing
     */
    private static final int IMERR_INGEST_URL_MISSING = 153;

    /**
     * The constant IMERROBJ_INGEST_URL_MISSING.
     */
    public static final IsometrikError IMERROBJ_INGEST_URL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_INGEST_URL_MISSING).setErrorMessage("Ingest url is missing.").setRemoteError(false).build();

    /**
     * Restream enabled missing
     */
    private static final int IMERR_RESTREAM_ENABLED_MISSING = 154;

    /**
     * The constant IMERROBJ_RESTREAM_ENABLED_MISSING.
     */
    public static final IsometrikError IMERROBJ_RESTREAM_ENABLED_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_RESTREAM_ENABLED_MISSING).setErrorMessage("Enabled(restream) is missing.").setRemoteError(false).build();
    /**
     * Channel name has invalid value
     */
    private static final int IMERR_CHANNEL_NAME_INVALID_VALUE = 155;

    /**
     * The constant IMERROBJ_CHANNEL_NAME_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_CHANNEL_NAME_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CHANNEL_NAME_INVALID_VALUE).setErrorMessage("Channel name has invalid value.").setRemoteError(false).build();

    /**
     * Ingest url missing
     */
    private static final int IMERR_INGEST_URL_INVALID_VALUE = 156;

    /**
     * The constant IMERROBJ_INGEST_URL_MISSING.
     */
    public static final IsometrikError IMERROBJ_INGEST_URL_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_INGEST_URL_INVALID_VALUE).setErrorMessage("Ingest url has invalid value.").setRemoteError(false).build();

    /**
     * Moderator id missing
     */
    private static final int IMERR_MODERATORID_MISSING = 157;

    /**
     * The constant IMERROBJ_MODERATORID_MISSING.
     */
    public static final IsometrikError IMERROBJ_MODERATORID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MODERATORID_MISSING).setErrorMessage("Moderator id is missing.").setRemoteError(false).build();

    /**
     * Product id missing
     */
    private static final int IMERR_PRODUCTID_MISSING = 158;

    /**
     * The constant IMERROBJ_PRODUCTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRODUCTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCTID_MISSING).setErrorMessage("Product id is missing.").setRemoteError(false).build();

    /**
     * Quantity missing
     */
    private static final int IMERR_QUANTITY_MISSING = 159;

    /**
     * The constant IMERROBJ_QUANTITY_MISSING.
     */
    public static final IsometrikError IMERROBJ_QUANTITY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_QUANTITY_MISSING).setErrorMessage("Quantity is missing.").setRemoteError(false).build();

    /**
     * Product name missing
     */
    private static final int IMERR_PRODUCT_NAME_MISSING = 160;

    /**
     * The constant IMERROBJ_PRODUCT_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRODUCT_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCT_NAME_MISSING).setErrorMessage("Product name is missing.").setRemoteError(false).build();

    /**
     * Count missing
     */
    private static final int IMERR_COUNT_MISSING = 161;

    /**
     * The constant IMERROBJ_COUNT_MISSING.
     */
    public static final IsometrikError IMERROBJ_COUNT_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_COUNT_MISSING).setErrorMessage("Count is missing.").setRemoteError(false).build();

    /**
     * External product id missing
     */
    private static final int IMERR_EXTERNAL_PRODUCTID_MISSING = 162;

    /**
     * The constant IMERROBJ_EXTERNAL_PRODUCTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_EXTERNAL_PRODUCTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_EXTERNAL_PRODUCTID_MISSING).setErrorMessage("External product id is missing.").setRemoteError(false).build();

    /**
     * Quantity invalid value
     */
    private static final int IMERR_QUANTITY_INVALID_VALUE = 163;

    /**
     * The constant IMERROBJ_QUANTITY_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_QUANTITY_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_QUANTITY_INVALID_VALUE).setErrorMessage("Quantity has invalid value.").setRemoteError(false).build();

    /**
     * id missing
     */
    private static final int IMERR_CHECKOUT_SECRET_MISSING = 164;

    /**
     * The constant IMERROBJ_CHECKOUT_SECRET_MISSING.
     */
    public static final IsometrikError IMERROBJ_CHECKOUT_SECRET_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CHECKOUT_SECRET_MISSING).setErrorMessage("Checkout secret is missing.").setRemoteError(false).build();

    /**
     * Product name has invalid value
     */
    private static final int IMERR_PRODUCT_NAME_INVALID_VALUE = 165;

    /**
     * The constant IMERROBJ_PRODUCT_NAME_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_PRODUCT_NAME_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCT_NAME_INVALID_VALUE).setErrorMessage("Product name has invalid value.").setRemoteError(false).build();

    /**
     * Product name has invalid value
     */
    private static final int IMERR_PRODUCT_NAME_METADATA_BOTH_NULL = 166;

    /**
     * The constant IMERROBJ_PRODUCT_NAME_METADATA_BOTH_NULL.
     */
    public static final IsometrikError IMERROBJ_PRODUCT_NAME_METADATA_BOTH_NULL = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCT_NAME_METADATA_BOTH_NULL).setErrorMessage("Atleast one of product name or metadata should be non-null.").setRemoteError(false).build();

    /**
     * Missing value of productsLinked
     */
    private static final int IMERR_PRODUCTS_LINKED_MISSING = 167;
    /**
     * The constant IMERROBJ_PRODUCTS_LINKED_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRODUCTS_LINKED_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCTS_LINKED_MISSING).setErrorMessage("Products linked missing.").setRemoteError(false).build();

    /**
     * Missing value of productsIds
     */
    private static final int IMERR_PRODUCT_IDS_MISSING = 168;
    /**
     * The constant IMERROBJ_PRODUCT_IDS_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRODUCT_IDS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRODUCT_IDS_MISSING).setErrorMessage("Products ids are missing.").setRemoteError(false).build();

    /**
     * Start featuring missing
     */
    private static final int IMERR_START_FEATURING_MISSING = 169;

    /**
     * The constant IMERROBJ_START_FEATURING_MISSING.
     */
    public static final IsometrikError IMERROBJ_START_FEATURING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_START_FEATURING_MISSING).setErrorMessage("Start featuring is missing.").setRemoteError(false).build();

    /**
     * Parent message id missing
     */
    private static final int IMERR_PARENT_MESSAGE_ID_MISSING = 170;

    /**
     * The constant IMERROBJ_PARENT_MESSAGE_ID_MISSING.
     */
    public static final IsometrikError IMERROBJ_PARENT_MESSAGE_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PARENT_MESSAGE_ID_MISSING).setErrorMessage("Parent message id is missing.").setRemoteError(false).build();

    /**
     * Parent message sent at missing
     */
    private static final int IMERR_PARENT_MESSAGE_SENT_AT_MISSING = 171;

    /**
     * The constant IMERROBJ_PARENT_MESSAGE_SENT_AT_MISSING.
     */
    public static final IsometrikError IMERROBJ_PARENT_MESSAGE_SENT_AT_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PARENT_MESSAGE_SENT_AT_MISSING).setErrorMessage("Parent message sent at is missing.").setRemoteError(false).build();

    /**
     * Message sent at missing
     */
    private static final int IMERR_MESSAGE_SENT_AT_MISSING = 172;

    /**
     * The constant IMERROBJ_MESSAGE_SENT_AT_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_SENT_AT_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_SENT_AT_MISSING).setErrorMessage("Message sent at is missing.").setRemoteError(false).build();

    /**
     * Member name missing
     */
    private static final int IMERR_MEMBER_NAME_MISSING = 173;

    /**
     * The constant IMERROBJ_MEMBER_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEMBER_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBER_NAME_MISSING).setErrorMessage("Member name is missing.").setRemoteError(false).build();

    /**
     * Moderator name missing
     */
    private static final int IMERR_MODERATOR_NAME_MISSING = 174;

    /**
     * The constant IMERROBJ_MODERATOR_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_MODERATOR_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MODERATOR_NAME_MISSING).setErrorMessage("Moderator name is missing.").setRemoteError(false).build();

    /**
     * Message id missing
     */
    private static final int IMERR_MESSAGE_ID_MISSING = 175;

    /**
     * The constant IMERROBJ_MESSAGE_ID_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_ID_MISSING).setErrorMessage("Message id is missing.").setRemoteError(false).build();

    private static final int IMERR_CONNECTION_STRING_MISSING = 176;

    /**
     * The constant IMERROBJ_CONNECTION_STRING_MISSING.
     */
    public static final IsometrikError IMERROBJ_CONNECTION_STRING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_MISSING).setErrorMessage("Connection string not configured.").setRemoteError(false).build();

    private static final int IMERR_CONNECTION_STRING_INVALID_VALUE = 177;

    /**
     * The constant IMERROBJ_CONNECTION_STRING_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_CONNECTION_STRING_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_INVALID_VALUE).setErrorMessage("Connection string has invalid value.").setRemoteError(false).build();

    private static final int IMERR_USER_TOKEN_MISSING = 178;

    /**
     * The constant IMERROBJ_USER_TOKEN_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_TOKEN_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_TOKEN_MISSING).setErrorMessage("userToken is missing.").setRemoteError(false).build();

    private static final int IMERR_APP_SECRET_MISSING = 179;

    /**
     * The constant IMERROBJ_APP_SECRET_MISSING.
     */
    public static final IsometrikError IMERROBJ_APP_SECRET_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_APP_SECRET_MISSING).setErrorMessage("App secret not configured.").setRemoteError(false).build();

    private static final int IMERR_USER_SECRET_MISSING = 180;

    /**
     * The constant IMERROBJ_USER_SECRET_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_SECRET_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_SECRET_MISSING).setErrorMessage("ReadByUser secret not configured.").setRemoteError(false).build();
    /**
     * Request by user id missing
     */
    private static final int IMERR_REQUEST_BY_USERID_MISSING = 181;

    /**
     * The constant IMERROBJ_REQUEST_BY_USERID_MISSING.
     */
    public static final IsometrikError IMERROBJ_REQUEST_BY_USERID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_REQUEST_BY_USERID_MISSING).setErrorMessage("Request by user id is missing.").setRemoteError(false).build();

    /**
     * ClientId missing
     */
    private static final int IMERR_CLIENT_ID_MISSING = 182;

    /**
     * The constant IMERROBJ_CLIENT_ID_MISSING.
     */
    public static final IsometrikError IMERROBJ_CLIENT_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CLIENT_ID_MISSING).setErrorMessage("Client id not configured.").setRemoteError(false).build();

    /**
     * Both uid and memberId cant be null
     */
    private static final int IMERR_UID_MEMBERID_BOTH_NULL = 183;

    /**
     * The constant IMERROBJ_UID_MEMBERID_BOTH_NULL.
     */
    public static final IsometrikError IMERROBJ_UID_MEMBERID_BOTH_NULL = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UID_MEMBERID_BOTH_NULL).setErrorMessage("Atleast one of uid or memberId should be non-null.").setRemoteError(false).build();
    /**
     * Start publishing missing
     */
    private static final int IMERR_START_PUBLISH_MISSING = 184;

    /**
     * The constant IMERROBJ_START_PUBLISH_MISSING.
     */
    public static final IsometrikError IMERROBJ_START_PUBLISH_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_START_PUBLISH_MISSING).setErrorMessage("Start publish is missing.").setRemoteError(false).build();

    private static final int IMERR_MESSAGE_TYPE_MISSING = 185;

    /**
     * The constant IMERROBJ_MESSAGE_TYPE_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_TYPE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_TYPE_MISSING).setErrorMessage("messageType is missing.").setRemoteError(false).build();

    private static final int IMERR_MESSAGE_BODY_MISSING = 186;

    /**
     * The constant IMERROBJ_MESSAGE_BODY_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_BODY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_BODY_MISSING).setErrorMessage("Message body is missing.").setRemoteError(false).build();
    private static final int IMERR_DEVICEID_MISSING = 187;

    /**
     * The constant IMERROBJ_DEVICEID_MISSING.
     */
    public static final IsometrikError IMERROBJ_DEVICEID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_DEVICEID_MISSING).setErrorMessage("deviceId is missing.").setRemoteError(false).build();
    private static final int IMERR_MEDIA_EXTENSION_MISSING = 188;

    /**
     * The constant IMERROBJ_MEDIA_EXTENSION_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEDIA_EXTENSION_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_EXTENSION_MISSING).setErrorMessage("mediaExtension is missing.").setRemoteError(false).build();

    private static final int IMERR_USER_PASSWORD_MISSING = 189;

    /**
     * The constant IMERROBJ_USER_PASSWORD_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_PASSWORD_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PASSWORD_MISSING).setErrorMessage("userPassword is missing.").setRemoteError(false).build();

    private static final int IMERR_UPDATE_USER_DETAILS_MISSING = 190;

    /**
     * The constant IMERROBJ_UPDATE_USER_DETAILS_MISSING.
     */
    public static final IsometrikError IMERROBJ_UPDATE_USER_DETAILS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPDATE_USER_DETAILS_MISSING).setErrorMessage("Atleast one of userIdentifier, userProfileImageUrl, userName, notification and metadata should be specified.").setRemoteError(false).build();

    private static final int IMERR_USER_PROFILE_IMAGEURL_MISSING = 191;

    /**
     * The constant IMERROBJ_USER_PROFILE_IMAGEURL_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_PROFILE_IMAGEURL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILE_IMAGEURL_MISSING).setErrorMessage("userProfileImageUrl is missing.").setRemoteError(false).build();

    private static final int IMERR_MEDIA_PATH_MISSING = 192;

    /**
     * The constant IMERROBJ_MEDIA_PATH_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEDIA_PATH_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_PATH_MISSING).setErrorMessage("media path is missing.").setRemoteError(false).build();

    private static final int IMERR_UPLOAD_MEDIA_FAILED = 193;

    /**
     * The constant IMERROBJ_UPLOAD_MEDIA_FAILED.
     */
    public static final IsometrikError IMERROBJ_UPLOAD_MEDIA_FAILED = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPLOAD_MEDIA_FAILED).setErrorMessage("media upload failed.").setRemoteError(false).build();

    private static final int IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND = 194;

    /**
     * The constant IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND.
     */
    public static final IsometrikError IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND).setErrorMessage("mediaUpload request to be canceled not found.").setRemoteError(false).build();
    private static final int IMERR_PRESIGNED_URL_MISSING = 195;
    /**
     * The constant IMERROBJ_PRESIGNED_URL_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRESIGNED_URL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRESIGNED_URL_MISSING).setErrorMessage("presignedUrl is missing.").setRemoteError(false).build();

    private static final int IMERR_USER_IMAGE_UPLOAD_CANCELED = 196;

    /**
     * The constant IMERROBJ_USER_IMAGE_UPLOAD_CANCELED.
     */
    public static final IsometrikError IMERROBJ_USER_IMAGE_UPLOAD_CANCELED = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IMAGE_UPLOAD_CANCELED).setErrorMessage("User image upload has been canceled.").setRemoteError(false).build();
    private static final int IMERR_REQUESTID_MISSING = 197;

    /**
     * The constant IMERROBJ_REQUESTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_REQUESTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_REQUESTID_MISSING).setErrorMessage("requestId is missing.").setRemoteError(false).build();

    /**
     * Stream start channel missing
     */
    private static final int IMERR_STREAM_START_CHANNEL_MISSING = 198;

    /**
     * The constant IMERROBJ_STREAM_START_CHANNEL_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_START_CHANNEL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_START_CHANNEL_MISSING).setErrorMessage("Stream start channel is missing.").setRemoteError(false).build();
    /**
     * Missing value of self hosted
     */
    private static final int IMERR_SELF_HOSTED_MISSING = 199;
    /**
     * The constant IMERROBJ_SELF_HOSTED_MISSING.
     */
    public static final IsometrikError IMERROBJ_SELF_HOSTED_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SELF_HOSTED_MISSING).setErrorMessage("Self hosted missing.").setRemoteError(false).build();
    /**
     * Missing value of self hosted
     */
    private static final int IMERR_SEARCH_TAG_INVALID_VALUE = 200;
    /**
     * The constant IMERROBJ_SEARCH_TAG_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_SEARCH_TAG_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SEARCH_TAG_INVALID_VALUE).setErrorMessage("Search tag can't be empty.").setRemoteError(false).build();


    private static final int IMERR_STREAM_DETAILS_MISSING = 201;

    /**
     * The constant IMERROBJ_STREAM_DETAILS_MISSING.
     */
    public static final IsometrikError IMERROBJ_STREAM_DETAILS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_STREAM_DETAILS_MISSING).setErrorMessage("Atleast one of metadata, customType and searchableTags should be specified.").setRemoteError(false).build();

    private static final int IMERR_RTMP_INGEST_MISSING = 202;

    /**
     * The constant IMERROBJ_STREAM_DETAILS_MISSING.
     */
    public static final IsometrikError IMERROBJ_RTMP_INGEST_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_RTMP_INGEST_MISSING).setErrorMessage("RTMP ingest missing.").setRemoteError(false).build();


    private static final int IMERR_PERSIST_RTMP_INGEST_ENDPOINT_MISSING = 203;

    /**
     * The constant IMERROBJ_STREAM_DETAILS_MISSING.
     */
    public static final IsometrikError IMERROBJ_PERSIST_RTMP_INGEST_ENDPOINT_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PERSIST_RTMP_INGEST_ENDPOINT_MISSING).setErrorMessage("Persist RTMP ingest endpoint missing.").setRemoteError(false).build();

    /**
     * The constant IMERROBJ_SEARCH_TAG_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_INVITE_ID_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SEARCH_TAG_INVALID_VALUE).setErrorMessage("Invite id can't be empty.").setRemoteError(false).build();
    public static final IsometrikError IMERROBJ_INVITE_ID_CURRENCY_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SEARCH_TAG_INVALID_VALUE).setErrorMessage("Currency can't be empty.").setRemoteError(false).build();


    private static final int IMERR_INVITE_ID_MISSING = 204;

    public static final IsometrikError IMERROBJ_PK_ID_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SEARCH_TAG_INVALID_VALUE).setErrorMessage("pk id can't be empty.").setRemoteError(false).build();

    /**
     * Gets imerr not found.
     *
     * @return the imerr not found
     */
    static int getImerrNotFound() {
        return IMERR_NOT_FOUND;
    }

    /**
     * Gets imerr forbidden.
     *
     * @return the imerr forbidden
     */
    static int getImerrForbidden() {
        return IMERR_FORBIDDEN;
    }

    /**
     * Gets imerr service unavailable.
     *
     * @return the imerr service unavailable
     */
    static int getImerrServiceUnavailable() {
        return IMERR_SERVICE_UNAVAILABLE;
    }

    /**
     * Gets imerr conflict.
     *
     * @return the imerr conflict
     */
    static int getImerrConflict() {
        return IMERR_CONFLICT;
    }

    /**
     * Gets imerr unprocessable entity.
     *
     * @return the imerr unprocessable entity
     */
    static int getImerrUnprocessableEntity() {
        return IMERR_UNPROCESSABLE_ENTITY;
    }

    /**
     * Gets imerr bad gateway.
     *
     * @return the imerr bad gateway
     */
    static int getImerrBadGateway() {
        return IMERR_BAD_GATEWAY;
    }

    /**
     * Gets imerr internal server error.
     *
     * @return the imerr internal server error
     */
    static int getImerrInternalServerError() {
        return IMERR_INTERNAL_SERVER_ERROR;
    }

    /**
     * Gets imerr parsing error.
     *
     * @return the imerr parsing error
     */
    static int getImerrParsingError() {
        return IMERR_PARSING_ERROR;
    }

    /**
     * Gets imerr network error.
     *
     * @return the imerr network error
     */
    static int getImerrNetworkError() {
        return IMERR_NETWORK_ERROR;
    }

    /**
     * Gets imerr bad request error.
     *
     * @return the imerr bad request error
     */
    static int getImerrBadRequestError() {
        return IMERR_BAD_REQUEST_ERROR;
    }

    /**
     * Gets imerr unauthorized error.
     *
     * @return the imerr unauthorized error
     */
    public static int getImerrUnauthorizedError() {
        return IMERR_UNAUTHORIZED_ERROR;
    }
}
