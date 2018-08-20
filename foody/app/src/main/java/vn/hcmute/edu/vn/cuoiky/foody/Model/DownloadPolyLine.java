package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class DownloadPolyLine extends AsyncTask<String, Void, String> {

    //Lấy chuỗi Json
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //Truyền đường dẫn
            URL url = new URL(strings[0]);
            //Mở kết nối
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //Khi truy cập vào, được luồng
            InputStream inputStream = connection.getInputStream();
            //Đọc luồng
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = "";

            while ((line = bufferedReader.readLine())!= null){
                //Lưu vào
                stringBuffer.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onPostExecute(String dataJson) {
        super.onPostExecute(dataJson);
    }
}
