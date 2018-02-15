package com.paybyonline.ebiz.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paybyonline.ebiz.Adapter.Model.ContactsModel;
import com.paybyonline.ebiz.Adapter.ParentRecycleViewAdapter;
import com.paybyonline.ebiz.Fragment.AboutUsFragment;
import com.paybyonline.ebiz.Fragment.ContactUsFragment;
import com.paybyonline.ebiz.Fragment.CustomerSimSalesFragment;
import com.paybyonline.ebiz.Fragment.DashboardMobaletFragment;
import com.paybyonline.ebiz.Fragment.DashboardSlidingFragment;
import com.paybyonline.ebiz.Fragment.FavouriteServicesFragment;
import com.paybyonline.ebiz.Fragment.KycDynamicForm;
import com.paybyonline.ebiz.Fragment.ProfileFragment;
import com.paybyonline.ebiz.Fragment.RechargeBuyFragment;
import com.paybyonline.ebiz.Fragment.RechargedDetailsFragment;
import com.paybyonline.ebiz.Fragment.SendMoneyDetailsFragment;
import com.paybyonline.ebiz.Fragment.SettingFragment;
import com.paybyonline.ebiz.Fragment.SimDetailsFragment;
import com.paybyonline.ebiz.Fragment.TransactionReportFragment;
import com.paybyonline.ebiz.Fragment.UserNotificationFragment;
import com.paybyonline.ebiz.Fragment.WalletFragment;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;
import com.paybyonline.ebiz.serverdata.ApiIndex;
import com.paybyonline.ebiz.serverdata.RetrofitHelper;
import com.paybyonline.ebiz.usersession.MyUserSessionManager;
import com.paybyonline.ebiz.usersession.ShowMyAlertProgressDialog;
import com.paybyonline.ebiz.usersession.UserDeviceDetails;
import com.paybyonline.ebiz.usersession.UserInstalledApplicationDetails;
import com.paybyonline.ebiz.util.Constants;
import com.paybyonline.ebiz.util.GoogleContactsAndEmail;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Fragment fragment = null;
    CoordinatorLayout coordinatorLayout;
    MyUserSessionManager myUserSessionManager;
    UserDeviceDetails userDeviceDetails;
    //    private static String url;
    boolean instanceStateNull = false;
    ShowMyAlertProgressDialog showMyAlertProgressDialog;
    LinearLayout fav_ll;
    ParentRecycleViewAdapter parentRecycleViewAdapter;
    NavigationView navigationView;
    public static MenuItem advSearchItem;
    //    private PboServerRequestHandler pboServerRequestHandler;
    String fragmentName = "";
    TextView dashboard, shareandEarn, notification_txt, favlist_txt;
    TextView wallet;
    TextView buyPage, kyc_status;
    TextView aboutUsShort, contactUsShort;
    TextView simSales;
    TextView contactUs;
    TextView sendMoneyDetails;
    TextView addMoney;
    TextView summaryReport, transactionalList;
    TextView report;
    TextView userBalance;
    TextView userHoldManeyBalanceStatus;
    TextView settingLink;
    TextView aboutUs;
    TextView simDetails;
    TextView signOut, payments, myProfile;
    ImageView profile_image;
    public final static int REQUEST_CODE_PICK_CONTACTS = 1;
    LinearLayout userNotification;
    LinearLayout fabServices;
    TextView notificationCount;
    TextView fabServicesCount;
    TextView txtbalance;
    Boolean showHide = false;
    Boolean showHide1 = false;
    MenuItem mainMenu;
    String facebookUrl = "";
    String twitterUrl = "";
    String googlePlusUrl = "";
    String gmailUrl = "";
    String yahooMailUrl = "";
    String viberUrl = "";
    String skypeUrl = "";
    String linkedinUrl = "";
    String microsoftOutlookUrl = "";
    UserInstalledApplicationDetails userInstalledApplicationDetails;
    String isPinCodePresent = "NO";
    public static Toolbar toolbar;
    public static MenuItem shareMenu;
    private String currency = "";
