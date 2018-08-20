package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import vn.hcmute.edu.vn.cuoiky.foody.Model.DownloadPolyLine;
import vn.hcmute.edu.vn.cuoiky.foody.Model.ParsePolyline;

public class DirectionsPlaceController {
    ParsePolyline parsePolyline;
    DownloadPolyLine downloadPolyLine;

    public DirectionsPlaceController(){

    }

    public void HienThiDanDuongToiQuanAn(GoogleMap googleMap, String duongdan){
        parsePolyline = new ParsePolyline();
        downloadPolyLine = new DownloadPolyLine();
        //Thực thi
        downloadPolyLine.execute(duongdan);

        try {
            String dataJson = downloadPolyLine.get();
            //Lấy chuỗi mã hóa, được các tọa độ
            List<LatLng> latLngList = parsePolyline.LayDanhSachToaDo(dataJson);

            PolylineOptions polylineOptions = new PolylineOptions();
            //Lấy từng tọa độ add vào
            for(LatLng toado : latLngList){
                polylineOptions.add(toado);
            }

            //Vẽ đường
            Polyline polyline = googleMap.addPolyline(polylineOptions);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
