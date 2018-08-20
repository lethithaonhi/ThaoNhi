package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import vn.hcmute.edu.vn.cuoiky.foody.R;

public class ForgetPassActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSubmit;
    EditText edtEmail;
    TextView txtRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgetpass);

        firebaseAuth = FirebaseAuth.getInstance();
        AnhXa();

        btnSubmit.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
    }

    void AnhXa(){
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        edtEmail =(EditText)findViewById(R.id.edtEmail);
        txtRegister=(TextView)findViewById(R.id.txtCreateAccount);
    }

    private boolean KiemTraEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch(id) {
            case R.id.btnSubmit:
                String email=edtEmail.getText().toString();
                if(KiemTraEmail(email)){
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetPassActivity.this,"Gửi email thành công!",Toast.LENGTH_SHORT).show();
                                Intent iLogin=new Intent(ForgetPassActivity.this,LoginActivity.class);
                                startActivity(iLogin);
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtCreateAccount:
                Intent iCreate = new Intent(ForgetPassActivity.this, RegisterActivity.class);
                startActivity(iCreate);
                break;
        }
    }
}
