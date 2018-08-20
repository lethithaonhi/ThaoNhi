package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.LoadImgeCommentAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.CommentController;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txtTenQuan, txtDiaChi, txtDang;
    Toolbar toolbar;
    EditText edtTieuDe, edtNoiDung;
    RecyclerView recyclerViewChonHinhBinhLuan;
    ImageButton btnCamera, btnChamDiem, btnHashtag;
    final int REQUEST_CHONHINHBINHLUAN = 123;
    LoadImgeCommentAdapter adapter;
    String maquan;
    Comment comment;
    SharedPreferences sharedPreferences;
    CommentController controller;
    List<String> listHinhDuocChon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment);

        AnhXa();
        String tenquan = getIntent().getStringExtra("tenquan");
        String diachi = getIntent().getStringExtra("diachi");
        maquan = getIntent().getStringExtra("maquan");

        comment = new Comment();
        controller = new CommentController();

        sharedPreferences = getSharedPreferences("luudangnhap",MODE_PRIVATE);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewChonHinhBinhLuan.setLayoutManager(layoutManager);

        //Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtTenQuan.setText(tenquan);
        txtDiaChi.setText(diachi);

        btnCamera.setOnClickListener(this);
        txtDang.setOnClickListener(this);

    }

    void AnhXa(){
        txtDang=(TextView)findViewById(R.id.btnDangComment);
        txtTenQuan = (TextView)findViewById(R.id.txtTenQuanAn);
        txtDiaChi =(TextView)findViewById(R.id.txtDiaChiQuanAn);
        toolbar = (Toolbar)findViewById(R.id.toolbarComment);
        btnCamera = (ImageButton)findViewById(R.id.btnCamera);
        btnHashtag = (ImageButton)findViewById(R.id.btnHashtag);
        btnChamDiem = (ImageButton)findViewById(R.id.btnChamDiem);
        recyclerViewChonHinhBinhLuan = (RecyclerView)findViewById(R.id.recycleChonHinhBinhLuan);
        edtTieuDe=(EditText)findViewById(R.id.edtTieuDe);
        edtNoiDung=(EditText)findViewById(R.id.edtNoiDung);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnCamera:
                Intent iBinhLuan = new Intent(this, ChooseImgCommentActivity.class);
                startActivityForResult(iBinhLuan, REQUEST_CHONHINHBINHLUAN);
                break;
            case R.id.btnDangComment:
                String tieude = edtTieuDe.getText().toString();
                String noidung = edtNoiDung.getText().toString();
                String mauser = sharedPreferences.getString("mauser", "");

                comment.setTieude(tieude);
                comment.setNoidung(noidung);
                comment.setChamdiem(0);
                comment.setLuotthich(0);
                comment.setMauser(mauser);

                controller.ThemBinhLuan(maquan, comment,listHinhDuocChon);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listHinhDuocChon = new ArrayList<>();

        //Lấy tất cả hình dược chọn
        if(requestCode == REQUEST_CHONHINHBINHLUAN){
            if(resultCode == RESULT_OK){
                listHinhDuocChon = data.getStringArrayListExtra("hinhduochon");
                adapter = new LoadImgeCommentAdapter(this, R.layout.custom_layout_load_img_comment, listHinhDuocChon);
                recyclerViewChonHinhBinhLuan.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
