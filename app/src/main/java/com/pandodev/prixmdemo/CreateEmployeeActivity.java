package com.pandodev.prixmdemo;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CreateEmployeeActivity extends AppCompatActivity {

    ProgressDialog progressDoalog;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FEED_SIZE = 5;

    MyApplication application;
    ViewGroup content;
    ProgressBar progressBar;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    ApolloCall<CreateTodo.Data> createTodoCall;





    @BindView(R.id.imgBck)
    ImageView imgBck;
    @BindView(R.id.editText2)
    EditText editTextName;
    @BindView(R.id.radioButtonJoined)
    RadioButton radioButtonJoined;
    @BindView(R.id.radioButtonNotJoined)
    RadioButton radioButtonNotJoined;
    @BindView(R.id.rgStatus)
    RadioGroup rgStatus;
    @BindView(R.id.buttonCreate)
    Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        ButterKnife.bind(this);



        progressDoalog = new ProgressDialog(CreateEmployeeActivity.this);
        progressDoalog.setMessage("Creating...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        application = (MyApplication) getApplication();

        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextName.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(CreateEmployeeActivity.this,"Please Enter Employee Name",5000).show();
                    return;
                }

                if (rgStatus.getCheckedRadioButtonId()==R.id.radioButtonJoined)
                {
                    createEmployee(editTextName.getText().toString().trim(), true);
                }
                else
                {
                    createEmployee(editTextName.getText().toString().trim(), false);
                }






            }
        });
    }




    private void createEmployee(String empName, boolean joiningStatus) {
        int unique_id= (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        progressDoalog.show();

        final CreateTodo feedQuery = CreateTodo.builder()
                .id(unique_id)
                .isCompleted(joiningStatus)
                .itemName(empName)
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

            if (response.data().createTodo().size()>0) {
                Toast.makeText(CreateEmployeeActivity.this, "Employee Created Successfully !", 5000).show();
                editTextName.setText("");
            }
            if ( progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            if ( progressDoalog.isShowing())
            {
                progressDoalog.dismiss();
            }
            Log.e(TAG, e.getMessage(), e);
        }
    }, uiHandler);





}
