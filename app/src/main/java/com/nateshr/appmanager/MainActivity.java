package com.nateshr.appmanager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.nateshr.appmanager.fragments.InstalledFragment;
import com.nateshr.appmanager.fragments.UninstalledFragment;
import com.nateshr.appmanager.fragments.UpdatedFragment;


public class MainActivity extends FragmentActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the ViewPager with the sections adapter.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //Set adapter to view Pager
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //To set off screen page limit, so that first fragment is no recreated when coming from third tab.
        mViewPager.setOffscreenPageLimit(2);
        //On changing page
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                //onSwiping right/left

                //change action bar tab
                getActionBar().setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //Set up action bar tabs
        final ActionBar actionBar = getActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };


        actionBar.addTab(
                actionBar.newTab()
                        .setText("Installed")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Updated")
                        .setTabListener(tabListener));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Uninstalled")
                        .setTabListener(tabListener));
    }

    //Adapter for view Pager
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    InstalledFragment installedFragment = new InstalledFragment();
                    return installedFragment;
                case 1:
                    UpdatedFragment updatedFragment = new UpdatedFragment();
                    return updatedFragment;
                case 2:
                    UninstalledFragment uninstalledFragment = new UninstalledFragment();
                    return uninstalledFragment;
                default:
                    InstalledFragment installedFragmentDefault = new InstalledFragment();
                    return installedFragmentDefault;

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
