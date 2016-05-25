package com.xh.recyclerviewitemdel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * Created by snaillove on 2016/3/28.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.onListItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //当saveInstanceState为null时才new一个MainFragment出来，否则每次旋转屏幕时都会new出来一个
        if(savedInstanceState == null){
            MainFragment fragment = new MainFragment();
            //用add将MainFragment添加到frameLayout上
            getSupportFragmentManager().beginTransaction().add(R.id.content,fragment).commit();
        }

    }

    @Override
    public void onListItemClick(int position) {
        //当MainFragment的Item被点击后 ， 就会回调此方法
        //在此方法中写真正的逻辑，这样Activity和Fragment之间就是松耦合关系，MainFragment可以复用
        Fragment fragment = null;
        switch (position){
            case 0:
                //当点击第一个item的时候，new一个RecyclerListFragment
                fragment = new RecyclerListFragment() ;
                break;
            case 1:
                //当点击第二个item的时候，new 一个RecyclerGridFragment
                fragment = new RecyclerGridFragment();
                break;
        }
        //这次用replace,替换framelayout MainFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).addToBackStack(null).commit();
    }


}
