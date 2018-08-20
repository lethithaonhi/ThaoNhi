package vn.hcmute.edu.vn.cuoiky.foody.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParsePolyline {

    public static List<LatLng> LayDanhSachToaDo(String dataJson){
        List<LatLng> latLngs = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(dataJson);
            JSONArray routes = jsonObject.getJSONArray("routes");
            for(int i=0; i<routes.length();i++){
                JSONObject jsonObjectArray = routes.getJSONObject(i);
                JSONObject overviewPolyline = jsonObjectArray.getJSONObject("overview_polyline");

                String point = overviewPolyline.getString("points");
                //Mã hóa data chỉ dường
                latLngs = PolyUtil.decode(point);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return latLngs;
    }
}
