package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener{
    Button loginFacebook;
    Button loginGoogle;
    TextView txtRegister, txtForgetPass;
    EditText edtemail, edtpass;
    GoogleApiClient apiClient;
    Button btnLogin;
    public static int REQUESTCODE_DANGNHAP_GOOGLE = 99;
    public static int ktra = 0;
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    LoginManager loginManager;
    List<String> permissionNeeds= Arrays.asList("public_profile", "email");
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //Tại firebaseAut
        firebaseAuth = FirebaseAuth.getInstance();
        loginManager = LoginManager.getInstance();

        progressDialog =  new ProgressDialog(this);

        callbackManager = CallbackManager.Factory.create();
        AnhXa();
        firebaseAuth.signOut();

        loginGoogle.setOnClickListener(this);
        loginFacebook.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtForgetPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("luudangnhap", MODE_PRIVATE);

    }

    void AnhXa(){
        loginGoogle=(Button) findViewById(R.id.login_google);
        loginFacebook=(Button)findViewById(R.id.login_button);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        txtForgetPass=(TextView)findViewById(R.id.txtForgetPass);
        txtRegister=(TextView)findViewById(R.id.txtRegister);
        edtemail=(EditText) findViewById(R.id.edtUsername);
        edtpass=(EditText)findViewById(R.id.edtPass);
    }

    private void DangNhapGoogle(GoogleApiClient apiClient){
        ktra = 1;
        Intent iDNGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iDNGoogle, REQUESTCODE_DANGNHAP_GOOGLE);
    }

    private void DangNhapFacebook(){
        //xét permission cho facebook, lấy email và profile
        loginManager.logInWithReadPermissions(this,permissionNeeds);
        //Hiện bản truy cập face
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //ĐN thành công
                ktra=2;
                String tokenID = loginResult.getAccessToken().getToken();
                ChungThucDNFirebase(tokenID);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DangNhap(){
        String email=edtemail.getText().toString();
        String password=edtpass.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng! Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    private void TaoClientLoginGoogle(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //Lấy những thông tin muốn lấy
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Gọi Api
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
    }

    private void ChungThucDNFirebase(String tokenID){
        AuthCredential authCredential;
        if(ktra==1){
            //Mở chứng thực ĐN bằng GG
            authCredential= GoogleAuthProvider.getCredential(tokenID,null);
            firebaseAuth.signInWithCredential(authCredential);
        }else if(ktra==2){
            authCredential= FacebookAuthProvider.getCredential(tokenID);
            firebaseAuth.signInWithCredential(authCredential);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Kiểm tra có phải đang ĐN bằng google ko?
        if(requestCode == REQUESTCODE_DANGNHAP_GOOGLE)
        {
            //ĐN Google được không?
            if(resultCode==RESULT_OK)
            {
                progressDialog.setMessage("Đang xử lý");
                progressDialog.setIndeterminate(true);
                //progressDialog.setIcon(R.drawable.progress_bar_loading);
                progressDialog.show();

                //Lấy thông tin
                GoogleSignInResult signInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                //Lấy thông tin tài khoản
                GoogleSignInAccount account=signInResult.getSignInAccount();
                String tokenID = account.getIdToken();
                ChungThucDNFirebase(tokenID);
            }
        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_google:
                TaoClientLoginGoogle();
                DangNhapGoogle(apiClient);
                break;
            case R.id.login_button:
                DangNhapFacebook();
                break;
            case R.id.txtRegister:
                Intent iRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(iRegister);
                break;
            case R.id.btnLogin:
                DangNhap();
                break;
            case R.id.txtForgetPass:
                Intent iForget =  new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(iForget);
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            progressDialog.dismiss();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mauser", user.getUid());
            editor.commit();

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent iHome=new Intent(LoginActivity.this, Home.class);
            startActivity(iHome);
        }else{
        }
    }

    /*
     private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUESTCODE_DANGNHAP_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUESTCODE_DANGNHAP_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
     */

}
