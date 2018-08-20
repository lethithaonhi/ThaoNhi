package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import vn.hcmute.edu.vn.cuoiky.foody.View.RestaurantDetailsActivity;

public class TienIch {
    String hinhtienich;
    String tentienich;
    String matienich;

    public TienIch(){

    }

    public String getMatienich() {
        return matienich;
    }

    public void setMatienich(String matienich) {
        this.matienich = matienich;
    }

    public String getHinhtienich() {
        return hinhtienich;
    }

    public void setHinhtienich(String hinhtienich) {
        this.hinhtienich = hinhtienich;
    }

    public String getTentienich() {
        return tentienich;
    }

    public void setTentienich(String tentienich) {
        this.tentienich = tentienich;
    }

}
