package com.example.ratio;


import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.DAO.Sqlite.DBHelper;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Fragments.FragmentAddNew;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.Fragments.FragmentSearch;
import com.example.ratio.HelperClasses.Utility;
import com.google.android.material.tabs.TabLayout;
import com.parse.Parse;
import com.parse.ParseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.container) ViewPager container;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabs;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AlertDialog alertDialog;
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private UserOperations<User> userOperations;
    private NukeOperations<Status> statusNukeOperations;
    private NukeOperations<Services> servicesNukeOperations;
    private NukeOperations<ProjectType> projectTypeNukeOperations;
    private BaseDialog baseDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate: Started...");
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: Toolbar initialized");
        initializeParse();
        if(ParseUser.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        Log.d(TAG, "onCreate: User already logged in");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new FragmentAddNew(), "Add new");
        mSectionsPagerAdapter.addFragment(new FragmentPortfolio(), "Portfolio");
        mSectionsPagerAdapter.addFragment(new FragmentSearch(), "Search");
        container.setAdapter(mSectionsPagerAdapter);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));
        userOperations = (UserOperations<User>) parseFactory.getUserDAO();
        statusNukeOperations = (NukeOperations<Status>) sqliteFactory.getStatusDAO();
        servicesNukeOperations = (NukeOperations<Services>) sqliteFactory.getServicesDAO();
        projectTypeNukeOperations = (NukeOperations<ProjectType>) sqliteFactory.getProjectTypeDAO();
        baseDialog = new BasicDialog(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        } else if(item.getItemId() == R.id.action_clearlocal){
            Log.d(TAG, "clearLocalStorage: Clicked");
            int statusDeleted = statusNukeOperations.deleteRows();
            int servicesDeleted = servicesNukeOperations.deleteRows();
            int projectTypesDeleted = projectTypeNukeOperations.deleteRows();
            Toast.makeText(this, "Local storage cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if(item.getItemId() == R.id.action_logout){
            alertDialog = Utility.getInstance().showLoading(this, "Logging out", false);
            alertDialog.show();
            Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    userOperations.logoutUser();
                    return "Done";
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            alertDialog.show();
                        }

                        @Override
                        public void onNext(String result) {
                            Log.d(TAG, "onNext: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            baseDialog.setTitle("Result");
                            baseDialog.setMessage(e.getMessage());
                            baseDialog.setCancellable(true);
                            baseDialog.showDialog();
                        }

                        @Override
                        public void onComplete() {
                            alertDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

            return true;
        }
        return super.onOptionsItemSelected(item);
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
