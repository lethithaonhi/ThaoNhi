package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    int layout;
    List<Comment> commentList;

    public CommentAdapter(Context context, int layout, List<Comment> commentList){
        this.context=context;
        this.layout=layout;
        this.commentList=commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        int socomment=commentList.size();
        if(socomment > 0 && socomment>5){
            return 5;
        }else{
            return socomment;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        RecyclerView recyclerViewHinhBinhLuan;
        TextView txtTieuDeBinhLuan, txtNoiDungBinhLuan, txtDiemBinhLuan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerViewHinhBinhLuan=(RecyclerView)itemView.findViewById(R.id.recycleHinhBinhLuan);
            circleImageView = (CircleImageView)itemView.findViewById(R.id.ccimgbinhluan1);
            txtTieuDeBinhLuan=(TextView)itemView.findViewById(R.id.txtTieuDe1);
            txtNoiDungBinhLuan=(TextView)itemView.findViewById(R.id.txtNoiDungBL1);
            txtDiemBinhLuan=(TextView)itemView.findViewById(R.id.txtDiemBL1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.txtTieuDeBinhLuan.setText(comment.getTieude());
        holder.txtNoiDungBinhLuan.setText(comment.getNoidung());
        holder.txtDiemBinhLuan.setText(comment.getChamdiem() + "");
        setHinhAnhBinhLuan(holder.circleImageView, comment.getMember().getHinhanh());

        final List<Bitmap> bitmapList = new ArrayList<>();
        bitmapList.clear();
        for (String linkHinh : comment.getHinhanhBLList()) {
            StorageReference storageHinhUser = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkHinh);
            long ONE_MEGABYTE = 1024 * 1024;
            storageHinhUser.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmapList.add(bitmap);
                    if (bitmapList.size() == comment.getHinhanhBLList().size()) {
                        RecycleImgCommentAdapter adapterHinhBl = new RecycleImgCommentAdapter(context, R.layout.custom_layout_imgcomment, bitmapList,comment, false);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        holder.recyclerViewHinhBinhLuan.setLayoutManager(layoutManager);
                        holder.recyclerViewHinhBinhLuan.setAdapter(adapterHinhBl);
                        adapterHinhBl.notifyDataSetChanged();
                    }
                }
            });
        }
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
