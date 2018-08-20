package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.ChooseImageComment;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class ChooseImgCommentAdapter extends RecyclerView.Adapter<ChooseImgCommentAdapter.ViewHolder> {
    Context context;
    int layout;
    List<ChooseImageComment> listDuongDan;

    public ChooseImgCommentAdapter(Context context, int layout, List<ChooseImageComment> listDuongDan){
        this.context=context;
        this.layout=layout;
        this.listDuongDan=listDuongDan;
    }


    @NonNull
    @Override
    public ChooseImgCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return listDuongDan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChonHinhBinhLuan;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgChonHinhBinhLuan = (ImageView)itemView.findViewById(R.id.imgChonHinhBinhLuan);
            checkBox=(CheckBox)itemView.findViewById(R.id.checkboxChonHingBinhLuan);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseImgCommentAdapter.ViewHolder holder, final int position) {
        final ChooseImageComment chooseImageComment = listDuongDan.get(position);
        Uri uri = Uri.parse(chooseImageComment.getDuongdan());

        holder.imgChonHinhBinhLuan.setImageURI(uri);
        holder.checkBox.setChecked(chooseImageComment.isCheck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                chooseImageComment.setCheck(checkBox.isChecked());
            }
        });
    }
}
