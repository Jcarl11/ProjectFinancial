package com.example.ratio;

import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Fragments.FragmentAddNew_;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.Fragments.FragmentSearch;
import com.example.ratio.Utilities.Utility;
import com.google.android.material.tabs.TabLayout;
import com.parse.Parse;
import com.parse.ParseUser;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SectionsPagerAdapter mSectionsPagerAdapter;
    @ViewById ViewPager container;
    @ViewById Toolbar toolbar;
    @ViewById TabLayout tabs;
    @OptionsMenuItem MenuItem action_settings;
    @OptionsMenuItem MenuItem action_clearlocal;
    @OptionsMenuItem MenuItem action_logout;
    DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    AlertDialog alertDialog;
    @AfterViews
    void afterView(){
        setSupportActionBar(toolbar);
        initializeParse();
        if(ParseUser.getCurrentUser() == null){
            LoginActivity_.intent(this).start();
            finish();
            return;
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new FragmentAddNew_(), "Add new");
        mSectionsPagerAdapter.addFragment(new FragmentPortfolio(), "Portfolio");
        mSectionsPagerAdapter.addFragment(new FragmentSearch(), "Search");
        container.setAdapter(mSectionsPagerAdapter);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));
    }

    @OptionsItem(R.id.action_settings)
    void settingsClicked(){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @OptionsItem(R.id.action_clearlocal)
    void clearLocalStorage(){
        Log.d(TAG, "clearLocalStorage: Clicked");
    }

    @OptionsItem(R.id.action_logout)
    void logoutClicked(){
        alertDialog = Utility.getInstance().showLoading(this, "Logging out", false);
        alertDialog.show();
        logoutTask();

    }
    @Background void logoutTask(){
        UserOperations<User> userUserOperations = (UserOperations<User>) parseFactory.getUserDAO();
        userUserOperations.logoutUser();
        logoutDone();
    }

    @UiThread void logoutDone(){
        alertDialog.dismiss();
        LoginActivity_.intent(this).start();
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentList.size();
        }
    }

    private void initializeParse() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }



}
