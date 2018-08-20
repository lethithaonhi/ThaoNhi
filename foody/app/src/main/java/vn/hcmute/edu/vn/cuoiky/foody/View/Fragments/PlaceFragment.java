package vn.hcmute.edu.vn.cuoiky.foody.View.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import vn.hcmute.edu.vn.cuoiky.foody.Controller.PlaceController;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class PlaceFragment extends Fragment {
    PlaceController placeController;
    RecyclerView recyclerODau;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    NestedScrollView nestedScrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place,container,false);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBarPlace);
        recyclerODau =(RecyclerView)v.findViewById(R.id.recycleOdau);
        nestedScrollView=(NestedScrollView)v.findViewById(R.id.nestScrollODau);

        //Lấy tọa độ vị trí hiện tại đã lưu
        sharedPreferences = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);

        Location vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude","0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longtitude","0")));

        placeController = new PlaceController(getContext());
        placeController.getDSQuanAnController(getContext(), nestedScrollView, recyclerODau, progressBar, vitrihientai);

        return v;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();

    }
}
