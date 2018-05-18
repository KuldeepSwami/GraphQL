package com.pandodev.prixmdemo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.cache.normalized.CacheControl;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GetListActivity extends AppCompatActivity {


    EmployeeListAdapter employeeListAdapter;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FEED_SIZE = 5;


    MyApplication application;
    ViewGroup content;
    ProgressBar progressBar;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    ApolloCall<MyTestData.Data> githuntFeedCall;
    List<MyTestData.GetTodo> getTodosList;
    ApolloCall<CreateTodo.Data> createTodoCall;


    @BindView(R.id.chat_recycleview)
    RecyclerView chatRecycleview;
    @BindView(R.id.imgBck)
    ImageView imgBck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_list);
        ButterKnife.bind(this);
        application = (MyApplication) getApplication();
        employeeListAdapter = new EmployeeListAdapter(this);
        chatRecycleview.setAdapter(employeeListAdapter);
        chatRecycleview.setLayoutManager(new LinearLayoutManager(this));


        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        fetchFeed();
    }


    private ApolloCall.Callback<MyTestData.Data> dataCallback = new ApolloCallback<>(new ApolloCall.Callback<MyTestData.Data>() {
        @Override
        public void onResponse(@Nonnull Response<MyTestData.Data> response) {
            Log.e("Response", "" + response.data().getTodo());
            employeeListAdapter.setFeed(response.data().getTodo());
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


}
