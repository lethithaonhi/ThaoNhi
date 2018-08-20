package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

public class Comment implements Parcelable{
    double chamdiem;
    long luotthich;
    String noidung, tieude, mauser, mabl;
    Member member;
    List<String> hinhanhBLList;
    public Comment(){

    }

    protected Comment(Parcel in) {
        chamdiem = in.readDouble();
        luotthich = in.readLong();
        noidung = in.readString();
        tieude = in.readString();
        mauser = in.readString();
        mabl = in.readString();
        hinhanhBLList = in.createStringArrayList();
        member= in.readParcelable(Member.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getMabl() {
        return mabl;
    }

    public void setMabl(String mabl) {
        this.mabl = mabl;
    }

    public List<String> getHinhanhBLList() {
        return hinhanhBLList;
    }

    public void setHinhanhBLList(List<String> hinhanhBLList) {
        this.hinhanhBLList = hinhanhBLList;
    }

    public String getMauser() {
        return mauser;
    }

    public void setMauser(String mauser) {
        this.mauser = mauser;
    }

    public double getChamdiem() {
        return chamdiem;
    }

    public void setChamdiem(double chamdiem) {
        this.chamdiem = chamdiem;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(chamdiem);
        parcel.writeLong(luotthich);
        parcel.writeString(noidung);
        parcel.writeString(tieude);
        parcel.writeString(mauser);
        parcel.writeString(mabl);
        parcel.writeStringList(hinhanhBLList);
        parcel.writeParcelable(member, i);
    }

    public void ThemBinhLuan(String maquan, Comment comment, final List<String> listHinh){
        DatabaseReference nodeBinhLuan = FirebaseDatabase.getInstance().getReference().child("binhluans");
        final String mabinhluan = nodeBinhLuan.child(maquan).push().getKey();
        nodeBinhLuan.child(maquan).child(mabinhluan).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(listHinh.size() > 0) {

                        //Lưu hình vào firebase storage
                        for (String valueHinh : listHinh) {
                            Uri uri = Uri.fromFile(new File(valueHinh));
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhanh/" + uri.getLastPathSegment());
                            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                }
                            });

                        }

                        //Lưu vào hình vào firebase data
                            for (String valueHinh: listHinh){
                                Uri uri = Uri.fromFile(new File(valueHinh));
                                DatabaseReference nodeHinhBL = FirebaseDatabase.getInstance().getReference().child("hinhanhbinhluans");
                                nodeHinhBL.child(mabinhluan).push().setValue(uri.getLastPathSegment());
                            }
                    }
                }
            }
        });
    }
}
