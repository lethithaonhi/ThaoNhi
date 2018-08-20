package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.Wifi;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    List<Wifi> wifiList;
    int resource;
    Context context;

    public WifiAdapter(Context context, int resource, List<Wifi> wifiList){
        this.context=context;
        this.resource=resource;
        this.wifiList=wifiList;
    }

    @NonNull
    @Override
    public WifiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiAdapter.ViewHolder holder, int position) {
        Wifi wifi = wifiList.get(position);

        holder.txtTenWifi.setText(wifi.getTen());
        holder.txtMatKhauWifi.setText(wifi.getMatkhau());
        holder.txtNgayDang.setText(wifi.getNgaydang());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenWifi, txtMatKhauWifi, txtNgayDang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTenWifi=(TextView)itemView.findViewById(R.id.txtTenWifi);
            txtMatKhauWifi=(TextView)itemView.findViewById(R.id.txtMatKhauWifi);
            txtNgayDang=(TextView)itemView.findViewById(R.id.txtNgayCapNhat);
        }
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }
}
