package com.app.leon.abfa.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.CompanyNames;
import com.app.leon.abfa.Models.Enums.ErrorHandlerType;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.LoginInfo;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.ConnectingManager;
import com.app.leon.abfa.Utils.Crypto;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class LoginActivity extends AppCompatActivity {
    final int ALLOWED_FAILURE_COUNT = 3;
    @BindView(R.id.editTextUsername)
    EditText editTextUsername;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.activity_admin_forget_password)
    RelativeLayout relativeLayout;
    @BindView(R.id.linearLayoutUsername)
    LinearLayout linearLayoutUsername;
    @BindView(R.id.linearLayoutPassword)
    LinearLayout linearLayoutPassword;
    @BindView(R.id.imageViewPerson)
    ImageView imageViewPerson;
    @BindView(R.id.imageViewLogo)
    ImageView imageViewLogo;
    @BindView(R.id.imageViewUsername)
    ImageView imageViewUsername;
    @BindView(R.id.imageViewPassword)
    ImageView imageViewPassword;
    private ISharedPreferenceManager sharedPreferenceManager;
    private String username, password, deviceId;
    private ConnectingManager connectingManager;
    private View viewFocus;
    private Context context;
    private int failureCount = 0;
    private FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void setEditTextUsernameChangedListener() {
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextUsername.setHint("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setEditTextPasswordChangedListener() {
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextPassword.setHint("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setEditTextUsernameOnFocusChangeListener() {
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editTextUsername.setHint("");
                if (b) {
                    linearLayoutUsername.setBackground(getResources().getDrawable(R.drawable.border__));
                    editTextPassword.setTextColor(getResources().getColor(R.color.black));
                } else {
                    linearLayoutUsername.setBackground(getResources().getDrawable(R.drawable.border_));
                    editTextPassword.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }

    private void setEditTextPasswordOnFocusChangeListener() {
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editTextPassword.setHint("");
                if (b) {
                    linearLayoutPassword.setBackground(getResources().getDrawable(R.drawable.border__));
                    editTextPassword.setTextColor(getResources().getColor(R.color.black));
                } else {
                    linearLayoutPassword.setBackground(getResources().getDrawable(R.drawable.border_));
                    editTextPassword.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }

    private void setImageViewOnClickListener() {
        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassword.getInputType() != InputType.TYPE_CLASS_NUMBER)
                    editTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                else
                    editTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                fontManager.setFont(relativeLayout);
            }
        });
    }

    void initialize() {
        context = this;
        connectingManager = new ConnectingManager(getApplicationContext());
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
        imageViewPassword.setImageResource(R.drawable.img_password);
        imageViewLogo.setImageResource(R.drawable.img_bg_logo);
        imageViewPerson.setImageResource(R.drawable.img_profile);
        imageViewUsername.setImageResource(R.drawable.img_user);
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        setEditTextUsernameChangedListener();
        setEditTextPasswordChangedListener();
        setEditTextUsernameOnFocusChangeListener();
        setEditTextPasswordOnFocusChangeListener();
        setImageViewOnClickListener();
        setButtonOnClickListener();
        setButtonOnLongClickListener();
    }

    private void setButtonOnClickListener() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                if (username.length() < 1) {
                    viewFocus = editTextUsername;
                    viewFocus.requestFocus();
                    editTextUsername.setError(getString(R.string.error_empty));
                    cancel = true;
                }
                if (!cancel && password.length() < 1) {
                    viewFocus = editTextPassword;
                    viewFocus.requestFocus();
                    editTextPassword.setError(getString(R.string.error_empty));
                    cancel = true;
                }
                if (!cancel) {
                    username = DifferentCompanyManager.getPrefixName(CompanyNames.TSW) + username;
                    attemptLogin();
                    ++failureCount;
                    if (canILoginOffline()) {
                        if (checkLastCredential(username, password)) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_is_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void setButtonOnLongClickListener() {
        buttonLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                boolean cancel = false;
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                if (username.length() < 1) {
                    viewFocus = editTextUsername;
                    viewFocus.requestFocus();
                    editTextUsername.setError(getString(R.string.error_empty));
                    cancel = true;
                }
                if (!cancel && password.length() < 1) {
                    viewFocus = editTextPassword;
                    viewFocus.requestFocus();
                    editTextPassword.setError(getString(R.string.error_empty));
                    cancel = true;
                }
                if (!cancel) {
                    username = DifferentCompanyManager.getPrefixName(CompanyNames.TSW) + username;
                    attemptSerial();
                }
                return false;
            }
        });
    }

    void attemptSerial() {
        deviceId = Build.SERIAL;
        Retrofit retrofit = NetworkHelper.getInstance(true, "");
        final IAbfaService serial = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = serial.signSerial(new LoginInfo(deviceId, username, password));
        SignSerialFeedBack signSerialFeedBack = new SignSerialFeedBack();
        HttpClientWrapper.callHttpAsync(call, signSerialFeedBack, this, ProgressType.SHOW.getValue(), ErrorHandlerType.login);
    }

    void attemptLogin() {
        deviceId = Build.SERIAL;
        Retrofit retrofit;
        if (DifferentCompanyManager.getActiveCompanyName() == CompanyNames.DEBUG) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            String baseUrl = "http://81.90.148.25/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(NetworkHelper.getHttpClient(""))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } else {
            retrofit = NetworkHelper.getInstance(true, "");
        }
        final IAbfaService loginInfo = retrofit.create(IAbfaService.class);
        Call<com.app.leon.abfa.Models.InterCommunation.LoginFeedBack> call = loginInfo.login(new LoginInfo(deviceId, username, password));
        LoginFeedBack loginFeedBack = new LoginFeedBack();
        HttpClientWrapper.callHttpAsync(call, loginFeedBack, this, ProgressType.SHOW.getValue(), ErrorHandlerType.login);
    }

    private boolean canILoginOffline() {
        String tokenKey = SharedReferenceKeys.TOKEN.getValue();
        String token = sharedPreferenceManager.get(tokenKey);
        if (!connectingManager.checkMobileDataIsOn()) {
            return false;
        }
        if (failureCount <= ALLOWED_FAILURE_COUNT) {
            return false;
        }
        if (token.length() < 20) {
            return false;
        }
        return sharedPreferenceManager.get(SharedReferenceKeys.REFRESH_TOKEN.getValue()).toString().length() >= 20;
    }

    private boolean checkLastCredential(String enteredUsername, String enteredPassword) {
        String username = sharedPreferenceManager.get(SharedReferenceKeys.USERNAME.getValue());
        String password = Crypto.decrypt(sharedPreferenceManager.get(SharedReferenceKeys.PASSWORD.getValue()).toString());
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        imageViewPerson.setImageDrawable(null);
        imageViewPassword.setImageDrawable(null);
        imageViewLogo.setImageDrawable(null);
        imageViewUsername.setImageDrawable(null);
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        System.gc();
        Runtime.getRuntime().gc();
    }

    class SignSerialFeedBack
            implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            Toast.makeText(context, simpleMessage.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class LoginFeedBack
            implements ICallback<com.app.leon.abfa.Models.InterCommunation.LoginFeedBack> {
        @Override
        public void execute(com.app.leon.abfa.Models.InterCommunation.LoginFeedBack loginFeedBack) {
            if (loginFeedBack.getAccess_token() == null ||
                    loginFeedBack.getRefresh_token() == null ||
                    loginFeedBack.getAccess_token().length() < 1 ||
                    loginFeedBack.getRefresh_token().length() < 1) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_is_not_match), Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferenceManager.put(SharedReferenceKeys.TOKEN.getValue(), loginFeedBack.getAccess_token());
                sharedPreferenceManager.put(SharedReferenceKeys.USERNAME.getValue(), username);
                sharedPreferenceManager.put(SharedReferenceKeys.PASSWORD.getValue(), Crypto.encrypt(password));
                sharedPreferenceManager.put(SharedReferenceKeys.REFRESH_TOKEN.getValue(), loginFeedBack.getRefresh_token());
//                sharedPreferenceManager.apply();
//                sharedPreferenceManager.apply();
//                sharedPreferenceManager.apply();
//                sharedPreferenceManager.apply();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
