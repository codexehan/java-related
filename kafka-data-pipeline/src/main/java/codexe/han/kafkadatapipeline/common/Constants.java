package codexe.han.kafkadatapipeline.common;

public class Constants {

    public final static String ES_INDEX_DEJA_PRODUCT = "deja_products";
    public final static String ES_TYPE_DEJA_PRODUCT = "tags";

    public final static String ES_INDEX_DEJA_PRODUCT_IMAGE = "deja_products_image";
    public final static String ES_TYPE_DEJA_PRODUCT_IMAGE = "tags";

    public final static String ES_INDEX_DEJA_PRODUCT_DETAIL = "deja_products_detail";
    public final static String ES_TYPE_DEJA_PRODUCT_DETAIL = "tags";

    public final static String ES_INDEX_DEJA_PRODUCT_INVENTORY = "deja_products_inventory";
    public final static String ES_TYPE_DEJA_PRODUCT_INVENTORY = "tags";

    public final static String ES_INDEX_DEJA_INFLUENCER = "deja_influencer";
    public final static String ES_TYPE_DEJA_INFLUENCER = "tags";

    public final static String ES_INDEX_DEJA_PS_RELATION = "deja_ps_relation";
    public final static String ES_TYPE_DEJA_PS_RELATION = "tags";

    public final static String ES_INDEX_DEJA_STREET_ITEM = "deja_street_item";
    public final static String ES_TYPE_DEJA_STREET_ITEM = "tags";

    public final static String ES_INDEX_DEJA_STREET_FOR_ANALYSIS = "deja_street_for_analysis";
    public final static String ES_TYPE_DEJA_STREET_FOR_ANALYSIS = "tags";

    public final static String ES_INDEX_DEJA_EVENT_TRACKING = "deja_event_tracking";
    public final static String ES_TYPE_DEJA_EVENT_TRACKING = "tags";



    /***************************Kafka Topic*******************************/
    public final static String KAFKA_TOPIC_INFLUENCER = "deja_influencer";

    public final static String KAFKA_TOPIC_INFLUENCER_STYLE_RELATION = "deja_influencer_style_relation";
    public final static String KAFKA_TOPIC_INFLUENCER_SIMILARITY_RELAITON = "deja_influencer_similarity_relation";
    public final static String KAFKA_TOPIC_INFLUENCER_RACE = "deja_influencer_race";
    public final static String KAFKA_TOPIC_INFLUENCER_REGION = "deja_influencer_region";
    public final static String KAFKA_TOPIC_INFLUENCER_STYLE = "deja_influencer_style";
    public final static String KAFKA_TOPIC_CELEBRITY_HOT_ACCOUNT = "deja_celebrity_hot_account_batch";
    public final static String KAFKA_TOPIC_CELEBRITY_HOT_STREET = "deja_celebrity_hot_street_batch";
    public final static String KAFKA_TOPIC_PS_RELATION = "deja_ps_relation";
    public final static String KAFKA_TOPIC_SS_RELATION = "deja_ss_relation";
    public final static String KAFKA_TOPIC_STREET_FOR_ANALYSIS = "deja_street_for_analysis";
    public final static String KAFKA_TOPIC_STREET_ITEM = "deja_street_item";
    public final static String KAFKA_TOPIC_TAG = "deja_tag";
    public final static String KAFKA_TOPIC_BAG_ITEM = "deja_shopping_bag_item";
    public final static String KAFKA_TOPIC_INVENTORY_QUICK_RESPONSE = "deja_inventory_quick_response";



    /***************************Kafka Stream Topic*******************************/
    public final static String KAFKA_STREAM_TOPIC_PRODUCT_PURCHASABLE_COUNT = "deja_product_purchasable_count";
    public final static String KAFKA_STREAM_TOPIC_STREET_ITEM_COUNT_CHANGE = "deja_street_item_count_change";

    /***************************Kafka Local State*******************************/
    public final static String LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS = "productPurchasableStatus";
    public final static String LOCAL_STORE_PRODUCT_STREET_ITEM_RELATION = "productStreetItemRelation";
    public final static String LOCAL_STORE_STREET_ITEM_STREETSNAP_RELATION = "streetItemStreetSnapRelation";
    public final static String LOCAL_STORE_STREET_SNAP_RECOMMENDED_STATUS = "streetSnapRecommendedStatus";
    public final static String LOCAL_STORE_STREET_ITEM_PURCHASABLE_COUNT_STATUS = "streetItemPurchasableCountStatus";


    /***************************Kafka Used Topic*******************************/
    public final static String KAFKA_TOPIC_PRODUCTS = "deja_products_global";
    public final static String KAFKA_TOPIC_PRODUCT_INVENTORY = "deja_products_inventory_global";
    public final static String KAFKA_TOPIC_PRODUCT_PRICE = "deja_products_price_global";

    public final static String KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE = "deja_product_validate_global";
    public final static String KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE = "deja_product_price_validate_global";
    public final static String KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE = "deja_product_inventory_validate_global";
    public final static String KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS = "deja_product_final_status_global";

    public final static String KAFKA_TOPIC_STREET_ITEM_REFRESH = "deja_street_item_refresh_global";


    public final static String KAFKA_TOPIC_PRODUCT_STATUS_CHANGE = "deja_product_status_change_global";

    public final static String LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE = "productPurchasableStatusChangeGlobal";

    public final static String KAFKA_TOPIC_EVENT_TRACKING = "deja_event_tracking";


    public final static Integer PURCHASABLE_COUNT_THRESHOLD = 3;

    public final static Integer MAX_COUNT_SIZE = 99999999;

    public final static Integer BULK_PAGE_SIZE = 1000;

    public final static String CURRENCY_SG = "S$";
    public static final Long ERROR_FORMAT_PRODUCT_ID = 0L;
}
