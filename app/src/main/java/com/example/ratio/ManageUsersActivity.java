package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ratio.Adapters.UsersAdapter;
import com.example.ratio.Dialogs.BasicDialog;
import com.example.ratio.Entities.User;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.Utility;
import com.example.ratio.RxJava.UserObservable;
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
    private UserObservable userObservable = new UserObservable();
    private AlertDialog alertDialog;
    private int pos = -1;
    private List<Userinfo> userinfoList = new ArrayList<>();
    private BasicDialog basicDialog = null;
    private int myResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        ButterKnife.bind(this);
        basicDialog = new BasicDialog(this);
        alertDialog = Utility.getInstance().showLoading(this, "Please wait", false);
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
                                alertDialog.show();
                            }

                            @Override
                            public void onNext(List<Userinfo> userinfos) {
                                Log.d(TAG, "onNext: Userinfo size: " + userinfos.size());
                                userinfoList = userinfos;
                                List<User> userList = new ArrayList<>();
                                for(Userinfo userinfo : userinfos) {
                                    if(userinfo.getStatus().equalsIgnoreCase(Constant.DELETED) == false) {
                                        User user = new User();
                                        user.setUserinfo(userinfo);
                                        userList.add(user);
                                    }

                                }
                                UsersAdapter usersAdapter = new UsersAdapter(ManageUsersActivity.this, userList);
                                manage_recyclerview.setAdapter(usersAdapter);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: Exception: " + e.getMessage());
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: Completed");
                                alertDialog.dismiss();
                            }
                        });
            }
        });

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(manage_recyclerview, callBack())
                .setIsVertical(false)
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        String[] choices = new String[]{"Make Apprentice", "Make Administrator", "Delete user"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageUsersActivity.this);
                        builder.setTitle("Choose");
                        builder.setCancelable(true);
                        builder.setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: Which: " + which);
                                Userinfo userinfo = userinfoList.get(pos);
                                switch (which) {
                                    case 0:
                                        if(manage_spinner_status.getSelectedIndex() == 1) {
                                            Toast.makeText(ManageUsersActivity.this, "User is already an Apprentice", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        userinfo.setPosition(Constant.APPRENTICE);
                                        userinfo.setVerified(true);
                                        userinfo.setStatus(Constant.ACTIVE);
                                        updateUser(userinfo);
                                        break;
                                    case 1:
                                        if(manage_spinner_status.getSelectedIndex() == 2) {
                                            Toast.makeText(ManageUsersActivity.this, "User is already an Administrator", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        userinfo.setPosition(Constant.ADMINISTRATOR);
                                        userinfo.setVerified(true);
                                        userinfo.setStatus(Constant.ACTIVE);
                                        updateUser(userinfo);
                                        break;
                                    case 2:
                                        deleteUser(userinfo);
                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }).create();
        manage_recyclerview.setOnTouchListener(listener);
    }

    private SwipeDismissRecyclerViewTouchListener.DismissCallbacks callBack() {
        SwipeDismissRecyclerViewTouchListener.DismissCallbacks callback = new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                pos = position;
                return true;
            }

            @Override
            public void onDismiss(View view) {

            }
        };
        return callback;
    }

    private void updateUser(Userinfo userinfo) {
        userinfoObservable.updateUserinfo(userinfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Userinfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        alertDialog.show();
                    }

                    @Override
                    public void onNext(Userinfo userinfo) {
                        Log.d(TAG, "onNext: Verified: " + String.valueOf(userinfo.isVerified()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        alertDialog.dismiss();
                        basicDialog.setNegativeText("");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage(e.getMessage());
                        basicDialog.showDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        userinfoList.remove(pos);
                        manage_recyclerview.getAdapter().notifyItemRemoved(pos);
                        alertDialog.dismiss();
                        basicDialog.setNegativeText("");
                        basicDialog.setPositiveText("OK");
                        basicDialog.setTitle("Result");
                        basicDialog.setCancellable(true);
                        basicDialog.setMessage("User updated");
                        basicDialog.showDialog();
                    }
                });
    }
    public void deleteUser(Userinfo userinfo) {
        myResult = 0;
        userinfoObservable.deleteUserinfo(userinfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Subscribed...");
                        alertDialog.show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: Result: " + integer);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Exception thrown: " + e.getMessage());
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Completed");
                        alertDialog.dismiss();
                        userinfoList.remove(pos);
                        manage_recyclerview.getAdapter().notifyItemRemoved(pos);
                    }
                });
    }
}
