package com.pandodev.prixmdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.cache.normalized.CacheControl;
import com.apollographql.apollo.exception.ApolloException;

import java.util.Date;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteEmployeeActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FEED_SIZE = 5;

    MyApplication application;
    ViewGroup content;
    ProgressBar progressBar;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    ApolloCall<DeleteTodo.Data> createTodoCall;




    @BindView(R.id.imgBck)
    ImageView imgBck;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.buttonDelete)
    Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_employee);
        ButterKnife.bind(this);

        application = (MyApplication) getApplication();

        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText2.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(DeleteEmployeeActivity.this,"Please Enter Employee ID",5000).show();
                    return;
                }

                createEmployee(Integer.parseInt(editText2.getText().toString()));

            }
        });

    }




    private void createEmployee(int unique_id) {

        final DeleteTodo feedQuery = DeleteTodo.builder()
                .id(unique_id)
                .build();
        createTodoCall = application.apolloClient()
                .query(feedQuery)
                .cacheControl(CacheControl.NETWORK_FIRST);
        createTodoCall.enqueue(crateTodoCallback);

    }


    private ApolloCall.Callback<DeleteTodo.Data> crateTodoCallback = new ApolloCallback<>(new ApolloCall.Callback<DeleteTodo.Data>() {
        @Override
        public void onResponse(@Nonnull Response<DeleteTodo.Data> response) {
            Log.e("Response", "" + response.data().removeTodo());
            //  getTodosList.addAll(response.data().getTodo());

            if (response.data().removeTodo().size()==0) {
                Toast.makeText(DeleteEmployeeActivity.this, "Employee Deleted Successfully !", 5000).show();
                editText2.setText("");
            }

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }, uiHandler);


}
