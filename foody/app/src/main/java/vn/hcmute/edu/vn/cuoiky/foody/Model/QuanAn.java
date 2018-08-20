package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.PlaceInterface;

public class QuanAn implements Parcelable{
    boolean giaohang;
    String giodongcua, giomocua, tenquanan, videogioithieu, maquanan;
    List<String> tienich, hinhanhList;
    List<Comment> commentList;
    List<Agencys> chinhanhList;
    List<Bitmap> bitmapList;
    List<Menu> menuList;
    long luotthich;
    DatabaseReference nodeRoot;

    protected QuanAn(Parcel in) {
        giaohang = in.readByte() != 0;
        giodongcua = in.readString();
        giomocua = in.readString();
        tenquanan = in.readString();
        videogioithieu = in.readString();
        maquanan = in.readString();
        tienich = in.createStringArrayList();
        hinhanhList = in.createStringArrayList();
        luotthich = in.readLong();
        chinhanhList = new ArrayList<Agencys>();
        in.readTypedList(chinhanhList,Agencys.CREATOR);
        commentList = new ArrayList<Comment>();
        in.readTypedList(commentList,Comment.CREATOR);
    }

    public static final Creator<QuanAn> CREATOR = new Creator<QuanAn>() {
        @Override
        public QuanAn createFromParcel(Parcel in) {
            return new QuanAn(in);
        }

        @Override
        public QuanAn[] newArray(int size) {
            return new QuanAn[size];
        }
    };

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public List<Agencys> getChinhanhList() {
        return chinhanhList;
    }

    public void setChinhanhList(List<Agencys> chinhanhList) {
        this.chinhanhList = chinhanhList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<String> getHinhanhList() {
        return hinhanhList;
    }

    public void setHinhanhList(List<String> hinhanhList) {
        this.hinhanhList = hinhanhList;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public QuanAn() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }


    public boolean isGiaohang() {
        return giaohang;
    }

    public void setGiaohang(boolean giaohang) {
        this.giaohang = giaohang;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }

    public String getVideogioithieu() {
        return videogioithieu;
    }

    public void setVideogioithieu(String videogioithieu) {
        this.videogioithieu = videogioithieu;
    }

    public String getMaquanan() {
        return maquanan;
    }

    public void setMaquanan(String maquanan) {
        this.maquanan = maquanan;
    }

    public List<String> getTienich() {
        return tienich;
    }

    public void setTienich(List<String> tienich) {
        this.tienich = tienich;
    }

    private DataSnapshot dataRoot;
    public void getDSQuanAn(final PlaceInterface placeInterface, final Location vitrihientai, final int itemtieptheo, final int itemdaco){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                LayDanhSachQuanAn(dataSnapshot, placeInterface, vitrihientai, itemtieptheo, itemdaco);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(dataRoot != null){
            LayDanhSachQuanAn(dataRoot, placeInterface, vitrihientai, itemtieptheo, itemdaco);
        }else{
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void LayDanhSachQuanAn(DataSnapshot dataSnapshot, PlaceInterface placeInterface, Location vitrihientai, int itemtieptheo, int itemdaco){
        DataSnapshot dataSnapshotQuanAn = dataSnapshot.child("quanans");

        int i=0;
        //Lấy danh sách quán ăn
        for (DataSnapshot valueQuanAn : dataSnapshotQuanAn.getChildren()){
            if(i == itemtieptheo){
                break;
            }

            if(i < itemdaco){
                i++;
                continue;
            }
            i++;

            QuanAn quanAn = valueQuanAn.getValue(QuanAn.class);
            quanAn.setMaquanan(valueQuanAn.getKey());

            // Lấy ds hình ảnh quán ăn theo mã
            DataSnapshot dataSnapshotHinhQA = dataSnapshot.child("hinhanhquanans").child(valueQuanAn.getKey());
            List<String> hinhanhList = new ArrayList<>();
            for(DataSnapshot valueHinhAnh : dataSnapshotHinhQA.getChildren()){
                hinhanhList.add(valueHinhAnh.getValue(String.class));
            }

            quanAn.setHinhanhList(hinhanhList);

            //Lấy ds bình luận quán ăn theo mã quán ăn
            DataSnapshot dataBinhLuan = dataSnapshot.child("binhluans").child(quanAn.getMaquanan());
            List<Comment> commentList = new ArrayList<>();

            for(DataSnapshot valueBinhLuan: dataBinhLuan.getChildren()){
                Comment comment = valueBinhLuan.getValue(Comment.class);
                Member mem = dataSnapshot.child("thanhviens").child("Dg4e9Phl0fSlChlvkOaEiijwc612").getValue(Member.class);
                comment.setMember(mem);
                comment.setMabl(valueBinhLuan.getKey());

                //Lấy hình ảnh bình luận
                List<String> hinhanhBLList = new ArrayList();
                DataSnapshot dataHinhAnhBL = dataSnapshot.child("hinhanhbinhluans").child(comment.getMabl());
                for(DataSnapshot valueHinhAnhBL : dataHinhAnhBL.getChildren()){
                    hinhanhBLList.add(valueHinhAnhBL.getValue(String.class));

                }
                comment.setHinhanhBLList(hinhanhBLList);
                commentList.add(comment);
            }

            quanAn.setCommentList(commentList);

            //Lấy chi nhánh quán ăn
            List<Agencys> chinhanhList = new ArrayList<>();
            DataSnapshot dataChiNhanh = dataSnapshot.child("chinhanhquanans").child(quanAn.getMaquanan());
            for(DataSnapshot valueChiNhanh: dataChiNhanh.getChildren()){
                Agencys chinhanh = valueChiNhanh.getValue(Agencys.class);
                Location vitriquanan = new Location("");
                vitriquanan.setLatitude(chinhanh.getLatitude());
                vitriquanan.setLongitude(chinhanh.getLongitude());

                double khoangcach = vitrihientai.distanceTo(vitriquanan)/1000;
                Log.d("kiemtra", khoangcach+" - "+chinhanh.getDiachi());
                chinhanh.setKhoangcach(khoangcach);

                chinhanhList.add(chinhanh);
            }
            quanAn.setChinhanhList(chinhanhList);

            placeInterface.getDSQuanAn(quanAn);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (giaohang ? 1 : 0));
        parcel.writeString(giodongcua);
        parcel.writeString(giomocua);
        parcel.writeString(tenquanan);
        parcel.writeString(videogioithieu);
        parcel.writeString(maquanan);
        parcel.writeStringList(tienich);
        parcel.writeStringList(hinhanhList);
        parcel.writeLong(luotthich);
        parcel.writeTypedList(chinhanhList);
        parcel.writeTypedList(commentList);
    }
}
