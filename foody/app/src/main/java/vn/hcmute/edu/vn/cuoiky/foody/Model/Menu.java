package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.MenuInterface;

public class Menu {
    String mathucdon, tenthucdon;
    List<MonAn> monAnList;

    public String getMathucdon() {
        return mathucdon;
    }

    public void setMathucdon(String mathucdon) {
        this.mathucdon = mathucdon;
    }

    public String getTenthucdon() {
        return tenthucdon;
    }

    public void setTenthucdon(String tenthucdon) {
        this.tenthucdon = tenthucdon;
    }

    public List<MonAn> getMonAnList() {
        return monAnList;
    }

    public void setMonAnList(List<MonAn> monAnList) {
        this.monAnList = monAnList;
    }

    public void getDanhSachThucDonTheoMa(final String maquan, final MenuInterface menuInterface){
        DatabaseReference nodeThucDonQuanAn = FirebaseDatabase.getInstance().getReference().child("thucdonquanans").child(maquan);
        nodeThucDonQuanAn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final List<Menu> menuList = new ArrayList<>();
                for(DataSnapshot valueThucDon : dataSnapshot.getChildren()){
                    final Menu menu = new Menu();
                    DatabaseReference nodeThucDon = FirebaseDatabase.getInstance().getReference().child("thucdons").child(valueThucDon.getKey());
                    nodeThucDon.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            menu.setMathucdon(dataSnapshot1.getKey());
                            menu.setTenthucdon(dataSnapshot1.getValue(String.class));

                            List<MonAn> monAnList = new ArrayList<>();
                             for(DataSnapshot valueMonAn :dataSnapshot.child(dataSnapshot1.getKey()).getChildren()){
                                 MonAn monAn = valueMonAn.getValue(MonAn.class);
                                 monAn.setMamon(valueMonAn.getKey());
                                 Log.d("kiemtrathu", monAn.getTenmon());
                                 monAnList.add(monAn);
                             }
                             menu.setMonAnList(monAnList);
                             menuList.add(menu);
                             menuInterface.getThucDonThanhCong(menuList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
