package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.RestaurantDetailsInterface;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Wifi;

public class RestaurantDetailsController {
    Wifi wifi;
    List<Wifi> wifiList;

    public RestaurantDetailsController(){
        wifi = new Wifi();
        wifiList = new ArrayList<>();
    }

    public void HienThiDanhSachWifiQuanAn(String maquan, final TextView txtTenWifi, final TextView txtMatKhauWifi, final TextView txtNgayCapNhat){
        wifi = new Wifi();
        RestaurantDetailsInterface restaurantDetailsInterface = new RestaurantDetailsInterface() {
            @Override
            public void HienThiDanhSachWifiQuanAn(Wifi wifi) {
                wifiList.add(wifi);
                txtTenWifi.setText(wifi.getTen());
                txtNgayCapNhat.setText(wifi.getNgaydang());
                txtMatKhauWifi.setText(wifi.getMatkhau());
            }
        };

        wifi.LayDanhSachWifiQuanAn(maquan, restaurantDetailsInterface);
    }
}