//    private DrawerLayout drawer;

    public static DrawerLayout drawer;
    private RetrofitHelper retrofitHelper;
    private GoogleContactsAndEmail googleContactsAndEmail;

    public List<ContactsModel> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        Log.v("Serial No :", "Serial No " + Build.SERIAL + "\n");
        // toolbar.setTitle("NB Insurance Co.Ltd.");
        if (getPackageName().contains("tirnus")) {
            toolbar.setLogo(R.drawable.tirnus);
        }
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            } else {
                getContacts();
            }
        }else {
            getContacts();
        }

        setSupportActionBar(toolbar);

        showMyAlertProgressDialog = new ShowMyAlertProgressDialog(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        url = PayByOnlineConfig.SERVER_URL;
        myUserSessionManager = new MyUserSessionManager(this);
        if (!myUserSessionManager.isUserLoggedIn()) {
            startActivity(new Intent(getApplicationContext(),
                    LoginActivity.class));
            finish();
            return;
        }

        if (!myUserSessionManager.ifUserHasCountry()) {
            startActivity(new Intent(getApplicationContext(),
                    AfterRegistration.class));
            finish();
            return;
        }

        retrofitHelper = new RetrofitHelper();
        getSupportFragmentManager().beginTransaction().add(retrofitHelper, ApiIndex.RETROFIT_HELPER_FRAGMENT_TAG).commitNow();

        userDeviceDetails = new UserDeviceDetails(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        dashboard = (TextView) findViewById(R.id.dashboard);
        notification_txt = (TextView) findViewById(R.id.notification_txt);
        shareandEarn = (TextView) findViewById(R.id.shareandEarn);
        myProfile = (TextView) findViewById(R.id.myProfile);
        notificationCount = (TextView) findViewById(R.id.notificationCount);
        fabServicesCount = (TextView) findViewById(R.id.fabServicesCount);
        userBalance = (TextView) findViewById(R.id.userBalance);
        userHoldManeyBalanceStatus = (TextView) findViewById(R.id.userHoldManeyBalanceStatus);
        txtbalance = (TextView) findViewById(R.id.txtbalance);
        wallet = (TextView) findViewById(R.id.wallet);
        buyPage = (TextView) findViewById(R.id.buyPage);
        kyc_status = (TextView) findViewById(R.id.kyc_status);
        simSales = (TextView) findViewById(R.id.simSales);
        report = (TextView) findViewById(R.id.report);
        settingLink = (TextView) findViewById(R.id.settingTxt);
        contactUs = (TextView) findViewById(R.id.contactUs);
        aboutUs = (TextView) findViewById(R.id.aboutUs);
        simDetails = (TextView) findViewById(R.id.simDetails);
        aboutUsShort = (TextView) findViewById(R.id.aboutUsShort);
        contactUsShort = (TextView) findViewById(R.id.contactUsShort);
        payments = (TextView) findViewById(R.id.Payments);
        signOut = (TextView) findViewById(R.id.signOut);
        sendMoneyDetails = (TextView) findViewById(R.id.sendMoneyDetails);
        addMoney = (TextView) findViewById(R.id.addMoney);
        transactionalList = (TextView) findViewById(R.id.transactionalList);
        summaryReport = (TextView) findViewById(R.id.summaryReport);
        favlist_txt = (TextView) findViewById(R.id.favlist_txt);

        fabServices = (LinearLayout) findViewById(R.id.fabServices);
        fav_ll = (LinearLayout) findViewById(R.id.fav_ll);

        userNotification = (LinearLayout) findViewById(R.id.userNotification);
        profile_image = (ImageView) findViewById(R.id.profile_image);

//        if (getPackageName().contains("MobaletTest")){
            favlist_txt.setVisibility(View.VISIBLE);
//        }
//        fav_ll.setVisibility(View.VISIBLE);

//        Log.v("fav_vo", String.valueOf(fav_ll.getVisibility()));
        aboutUs.setOnClickListener(this);
        profile_image.setOnClickListener(view -> selectPageToDisplay(R.id.myProfile, false, ""));


        //ClickListener
        kyc_status.setOnClickListener(this);
        payments.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        userNotification.setOnClickListener(this);
        notification_txt.setOnClickListener(this);
        favlist_txt.setOnClickListener(this);
        sendMoneyDetails.setOnClickListener(this);
        addMoney.setOnClickListener(this);
        fabServices.setOnClickListener(this);
        dashboard.setOnClickListener(this);
        report.setOnClickListener(this);
        wallet.setOnClickListener(this);
        signOut.setOnClickListener(this);
        simDetails.setOnClickListener(this);
        buyPage.setOnClickListener(this);
        transactionalList.setOnClickListener(this);
        summaryReport.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        simSales.setOnClickListener(this);
        settingLink.setOnClickListener(this);
        aboutUsShort.setOnClickListener(this);
        contactUsShort.setOnClickListener(this);
        shareandEarn.setOnClickListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        if (getPackageName().contains("tirnus")) {
            ab.setIcon(R.drawable.ic_trinus_action_bar);
        } else if (getPackageName().contains("ebiz")) {
            ab.setIcon(R.mipmap.ic_new_logo);
        } else {
            ab.setIcon(R.mipmap.ic_launcher);
        }


        if (savedInstanceState == null) {
            instanceStateNull = true;
            loadUserMenuDetails();
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                showDefaultPage();
            } else {
                String defaultPage = bundle.getString("defaultPage");
                if (defaultPage != null) {
                    switch (defaultPage) {
                        case "SendMoney":
                            selectPageToDisplay(R.id.sendMoneyDetails, false, "");
                            break;
                        case "AddMoney":
                            selectPageToDisplay(R.id.wallet, false, "");
                            break;

                        default:
                            showDefaultPage();
                            /*selectPageToDisplay(R.id.buyPage,false,"");*/

                            break;
                    }
                } else {
                    showDefaultPage();
//                    selectPageToDisplay(R.id.buyPage,false,"");

                }
            }


        }

    }

    public void setUserBalance(String currency, String balance) {
        userBalance.setText(balance);
        this.currency = currency;
    }

    public String getUserCurrency() {
        return currency;
    }

    public void loadUserMenuDetails() {
//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
//        params.put("childTask", "getAccountSummary");
        params.put("childTask", "getUserMenuDetails");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        params.put("role", "USER");
//        pboServerRequestHandler = PboServerRequestHandler.getInstance(coordinatorLayout,DashBoardActivity.this);
//        pboServerRequestHandler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "Please Wait !!!", params, new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    handleLoadUserMenuDetailsResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    handleLoadUserMenuDetailsResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.d("Dashboard", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    @SuppressLint("ResourceAsColor")
    public void handleLoadUserMenuDetailsResponse(JSONObject response) throws JSONException {
        JSONObject jsonObject = response;
//        kyc_status.setText("KYC: "+response.getString("status"));
        if (response.getString("status").equals("Pending")) {
            kyc_status.setBackgroundResource(R.color.kyc_pending);
            kyc_status.setTextColor(R.color.white);
            kyc_status.setText("KYC: " + response.getString("status"));
        } else if (response.getString("status").equals("Rejected")) {
            kyc_status.setBackgroundResource(R.color.kyc_red);
            kyc_status.setTextColor(R.color.white);
            kyc_status.setText("KYC: " + response.getString("status"));
        } else if (response.getString("status").equals("Verified")) {
            kyc_status.setBackgroundResource(R.color.kyc_verified);
            kyc_status.setTextColor(R.color.white);
            kyc_status.setText("KYC: " + response.getString("status"));
        } else if (response.getString("status").equals("not-verified")) {
            kyc_status.setBackgroundResource(R.color.kyc_red);
            kyc_status.setTextColor(Color.WHITE);
            kyc_status.setText("KYC: Not Submitted");
        }
        Log.i("res ", "" + jsonObject);

        if (jsonObject.getString("msg").equals("Success")) {

            if (response.has("userDetails")) {
                JSONObject userDetails = response.getJSONObject("userDetails");

                String userBalanceMoney = userDetails.getString("userBalance");
                userBalance.setText(userBalanceMoney);
                myUserSessionManager.setKeyUserAmt(userBalanceMoney);
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(R.id.dashboard) + "tag");
                if (getPackageName().contains("ebiz")) {
                    if (currentFragment == null) {
                        currentFragment = getSupportFragmentManager().findFragmentByTag(Constants.DASHBOARD_MOBALET_FRAGMENT);
                    }
                    if (currentFragment != null && currentFragment instanceof DashboardMobaletFragment) {
                        ((DashboardMobaletFragment) currentFragment).setUserBalance(userBalanceMoney);
                    }
                }

                String currencyCode = userDetails.getString("currencyCode");
                String userCountry = "";
                if (userDetails.has("userCountry")) {
                    userCountry = userDetails.getString("userCountry");
                }
                //String userCountry = userDetails.getString("userCountry");
                String hasPinCode = userDetails.has("hasPinCode") ? userDetails.getString("hasPinCode") : "NO";
                String userPhoto = userDetails.getString("userPhoto");
                String notViewNotification = userDetails.getString("notViewNotification");
                String favouriteServicesCount = userDetails.getString("favouriteServicesCount");
                Double holdMoneyAmount = Double.parseDouble(userDetails.getString("holdMoneyAmount"));
                if (userDetails.has("gnData")) {
                    JSONObject obj = userDetails.getJSONObject("gnData");
                    setUserDetails(userCountry, currencyCode, userBalanceMoney, userPhoto, notViewNotification, favouriteServicesCount, hasPinCode, holdMoneyAmount,
                            obj.getString("Viber"),
                            obj.getString("Facebook"),
                            obj.getString("Twitter"),
                            obj.getString("Google_plus"),
                            obj.getString("Gmail"),
                            obj.getString("Yahoo"),
                            obj.getString("Skype"),
                            obj.getString("Linkedin"),
                            obj.getString("Microsoft_Outlook"));
                } else {
                    setUserDetails(userCountry, currencyCode, userBalanceMoney, userPhoto, notViewNotification, favouriteServicesCount, hasPinCode, holdMoneyAmount, "", "", "", "", "", "", "", "", "");
                }

            }


        } else {
            userDeviceDetails.showToast("Error Occurred. Please try again !!!");
        }
    }


    public void showDefaultPage() {
        if (myUserSessionManager.getKeyDefaultPage().length() > 0) {
            switch (myUserSessionManager.getKeyDefaultPage()) {
                case "Dashboard":
                    //selectPageToDisplay(R.id.dashboard,false,"");
                    selectPageToDisplay(R.id.dashboard, false, "");

                    break;
                case "Buy/Recharge":
                    selectPageToDisplay(R.id.buyPage, false, "");
                    break;
                case "Share And Earn":
                    selectPageToDisplay(R.id.shareandEarn, false, "");
                    break;
                case "Add Money":
                    selectPageToDisplay(R.id.wallet, false, "");
                    break;
                case "Send Money":
                    selectPageToDisplay(R.id.sendMoneyDetails, false, "");
                    break;
                case "Report":
                    selectPageToDisplay(R.id.report, false, "");
                    break;
                case "Payments":
                    selectPageToDisplay(R.id.Payments, false, "");
                    break;
                case "My Profile":
                    selectPageToDisplay(R.id.myProfile, false, "");
                    break;

                default:
                    selectPageToDisplay(R.id.dashboard, false, "");


                    break;
            }
        } else {
            selectPageToDisplay(R.id.dashboard, false, "");

        }
    }

    public String getPinCodeStatus() {
        return isPinCodePresent;
    }

    public void updateUserBalance(String currencyCode, String userCurrentBalance, Double holdMoneyAmount) {
        Log.e("userBalance=========", userCurrentBalance);
//        userBalance.setText(userCurrentBalance);
        userHoldManeyBalanceStatus.setText("");
        userBalance.setText(currencyCode + " " + userCurrentBalance);
        myUserSessionManager.setKeyUserAmt(currencyCode + " " + userCurrentBalance);

        /*if((Double.parseDouble(userCurrentBalance))<0){
            txtbalance.setText("On Credit : ");
        }else{
            txtbalance.setText("Balance : ");
        }*/
        if (holdMoneyAmount > 0) {
            userHoldManeyBalanceStatus.setText("Hold Amount : " + holdMoneyAmount);
            userHoldManeyBalanceStatus.setVisibility(View.VISIBLE);
        } else {
            userHoldManeyBalanceStatus.setVisibility(View.GONE);
        }
    }


    public void updateUserPhoto(String userPhoto) {
        if (getPackageName().contains("guheshwori")) {
            Picasso.with(DashBoardActivity.this)
                    .load(userPhoto)
                    .placeholder(R.drawable.ic_user_dummy)
                    .error(R.drawable.ic_user_dummy)
                    .into(profile_image);
        } else {
            Picasso.with(DashBoardActivity.this)
                    .load(userPhoto)
                    .placeholder(R.drawable.ic_user_dummy)
                    .error(R.drawable.ic_user_dummy)
                    .into(profile_image);
        }
    }

    public void setUserDetails(String userCountry, String currencyCode, String userCurrentBalance, String userPhoto, String notViewNotificationCount, String favouriteServicesCount, String hasPinCode, Double holdMoneyAmount,
                               String viber, String facebook, String twitter,
                               String google_plus, String gmail, String yahoo,
                               String skype, String linkedin, String outlook) {

        myUserSessionManager.addUserCountryName(userCountry);

        updateUserBalance(currencyCode, userCurrentBalance, holdMoneyAmount);
        isPinCodePresent = hasPinCode;
        notificationCount.setText(notViewNotificationCount);
        fabServicesCount.setText(favouriteServicesCount);

        updateUserPhoto(userPhoto);

        viberUrl = viber;
        facebookUrl = facebook;
        twitterUrl = twitter;
        googlePlusUrl = google_plus;
        gmailUrl = gmail;
        yahooMailUrl = yahoo;
        skypeUrl = skype;
        linkedinUrl = linkedin;
        microsoftOutlookUrl = outlook;

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment neaBillFrag = manager.findFragmentByTag(Constants.NEA_BILL_FRAGMENT);
        Fragment buyConfirmFrag = manager.findFragmentByTag(Constants.BUY_CONFIRM_FRAGMENT);
//        Fragment buyProductFragment = manager.findFragmentByTag(Constants)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (neaBillFrag != null || buyConfirmFrag != null) {
            if (buyConfirmFrag != null) {
                manager.beginTransaction().remove(buyConfirmFrag).commitNow();
            }
            if (neaBillFrag != null) {
                manager.beginTransaction().remove(neaBillFrag).commitNow();
            }
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        // return true;
//        this.mainMenu = (MenuItem) menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dash_board, menu);


//        MenuItem itemAddMoney = menu.findItem(R.id.addMoney);
//        MenuItem sendMoneyItem = menu.findItem(R.id.sendMoney);
//        itemAddMoney.setVisible(false);
//        sendMoneyItem.setVisible(false);
//        MenuItem addServices = menu.findItem(R.id.addServices);
//        advSearchItem = menu.findItem(R.id.trans_adv_search);;
//        addServices.setVisible(false);
//        advSearchItem.setVisible(false);
        shareMenu = menu.findItem(R.id.menu_item_action_parameters);
        shareMenu.setVisible(true);

        return true;
    }

    MenuItem item;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Clicked ", "Clicked");
        switch (item.getItemId()) {

/*
            case R.id.trans_adv_search:

                TransactionReportFragment  reportFragment = (TransactionReportFragment) fragment;
                reportFragment.advanceSearchModel(DashBoardActivity.this);
                return true;
*/

        /*    case R.id.nibl:
                startActivity(new Intent(DashBoardActivity.this,OnlineBankingActivity.class));
                return true;*/


            case R.id.btnGplus:
                userShare("Google+", googlePlusUrl);
                return true;
            case R.id.btnFb:
                userShare("Facebook", facebookUrl);
                return true;
            case R.id.btnTwitter:
                userShare("Twitter", twitterUrl);
                return true;
            case R.id.btnGmail:
                userShare("Gmail", gmailUrl);
                return true;
            case R.id.btnYahooMail:
                userShare("Yahoo Mail", yahooMailUrl);
                return true;
            case R.id.btnViber:
                userShare("Viber", viberUrl);
                return true;
            case R.id.btnSkype:
                userShare("Skype", skypeUrl);
                return true;
            case R.id.btnLinkedin:
                userShare("Linkedin", linkedinUrl);
                return true;
            case R.id.btnMicrosoftOutlook:
                userShare("Microsoft Outlook", microsoftOutlookUrl);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void userShare(String appName, String appUrl) {

        userInstalledApplicationDetails = new UserInstalledApplicationDetails(DashBoardActivity.this);
        Boolean appInstalled = userInstalledApplicationDetails.checkIfApplicationIsInstalled(appName);
        if (appInstalled) {
            String subject = getString(R.string.app_name) + " Invitation";
            //  String text = "https://www.paybyonline.com/rechargeSystem/home?param1=ydlK6%2BtpJtRxiZ3iiAIuWQ%3D%3D&param2=v6RtqzCBJnmY3DlhjtOEKg%3D%3D&param4=pkBFXCv4RGXjh97HKf0sSi%2FqXFNUqwmIPnL5%2FL7Vtu8%3D";
            if (appUrl.length() > 0) {
                startSharingApplication(subject, appUrl, userInstalledApplicationDetails.getPackageName());
            } else {
                userDeviceDetails.showToast("Please Wait While Loading....");
            }
            // startSharingApplication(subject,text,userInstalledApplicationDetails.getPackageName());
        } else {
            Toast.makeText(DashBoardActivity.this, appName + " Not Installed. Please Install to share", Toast.LENGTH_SHORT).show();
        }
    }

    public void startSharingApplication(String subject, String text, String packageName) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.setType("text/plain");
        shareIntent.setPackage(packageName);
        if (packageName.contains("com.google.android.gm") || packageName.contains("com.microsoft.office.outlook") ||
                packageName.contains("com.yahoo.mobile.client.android.mail")) {
            if (packageName.contains("com.yahoo.mobile.client.android.mail") || packageName.contains("com.microsoft.office.outlook")) {
                obtainUserProfileInfo(true, false, shareIntent, text);
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                GoogleContactsAndEmail googleContactsAndEmailFragment = (GoogleContactsAndEmail) fragmentManager.findFragmentByTag(Constants.GOOGLE_CONTACTS_FRAGMENT);
                if (googleContactsAndEmailFragment == null || !googleContactsAndEmailFragment.isAdded()) {
                    googleContactsAndEmail = new GoogleContactsAndEmail();
                    fragmentManager.beginTransaction().add(googleContactsAndEmail,
                            Constants.GOOGLE_CONTACTS_FRAGMENT).commitNow();
                } else {
                    googleContactsAndEmail = googleContactsAndEmailFragment;
                    googleContactsAndEmail.start();
                }
                googleContactsAndEmail.setOnGoogleContactsFetchedListener(new GoogleContactsAndEmail.OnGoogleContactsFetched() {
                    @Override
                    public void onFetchSuccess(List<GoogleContactsAndEmail.ContactsGoogle> contactsList) {
                        obtainUserProfileInfo(true, true, shareIntent, text);
                    }

                    @Override
                    public void onFetchFailure(String message) {
                        obtainUserProfileInfo(true, true, shareIntent, text);
                    }
                });
            }
        } else {
            obtainUserProfileInfo(false, false, shareIntent, text);
        }
    }

    private void emailWatch(String senderName, String visitUrl) {
        googleContactsAndEmail.setOnEmailSendListener(new GoogleContactsAndEmail.OnEmailsSent() {
            @Override
            public void onSendSuccess() {
                Log.d("EmailSend", "Success");
                Toast.makeText(DashBoardActivity.this, "Emails Sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendFailure() {
                Log.d("EmailSend", "Failure");
                Toast.makeText(DashBoardActivity.this, "Could not send emails", Toast.LENGTH_SHORT).show();
            }
        });
        googleContactsAndEmail.sendEmail(senderName, visitUrl);
    }

    private void removeGoogleContactsFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GoogleContactsAndEmail googleContactsAndEmailFragment = (GoogleContactsAndEmail) fragmentManager.findFragmentByTag(Constants.GOOGLE_CONTACTS_FRAGMENT);
        if (googleContactsAndEmailFragment != null && googleContactsAndEmailFragment.isAdded()) {
            fragmentManager.beginTransaction().remove(googleContactsAndEmailFragment).commitNow();
        }
    }

    private void startEmailIntent(Intent shareIntent, String text, String userName) {
        String appName = getString(R.string.app_name);
//        String appLink = getString(R.string.email_share_link);
        String emailContent = "Hi,\n\n" + userName + " would like to share you a very useful Online Payment system. Its name is " + appName + "." +
//                " Please visit the site: \n" + text +
                "\n\nYou can easily buy services online, transfer balances, top up mobile etc from any part of the world. Whether you are within the country or out bound, you can easily pay bill online in time and without any lengthy processes. Not just mobiles, you can also settle your Cable Bills, Landline Bills, Utility Bills and other several service providers. " +
                appName + " has partnership with several merchants and user can easily buy services online.\n\n" + appName +
                " is an online pay service introduced to make life much more easier and simpler. So now you can just sit back, relax and pay for services or easy recharge mobile in just a click for your mouse. To register please click " +
                text + "\n\n\n\n —Sincerely,\n   " + appName + " Team\n   " + getString(R.string.domain);
        shareIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
        startActivity(shareIntent);
    }

    private void startOtherIntent(Intent shareIntent, String text, String userName) {
        String content = "Hi," + userName + " would like to share you a very useful Online Payment system.\n" + text;
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(shareIntent);
    }

    public void obtainUserProfileInfo(boolean isEmail, boolean isGmail, Intent shareIntent, String text) {
        Map<String, String> params = new HashMap<>();
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "getProfileUserInfo");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                String userName = "A Current User";
                try {
                    userName = jsonObject.getJSONObject("data").getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isEmail && isGmail) {
                    emailWatch(userName, text);
                } else if (isEmail) {
                    startEmailIntent(shareIntent, text, userName);
                } else {
                    startOtherIntent(shareIntent, text, userName);
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Profile User:", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void showConfirmLogOutForm() {
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("OK", null)
                .setNegativeButton("CANCEL", null)
                .setMessage("Are you sure to sign out?")
                .create();

        builder.setTitle("Confirmation");

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                final Button btnAccept = builder.getButton(
                        AlertDialog.BUTTON_POSITIVE);

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                        myUserSessionManager.logoutUser();
                    }
                });

                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);

                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.dismiss();

                    }
                });
            }
        });

        /* Show the dialog */
        builder.show();
    }


    public void selectPageToDisplay(int id, Boolean saveFragmentBackstack, String backPageName) {

        // fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(id) + "tag");
//        fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(id)+"tag");
//        Log.i("fragment",""+fragment);

//        if(fragment!=null){

        switch (id) {

            case R.id.signOut:
                showConfirmLogOutForm();
                closeDrawer();
                break;
            case R.id.shareandEarn:

                DashBoardActivity.this.openOptionsMenu();

                toolbar.showOverflowMenu();
                closeDrawer();
                break;

            case R.id.settingTxt:
                resetSelectedPageDrawable(settingLink);
                fragment = new SettingFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            /*case R.id.nibl:
                fragment = new NetBankingFragment();
                setCurrentFragment(saveFragmentBackstack,id,backPageName);
                break;*/
            case R.id.sendMoneyDetails:
                resetSelectedPageDrawable(sendMoneyDetails);
                fragment = new SendMoneyDetailsFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                showHide = false;
                addMoney.setVisibility(View.GONE);
                sendMoneyDetails.setVisibility(View.GONE);
                wallet.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wallet, 0, R.drawable.ic_arrow_down, 0);
                closeDrawer();
                break;
            case R.id.addMoney:
                resetSelectedPageDrawable(addMoney);
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("openAddForm", true);
                fragment = new WalletFragment();
                fragment.setArguments(bundle1);
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                showHide = false;
                addMoney.setVisibility(View.GONE);
                sendMoneyDetails.setVisibility(View.GONE);
                wallet.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wallet, 0, R.drawable.ic_arrow_down, 0);
                closeDrawer();
                break;
            case R.id.purchaseReport:
                fragment = new RechargedDetailsFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.simDetails:
                resetSelectedPageDrawable(simDetails);
                fragment = new SimDetailsFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;

            case R.id.fabServices:
                fragment = new FavouriteServicesFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.favlist_txt:
                fragment = new FavouriteServicesFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.userNotification:
                fragment = new UserNotificationFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;


            case R.id.aboutUsShort:
