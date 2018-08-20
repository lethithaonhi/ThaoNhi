package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.R;
import vn.hcmute.edu.vn.cuoiky.foody.View.CommentDatailsActivity;

public class RecycleImgCommentAdapter extends RecyclerView.Adapter<RecycleImgCommentAdapter.ViewHolder> {
    Context context;
    int layout;
    List<Bitmap> imgList;
    Comment comment;
    boolean isChiTiet;

    public RecycleImgCommentAdapter(Context context, int layout, List<Bitmap> imgList, Comment comment, boolean isChiTiet){
        this.context=context;
        this.layout=layout;
        this.imgList=imgList;
        this.comment=comment;
        this.isChiTiet=isChiTiet;
    }
    @NonNull
    @Override
    public RecycleImgCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        RecycleImgCommentAdapter.ViewHolder viewHolder = new RecycleImgCommentAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleImgCommentAdapter.ViewHolder holder, int position) {
        holder.imgHinhBL.setImageBitmap(imgList.get(position));
        if(!isChiTiet) {
            if (position == 3) {
                int hinhconlai = imgList.size() - 4;
                if (hinhconlai > 0) {
                    holder.frameLayout.setVisibility(View.VISIBLE);
                    holder.txtTongImgBl.setText("+" + hinhconlai);
                    holder.imgHinhBL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, CommentDatailsActivity.class);
                            intent.putExtra("comment", comment);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(!isChiTiet)
            if (imgList.size() > 3)
                return 4;
            else
                return imgList.size();
        else
            return imgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhBL;
        TextView txtTongImgBl;
        FrameLayout frameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout=(FrameLayout)itemView.findViewById(R.id.KhungSoHinhBL);
            imgHinhBL=(ImageView)itemView.findViewById(R.id.imgBinhLuan);
            txtTongImgBl=(TextView)itemView.findViewById(R.id.txtTongImgBinhLuan);
        }
    }
}
