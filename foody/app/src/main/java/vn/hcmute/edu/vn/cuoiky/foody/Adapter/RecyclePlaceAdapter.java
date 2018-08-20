package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Agencys;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;
import vn.hcmute.edu.vn.cuoiky.foody.Model.QuanAn;
import vn.hcmute.edu.vn.cuoiky.foody.R;
import vn.hcmute.edu.vn.cuoiky.foody.View.RestaurantDetailsActivity;

public class RecyclePlaceAdapter extends RecyclerView.Adapter<RecyclePlaceAdapter.ViewHolder> {
    List<QuanAn> quanAnList;
    int resource;
    Context context;

    public RecyclePlaceAdapter(Context context, List<QuanAn> quanAnList, int resource){
        this.quanAnList = quanAnList;
        this.resource = resource;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclePlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return quanAnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenQuanAnODau, txtLuotThich, txtNoiDung1, txtNoiDung2, txtTieuDe1, txtTieuDe2, txtDiemBL1, txtDiemBL2, txtTongBL, txtTongHABL, txtDiaChiQuanAn, txtKhoangCach;
        Button btnDatMon;
        ImageView imgquanan;
        CircleImageView imgbl1, imgbl2;
        LinearLayout containerBL1, containerBL2;
        CardView cardviewQA;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenQuanAnODau = (TextView)itemView.findViewById(R.id.txtTenQuanAnODau);
            txtLuotThich =(TextView) itemView.findViewById(R.id.txtLuotThich);
            btnDatMon =(Button)itemView.findViewById(R.id.btnDatMon);
            imgquanan =(ImageView)itemView.findViewById(R.id.imgQuanAnPlace);
            imgbl1 = (CircleImageView)itemView.findViewById(R.id.ccimgbinhluan1);
            imgbl2 = (CircleImageView)itemView.findViewById(R.id.ccimgbinhluan2);
            txtNoiDung1=(TextView)itemView.findViewById(R.id.txtNoiDungBL1);
            txtNoiDung2=(TextView)itemView.findViewById(R.id.txtNoiDungBL2);
            txtTieuDe1=(TextView)itemView.findViewById(R.id.txtTieuDe1);
            txtTieuDe2=(TextView)itemView.findViewById(R.id.txtTieuDe2);
            txtDiemBL1=(TextView)itemView.findViewById(R.id.txtDiemBL1);
            txtDiemBL2=(TextView)itemView.findViewById(R.id.txtDiemBL2);
            txtTongBL=(TextView)itemView.findViewById(R.id.txtTongBL);
            txtTongHABL=(TextView)itemView.findViewById(R.id.txtTongHABL);
            txtKhoangCach=(TextView)itemView.findViewById(R.id.txtKhoangCachODau);
            txtDiaChiQuanAn=(TextView)itemView.findViewById(R.id.txtDiaChiQuanAnODau);
            containerBL1=(LinearLayout)itemView.findViewById(R.id.containerBinhLuan1);
            containerBL2=(LinearLayout)itemView.findViewById(R.id.containerBinhLuan2);
            cardviewQA=(CardView)itemView.findViewById(R.id.cardviewQA);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclePlaceAdapter.ViewHolder holder, int position) {
        final QuanAn quanAn = quanAnList.get(position);
        holder.txtTenQuanAnODau.setText(quanAn.getTenquanan());

        if(quanAn.isGiaohang()){
            holder.btnDatMon.setVisibility(View.VISIBLE);
        }

        if(quanAn.getBitmapList().size() >0){
            holder.imgquanan.setImageBitmap(quanAn.getBitmapList().get(0));
        }

       //Kiểm tra có ds Bình luận hay ko?
       if(quanAn.getCommentList().size() > 0){
            Comment comment = quanAn.getCommentList().get(0);
            holder.txtTieuDe1.setText(comment.getTieude());
            holder.txtNoiDung1.setText(comment.getNoidung());
            holder.txtDiemBL1.setText(comment.getChamdiem()+"");
            setHinhAnhBinhLuan(holder.imgbl1, comment.getMember().getHinhanh());

            if(quanAn.getCommentList().size() > 2) {
                Comment comment1 = quanAn.getCommentList().get(1);
                holder.txtTieuDe2.setText(comment1.getTieude());
                holder.txtNoiDung2.setText(comment1.getNoidung());
                holder.txtDiemBL2.setText(comment1.getChamdiem()+"");
                setHinhAnhBinhLuan(holder.imgbl2, comment1.getMember().getHinhanh());
            }
            holder.txtTongBL.setText(quanAn.getCommentList().size()+"");

            int tongHinhBL =0;
            double TongDiem=0;
            // Tính tổng điểm tb, tổng số bình luân của quán ăn
            for(Comment comment3 : quanAn.getCommentList()){
                tongHinhBL += comment3.getHinhanhBLList().size();
                TongDiem+=comment3.getChamdiem();
            }

           double DiemTB=TongDiem/ quanAn.getCommentList().size();
           holder.txtLuotThich.setText(String.format("%.1f", DiemTB));
           holder.txtTongHABL.setText(tongHinhBL+"");
        }else{
            holder.containerBL1.setVisibility(View.GONE);
            holder.containerBL2.setVisibility(View.GONE);
        }

        //Lấy chi nhánh quán ăn
        if(quanAn.getChinhanhList().size() > 0){
            Agencys chinhanhtam = quanAn.getChinhanhList().get(0);
            for(Agencys chinhanh : quanAn.getChinhanhList()){
                if(chinhanhtam.getKhoangcach() > chinhanh.getKhoangcach())
                    chinhanhtam = chinhanh;
            }
            holder.txtDiaChiQuanAn.setText(chinhanhtam.getDiachi());
            holder.txtKhoangCach.setText(String.format("%.1f",chinhanhtam.getKhoangcach())+" km");
        }

        //Truy cập quán an9 chi tiết
        holder.cardviewQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iQAChiTiet = new Intent(context, RestaurantDetailsActivity.class);
                iQAChiTiet.putExtra("quanan", quanAn);
                context.startActivity(iQAChiTiet);
            }
        });
    }

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
