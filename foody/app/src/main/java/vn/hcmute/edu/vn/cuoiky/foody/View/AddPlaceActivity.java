package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.AddMenu;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Agencys;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Menu;
import vn.hcmute.edu.vn.cuoiky.foody.Model.MonAn;
import vn.hcmute.edu.vn.cuoiky.foody.Model.QuanAn;
import vn.hcmute.edu.vn.cuoiky.foody.Model.TienIch;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class AddPlaceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    final int RESULT_IMG1=111;
    final int RESULT_IMG2=222;
    final int RESULT_IMG3=333;
    final int RESULT_IMG4=444;
    final int RESULT_IMG5=555;
    final int RESULT_IMG6=666;
    final int RESULT_IMG_THUCDON=777;
    final int RESULT_IMG_VIDEO=200;

    EditText edtTenQuan, edtGiaToiDa, edtGiaToiThieu;
    RadioGroup rdgTrangThai;
    Button btnGioMoCua, btnGioDongCua, btnThemQuanAn;
    String giomocua, giodongcua, khuvuc="tphcm", maquanan;
    Spinner spinnerKhuVuc;
    ImageView imgtam;
    List<Menu> menuList;
    List<AddMenu> addMenuList;
    List<String> khucvucList, thucdonList, chinhanhList;
    List<Bitmap> imgDaChup;
    List<Uri> hinhQuanAnList;
    ArrayAdapter<String> adapterKhuVuc;
    List<String> tienIchList;
    LinearLayout khungTienIch, khungChiNhanh, khungThemChiNhanh, khungChuaThucDon;
    ImageView imgVideo, imgQuan1, imgQuan2, imgQuan3, imgQuan4, imgQuan5, imgQuan6;
    VideoView videoView;
    Uri videochoose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_place);

        AnhXa();
        menuList = new ArrayList<>();
        khucvucList= new ArrayList<>();
        thucdonList = new ArrayList<>();
        tienIchList = new ArrayList<>();
        chinhanhList = new ArrayList<>();
        addMenuList = new ArrayList<>();
        imgDaChup = new ArrayList<>();
        hinhQuanAnList = new ArrayList<>();

        adapterKhuVuc = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, khucvucList);
        spinnerKhuVuc.setAdapter(adapterKhuVuc);
        adapterKhuVuc.notifyDataSetChanged();

        CLoneChiNhanh();
        CloneThucDon();

        LayDSKhuVuc();
        LayDSTienIch();

        btnGioMoCua.setOnClickListener(this);
        btnGioDongCua.setOnClickListener(this);
        imgQuan1.setOnClickListener(this);
        imgQuan2.setOnClickListener(this);
        imgQuan3.setOnClickListener(this);
        imgQuan4.setOnClickListener(this);
        imgQuan5.setOnClickListener(this);
        imgQuan6.setOnClickListener(this);
        imgVideo.setOnClickListener(this);
        btnThemQuanAn.setOnClickListener(this);
    }


    private void CLoneChiNhanh(){
        final View view1 = LayoutInflater.from(AddPlaceActivity.this).inflate(R.layout.layout_clone_chinhanh, null);
        ImageButton imgThemChiNhanh = view1.findViewById(R.id.btnThemChiNhanh);
        final ImageButton imgXoaChiNhanh = view1.findViewById(R.id.btnXoaChiNhanh);
        imgThemChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtTenChiNhanh = view1.findViewById(R.id.edtTenChiNhanh);
                String tenchinhanh = edtTenChiNhanh.getText().toString();

                view.setVisibility(View.GONE);
                imgXoaChiNhanh.setVisibility(View.VISIBLE);
                imgXoaChiNhanh.setTag(tenchinhanh);

                chinhanhList.add(edtTenChiNhanh.getText().toString());
                CLoneChiNhanh();
            }
        });

        imgXoaChiNhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenchinhanh = view.getTag().toString();
                chinhanhList.remove(tenchinhanh);
                khungThemChiNhanh.removeView(view1);
            }
        });
        khungThemChiNhanh.addView(view1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_IMG1:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan1.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG2:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan2.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG3:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan3.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG4:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan4.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG5:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan5.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG6:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    imgQuan6.setImageURI(uri);
                    hinhQuanAnList.add(uri);
                }
                break;
            case RESULT_IMG_THUCDON:
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgtam.setImageBitmap(bitmap);
                imgDaChup.add(bitmap);
                break;
            case RESULT_IMG_VIDEO:
                if (resultCode == RESULT_OK) {
                    //Lấy đường dẫn
                    Uri uri = data.getData();
                    videochoose = uri;
                    videoView.setVideoURI(uri);
                    videoView.start();
                    imgVideo.setVisibility(View.GONE);
                }
                break;
        }

    }

    private void CloneThucDon(){
        final View v = LayoutInflater.from(AddPlaceActivity.this).inflate(R.layout.layout_clone_menu, null);
        final Spinner spinnerThucDon=v.findViewById(R.id.spinnerThucDon);
        final EditText edtTenMonAn = v.findViewById(R.id.edtTenMon);
        final EditText edtGiaTien = v.findViewById(R.id.edtGiaTien);
        final ImageView imgMonAn = v.findViewById(R.id.imgMonAn);
        imgtam = imgMonAn;
        Button btnThemThucDon = v.findViewById(R.id.btnThemThucDon);

        final ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(AddPlaceActivity.this, android.R.layout.simple_list_item_1, thucdonList);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();

        if(thucdonList.size() == 0){
            LayDSThucDon(adapterThucDon);

        }else {
            adapterThucDon.notifyDataSetChanged();
        }

        imgMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_IMG_THUCDON);
            }
        });

        btnThemThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);

                String tenhinh = String.valueOf(Calendar.getInstance().getTimeInMillis())+ ".jpg";

                int pos = spinnerThucDon.getSelectedItemPosition();
                String mathucdon = menuList.get(pos).getMathucdon();

                String tenmonan = edtTenMonAn.getText().toString();
                String giatien = edtGiaTien.getText().toString();
                MonAn monAn = new MonAn();
                monAn.setTenmon(tenmonan);
                monAn.setGiatien(Long.parseLong(giatien));
                monAn.setHinhanh(tenhinh);

                AddMenu addMenu = new AddMenu();
                addMenu.setMathucdon(mathucdon);
                addMenu.setMonAn(monAn);

                addMenuList.add(addMenu);

                CloneThucDon();
            }
        });

        khungChuaThucDon.addView(v);

    }


    public void AnhXa(){
        btnGioMoCua=(Button)findViewById(R.id.btnGioMoCua);
        btnGioDongCua=(Button)findViewById(R.id.btnGioDongCua);
        spinnerKhuVuc=(Spinner)findViewById(R.id.spinnerKhuVuc);
        khungTienIch =(LinearLayout) findViewById(R.id.khungTienIch);
        khungChiNhanh=(LinearLayout)findViewById(R.id.khungChiNhanh);
        khungThemChiNhanh=(LinearLayout)findViewById(R.id.khungThemChiNhanh);
        khungChuaThucDon=(LinearLayout)findViewById(R.id.khungChuaThucDon);
        imgQuan1 = (ImageView)findViewById(R.id.imgQuanAn1);
        imgQuan2 = (ImageView)findViewById(R.id.imgQuanAn2);
        imgQuan3 = (ImageView)findViewById(R.id.imgQuanAn3);
        imgQuan4 = (ImageView)findViewById(R.id.imgQuanAn4);
        imgQuan5 = (ImageView)findViewById(R.id.imgQuanAn5);
        imgQuan6 = (ImageView)findViewById(R.id.imgQuanAn6);
        imgVideo = (ImageView) findViewById(R.id.imgVideo);
        videoView=(VideoView)findViewById(R.id.VideoView);
        btnThemQuanAn=(Button)findViewById(R.id.btnThemQuanAn);
        rdgTrangThai = (RadioGroup)findViewById(R.id.rdgTrangThai);
        edtTenQuan = (EditText)findViewById(R.id.edtTenQuanAn);
        edtGiaToiDa = (EditText)findViewById(R.id.edtGiaToiDa);
        edtGiaToiThieu =(EditText)findViewById(R.id.edtGiaToiThieu);
    }

    private void LayDSKhuVuc(){
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot valueKhuVuc : dataSnapshot.getChildren()){
                    String tenkhuvuc = valueKhuVuc.getKey();
                    khucvucList.add(tenkhuvuc);
                }

                adapterKhuVuc.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LayDSThucDon(final ArrayAdapter<String> adapterThucDon){
        FirebaseDatabase.getInstance().getReference().child("thucdons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot valueThucDon : dataSnapshot.getChildren()){
                    Menu menu = new Menu();
                    String tenthucdon = (String) valueThucDon.getValue();
                    String key = valueThucDon.getKey();

                    menu.setMathucdon(key);
                    menu.setTenthucdon(tenthucdon);

                    menuList.add(menu);
                    thucdonList.add(tenthucdon);
                }

                adapterThucDon.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LayDSTienIch(){
        FirebaseDatabase.getInstance().getReference().child("quanlytienichs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot valueTienIch : dataSnapshot.getChildren()){
                    String matienich = valueTienIch.getKey();
                    final TienIch tienIch = valueTienIch.getValue(TienIch.class);
                    tienIch.setMatienich(matienich);

                    //Tạo checkbox
                    CheckBox checkBox = new CheckBox(AddPlaceActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    checkBox.setText(tienIch.getTentienich());
                    checkBox.setTag(matienich);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            String maTienIch = compoundButton.getTag().toString();
                            if(isChecked){
                                tienIchList.add(maTienIch);
                            }else{
                                tienIchList.remove(maTienIch);
                            }
                        }
                    });

                    khungTienIch.addView(checkBox);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(final View view) {
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        int id = view.getId();
        switch (id){
            case R.id.btnGioMoCua:
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPlaceActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        giomocua = hour+":"+minute;
                        ((Button)view).setText(giomocua);
                    }
                }, gio, phut, true);

                timePickerDialog.show();
                break;
            case R.id.btnGioDongCua:
                TimePickerDialog timePickerDialogDongCua = new TimePickerDialog(AddPlaceActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        giodongcua = hour+":"+minute;
                        ((Button)view).setText(giodongcua);
                    }
                }, gio, phut, true);

                timePickerDialogDongCua.show();
                break;
            case R.id.imgQuanAn1:
                ChonHinhGallary(RESULT_IMG1);
                break;
            case R.id.imgQuanAn2:
                ChonHinhGallary(RESULT_IMG2);
                break;
            case R.id.imgQuanAn3:
                ChonHinhGallary(RESULT_IMG3);
                break;
            case R.id.imgQuanAn4:
                ChonHinhGallary(RESULT_IMG4);
                break;
            case R.id.imgQuanAn5:
                ChonHinhGallary(RESULT_IMG5);
                break;
            case R.id.imgQuanAn6:
                ChonHinhGallary(RESULT_IMG6);
                break;
            case R.id.imgVideo:
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Chọn hình...", RESULT_IMG1));
                startActivityForResult(intent, RESULT_IMG_VIDEO);
                break;
            case R.id.btnThemQuanAn:
                ThemQuanAn();
                 break;
        }
    }


    class DownloadToaDo extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer = new StringBuffer();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine())!= null){
                    stringBuffer.append(line+"\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i=0; i< results.length(); i++){
                    JSONObject object = results.getJSONObject(i);
                    String address = object.getString("formatted_address");
                    JSONObject geometry = object.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = (double) location.get("lat");
                    double longitude = (double) location.get("lng");

                    Agencys chinhanh = new Agencys();
                    chinhanh.setDiachi(address);
                    chinhanh.setLatitude(latitude);
                    chinhanh.setLongitude(longitude);

                    FirebaseDatabase.getInstance().getReference().child("chinhanhquanans").child(maquanan).push().setValue(chinhanh);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void ThemQuanAn(){
        String tenquanan = edtTenQuan.getText().toString();

        int idRadioSelected = rdgTrangThai.getCheckedRadioButtonId();
        boolean giaoHang = false;
        if(idRadioSelected == R.id.rdGiaoHang){
            giaoHang = true;
        }else{
            giaoHang = false;
        }

        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();

        DatabaseReference nodeQuanAn = nodeRoot.child("quanans");
        maquanan = nodeQuanAn.push().getKey();

        nodeRoot.child("khuvucs").child(khuvuc).push().setValue(maquanan);

        //Thêm chi nhánh
        for (String chinhanh : chinhanhList){
            //Lấy lat và lng từ Geocoding Maps
            String urlGeoCoding = "https://maps.googleapis.com/maps/api/geocode/json?address="+chinhanh.replace(" ","%20")+"&key=AIzaSyARWA3FxY07XNpjC_dEjU49PyBkzohWfJQ";
            DownloadToaDo downloadToaDo = new DownloadToaDo();
            downloadToaDo.execute(urlGeoCoding);
        }

        QuanAn quanAn = new QuanAn();
        quanAn.setTenquanan(tenquanan);
        quanAn.setGiaohang(giaoHang);
        quanAn.setGiodongcua(giodongcua);
        quanAn.setGiomocua(giomocua);
        quanAn.setVideogioithieu(videochoose.getLastPathSegment());
        quanAn.setTienich(tienIchList);
        quanAn.setLuotthich(0);

        nodeQuanAn.child(maquanan).setValue(quanAn).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPlaceActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        //Thêm video vào firebase
        FirebaseStorage.getInstance().getReference().child("video/"+videochoose.getLastPathSegment()).putFile(videochoose);

        for (Uri hinhquan : hinhQuanAnList){
            FirebaseStorage.getInstance().getReference().child("hinhanh/"+hinhquan.getLastPathSegment()).putFile(hinhquan);
            nodeRoot.child("hinhanhquanans").child(maquanan).push().child(hinhquan.getLastPathSegment());
        }

        for (int i=0; i< addMenuList.size(); i++){
            nodeRoot.child("thucdonquanans").child(maquanan).child(addMenuList.get(i).getMathucdon()).push().setValue(addMenuList.get(i).getMonAn());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = imgDaChup.get(i);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage.getInstance().getReference().child("hinhanh/"+addMenuList.get(i).getMonAn().getHinhanh()).putBytes(data);

        }


    }

    private void ChonHinhGallary(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Chọn hình...", RESULT_IMG1));
        startActivityForResult(intent, requestCode);
    }

    private void DateTimePickerDialog(){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        switch (adapterView.getId()){
            case R.id.spinnerKhuVuc:
                khuvuc = khucvucList.get(pos);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
