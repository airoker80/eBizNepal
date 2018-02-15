package com.paybyonline.ebiz.serverdata;

/**
 * Created by Anish on 2/17/2016.
 */
public class PboServerRequestHandler {

    //    private static PboServerRequestHandler instance;
//    private AsyncHttpClient client;
//    private static final String BASE_URL = PayByOnlineConfig.SERVER_URL;
//    private CoordinatorLayout coordinatorLayout;
//    private Context context;
//    private static ProgressDialog progress;
//    MyUserSessionManager myUserSessionManager;
//
//    static NotificationProvider notificationProvider;
    /*private PboServerRequestHandler(){
        client = new AsyncHttpClient();
    }*/

//    public static PboServerRequestHandler getInstance(CoordinatorLayout coordinatorLayout,Context context){
//        if(instance == null){
//            instance = new PboServerRequestHandler();
//        }
//        instance.coordinatorLayout = coordinatorLayout;
//        instance.context=context;
//        notificationProvider=new NotificationProvider(coordinatorLayout);
//        instance.myUserSessionManager=new MyUserSessionManager(context);
//        return instance;
//    }

//    public boolean isConnected() {
//        ConnectivityManager connMgr = (ConnectivityManager) context
//                .getSystemService(Activity.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }

//    private static String getAbsoluteUrl(String relativeUrl) {
//
//        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "target url : " + BASE_URL + relativeUrl);
//        return BASE_URL + relativeUrl;
//    }

/*    public void makeRequest(String url, final String processingMessage,
                            RequestParams params, final PboServerRequestListener listener){

        Log.v("params",params.toString());
        if(isConnected()){

            client.setTimeout(60 * 1000);
            client.get(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    if (processingMessage.length() > 0) {
                        if (processingMessage.length() > 0) {
                            if(progress!=null){
                                if(progress.isShowing()){
                                    hideProgressDialog();
                                }
                                showProgressDialog(processingMessage);
                            }else{
                                showProgressDialog(processingMessage);
                            }

                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("response===","=="+response.toString());
                    try{
                        if (processingMessage.length() > 0) {
                            hideProgressDialog();
                        }
                        if((context instanceof CreateAccountActivity)||(context instanceof InitialLoadingActivity)||(context instanceof DashBoardActivity)){
                            listener.onSuccess(statusCode, headers, response);
                        }else{
                            if(response.getString("msg").equals("User Authentication Failed")){
                                MyUserSessionManager userSessionManager = new MyUserSessionManager(context);
                                userSessionManager.logoutUser();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else{
                                listener.onSuccess(statusCode, headers, response);
                            }
                        }
                    }catch(JSONException e){
                        Log.i("Error", "" + e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject jo) {
                    if (processingMessage.length() > 0) {
                        hideProgressDialog();
                    }
                    showFailureMessage(statusCode);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                    if (processingMessage.length() > 0) {
                        hideProgressDialog();
                    }
                    showFailureMessage(statusCode);
                }

                @Override
                public void onRetry(int retryNo) {
                    //Some debugging code here-------
                }
            });
        } else {
            notificationProvider.showToast("Device might not be connected to Internet",context);
            loadInitialErrorPage();
        }


    }*/

//    public void loadInitialErrorPage() {
//        Intent intent = new Intent(context, InitialLoadingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

/*    public void makePostRequest(String url, final String processingMessage,
                                RequestParams params,final PboServerRequestListener listener){


        if(isConnected()){
            client.setTimeout(60 * 1000);

            if(!((context instanceof LoginActivity)||(context instanceof CreateAccountActivity))){
                url=getAbsoluteUrl(url);
                params.put("accessToken", instance.myUserSessionManager.getKeyAccessToken());
            }

            Log.d("paramsForPost", params.toString());

            client.post(url, params,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onStart() {

                            if (processingMessage.length() > 0) {
                                if(progress!=null){
                                    if(progress.isShowing()){
                                        hideProgressDialog();
                                    }
                                    showProgressDialog(processingMessage);
                                }else{
                                    showProgressDialog(processingMessage);
                                }

                            }

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.i("response------------- ","data"+response);
                            try{
                                if (processingMessage.length() > 0) {
                                    hideProgressDialog();
                                }

                                if((context instanceof InitialLoadingActivity) ||
                                        ( context instanceof LoginActivity)||( context instanceof CreateAccountActivity)){
                                    listener.onSuccess(statusCode, headers, response);
                                }else{
                                    if(response.getString("msg").equals("User Authentication Failed")){
                                        MyUserSessionManager userSessionManager = new MyUserSessionManager(context);
                                        userSessionManager.logoutUser();
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }else{
                                        listener.onSuccess(statusCode, headers, response);
                                    }
                                }


                            }catch(JSONException e){
                                Log.i("Error",""+e);
                                if (processingMessage.length() > 0){
                                    hideProgressDialog();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject jo) {
                            if (processingMessage.length() > 0) {
                                hideProgressDialog();
                            }
                            showFailureMessage(statusCode);
                        }

                    });

        }else{
            notificationProvider.showMessage("Device might not be connected to Internet");
            loadInitialErrorPage();
        }


    }*/


//    public void showFailureMessage(int statusCode) {
//        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "statusCode " + statusCode);
//        if (statusCode == 400) {
//            notificationProvider.showToast("Error Occurred", context);
//        } else if (statusCode == 404) {
//            notificationProvider.showToast("Requested resource not found", context);
//        } else if (statusCode == 500) {
//            notificationProvider.showToast("Something went wrong at server end", context);
//        } else {
//            notificationProvider.showToast("Device might not be connected to Internet or server might be having some issue. Please try again !!!", context);
//            loadInitialErrorPage();
//        }
//    }

//    public void showProgressDialog(String message) {
//
//        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "showProgressDialog");
//        progress = new ProgressDialog(context);
//        if (!progress.isShowing()) {
//            SpannableString titleMsg = new SpannableString("Processing");
//            titleMsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleMsg.length(), 0);
//            progress.setTitle(titleMsg);
//            progress.setMessage(message);
//            progress.setCancelable(false);
//            progress.setIndeterminate(true);
//            progress.show();
//        }
//    }
//
//    public void hideProgressDialog() {
//        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "hideProgressDialog");
//        if (progress != null) {
//            if (progress.isShowing()) {
//                progress.dismiss();
//            }
//        }
//
//    }
/*    public void makeRequestService(String url, final String processingMessage,
                            RequestParams params, final PboServerRequestListener listener){

        if(isConnected()){

            client.setTimeout(60 * 1000);
            client.get(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    if (processingMessage.length() > 0) {
                        if (processingMessage.length() > 0) {
                            if(progress!=null){
                                if(progress.isShowing()){
                                    hideProgressDialog();
                                }
                                //showProgressDialog(processingMessage);
                            }else{
                                //showProgressDialog(processingMessage);
                            }

                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try{
                        if (processingMessage.length() > 0) {
                            //hideProgressDialog();
                        }
                        if((context instanceof CreateAccountActivity)||(context instanceof InitialLoadingActivity)){
                            listener.onSuccess(statusCode, headers, response);
                        }else{
                            if(response.getString("msg").equals("User Authentication Failed")){
                                MyUserSessionManager userSessionManager = new MyUserSessionManager(context);
                                userSessionManager.logoutUser();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else{
                                listener.onSuccess(statusCode, headers, response);
                            }
                        }
                    }catch(JSONException e){
                        Log.i("Error", "" + e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject jo) {
                    if (processingMessage.length() > 0) {
                        //hideProgressDialog();
                    }
                    showFailureMessage(statusCode);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                    if (processingMessage.length() > 0) {
                        //hideProgressDialog();
                    }
                    showFailureMessage(statusCode);
                }

                @Override
                public void onRetry(int retryNo) {
                    //Some debugging code here-------
                }
            });
        } else {
            notificationProvider.showToast("Device might not be connected to Internet",context);
            loadInitialErrorPage();
        }


    }*/
}
