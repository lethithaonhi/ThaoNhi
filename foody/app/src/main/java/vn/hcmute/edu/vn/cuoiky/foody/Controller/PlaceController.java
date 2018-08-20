package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.RecyclePlaceAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.Controller.Interfaces.PlaceInterface;
import vn.hcmute.edu.vn.cuoiky.foody.Model.QuanAn;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class PlaceController {
    Context context;
    QuanAn quanAn;
    RecyclePlaceAdapter recyclePlaceAdapter;
    int itemdaco = 3;

    public PlaceController(Context context){
        this.context=context;
        quanAn = new QuanAn();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getDSQuanAnController(Context context, final NestedScrollView nestedScrollView,final RecyclerView recyclerODau, final ProgressBar progressBar,final Location vitrihientai){
        final List<QuanAn> quanAnList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerODau.setLayoutManager(layoutManager);

        recyclePlaceAdapter = new RecyclePlaceAdapter(context, quanAnList, R.layout.custom_fragment_place);
        recyclerODau.setAdapter(recyclePlaceAdapter);

        progressBar.setVisibility(View.VISIBLE);

        final PlaceInterface placeInterface = new PlaceInterface() {
            @Override
            public void getDSQuanAn(final QuanAn quanAn) {
                final List<Bitmap> bitmaps = new ArrayList<>();
                for(String linkhinh : quanAn.getHinhanhList()){
                    StorageReference storageHinhAnh = FirebaseStorage.getInstance().getReference().child("hinhanh").child(linkhinh);
                    long ONE_MEGABYTE=1024*1024;
                    storageHinhAnh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            bitmaps.add(bitmap);
                            quanAn.setBitmapList(bitmaps);

                            if(quanAn.getBitmapList().size() == quanAn.getHinhanhList().size()) {
                                quanAnList.add(quanAn);
                                recyclePlaceAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        };

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null){
                    //Kiểm tra có scroll tới vị trí cuối cùng chưa?
                    if(scrollY >= (v.getChildAt(v.getChildCount()-1)).getMeasuredHeight() - v.getMeasuredHeight()){
                        itemdaco +=3;
                        quanAn.getDSQuanAn(placeInterface, vitrihientai, itemdaco, itemdaco - 3);
                    }
                }
            }
        });

        quanAn.getDSQuanAn(placeInterface, vitrihientai, itemdaco, 0);
    }
}
