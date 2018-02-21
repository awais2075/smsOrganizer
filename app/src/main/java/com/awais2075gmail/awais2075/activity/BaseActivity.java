package com.awais2075gmail.awais2075.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.model.Contact;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Muhammad Awais Rashid on 17-Dec-17.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private boolean check = false;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsEmailSignIn, mIsGmailSignIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        gmailInit();
        emailInit();
    }


    protected abstract int setView();


    private void gmailInit() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    protected void gmailSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN );


    }

    private void emailInit() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        //isEmailSignedIn();
    }

    protected void emailSignIn(String userEmail, String userPassword) {
        Constants.showProgressDialog(this);
        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Constants.hideProgressDialog();
                if (task.isSuccessful()) {
                    Constants.emailLoginCheck = true;
                    //Toast.makeText(BaseActivity.this, "Pass", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BaseActivity.this, ConversationActivity.class));
                } else {
                    //Toast.makeText(BaseActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void emailRegistration(String userEmail, String userPassword) {
        Constants.showProgressDialog(this);
        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Constants.hideProgressDialog();
                if (task.isSuccessful()) {
                    Toast.makeText(BaseActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    Constants.emailLoginCheck = true;
                    mIsEmailSignIn = true;
                    startActivity(new Intent(BaseActivity.this, ConversationActivity.class));
                } else {
                    //Toast.makeText(BaseActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void isEmailSignedIn() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mIsEmailSignIn = true;
            startActivity(new Intent(this, ConversationActivity.class));
            Constants.emailLoginCheck = true;
            mIsEmailSignIn = true;
            Utils.userId = mFirebaseAuth.getCurrentUser().getUid().toString();
            //Toast.makeText(this, "Email Check : "+Constants.emailLoginCheck, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, Utils.userId+" is Email Id", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Email Check : "+Constants.emailLoginCheck, Toast.LENGTH_SHORT).show();
        }
    }

    protected void isGoogleSignedIn() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            //Toast.makeText(this, "OnStart if", Toast.LENGTH_SHORT).show();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            Constants.showProgressDialog(this);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //                  hideProgressDialog();
                    //Toast.makeText(BaseActivity.this, "OnStart else", Toast.LENGTH_SHORT).show();
                    Constants.hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Utils.userId = acct.getId();
            Constants.gmailLoginCheck = true;
            Toast.makeText(this, Utils.userId+" is gmail Id", Toast.LENGTH_SHORT).show();
            //Utils.userId = userId;
            //Toast.makeText(this, " if handle signInResult", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConversationActivity.class));
        } else {
            //isEmailSignedIn();
            // Signed out, show unauthenticated UI.
            //Toast.makeText(this, " else handle signInResult", Toast.LENGTH_SHORT).show();

        }
    }

    protected void emailSignOut() {
        mFirebaseAuth.signOut();
        Constants.emailLoginCheck = false;
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            Toast.makeText(this, "OnActivityResult", Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void gmailSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(BaseActivity.this, "SignOut", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings Action", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

                //showDialogBox();
                break;
            case R.id.action_logout:
                if (Constants.gmailLoginCheck) {
                    gmailSignOut();
                }else if (Constants.emailLoginCheck) {
                    emailSignOut();
                }
                break;
        }
        return true;
    }
}