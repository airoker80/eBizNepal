package com.paybyonline.ebiz.configuration;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by Anish on 11/4/15.
 */
public class PayByOnlineConfig {


    public static final String PAY_BY_ONLINE_TAG_NAME = "Paybyonline";
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String PAYPAL_TAG = "Paypal Payment";
    public static final String SERVER_ACTION = "authenticateAppUser";
    public static final String CONTACT_URL = "http://128.199.242.18:8080/frontPage/defaultPage?domainUrl=8";

    /*Test Environment*/
//    public static final String PAYPAL_CLIENT_ID = "ARWMpvr1PPSkxtV6Bxd8dmkbIkfAAZiVbEJ-pwdhCyi_OPqzXuAhW74msB4zwq1QvzTYSXhjBH4Hz8Mr"; // Test App
//    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
//    public static final String STRIPE_PUBLISHABLE_KEY = "pk_test_Z8xxzOMbMFEEFwKilM6PxUSd";
//    public static String BASE_URL="http://188.166.177.242:8080/";
//     public static String BASE_URL="http://192.168.1.54:8080/RechargeSystem/";
//     public static String BASE_URL="http://138.197.14.125:8080/pbo/";
//     public static String BASE_URL="http://192.168.1.19:8080/Pbo-Android/";
//     public static String BASE_URL="http://ncell.harati.com.np/";
//    public static final String SERVER_URL =BASE_URL+"androidApp/";
    /*Test Environment*/


    /*Live Environment*/
    public static final String PAYPAL_CLIENT_ID = "Ad_cypVG1ZF9eOvqO8laZaZQOtNA7OL4u1vOMdqMy3MShzdPjD4Pm3qRbqRIMzbNgx4YAnIOydXMGEgi";
    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    public static final String STRIPE_PUBLISHABLE_KEY = "pk_live_G34XslJzjI7MRdWCo5cxXJ3F";
    public static String BASE_URL = "https://www.paybyonline.com/";
    //    public static String BA````                                                                                                                           `   ~~~~        `                           ~[SE_URL = "http://138.197.14.125:8080/pbo/";
//    public static  String BASE_URL = "http://138.197.14.125:8080/pbo/";
//    public static String BASE_URL="http://192.168.1.85:8080/Pbo-Android/";
//   public static  String BASE_URL = "http://192.168.1.87:8080/pbo-deployment/";
//    public static  String BASE_URL = "http://192.168.1.87:8080/customizedMerchant/";
//       public static  String BASE_URL = "http://192.168.1.46:8080/pbo/";
//       public static  String BASE_URL = "http://192.168.1.45:8080/pbo/";
//       public static  String BASE_URL = "http://192.168.1.93:8080/pbo/";
//       public static  String BASE_URL = "http://192.168.1.87:8080/pbo/";
//    public static  String BASE_URL = "http://192.168.1.93:8080/Pbo-Android/";
//    public static  String BASE_URL = "http://192.168.1.46:8080/paybyonline/";
//    public static String BASE_URL = "http://192.168.1.87:8080/paybyonline/";
//    public static String BASE_URL = "http://192.168.1.93:8080/paybyonline/";
//    public static String BASE_URL = " http://192.168.1.99:8080/paybyonline/";
//    public static String BASE_URL = "http://192.168.1.93:9090/Pbo-Android/";
//    public static String BASE_URL = "http://192.168.1.99:8080/Pbo-Android/";
//    public static String BASE_URL = "http://192.168.1.93:8090/Pbo-Android/";
//    public static final String BASE_URL = "http://192.168.1.87:8080/paybyonline/";
    public static String SERVER_URL = BASE_URL + "androidApp/";

    public static final String GOOGLE_CLIENT_ID = "150981418851-38oquuiltaq1o7s3nc4up9723o9dslvo.apps.googleusercontent.com";
    public static final String GOOGLE_REDIRECT_URI = SERVER_URL + "listGmailResult";
    public static final String GOOGLE_SCOPE = "https://www.google.com/m8/feeds/";
    public static final String GOOGLE_RESPONSE_TYPE = "code";
    public static final String NIBL_CHECKOUT = SERVER_URL + "niblCheckout";
    public static final String CHECKOUT = SERVER_URL + "ebankingCheckOut";
    public static final String DEBIT_CREDIT_CARD_CHECKOUT = SERVER_URL + "debitCreditCardCheckOut";

    //SCT
    public static final String SCTMERCHANTID = "80";
    public static final String SCTMERCHANTPASSWORD = "ebiznepalapp@live-api";
    public static final String SCTMERCHANTSIGNATUREPASSWORD = "EBNAPP01";
    public static final String SCTMERCHANTUSERNAME = "ebizapp";


}
