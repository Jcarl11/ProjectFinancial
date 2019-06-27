package com.example.ratio;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Dialogs.BaseDialog;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Entities.User;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Enums.USERINFO;
import com.example.ratio.Fragments.FragmentAddNew;
import com.example.ratio.Fragments.FragmentPortfolio;
import com.example.ratio.Fragments.FragmentSearch;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.UserinfoObservable;
import com.google.android.material.tabs.TabLayout;
import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.container) ViewPager container;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabs;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AlertDialog alertDialog;
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private DAOFactory sqliteFactory = DAOFactory.getDatabase(DATABASES.SQLITE);
    private BaseDAO<User> userBaseDAO = parseFactory.getUserDAO();
    private UserOperations<User> userOperations;
    private NukeOperations<Status> statusNukeOperations;
    private NukeOperations<Services> servicesNukeOperations;
    private NukeOperations<ProjectType> projectTypeNukeOperations;
    private NukeOperations<Subcategory> subcategoryNukeOperations;
    private NukeOperations<Projects> projectsNukeOperations;
    private NukeOperations<Image> imageNukeOperations;
    private NukeOperations<Pdf> pdfNukeOperations;
    private UserinfoObservable userinfoObservable = new UserinfoObservable();
    private BaseDialog baseDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        alertDialog = Utility.getInstance().showLoading(this, "Logging out", false);
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
        userinfoObservable.retrieveUsersFromParent(ParseUser.getCurrentUser().getObjectId())
                .map(new Function<Userinfo, User>() {
                    @Override
                    public User apply(Userinfo userinfo) throws Exception {
                        User user = new User();
                        user.setUserinfo(userinfo);
                        return userBaseDAO.update(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        alertDialog.show();
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: User: " + user.getObjectId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: "+ e.getMessage());
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        alertDialog.dismiss();
                    }
                });

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
        subcategoryNukeOperations = (NukeOperations<Subcategory>) sqliteFactory.getSubcategoryDAO();
        projectsNukeOperations = (NukeOperations<Projects>) sqliteFactory.getProjectDAO();
        imageNukeOperations = (NukeOperations<Image>) sqliteFactory.getImageDAO();
        pdfNukeOperations = (NukeOperations<Pdf>) sqliteFactory.getFileDAO();
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
            deleteLocalStorage();
            Toast.makeText(this, "Local storage cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if(item.getItemId() == R.id.action_logout){

            alertDialog.show();
            Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    userOperations.logoutUser();
                    deleteLocalStorage();
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
        } else if (item.getItemId() == R.id.action_manage) {
            Log.d(TAG, "onOptionsItemSelected: Position: " + ParseUser.getCurrentUser().getString(USERINFO.POSITION.toString()));
            String pos = ParseUser.getCurrentUser().getString(USERINFO.POSITION.toString());
            if(pos.equalsIgnoreCase(Constant.ADMINISTRATOR)) {
                Intent intent = new Intent(MainActivity.this, ManageUsersActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(MainActivity.this, RestrictedActivity.class));
            }

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
    private void deleteLocalStorage(){
        int statusDeleted = statusNukeOperations.deleteRows();
        int servicesDeleted = servicesNukeOperations.deleteRows();
        int projectTypesDeleted = projectTypeNukeOperations.deleteRows();
        int subCategoryDeleted = subcategoryNukeOperations.deleteRows();
        int projectDeleted = projectsNukeOperations.deleteRows();
        int imageDeleted = imageNukeOperations.deleteRows();
        int pdfDeleted = pdfNukeOperations.deleteRows();
    }
}
