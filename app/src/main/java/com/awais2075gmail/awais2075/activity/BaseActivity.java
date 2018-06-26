package com.awais2075gmail.awais2075.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.awais2075gmail.awais2075.R;
import com.awais2075gmail.awais2075.model.SilentKiller;
import com.awais2075gmail.awais2075.util.Constants;
import com.awais2075gmail.awais2075.util.FastSave;
import com.awais2075gmail.awais2075.util.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



/**
 * Created by Muhammad Awais Rashid on 17-Dec-17.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CompoundButton.OnCheckedChangeListener {

    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsEmailSignIn, mIsGmailSignIn = false;
    protected MaterialDialog materialDialog;


    private String evacuationCode = "";
    private boolean isEnabled = true;


    private EditText passwordInput;
    private View positiveAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());

        gmailInit();
        emailInit();
        setUpMaterialDialog(false);
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
        materialDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);


    }

    private void emailInit() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        //isEmailSignedIn();
    }

    protected void emailSignIn(String userEmail, String userPassword) {
        //Constants.showProgressDialog(this);
        materialDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                materialDialog.hide();
                if (task.isSuccessful()) {
                    Constants.emailLoginCheck = true;
                    Toast.makeText(BaseActivity.this, "Pass", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BaseActivity.this, ConversationActivity.class));
                } else {
                    Toast.makeText(BaseActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void emailRegistration(String userEmail, String userPassword) {
        materialDialog.show();
        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Constants.hideProgressDialog();
                if (task.isSuccessful()) {
                    materialDialog.hide();
                    Toast.makeText(BaseActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    Constants.emailLoginCheck = true;
                    mIsEmailSignIn = true;
                    startActivity(new Intent(BaseActivity.this, ConversationActivity.class));
                } else {
                    materialDialog.hide();
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
            Toast.makeText(this, Utils.userId + " is Email Id", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, Utils.userId + " is gmail Id", Toast.LENGTH_SHORT).show();
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
            materialDialog.hide();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_silent:
                showCustomView();
                //showSilentKillerDialog();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings Action", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AutoResponseActivity.class));
                //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

                //showDialogBox();
                break;
            case R.id.action_logout:
                if (Constants.gmailLoginCheck) {
                    gmailSignOut();
                } else if (Constants.emailLoginCheck) {
                    emailSignOut();
                }
                break;
        }
        return true;
    }

    private void setUpMaterialDialog(boolean isHorizontal) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .buttonRippleColor(getResources().getColor(R.color.colorPrimary))
                .cancelable(false)
                .content(R.string.loading_content)
                .progress(true, 0)
                .progressIndeterminateStyle(isHorizontal)
                .build();

    }

    public void showSilentKillerDialog() {

        if (FastSave.getInstance().isKeyExists("silentKiller")) {
            SilentKiller silentKiller = FastSave.getInstance().getObject("silentKiller", SilentKiller.class);
            evacuationCode = silentKiller.getEvacuationCode();
            isEnabled = silentKiller.getIsEnabled();
        }
        int resourceId = this.getResources().
                getIdentifier(evacuationCode, "string", this.getPackageName());

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.silent_killer_content)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(5, 8)
                .positiveText(R.string.submit)
                .input(
                        R.string.empty_string,
                        R.string.empty_string,
                        isEnabled,
                        (dialog, input) -> {
                            evacuationCode = input.toString();
                            if (evacuationCode.length() == 0 && isEnabled) {
                                Toast.makeText(this, "Can't be empty", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, input + "", Toast.LENGTH_SHORT).show();
                                FastSave.getInstance().saveObject("silentKiller", new SilentKiller(evacuationCode, isEnabled));
                            }
                        }
                )
                .checkBoxPromptRes(R.string.silent_promt, isEnabled, (compoundButton, b) -> {
                    Toast.makeText(BaseActivity.this, b + "", Toast.LENGTH_SHORT).show();
                    isEnabled = b;
                })
                .show();
    }


    public void showCustomView() {
        if (FastSave.getInstance().isKeyExists("silentKiller")) {
            SilentKiller silentKiller = FastSave.getInstance().getObject("silentKiller", SilentKiller.class);
            evacuationCode = silentKiller.getEvacuationCode();
            isEnabled = silentKiller.getIsEnabled();
        }
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.agree)
                .negativeText(android.R.string.cancel)
                .onPositive(
                        (dialog1, which) -> {
                            evacuationCode = passwordInput.getText().toString();
                            FastSave.getInstance().saveObject("silentKiller", new SilentKiller(evacuationCode, isEnabled));
                        })
                .build();

        positiveAction = materialDialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        passwordInput = materialDialog.getCustomView().findViewById(R.id.password);
        passwordInput.setText(evacuationCode);
        passwordInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        // Toggling the show password CheckBox will mask or unmask the password input EditText
        CheckBox checkboxPassword = materialDialog.getCustomView().findViewById(R.id.checkbox_showPassword);
        CheckBox checkboxSilent = materialDialog.getCustomView().findViewById(R.id.checkbox_silent);
        checkboxSilent.setChecked(isEnabled);
        checkboxPassword.setOnCheckedChangeListener(this);
        checkboxSilent.setOnCheckedChangeListener(this);
        /*checkboxPassword.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    passwordInput.setInputType(
                            !isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                    passwordInput.setTransformationMethod(
                            !isChecked ? PasswordTransformationMethod.getInstance() : null);
                });
*/
        int widgetColor = ThemeSingleton.get().widgetColor;
        MDTintHelper.setTint(
                checkboxPassword, widgetColor == 0 ? ContextCompat.getColor(this, R.color.colorAccent) : widgetColor);

        MDTintHelper.setTint(
                passwordInput,
                widgetColor == 0 ? ContextCompat.getColor(this, R.color.colorAccent) : widgetColor);

        materialDialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.checkbox_showPassword:
                passwordInput.setInputType(
                        !b ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                passwordInput.setTransformationMethod(
                        !b ? PasswordTransformationMethod.getInstance() : null);
                Toast.makeText(this, b + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkbox_silent:
                isEnabled = b;
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}