//                resetSelectedPageDrawable(aboutUsShort);
                fragment = new AboutUsFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.contactUsShort:
//                resetSelectedPageDrawable(contactUsShort);
                if (getPackageName().equals("com.paybyonline")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PayByOnlineConfig.CONTACT_URL));
                    startActivity(browserIntent);
                } else if (getPackageName().equals("com.paybyonline.necpay")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://128.199.242.18:8080/frontPage/defaultPage?domainUrl=14"));
                    startActivity(browserIntent);
                } else {
                    resetSelectedPageDrawable(contactUs);
                    fragment = new ContactUsFragment();
                    setCurrentFragment(saveFragmentBackstack, id, backPageName);
                    closeDrawer();
                }
                break;
            case R.id.Payments:
                resetSelectedPageDrawable(payments);

                fragment = new DashboardSlidingFragment();
                //fragment = new GridDashboardFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.myProfile:
                resetSelectedPageDrawable(myProfile);
                fragment = new ProfileFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.aboutUs:
                resetSelectedPageDrawable(aboutUs);
                fragment = new AboutUsFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.dashboard:
                resetSelectedPageDrawable(dashboard);
                //fragment = new DashboardSlidingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userBalance", userBalance.getText().toString());
                if (getPackageName().contains("ebiz")) {
                    fragment = new DashboardMobaletFragment();
                } else {
                    fragment = new com.paybyonline.ebiz.Fragment.GridDashboardFragment();
                }

