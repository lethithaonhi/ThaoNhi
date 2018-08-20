package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.MenuAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.MenuInterface;
import vn.hcmute.edu.vn.cuoiky.foody.Model.Menu;

public class MenuController {
    Menu menu;

    public MenuController(){
        menu = new Menu();
    }

    public void getDanhSachThucDonTheoMa(final Context context, String maquan, final RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuInterface menuInterface = new MenuInterface() {
            @Override
            public void getThucDonThanhCong(List<Menu> menuList) {
                MenuAdapter adapterThucDon = new MenuAdapter(context,menuList);
                recyclerView.setAdapter(adapterThucDon);
                adapterThucDon.notifyDataSetChanged();
            }
        };
        menu.getDanhSachThucDonTheoMa(maquan, menuInterface);
    }
}
