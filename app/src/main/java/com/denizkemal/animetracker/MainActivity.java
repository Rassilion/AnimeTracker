package com.denizkemal.animetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denizkemal.animetracker.api.BaseModels.Profile;
import com.denizkemal.animetracker.api.MALApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkTask.NetworkTaskListener {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


    @Override
    public void onNetworkTaskFinished(Object result, TaskJob job, MALApi.ListType type) {
        if (job == TaskJob.GETPROFILE) {
            User.user = (Profile) result;
            View header = mNavigationView.getHeaderView(0);
            TextView name = (TextView) header.findViewById(R.id.username_textView);
            ImageView navAvatar = (ImageView) header.findViewById(R.id.imageView);
            name.setText(User.username);
            Picasso.with(header.getContext())
                    .load(User.user.getImageUrl())
                    .transform(new RoundedTransformation(User.username)).fit()
                    .into(navAvatar);
        }
    }

    @Override
    public void onNetworkTaskError(TaskJob job) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle message = intent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View header = mNavigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.username_textView);
        name.setText(User.username);
/*
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new AnimeFragment(), "Anime");
        adapter.addFragment(new MangaFragment(), "Manga");

        viewPager.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                int tab = viewPager.getCurrentItem();
                myIntent.putExtra("tab", tab);
                MainActivity.this.startActivity(myIntent);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        */
        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("myList");
        if(fragment!=null){
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }else{
            fragment = new myList();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "myList").commit();
        }


        new NetworkTask(TaskJob.GETPROFILE, MALApi.ListType.ANIME, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, User.username);

    }

    // Adapter for the viewpager using FragmentPagerAdapter
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            fragment = fragmentManager.findFragmentByTag("myProfile");
            if(fragment!=null){
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }else{
                fragment=new myProfile();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "myProfile").commit();
            }
        } else if (id == R.id.nav_list) {
            fragment = fragmentManager.findFragmentByTag("myList");
            if(fragment!=null){
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }else{
                fragment = new myList();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "myList").commit();
            }

        } else if (id == R.id.nav_settings) {
            fragment = fragmentManager.findFragmentByTag("myList");
            if(fragment!=null){
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }else{
                fragment = new myList();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "myList").commit();
            }
        }
        else
        {
            fragment = fragmentManager.findFragmentByTag("myList");
            if(fragment!=null){
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }else{
                fragment = new myList();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "myList").commit();
            }
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
