package com.pandodev.prixmdemo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.cache.normalized.CacheControl;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // MyTestData myTestData ;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FEED_SIZE = 5;

    MyApplication application;
    ViewGroup content;
    ProgressBar progressBar;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    ApolloCall<MyTestData.Data> githuntFeedCall;
    List<MyTestData.GetTodo> getTodosList;
    ApolloCall<CreateTodo.Data> createTodoCall;

    @BindView(R.id.btCreate)
    AppCompatButton btCreate;
    @BindView(R.id.btGetList)
    AppCompatButton btGetList;
    @BindView(R.id.btDelete)
    AppCompatButton btDelete;
    @BindView(R.id.btUpdate)
    AppCompatButton btUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        application = (MyApplication) getApplication();
        getTodosList = new ArrayList<>();


       // createEmployee();

      //  fetchFeed();

    }

    private void createEmployee() {
        final CreateTodo feedQuery = CreateTodo.builder()
                .id(1)
                .isCompleted(true)
                .itemName("My Test From Android")
                .build();
        createTodoCall = application.apolloClient()
                .query(feedQuery)
                .cacheControl(CacheControl.NETWORK_FIRST);
        createTodoCall.enqueue(crateTodoCallback);

    }


    private ApolloCall.Callback<CreateTodo.Data> crateTodoCallback = new ApolloCallback<>(new ApolloCall.Callback<CreateTodo.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTodo.Data> response) {
            Log.e("Response", "" + response.data().createTodo());
            //  getTodosList.addAll(response.data().getTodo());

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }, uiHandler);


    private ApolloCall.Callback<MyTestData.Data> dataCallback = new ApolloCallback<>(new ApolloCall.Callback<MyTestData.Data>() {
        @Override
        public void onResponse(@Nonnull Response<MyTestData.Data> response) {
            Log.e("Response", "" + response.data().getTodo());
            getTodosList.addAll(response.data().getTodo());


            for (int i = 0; i < getTodosList.size(); i++) {
                Log.e("Ename", getTodosList.get(i).item());
                Log.e("EId", "" + getTodosList.get(i).itemId());
                Log.e("ECompleted", "" + getTodosList.get(i).completed() + "\n\n");

            }


        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }, uiHandler);

    private void fetchFeed() {
        final MyTestData feedQuery = MyTestData.builder()
                .build();
        githuntFeedCall = application.apolloClient()
                .query(feedQuery)
                .cacheControl(CacheControl.NETWORK_FIRST);
        githuntFeedCall.enqueue(dataCallback);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (githuntFeedCall != null) {
            githuntFeedCall.cancel();
        }
    }

    @OnClick({R.id.btCreate, R.id.btGetList, R.id.btDelete, R.id.btUpdate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btCreate:
                Intent mIntent = new Intent(MainActivity.this, CreateEmployeeActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btGetList:
                Intent mIntentbtGetList = new Intent(MainActivity.this, GetListActivity.class);
                startActivity(mIntentbtGetList);

                break;
            case R.id.btDelete:
                Intent mIntentbtDelete = new Intent(MainActivity.this, DeleteEmployeeActivity.class);
                startActivity(mIntentbtDelete);

                break;
            case R.id.btUpdate:
                Intent mIntentbtUpdate = new Intent(MainActivity.this, UpdateEmployeeActivity.class);
                startActivity(mIntentbtUpdate);

                break;
        }
    }
}
