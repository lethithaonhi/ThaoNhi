package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.UpdateWifiController;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Wifi;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class PopupAddWifiActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edtTenWifi, edtPassWifi;
    Button btnDongYCapNhatWifi;
    UpdateWifiController controller;
    String maquan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_popup_add_wifi);

        maquan = getIntent().getStringExtra("maquan");
        AnhXa();
        btnDongYCapNhatWifi.setOnClickListener(this);
    }

    void AnhXa(){
        edtTenWifi=(EditText)findViewById(R.id.edtWifiName);
        edtPassWifi=(EditText)findViewById(R.id.edtPassWifi);
        btnDongYCapNhatWifi=(Button)findViewById(R.id.btnDongYCapNhatWifi);
    }

    @Override
    public void onClick(View view) {
        String tenwifi = edtTenWifi.getText().toString();
        String passwifi = edtPassWifi.getText().toString();

        if(tenwifi.trim().length() > 0 && passwifi.trim().length() >0){
            controller = new UpdateWifiController(this);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String ngaydang = simpleDateFormat.format(calendar.getTime());

            Wifi wifi = new Wifi();
            wifi.setTen(tenwifi);
            wifi.setMatkhau(passwifi);
            wifi.setNgaydang(ngaydang);

            controller.ThemWifiQuanAn(this, wifi, maquan);
        }else{
            Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }
    }
}
