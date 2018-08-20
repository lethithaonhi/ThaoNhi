package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.ChooseImgCommentAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Model.ChooseImageComment;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class ChooseImgCommentActivity extends AppCompatActivity implements View.OnClickListener{
    List<ChooseImageComment> listDuongDan;
    List<String> listHinhDuocChon;
    RecyclerView recyclerView;
    ChooseImgCommentAdapter chooseImgCommentAdapter;
    TextView txtDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_comment);

        int checkReadExStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        listDuongDan = new ArrayList<>();
        listHinhDuocChon = new ArrayList<>();
        txtDone=(TextView)findViewById(R.id.txtDone);
        recyclerView = (RecyclerView)findViewById(R.id.recycleChonHinhBinhLuan);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        chooseImgCommentAdapter = new ChooseImgCommentAdapter(this, R.layout.custom_layout_choose_img_commet,listDuongDan);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chooseImgCommentAdapter);

        if(checkReadExStorage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else{
            LayTatCaAnh();
        }

        txtDone.setOnClickListener(this);
    }

    public void LayTatCaAnh(){
        String[] projection = {MediaStore.Images.Media.DATA};
        //Đường dẫn tới thẻ nhớ
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        //Gọi tới bộ truy vấn vào thẻ nhớ
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            String duongdan = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            ChooseImageComment chooseImageComment = new ChooseImageComment(duongdan, false);


            listDuongDan.add(chooseImageComment);
            chooseImgCommentAdapter.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }

    //Xin quyền truy cập hình ảnh trong thẻ nhớ
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LayTatCaAnh();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.txtDone:
                for(ChooseImageComment value : listDuongDan){
                    if(value.isCheck()){
                        listHinhDuocChon.add(value.getDuongdan());
                    }
                }

                Intent data = getIntent();
                data.putStringArrayListExtra("hinhduochon", (ArrayList<String>) listHinhDuocChon);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}
