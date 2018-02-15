package com.paybyonline.ebiz.serverdata;

import com.paybyonline.ebiz.configuration.PayByOnlineConfig;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Swatin on 11/16/2017.
 */

public class ApiIndex {

    public static int POST_ON_AUTH_APP_USER_ENDPOINT = 1;
    public static int GET_ON_AUTH_APP_USER_ENDPOINT = 2;
    public static int GET_ON_VERIFY_INSTALLED_APP_ENDPOINT = 3;
    public static int GET_USER_LOGIN = 4;
    public static int GET_ON_AUTH_USER_ENDPOINT = 5;
    public static int GET_ON_REGISTER_USER_ENDPOINT = 6;
    public static int GET_ON_FORGOTPASSWORD = 7;
    public static int GET_FAVOURITE_SERVICE = 8;
    public static int POST_FAVOURITE_SERVICE = 9;

    public interface API {

        //apiCode = 1
        @FormUrlEncoded
        @POST(PayByOnlineConfig.SERVER_ACTION)
        Call<ResponseBody> postOnAuthAppUserEndpoint(
                @FieldMap Map<String, String> bodyMap
        );

        //apiCode = 2
        @GET(PayByOnlineConfig.SERVER_ACTION)
        Call<ResponseBody> getOnAuthAppUserEndpoint(
                @QueryMap Map<String, String> bodyMap
        );

        //apiCode= 3
        @GET("verifyInstalledApp")
        Call<ResponseBody> getOnVerifyInstalledAppEndpoint(
                @QueryMap Map<String, String> bodyMap
        );

        //apiCode = 4
        @GET("userLogin")
        Call<ResponseBody> getUserLogin(
                @QueryMap Map<String, String> bodyMap
        );

        //apiCode = 5
        @GET("authenticateUser")
        Call<ResponseBody> getOnAuthUserEndpoint(
                @QueryMap Map<String, String> bodyMap
        );

        @GET("forgotPassword")
        Call<ResponseBody> getForgotPassword(
                @QueryMap Map<String, String> bodyMap
        );

        //apiCode = 6
        @GET("userRegister")
        Call<ResponseBody> registerUser(
                @QueryMap Map<String, String> query
        );

        //apiCode = 6
        @GET("myServicesList")
        Call<ResponseBody> serviceList(
                @QueryMap Map<String, String> query
        );
        @POST("updateServiceList")
        Call<ResponseBody> postserviceList(
                @QueryMap Map<String, String> query
        );

    }

    public static String RETROFIT_HELPER_FRAGMENT_TAG = "retrofit_helper";
//    public static String RETROFIT_HELPER_FRAGMENT_SECONDARY_TAG = "retrofit_helper_secondary";

    static Call<ResponseBody> getApi(API api, int apiCode, Map<String, String> query, Map<String, String> header, Map<String, String> body) {
        switch (apiCode) {

            case 1:
                return api.postOnAuthAppUserEndpoint(body);
            case 2:
                return api.getOnAuthAppUserEndpoint(query);
            case 3:
                return api.getOnVerifyInstalledAppEndpoint(query);
            case 4:
                return api.getUserLogin(query);
            case 5:
                return api.getOnAuthUserEndpoint(query);
            case 6:
                return api.registerUser(query);
            case 7:
                return api.getForgotPassword(query);
            case 8:
                return api.serviceList(query);
                case 9:
                return api.postserviceList(query);
        }
        return null;
    }
}
