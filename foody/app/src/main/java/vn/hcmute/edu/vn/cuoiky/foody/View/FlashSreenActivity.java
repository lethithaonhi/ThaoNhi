package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import vn.hcmute.edu.vn.cuoiky.foody.R;

public class FlashSreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextView txtversion;
    GoogleApiClient googleApiClient;
    public static final int REQUEST_PERMISSION_LOCATION = 1;
    SharedPreferences sharedPreferences;
    Location vitriHienTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flashscreen);

        AnhXa();

        sharedPreferences = getSharedPreferences("toado",MODE_PRIVATE);

        //yêu cầu truy cập Googleplay service
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        int checkPermissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //Kiểm tra permission đã được mở chưa?
        //Khi chưa được chấp nhận
        if (checkPermissionFineLocation != PackageManager.PERMISSION_GRANTED && checkPermissionCoarseLocation != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            googleApiClient.connect();
            Toast.makeText(this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            //Kiểm tra người dùng có nhấn allow ko?
            case REQUEST_PERMISSION_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    googleApiClient.connect();
                    Toast.makeText(FlashSreenActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FlashSreenActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    //Kết nối thành công
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Lấy vị trí hiện tại
        @SuppressLint("MissingPermission") Location vitriHienTai = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(vitriHienTai != null) {
            //Ghi tọa độ vào sharedpreference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude", String.valueOf(vitriHienTai.getLatitude()));
            editor.putString("longtitude", String.valueOf(vitriHienTai.getLongitude()));
            editor.commit();


            //Chuyển trang
            try {
                //Lấy version của ứng dụng
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                txtversion.setText(getString(R.string.version) + " " + packageInfo.versionName);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(FlashSreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 200);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    //Kêt nối lúc đang chờ
    @Override
    public void onConnectionSuspended(int i) {

    }

    //Kết nối thất bại
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void AnhXa(){
        txtversion = (TextView)findViewById(R.id.txtversion);
    }
}
