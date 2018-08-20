package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.CommentAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.MenuController;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.RestaurantDetailsController;
import vn.hcmute.edu.vn.cuoiky.foody.Model.QuanAn;
import vn.hcmute.edu.vn.cuoiky.foody.Model.TienIch;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    TextView txtTenQuanAn, txtDiaChi, txtThoiGianHoatDong, txtTrangThaiHoatDong, txtTongImg, txtTongBinhLuan, txtTongCheckIn, txtTongLuuLai, txtTieuDeToolBar, txtTenWifi, txtMatKhauWifi, txtNgayCapNhat;
    ImageView imgQuanAn, imgPlayVideo;
    QuanAn quanAn;
    LinearLayout khungTienIch, khungWifi;
    Toolbar toolbar;
    RecyclerView recyclerViewBinhLuan, recyclerViewMenu;
    CommentAdapter commentAdapter;
    NestedScrollView nestedScrollView;
    GoogleMap googleMap;
    MapFragment mapFragment;
    RestaurantDetailsController controller;
    View khungTinhNang;
    Button btnComment;
    VideoView videoTrailer;
    MenuController menuController;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_res_detail);

        quanAn = getIntent().getParcelableExtra("quanan");

        AnhXa();
        mapFragment.getMapAsync(this);

    }

    void AnhXa(){
        txtTenQuanAn=(TextView)findViewById(R.id.txtTenQuanAnChiTiet);
        txtDiaChi=(TextView)findViewById(R.id.txtDiaChiQuanAnChiTiet);
        txtThoiGianHoatDong=(TextView)findViewById(R.id.txtGioHoatDong);
        txtTrangThaiHoatDong=(TextView)findViewById(R.id.txtTrangThaiHoatDong);
        txtTongImg=(TextView)findViewById(R.id.txtTongImg);
        txtTongBinhLuan=(TextView)findViewById(R.id.txtTongBinhLuan);
        txtTongCheckIn=(TextView)findViewById(R.id.txtTongCheckIn);
        txtTongLuuLai=(TextView)findViewById(R.id.txtTongLuuLai);
        txtTieuDeToolBar=(TextView)findViewById(R.id.txtTieuDeToolBar);
        txtTenWifi=(TextView)findViewById(R.id.txtTenWifi);
        txtMatKhauWifi=(TextView)findViewById(R.id.txtMatKhauWifi);
        txtNgayCapNhat=(TextView)findViewById(R.id.txtNgayCapNhat);
        btnComment = (Button)findViewById(R.id.btnComment);
        videoTrailer=(VideoView)findViewById(R.id.videoTrailer);
        imgPlayVideo=(ImageView)findViewById(R.id.imgPlayVideo);
        khungTienIch=(LinearLayout)findViewById(R.id.KhungTienIch);
        khungWifi=(LinearLayout)findViewById(R.id.khungWifi);
        khungTinhNang=(View)findViewById(R.id.khungTinhNang);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Hiển trị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgQuanAn=(ImageView)findViewById(R.id.imgQuanAnChiTiet);
        recyclerViewBinhLuan=(RecyclerView)findViewById(R.id.recycleChiTietQuanAn);
        nestedScrollView=(NestedScrollView)findViewById(R.id.nestScrollChiTietQuanAn);
        recyclerViewMenu=(RecyclerView)findViewById(R.id.recyclMenu);

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        HienThiChiTietQuanAn();
        khungTinhNang.setOnClickListener(this);
        btnComment.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void HienThiChiTietQuanAn(){
        //Lấy thời gian hiện tai
        Calendar calendar = Calendar.getInstance();
        //Format thời gian lấy giờ:phút
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String giohientai = dateFormat.format(calendar.getTime());
        String giomocua=quanAn.getGiomocua();
        String giodongcua=quanAn.getGiodongcua();

        try{
            Date datehientai = dateFormat.parse(giohientai);
            Date datemocua = dateFormat.parse(giomocua);
            Date datedongcua = dateFormat.parse(giodongcua);

            if(datehientai.after(datemocua) && datehientai.before(datedongcua)){
                txtTrangThaiHoatDong.setText("Đang mở cửa");
            }else{
                txtTrangThaiHoatDong.setText("Đã đóng cửa");
            }
        }catch (Exception e){
            System.out.print("Message: " + e.getMessage());
        }

        txtTenQuanAn.setText(quanAn.getTenquanan());
        txtTieuDeToolBar.setText(quanAn.getTenquanan());
        txtDiaChi.setText(quanAn.getChinhanhList().get(0).getDiachi());
        txtThoiGianHoatDong.setText(quanAn.getGiomocua()+"  -  "+quanAn.getGiodongcua());
        txtTongImg.setText(quanAn.getHinhanhList().size()+"");
        txtTongBinhLuan.setText(quanAn.getCommentList().size()+"");


        //Lấy hình ảnh của quán ăn
        StorageReference storageImgQuanAn = FirebaseStorage.getInstance().getReference().child("hinhanh").child(quanAn.getHinhanhList().get(0));
        long ONE_MEGABYTE=1024*1024;
        storageImgQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgQuanAn.setImageBitmap(bitmap);
            }
        });

        if(quanAn.getVideogioithieu() != null){
            videoTrailer.setVisibility(View.VISIBLE);
            imgQuanAn.setVisibility(View.GONE);
            imgPlayVideo.setVisibility(View.VISIBLE);
            //Lấy video giới thiệu của quán ăn
            StorageReference storageVideo = FirebaseStorage.getInstance().getReference().child("video").child(quanAn.getVideogioithieu());
            storageVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    videoTrailer.setVideoURI(uri);
                    videoTrailer.seekTo(3);
                }
            });

            imgPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  videoTrailer.start();
                  imgPlayVideo.setVisibility(View.GONE);
                    MediaController mediaController = new MediaController(RestaurantDetailsActivity.this);
                    videoTrailer.setMediaController(mediaController);
                }
            });
        }else{
            videoTrailer.setVisibility(View.GONE);
            imgQuanAn.setVisibility(View.VISIBLE);
            imgPlayVideo.setVisibility(View.GONE);
        }

        //Load danh sách bình luận của quán ăn
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBinhLuan.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, R.layout.custom_layout_comment_res_detail, quanAn.getCommentList());
        recyclerViewBinhLuan.setAdapter(commentAdapter);

        //Cho màn hình về top do bị tràn bình luận
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fling(0);
                nestedScrollView.smoothScrollTo(0, 0);
            }
        });

        DownloadHinhTienIch();

        controller = new RestaurantDetailsController();
        controller.HienThiDanhSachWifiQuanAn(quanAn.getMaquanan(), txtTenWifi, txtMatKhauWifi, txtNgayCapNhat);

        menuController = new MenuController();
        menuController.getDanhSachThucDonTheoMa(this, quanAn.getMaquanan(), recyclerViewMenu);

        khungWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, UpdateWifiActivity.class);
                intent.putExtra("maquanan", quanAn.getMaquanan());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;

        double latitude = quanAn.getChinhanhList().get(0).getLatitude();
        double longtitude = quanAn.getChinhanhList().get(0).getLongitude();
        LatLng latLng = new LatLng(latitude, longtitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(quanAn.getTenquanan());

        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        googleMap.moveCamera(cameraUpdate);
    }

    //Hiển thị hình tiện ích
    private void DownloadHinhTienIch(){

        for(String matienich : quanAn.getTienich()) {
            DatabaseReference nodeTienIch = FirebaseDatabase.getInstance().getReference().child("quanlytienichs").child(matienich);
            nodeTienIch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    TienIch tienIch = dataSnapshot.getValue(TienIch.class);

                    StorageReference storageImgQuanAn = FirebaseStorage.getInstance().getReference().child("hinhtienich").child(tienIch.getHinhtienich());
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageImgQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView imgTienIch = new ImageView(RestaurantDetailsActivity.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                            layoutParams.setMargins(10,10,10,10);
                            imgTienIch.setLayoutParams(layoutParams);
                            imgTienIch.setScaleType(ImageView.ScaleType.FIT_XY);
                            imgTienIch.setPadding(5,5,5,5);
                            imgTienIch.setImageBitmap(bitmap);
                            khungTienIch.addView(imgTienIch);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.khungTinhNang:
                Intent intent = new Intent(this, DirectionsPlaceActivity.class);
                intent.putExtra("latitude", quanAn.getChinhanhList().get(0).getLatitude());
                intent.putExtra("longitude", quanAn.getChinhanhList().get(0).getLongitude());
                startActivity(intent);
                break;
            case R.id.btnComment:
                Intent iBinhLuan = new Intent(this, CommentActivity.class);
                iBinhLuan.putExtra("maquan", quanAn.getMaquanan());
                iBinhLuan.putExtra("tenquan", quanAn.getTenquanan());
                iBinhLuan.putExtra("diachi", quanAn.getChinhanhList().get(0).getDiachi());
                startActivity(iBinhLuan);
                break;
        }
    }
}
