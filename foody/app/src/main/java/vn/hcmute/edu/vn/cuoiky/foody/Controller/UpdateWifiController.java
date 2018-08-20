package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.WifiAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.RestaurantDetailsInterface;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Wifi;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class UpdateWifiController {
    Wifi wifi;
    Context context;
    List<Wifi> wifiList;

    public UpdateWifiController(Context context){
        wifi = new Wifi();
        this.context=context;
        wifiList = new ArrayList<>();
    }
     public void HienThiDanhSachWifi(String maquan, final RecyclerView recyclerView){
         RestaurantDetailsInterface restaurantDetailsInterface = new RestaurantDetailsInterface() {
             @Override
             public void HienThiDanhSachWifiQuanAn(Wifi wifi) {
                 wifiList.add(wifi);
                 WifiAdapter adapter= new WifiAdapter(context, R.layout.layout_wifi_res_detail, wifiList);
                 recyclerView.setAdapter(adapter);
                 adapter.notifyDataSetChanged();
             }
         };

         wifi.LayDanhSachWifiQuanAn(maquan, restaurantDetailsInterface);
     }

     public void ThemWifiQuanAn(Context context, Wifi wifi, String maquan){
        wifi.ThemWifiWuanAn(context, wifi, maquan);
     }
}
