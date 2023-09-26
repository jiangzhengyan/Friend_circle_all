package com.example.csonezp.imagezoomdemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static String url1 = "http://img.shu163.com/uploadfiles/wallpaper/2010/6/2010063006111517.jpg";
    public static String url2 = "http://pic.pp3.cn/uploads//allimg/111116/11021321R-4.jpg";
    public static String url3 = "http://pic.yesky.com/imagelist/07/03/1769316_2073.jpg";
    public static String url4 = "http://pic.yesky.com/imagelist/07/03/1769316_2073.jpg";
    public static String url5 = "http://pic.yesky.com/imagelist/07/03/1769316_2073.jpg";
    public static String url6 = "http://pic.yesky.com/imagelist/07/03/1769316_2073.jpg";

    ImageViewPagerAdapter adapter;


    HackyViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (HackyViewPager) findViewById(R.id.pager);
        List<String> list = new ArrayList<>();
        list.add(url1);
        list.add(url1);
        list.add(url2);
        list.add(url2);
        list.add(url3);
        list.add(url3);
        list.add(url4);
        list.add(url4);
        list.add(url5);
        list.add(url5);
        list.add(url6);
        list.add(url6);
        adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
    }

}
