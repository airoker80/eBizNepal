package com.paybyonline.ebiz.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.paybyonline.ebiz.R;
import com.paybyonline.ebiz.configuration.PayByOnlineConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Swatin on 12/8/2017.
 */

public class GoogleContactsAndEmail extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private final int RC_INTENT = 150;
    private String TAG = "GoogleContactsAndEmail";
    private ProgressDialog progress;
    private OnGoogleContactsFetched onGoogleContactsFetched;
    private boolean isResultGot = false;
    private boolean isManualCall = false;
    private AppCompatActivity activity;
    private List<ContactsGoogle> googleContactsList;
    private OnEmailsSent onEmailSendListener;
    private String serverAuthCode;

    public interface OnGoogleContactsFetched {

        void onFetchSuccess(List<ContactsGoogle> contactsList);

        void onFetchFailure(String message);
    }

    public interface OnEmailsSent {

        void onSendSuccess();

        void onSendFailure();
    }

    public void setOnGoogleContactsFetchedListener(OnGoogleContactsFetched onGoogleContactsFetchedListener) {
        this.onGoogleContactsFetched = onGoogleContactsFetchedListener;
    }

    public void setOnEmailSendListener(OnEmailsSent onEmailSendListener) {
        this.onEmailSendListener = onEmailSendListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progress = new ProgressDialog(context);
        showProgressDialog("Fetching Contacts...");
        activity = (AppCompatActivity) context;
        buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.clientId))
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"),
                        new Scope("https://www.googleapis.com/auth/user.emails.read"),
                        new Scope("https://www.googleapis.com/auth/user.phonenumbers.read"),
                        new Scope("https://www.googleapis.com/auth/gmail.compose"))

                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
        isManualCall = true;
        mGoogleApiClient.connect();
    }

    public void start() {
        if (mGoogleApiClient.isConnected()) {
            if (isGooglePlayServicesAvailable()) {
                isResultGot = false;
                startIntent();
            } else {
                Toast.makeText(getActivity(), "No play services", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    private void startIntent() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_INTENT);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), status, 0).show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case RC_INTENT:
                if (!isResultGot) {
                    isResultGot = true;
                    Log.d(TAG, "sign in result");
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                    if (result.isSuccess()) {
                        GoogleSignInAccount acct = result.getSignInAccount();
                        Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
                        if (acct != null) {
                            serverAuthCode = acct.getServerAuthCode();
                            Log.d(TAG, "auth Code:" + serverAuthCode);
                            new PeoplesAsync().execute(serverAuthCode);
                        }
                    } else {
                        Log.d(TAG, result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
                        hideProgressDialog();
                        Toast.makeText(getActivity(), "Could not fetch contacts", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isResultGot = false;
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        boolean isGooglePlayAvailable = isGooglePlayServicesAvailable();
        if (isManualCall && isGooglePlayAvailable) {
            isManualCall = false;
            startIntent();
        } else if (!isGooglePlayAvailable) {
            Toast.makeText(getActivity(), "No play services", Toast.LENGTH_SHORT).show();
            hideProgressDialog();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @SuppressLint("StaticFieldLeak")
    class PeoplesAsync extends AsyncTask<String, Void, List<ContactsGoogle>> {

        private String TAG = "Google Contacts";

        @Override
        protected List<ContactsGoogle> doInBackground(String... params) {

            List<ContactsGoogle> contactsGoogleList = new ArrayList<>();

            try {
                Log.d(TAG, params[0]);

                PeopleService peopleService = GoogleClientApiHelper.setUpPeople(params[0], getString(R.string.clientId), getString(R.string.clientSecret), getString(R.string.app_name));

                ListConnectionsResponse response = peopleService.people().connections()
                        .list("people/me")
                        // not setting mask does not default to 'all' (currently), default page size = 100, let this be known. --Swatin
                        .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers")
                        .setPageSize(2000)
                        .execute();

                List<Person> connections = response.getConnections();

                if (connections != null) {
                    for (Person person : connections) {
                        if (!person.isEmpty()) {
                            if (person.getEmailAddresses() != null) {
                                String name = "";
                                if (person.getNames() != null) {
                                    name = person.getNames().get(0).getDisplayName();
                                    Log.d(TAG, name);
                                }
                                String emailAddress = person.getEmailAddresses().get(0).getValue();
                                Log.d(TAG, emailAddress);
                                String phoneNumber = "";
                                if (person.getPhoneNumbers() != null) {
                                    phoneNumber = person.getPhoneNumbers().get(0).getValue();
                                    Log.d(TAG, phoneNumber);
                                }
                                contactsGoogleList.add(new ContactsGoogle(name, emailAddress, phoneNumber));
                            } else if (person.getNames() != null) {
                                Log.d(TAG, person.getNames().get(0).getDisplayName() + " email empty");
                            }
                        }
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contactsGoogleList;
        }


        @Override
        protected void onPostExecute(List<ContactsGoogle> contactsGoogleList) {
            super.onPostExecute(contactsGoogleList);
            if (getActivity() != null) {
                if (contactsGoogleList != null) {
                    googleContactsList = contactsGoogleList;
                    Log.d(TAG, "Got It");
                    onGoogleContactsFetched.onFetchSuccess(contactsGoogleList);
                } else {
                    onGoogleContactsFetched.onFetchFailure("Could not fetch contacts");
                }
                hideProgressDialog();
            }
        }
    }

    public void sendEmail(String senderName, String visitUrl) {
        showProgressDialog("Sending Emails...");
        if (serverAuthCode != null && googleContactsList != null) {
            try {
                new SendMailAsync().execute(new EmailParams(senderName, visitUrl)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    class EmailParams {
        private String senderName;
        private String visitUrl;

        EmailParams(String senderName, String visitUrl) {
            this.senderName = senderName;
            this.visitUrl = visitUrl;
        }

        String getSenderName() {
            return senderName;
        }

        String getVisitUrl() {
            return visitUrl;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SendMailAsync extends AsyncTask<EmailParams, Void, Boolean> {

        @Override
        protected Boolean doInBackground(EmailParams... emailParams1) {
            try {
                EmailParams emailParams = emailParams1[0];
                String senderName = emailParams.getSenderName();
                String visitUrl = emailParams.getVisitUrl();
                /*since accesstoken is set when requesting people api, if you are shifting this code to
                your project, make sure you look at people api accesstoken code*/
                Gmail gmailService = GoogleClientApiHelper.setUpMail(getString(R.string.app_name));
                BatchRequest batchRequest = gmailService.batch();
                if (googleContactsList != null && googleContactsList.size() > 0) {
                    for (ContactsGoogle googleContacts : googleContactsList) {
                        constructEmail(gmailService, batchRequest, googleContacts.getContactEmail(), googleContacts.getContactName(),
                                senderName, visitUrl);
                    }
                    batchRequest.execute();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (getActivity() != null) {
                hideProgressDialog();
                if (isSuccess) {
                    onEmailSendListener.onSendSuccess();
                } else {
                    onEmailSendListener.onSendFailure();
                }
            }
        }
    }

    private void constructEmail(Gmail gmailService, BatchRequest batchRequest, String emailTo, String recipientName,
                                String senderName, String visitUrl) throws IOException {
        String subject = "Mobalet Invitation";
        String appName = getString(R.string.app_name);
        if (recipientName == null || recipientName.isEmpty()) {
            String[] email = emailTo.split("@");
            recipientName = email[0];
        }
        String content = "";
        content += "<html><body>";
        content += "Dear " + recipientName + ",<br><br>";
        content +=
                senderName + " would like to share with you a very useful Online Payment system. It's name is <b>" + appName + "</b>.<br>" +
//                        "Please visit the site: \n" + visitUrl +
                        "<br>You can easily buy services online, transfer balances, top up mobile etc from any part of the world. " +
                        "Whether you are within the country or out bound, you can easily pay bill online in time and without any" +
                        " lengthy processes. Not just mobiles, you can also settle your Cable Bills, Landline Bills, " +
                        "Utility Bills and other several service providers. " + "<br><b>" +
                        appName + "</b> has partnership with several merchants and user can easily buy services online.<br><br><b>" +
                        appName + "</b> is an online pay service introduced to make life much more easier and simpler. " +
                        "So now you can just sit back, relax and pay for services or easy recharge mobile in just a click for " +
                        "your mouse. To register please click" + "<br>" + "<a href=" +
                        visitUrl + "> Visit Us</a>" + "<br><br><br><br>Sincerely,<br>" + appName + " Team<br>" + getString(R.string.domain);
        content += "</body></html>";
        try {
            MimeMessage emailMime = createMimeMessage(emailTo, subject, content);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (emailMime != null) {
                emailMime.writeTo(baos);
                String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
                Message message = new Message();
                message.setRaw(encodedEmail);
                gmailService.users().messages().send("me", message).queue(batchRequest, new JsonBatchCallback<Message>() {
                    @Override
                    public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                        Log.d(TAG, "failed to add to batch");
                    }

                    @Override
                    public void onSuccess(Message message, HttpHeaders responseHeaders) throws IOException {
                        Log.d(TAG, "added to batch");
                    }
                });
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private MimeMessage createMimeMessage(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("me"));
            message.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(body, "text/html");
            return message;
        } catch (MessagingException ex) {
            Logger.getLogger(GoogleContactsAndEmail.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void showProgressDialog(String message) {
        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "showProgressDialog");
        if (!progress.isShowing()) {
            SpannableString titleMsg = new SpannableString("Processing");
            titleMsg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleMsg.length(), 0);
            progress.setTitle(titleMsg);
            progress.setMessage(message);
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }
    }

    public void hideProgressDialog() {
        Log.i(PayByOnlineConfig.PAY_BY_ONLINE_TAG_NAME, "hideProgressDialog");
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    public class ContactsGoogle {

        private String contactName;
        private String contactEmail;
        private String contactPhone;

        ContactsGoogle(String contactName, String contactEmail, String contactPhone) {
            this.contactName = contactName;
            this.contactEmail = contactEmail;
            this.contactPhone = contactPhone;
        }

        public String getContactName() {
            return contactName;
        }

        String getContactEmail() {
            return contactEmail;
        }

        public String getContactPhone() {
            return contactPhone;
        }
    }

}
