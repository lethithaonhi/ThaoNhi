package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.DirectionsPlaceController;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class DirectionsPlaceActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap googleMap;
    MapFragment mapFragment;
    double longtitude = 0, latitude = 0;
    SharedPreferences sharedPreferences;
    Location vitrihientai;
    DirectionsPlaceController controller;
    String duongdan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_directions_place);

        controller = new DirectionsPlaceController();

        latitude = getIntent().getDoubleExtra("latitude",0);
        longtitude = getIntent().getDoubleExtra("longitude",0);

        //Lấy tọa độ vị trí hiện tại đã lưu
        sharedPreferences = getSharedPreferences("toado", Context.MODE_PRIVATE);

        vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude","0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longtitude","0")));

        duongdan= "https://maps.googleapis.com/maps/api/directions/json?origin=" +vitrihientai.getLatitude()+ "," +vitrihientai.getLongitude()+ "&destination="+latitude+","+longtitude+"&key=AIzaSyARWA3FxY07XNpjC_dEjU49PyBkzohWfJQ";
        Log.d("kiemtraduongdan",duongdan);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.clear();

        LatLng latLngHienTai = new LatLng(vitrihientai.getLatitude(), vitrihientai.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngHienTai);

        LatLng vitriquanan = new LatLng(latitude,longtitude);
        MarkerOptions markeroptionquanan = new MarkerOptions();
        markeroptionquanan.position(vitriquanan);
        googleMap.addMarker(markeroptionquanan);

        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngHienTai, 14);
        googleMap.moveCamera(cameraUpdate);

        controller.HienThiDanDuongToiQuanAn(googleMap, duongdan);
    }
}
