package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hcmute.edu.vn.cuoiky.foody.Adapter.RecycleImgCommentAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class CommentDatailsActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    RecyclerView recyclerViewHinhBinhLuan;
    TextView txtTieuDeBinhLuan, txtNoiDungBinhLuan, txtDiemBinhLuan;
    List<Bitmap> bitmapList;
    Comment comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout_comment_res_detail);

        AnhXa();

        bitmapList = new ArrayList<>();
        comment=getIntent().getParcelableExtra("comment");

        txtTieuDeBinhLuan.setText(comment.getTieude());
        txtNoiDungBinhLuan.setText(comment.getNoidung());
        txtDiemBinhLuan.setText(comment.getChamdiem() + "");
        setHinhAnhBinhLuan(circleImageView, comment.getMember().getHinhanh());

        for (String linkHinh : comment.getHinhanhBLList()) {
            StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkHinh);
            long ONE_MEGABYTE = 1024 * 1024;
            storageHinhUser.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmapList.add(bitmap);
                    if (bitmapList.size() == comment.getHinhanhBLList().size()) {
                        RecycleImgCommentAdapter adapterHinhBl = new RecycleImgCommentAdapter(CommentDatailsActivity.this, R.layout.custom_layout_imgcomment, bitmapList,comment, true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(CommentDatailsActivity.this, 2);
                        recyclerViewHinhBinhLuan.setLayoutManager(layoutManager);
                        recyclerViewHinhBinhLuan.setAdapter(adapterHinhBl);
                        adapterHinhBl.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    void AnhXa(){
        recyclerViewHinhBinhLuan=(RecyclerView)findViewById(R.id.recycleHinhBinhLuan);
        circleImageView = (CircleImageView)findViewById(R.id.ccimgbinhluan1);
        txtTieuDeBinhLuan=(TextView)findViewById(R.id.txtTieuDe1);
        txtNoiDungBinhLuan=(TextView)findViewById(R.id.txtNoiDungBL1);
        txtDiemBinhLuan=(TextView)findViewById(R.id.txtDiemBL1);
    }

    //Lấy avatar của người bình luận
    private void setHinhAnhBinhLuan(final CircleImageView circleImageView, String linkHinh){
        StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("thanhviens").child(linkHinh);
        long ONE_MEGABYTE=1024*1024;
        storageHinhUser.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                circleImageView.setImageBitmap(bitmap);
            }
        });
    }
}
