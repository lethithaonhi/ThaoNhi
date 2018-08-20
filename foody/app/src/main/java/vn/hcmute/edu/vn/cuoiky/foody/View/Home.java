package vn.hcmute.edu.vn.cuoiky.foody.View;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.hcmute.edu.vn.cuoiky.foody.Adapter.HomeAdapter;
import vn.hcmute.edu.vn.cuoiky.foody.R;

public class Home extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{
    ViewPager viewPager;
    RadioButton rdplace, rdfood;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        AnhXa();

        HomeAdapter adapter= new HomeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);

    }

    void AnhXa(){
        viewPager=(ViewPager)findViewById(R.id.viewpager_home);
        rdfood=(RadioButton)findViewById(R.id.rd_food);
        rdplace=(RadioButton)findViewById(R.id.rd_place);
        radioGroup=(RadioGroup)findViewById(R.id.rdg_place_food);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    //Hàm chuyển 2 fragment khi vuốt qua lại radio
    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                rdplace.setChecked(true);
                break;
            case 1:
                rdfood.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //Hàm chuyển fragment_place và fragment_food khi click vào radiobutton
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rd_place:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rd_food:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
