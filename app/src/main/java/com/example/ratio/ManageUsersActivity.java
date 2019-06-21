package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.ratio.Adapters.UsersAdapter;
import com.example.ratio.Entities.User;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.UserinfoObservable;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private static final String TAG = "ManageUsersActivity";
    @BindView(R.id.manage_spinner_status) MaterialSpinner manage_spinner_status;
    @BindView(R.id.manage_recyclerview) RecyclerView manage_recyclerview;
    private static String[] CHOICES = {"Pending", "Apprentice", "Administrator"};
    private UserinfoObservable userinfoObservable = new UserinfoObservable();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        ButterKnife.bind(this);
        dialog = Utility.getInstance().showLoading(this, "Please wait", false);
        manage_recyclerview.setHasFixedSize(true);
        manage_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        manage_spinner_status.setItems(CHOICES);
        manage_spinner_status.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String status = null;
                if(position == 0)
                    status = Constant.PENDING;
                else if (position == 1)
                    status = Constant.APPRENTICE;
                else if (position == 2)
                    status = Constant.ADMINISTRATOR;
                userinfoObservable.retrieveUsers(status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Userinfo>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: Subscribed");
                                dialog.show();
                            }

                            @Override
                            public void onNext(List<Userinfo> userinfos) {
                                Log.d(TAG, "onNext: Userinfo size: " + userinfos.size());
                                List<User> userList = new ArrayList<>();
                                for(Userinfo userinfo : userinfos) {
                                    User user = new User();
                                    user.setUserinfo(userinfo);
                                    userList.add(user);
                                }
                                UsersAdapter usersAdapter = new UsersAdapter(ManageUsersActivity.this, userList);
                                manage_recyclerview.setAdapter(usersAdapter);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: Exception: " + e.getMessage());
                                dialog.dismiss();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: Completed");
                                dialog.dismiss();
                            }
                        });
            }
        });
    }
}
