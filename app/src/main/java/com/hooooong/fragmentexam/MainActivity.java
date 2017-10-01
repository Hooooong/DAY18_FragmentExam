package com.hooooong.fragmentexam;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ListFragment.CallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 기준이 되는 Activity 는 activity_main.xml 을 사용
        // 가로모드 에서 되는 Activity 는 activity_main.xml(layout-land) 를 사용
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            return;

        init();
    }

    private void init(){
        if(getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT){
            // 새로모드일 때
            // 프래그먼트를 Setting
            initFragment();
        }else{
            // 가로모드일 때
            // 작업 없음
        }
    }

    /**
     * Fragment 초기화
     */
    private void initFragment(){
        // Fragment 부착하기
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ListFragment())
                .commit();
    }

    /**
     * Fragment 더하기
     */
    private void addFragment(String value){
        //Fragment 에 값을 넘겨주는 방법
        DetailFragment detailFragment = new DetailFragment();
        Bundle data = new Bundle();
        data.putString("value", value);
        detailFragment.setArguments(data);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goDetail(String value) {
        if(getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT){
            // 새로모드일 때
            // DetailFragment 를 Setting
            addFragment(value);
        }else{
            // 가로모드일 때
            // 현재 레이아웃에 삽입되어 있는 Fragment 를 가져온다.
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
            detailFragment.setTextView(value);
        }
    }
}
