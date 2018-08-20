package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.Menu;
import vn.hcmute.edu.vn.cuoiky.foody.Model.MonAn;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context conext;
    List<Menu> listMenu;

    public MenuAdapter(Context conext, List<Menu> listMenu){
        this.conext=conext;
        this.listMenu=listMenu;

    }


    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(conext).inflate(R.layout.custom_layout_menu_detial,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        Menu menu = listMenu.get(position);
        holder.txtTenThucDon.setText(menu.getTenthucdon());
        holder.recyclerViewMonAn.setLayoutManager(new LinearLayoutManager(conext));
        MonAnAdapter anAdapterMonAn = new MonAnAdapter(conext, menu.getMonAnList());
        holder.recyclerViewMonAn.setAdapter(anAdapterMonAn);
        anAdapterMonAn.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenThucDon;
        RecyclerView recyclerViewMonAn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTenThucDon=(TextView)itemView.findViewById(R.id.txtTenThucDuc);
            recyclerViewMonAn=(RecyclerView)itemView.findViewById(R.id.recycleMonAn);

        }
    }
}