//                fragment = new DashboardMobaletFragment();
                fragment.setArguments(bundle);
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.kyc_status:
                fragment = new KycDynamicForm();
                Bundle arggs = new Bundle();
                arggs.putString("status", kyc_status.getText().toString());
                fragment.setArguments(arggs);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                closeDrawer();
                break;

 /*               fragment = new GridDashboardFragment();
                FragmentManager fragmentManager =getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();*/

            case R.id.wallet:
                /*resetSelectedPageDrawable(wallet);
                Bundle bundle1= new Bundle();
                bundle1.putBoolean("openAddForm", false);
                fragment = new WalletFragment();
                fragment.setArguments(bundle1);
                setCurrentFragment(saveFragmentBackstack,id,backPageName);*/
                if (showHide) {
                    showHide = false;
                    addMoney.setVisibility(View.GONE);
                    sendMoneyDetails.setVisibility(View.GONE);
                    wallet.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wallet, 0, R.drawable.ic_arrow_down, 0);
                } else {
                    showHide = true;
                    addMoney.setVisibility(View.VISIBLE);
                    sendMoneyDetails.setVisibility(View.VISIBLE);
                    wallet.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wallet, 0, R.drawable.ic_arrow_up, 0);
                }
                break;

            case R.id.report:
                /*resetSelectedPageDrawable(wallet);
                Bundle bundle1= new Bundle();
                bundle1.putBoolean("openAddForm", false);
                fragment = new WalletFragment();
                fragment.setArguments(bundle1);
                setCurrentFragment(saveFragmentBackstack,id,backPageName);*/
                if (showHide1) {
                    showHide1 = false;
                    transactionalList.setVisibility(View.GONE);
                    summaryReport.setVisibility(View.GONE);
                    report.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.report, 0, R.drawable.ic_arrow_down, 0);
                } else {
                    showHide1 = true;
                    transactionalList.setVisibility(View.VISIBLE);
                    summaryReport.setVisibility(View.VISIBLE);
                    report.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.report, 0, R.drawable.ic_arrow_up, 0);
                }
                break;


           /* case R.id.recharge:

                fragment = new RechargePageFragment();
                setCurrentFragment(saveFragmentBackstack,id);
                break;*/

            case R.id.buyPage:
                resetSelectedPageDrawable(buyPage);
