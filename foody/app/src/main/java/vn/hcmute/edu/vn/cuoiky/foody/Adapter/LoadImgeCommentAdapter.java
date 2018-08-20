package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.R;

public class LoadImgeCommentAdapter extends RecyclerView.Adapter<LoadImgeCommentAdapter.ViewHolder>  {
    Context context;
    int layout;
    List<String> listHinhChon;

    public LoadImgeCommentAdapter (Context context, int layout, List<String> listHinhChon){
        this.context = context;
        this.layout=layout;
        this.listHinhChon=listHinhChon;
    }

    @NonNull
    @Override
    public LoadImgeCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return listHinhChon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img=(ImageView)itemView.findViewById(R.id.imgChonHinhBinhLuan);
            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull LoadImgeCommentAdapter.ViewHolder holder, final int position) {
        Uri uri = Uri.parse(listHinhChon.get(position));
        holder.img.setImageURI(uri);
        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vitri = (int) view.getTag();
                listHinhChon.remove(vitri);
                notifyDataSetChanged();
            }
        });
    }

}
