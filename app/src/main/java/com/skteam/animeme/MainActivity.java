package com.skteam.animeme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skteam.animeme.fragments.HomeFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BubbleNavigationLinearView bubbleNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(new HomeFragment());
        SwipeBackActivityHelper.activityBuilder(MainActivity.this)
                .intent(new Intent(MainActivity.this,HomeActivity.class))
                .needParallax(true)
                .needBackgroundShadow(true)
                // .fitSystemWindow(true) // status bar height
                // .prepareView(swipeRefreshLayout)
                // see http://stackoverflow.com/questions/29356607/android-swiperefreshlayout-cause-recyclerview-not-update-when-take-screenshot
                .startActivity();

//        bubbleNavigation = findViewById(R.id.bottom_navigation_view_linear);
//
//
//
//        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
//            @Override
//            public void onNavigationChanged(View view, int position) {
//                //navigation changed, do something
//                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//                switch (position){
//
//                    case 0:
//
//                        break;
//                    case 1:
////                        selectTheme();
//                        break;
//                    case 2:
////                        setFragment(new ProfileFragment());
//                        break;
//
//                }
//            }
//        });
//        bubbleNavigation.setCurrentActiveItem(0);


    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }
}