//                fragment = new BuyProductFragment();
                fragment = new RechargeBuyFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.transactionalList:
                resetSelectedPageDrawable(buyPage);
//                fragment = new BuyProductFragment();
                showHide1 = false;
                report.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.report, 0, R.drawable.ic_arrow_down, 0);
                transactionalList.setVisibility(View.GONE);
                summaryReport.setVisibility(View.GONE);
                fragment = new TransactionReportFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.summaryReport:
                resetSelectedPageDrawable(buyPage);
                showHide1 = false;
                report.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.report, 0, R.drawable.ic_arrow_down, 0);
                transactionalList.setVisibility(View.GONE);
                summaryReport.setVisibility(View.GONE);
//                fragment = new BuyProductFragment();
                fragment = new DashboardSlidingFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            case R.id.simSales:
                resetSelectedPageDrawable(simSales);
                fragment = new CustomerSimSalesFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;

           /* case R.id.growNetwork:

                fragment = new GrowNetworkFragment();
                closeDrawer();
                setCurrentFragment(saveFragmentBackstack,id);
                break;*/

/*            case R.id.report:
                resetSelectedPageDrawable(report);
                fragment = new TransactionReportFragment();
//                fragment = new ReportFragment();
                setCurrentFragment(saveFragmentBackstack,id,backPageName);
                closeDrawer();
                break;*/

            case R.id.contactUs:
                if (getPackageName().equals("com.paybyonline")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PayByOnlineConfig.CONTACT_URL));
                    startActivity(browserIntent);
                } else {
                    resetSelectedPageDrawable(contactUs);
                    fragment = new ContactUsFragment();
                    setCurrentFragment(saveFragmentBackstack, id, backPageName);
                    closeDrawer();
                }

               /* resetSelectedPageDrawable(contactUs);
                fragment = new ContactUsFragment();
                setCurrentFragment(saveFragmentBackstack,id);*/

                break;

            case R.id.notification_txt:
                resetSelectedPageDrawable(notification_txt);
                fragment = new UserNotificationFragment();
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
            default:
