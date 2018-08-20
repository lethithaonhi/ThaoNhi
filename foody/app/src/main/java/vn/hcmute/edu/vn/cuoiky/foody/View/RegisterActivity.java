package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.RegisterController;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Member;
import vn.hcmute.edu.vn.cuoiky.foody.R;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edtUsername, edtPassNew, edtConfirmPassNew;
    Button btnRegister;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    RegisterController registerController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog =  new ProgressDialog(this);
        AnhXa();


        btnRegister.setOnClickListener(this);
    }

    void AnhXa(){
        edtUsername     = (EditText)findViewById(R.id.edtUsernameNew);
        edtPassNew      = (EditText)findViewById(R.id.edtPassNew);
        edtConfirmPassNew =(EditText)findViewById(R.id.edtPassNewAg);
        btnRegister     =(Button)findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View view) {
        final String email = edtUsername.getText().toString();
        String passnew = edtPassNew.getText().toString();
        String passnewconfrim=edtConfirmPassNew.getText().toString();
        String error="";

        if(email.trim().length() == 0){
            error="Bạn chưa nhập email!";
        }else if(passnew.trim().length() == 0) {
            error = "Bạn chưa nhập mật khẩu!";
        }else if(!passnewconfrim.equals(passnew)) {
            error = "Mật khẩu không trùng khớp!";
        }

        if(error.length() > 0)
        {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setMessage("Đang xử lý");
            progressDialog.setIndeterminate(true);
            //progressDialog.setIcon(R.drawable.progress_bar_loading);
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,passnew).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //Đóng ProgressDialog
                        progressDialog.dismiss();

                        //Tạo 1 member với thông tin cần thiết
                        Member member = new Member();
                        member.setHoten(email);
                        member.setHinhanh("user.png");

                        //Thực hiện thêm thông tin thành viên
                        String uid = task.getResult().getUser().getUid();
                        registerController = new RegisterController();
                        registerController.ThemThongTinThanhVienController(member, uid);

                        //Thông báo
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent iLogin =new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                    }
                }
            });
        }
    }
}
