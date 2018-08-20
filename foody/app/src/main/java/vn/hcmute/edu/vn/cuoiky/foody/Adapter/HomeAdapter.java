package vn.hcmute.edu.vn.cuoiky.foody.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.hcmute.edu.vn.cuoiky.foody.View.Fragments.FoodFragment;
import vn.hcmute.edu.vn.cuoiky.foody.View.Fragments.PlaceFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {
    PlaceFragment placeFragment;
    FoodFragment foodFragment;

    public HomeAdapter(FragmentManager fm) {
        super(fm);
        placeFragment=new PlaceFragment();
        foodFragment = new FoodFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return placeFragment;
            case 1:
                return foodFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
