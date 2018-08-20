package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.MonAn;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Order;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.ViewHolder> {
    Context context;
    List<MonAn> monAnList;
    public static List<Order> orderList = new ArrayList<>();

    public MonAnAdapter(Context context, List<MonAn> monAnList){
        this.context=context;
        this.monAnList=monAnList;

    }

    @NonNull
    @Override
    public MonAnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_monan,parent, false);

        return new MonAnAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MonAnAdapter.ViewHolder holder, int position) {
        final MonAn monAn= monAnList.get(position);
        holder.txtSoLuong.setTag(0);
        holder.txtTenMonAn.setText(monAn.getTenmon());
        holder.imgTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                dem++;
                holder.txtSoLuong.setTag(dem);
                holder.txtSoLuong.setText(dem+"");

                Order orderTag = (Order) holder.imgGiam.getTag();
                if(orderTag != null){
                    MonAnAdapter.orderList.remove(orderTag);
                }

                Order order= new Order();
                order.setSoluong(dem);
                order.setTenMonAn(monAn.getTenmon());

                holder.imgGiam.setTag(order);
                MonAnAdapter.orderList.add(order);
            }
        });

        holder.imgGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dem = Integer.parseInt(holder.txtSoLuong.getTag().toString());
                if(dem > 0){
                    dem--;
                    if(dem == 0) {
                        Order order = (Order) view.getTag();
                        MonAnAdapter.orderList.remove(order);
                    }
                }

                holder.txtSoLuong.setTag(dem);
                holder.txtSoLuong.setText(dem+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return monAnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn, txtSoLuong;
        ImageView imgGiam, imgTang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTenMonAn = (TextView)itemView.findViewById(R.id.txtTenMonAn);
            imgGiam=(ImageView)itemView.findViewById(R.id.imgGiamSoLuong);
            imgTang = (ImageView)itemView.findViewById(R.id.imgTangSoLuong);
            txtSoLuong = (TextView)itemView.findViewById(R.id.txtKetQuaSoLuong);
        }
    }
}
