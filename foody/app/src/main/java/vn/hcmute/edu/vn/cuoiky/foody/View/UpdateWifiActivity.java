package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.UpdateWifiController;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class UpdateWifiActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnCapNhatWifi;
    RecyclerView recyclerDSWifi;
    UpdateWifiController controller;
    String maquan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_wifi);

        AnhXa();

        maquan = getIntent().getStringExtra("maquanan");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerDSWifi.setLayoutManager(layoutManager);

        controller = new UpdateWifiController(this);
        controller.HienThiDanhSachWifi(maquan, recyclerDSWifi);

        btnCapNhatWifi.setOnClickListener(this);
    }

    public void AnhXa(){
        btnCapNhatWifi=(Button)findViewById(R.id.btnCapnhatWifi);
        recyclerDSWifi=(RecyclerView)findViewById(R.id.recycleDanhSachWifi);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, PopupAddWifiActivity.class);
        intent.putExtra("maquan",maquan);
        startActivity(intent);

    }
}
