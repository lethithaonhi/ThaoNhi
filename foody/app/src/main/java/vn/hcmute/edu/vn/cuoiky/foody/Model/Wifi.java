package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.RestaurantDetailsInterface;

public class Wifi {
    String ten, matkhau, ngaydang;

    public Wifi(){

    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getNgaydang() {
        return ngaydang;
    }

    public void setNgaydang(String ngaydang) {
        this.ngaydang = ngaydang;
    }
private DatabaseReference nodeWifiQA;
    public void LayDanhSachWifiQuanAn(String maquan, final RestaurantDetailsInterface restaurantDetailsInterface){
        Query querynodeWifiQA = FirebaseDatabase.getInstance().getReference().child("wifiquanans").child(maquan).orderByKey();
        querynodeWifiQA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot valueWifi : dataSnapshot.getChildren()){
                    Wifi wifi = valueWifi.getValue(Wifi.class);
                    restaurantDetailsInterface.HienThiDanhSachWifiQuanAn(wifi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void ThemWifiWuanAn(final Context context, Wifi wifi, String maquan){
        DatabaseReference dataNodeWifi = FirebaseDatabase.getInstance().getReference().child("wifiquanans").child(maquan);
        dataNodeWifi.push().setValue(wifi, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Thêm thành công!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