/*                resetSelectedPageDrawable(buyPage);
                fragment = new RechargeBuyFragment();
                setCurrentFragment(saveFragmentBackstack,id,backPageName);*/
                resetSelectedPageDrawable(dashboard);
                Bundle bundle2 = new Bundle();
                bundle2.putString("userBalance", myUserSessionManager.getKeyUserAmt());
                if (getPackageName().contains("ebiz")) {
                    fragment = new DashboardMobaletFragment();
                } else {
                    fragment = new com.paybyonline.ebiz.Fragment.GridDashboardFragment();
                }
                fragment.setArguments(bundle2);
                setCurrentFragment(saveFragmentBackstack, id, backPageName);
                closeDrawer();
                break;
        }


      /*  }else{

            fragment = new DashboardSlidingFragment();
            Toast.makeText(getApplicationContext(),"DashboardSlide Clicked", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }*/

    }


    View currentSelectedPage;

    public void resetSelectedPageDrawable(View selectedView) {
        if (currentSelectedPage != null) {
            currentSelectedPage.setBackground(ContextCompat.getDrawable(DashBoardActivity.this, R.drawable.nav_menu_normal_state));
        }
        currentSelectedPage = selectedView;
        currentSelectedPage.setBackground(ContextCompat.getDrawable(DashBoardActivity.this, R.drawable.nav_menu_selected_item));
    }


    public void setCurrentFragment(Boolean saveFragmentBackstack, int id, String backPageName) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (saveFragmentBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        if (backPageName.length() > 0) {
            Bundle args = new Bundle();
            args.putString("returnPage", backPageName);
            fragment.setArguments(args);
        }

        fragmentTransaction.replace(R.id.content_frame, fragment, String.valueOf(id) + "tag");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.

       /* if (item.getItemId() == R.id.logout) {

            showLogoutConfirmation();

        } else {
            selectPageToDisplay(item.getItemId(), true);

            // Highlight the selected item, update the title, and close the drawer
            item.setChecked(true);
            setTitle(item.getTitle());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }*/

        return false;
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onClick(View view) {
        int selectedId = view.getId();
        selectPageToDisplay(selectedId, true, "");


    }

    public void closeDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
    /*    Toast.makeText(getApplicationContext(), "reqCode : "+fileName,
                Toast.LENGTH_LONG).show();*/

        switch (reqCode) {

            case REQUEST_CODE_PICK_CONTACTS:

                if (data != null) {
                    Uri result = data.getData();

                    Cursor c = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone._ID + "=?",
                            new String[]{result.getLastPathSegment()}, null);

                    if (c.getCount() >= 1 && c.moveToFirst()) {

                        final String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        final String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    }
                }
                break;

        }
    }

    public void HttpAsyncTaskUserBalance() {

//        RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<>();
        Log.i("HttpAsyncTaskUserBal", "dashboard");
        params.put("parentTask", "rechargeApp");
        params.put("childTask", "userBalances");
        params.put("userCode", myUserSessionManager.getSecurityCode());
        params.put("authenticationCode", myUserSessionManager.getAuthenticationCode());
        //   params.put("authenticationCode", userSessionManager.getAuthenticationCode());

//        PboServerRequestHandler handler = PboServerRequestHandler.getInstance(coordinatorLayout,
//                DashBoardActivity.this);
//        handler.makeRequest(PayByOnlineConfig.SERVER_ACTION, "", params,
//                new PboServerRequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                try {
//                    Log.i("PboServerRequestHandler", "dashboard");
//                    httpAsyncTaskUserBalanceResponse(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(getApplicationContext(), "JSONObject "+response+"",Toast.LENGTH_LONG).show();
//            }
//        });
        retrofitHelper.setOnResponseListener(new RetrofitHelper.OnRetrofitResponse() {
            @Override
            public void onRetrofitSuccess(JSONObject jsonObject, int apiCode) {
                try {
                    Log.i("PboServerRequestHandler", "dashboard");
                    httpAsyncTaskUserBalanceResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrofitFailure(String errorMessage, int apiCode) {
                Log.i("Dashboard", "error");
            }
        });
        retrofitHelper.sendRequest(ApiIndex.GET_ON_AUTH_APP_USER_ENDPOINT, params, null, null);
    }

    public void httpAsyncTaskUserBalanceResponse(JSONObject response) throws JSONException {

        JSONObject json = new JSONObject();
        ;
        String connectionStatus;
        String currency;
        String currentBalance;
        // String userOverviews;
        try {


            json = response;
            // connectionStatus = json.getString("connectionStatus");
            Log.i("UserBalanceResponse", "" + response);

            currency = json.getString("currency");
            currentBalance = json.getString("currentBalance");
            userBalance.setText(Html.fromHtml(currency + " "
                    + currentBalance));

            userHoldManeyBalanceStatus.setVisibility(View.GONE);
            String numberOnly[] = currency.split("-");

            if ((Double.parseDouble(currentBalance)) < 0) {

                userHoldManeyBalanceStatus.setVisibility(View.VISIBLE);
                userHoldManeyBalanceStatus.setText("On Credit");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JSONObject res = new JSONObject();
            try {
                res.put("connectionStatus", "failed");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }


    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
//        return result == PackageManager.PERMISSION_GRANTED;
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startYourCameraIntent();
                    getContacts();

                } else {
                    Toast.makeText(this, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.v("contact"+name,"==>"+phoneNumber);
            contactList.add(new ContactsModel(name,phoneNumber));
        }
        phones.close();
    }

    public List<ContactsModel> getContactsList() {
        List<ContactsModel> contactsModels= new ArrayList<>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.v("contact"+name,"==>"+phoneNumber);
            contactsModels.add(new ContactsModel(name,phoneNumber));
        }
        phones.close();
        return  contactsModels;
    }
    public void getBothContacts(ArrayList<ContactsModel> ncell,ArrayList<ContactsModel> ntc){
        List<ContactsModel> allContact = getContactsList();
        for (ContactsModel contactsModel : allContact){
            if (contactsModel.getContactPhone().startsWith("981")|contactsModel.getContactPhone().startsWith("980")){
                ncell.add(contactsModel);
            }else if(contactsModel.getContactPhone().startsWith("984")|contactsModel.getContactPhone().startsWith("985")){
                ntc.add(contactsModel);
            }
        }
    }
}


