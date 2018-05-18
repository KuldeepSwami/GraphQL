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

public class UpdateEmployeeActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FEED_SIZE = 5;

    MyApplication application;
    ViewGroup content;
    ProgressBar progressBar;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    ApolloCall<UpdateTodo.Data> createTodoCall;

    @BindView(R.id.imgBck)
    ImageView imgBck;
    @BindView(R.id.editTextID)
    EditText editTextID;
    @BindView(R.id.editText2)
    EditText editText2;
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
        setContentView(R.layout.activity_update_employee);
        ButterKnife.bind(this);
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


                if (editText2.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(UpdateEmployeeActivity.this,"Please Enter Employee ID",5000).show();
                    return;
                }

                if (editTextID.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(UpdateEmployeeActivity.this,"Please Enter Employee Name",5000).show();
                    return;
                }

                if (rgStatus.getCheckedRadioButtonId()==R.id.radioButtonJoined)
                {
                    createEmployee(Integer.parseInt(editTextID.getText().toString()),editText2.getText().toString().trim(), true);
                }
                else
                {
                    createEmployee(Integer.parseInt(editTextID.getText().toString()),editText2.getText().toString().trim(), false);
                }





            }
        });

    }




    private void createEmployee(int itmId, String empName, boolean joiningStatus) {

        final UpdateTodo feedQuery = UpdateTodo.builder()
                .itemId(itmId)
                .isCompleted(joiningStatus)
                .itemName(empName)
                .build();
        createTodoCall = application.apolloClient()
                .query(feedQuery)
                .cacheControl(CacheControl.NETWORK_FIRST);
        createTodoCall.enqueue(crateTodoCallback);

    }


    private ApolloCall.Callback<UpdateTodo.Data> crateTodoCallback = new ApolloCallback<>(new ApolloCall.Callback<UpdateTodo.Data>() {
        @Override
        public void onResponse(@Nonnull Response<UpdateTodo.Data> response) {
            Log.e("Response", "" + response.data().updateTodo());
            //  getTodosList.addAll(response.data().getTodo());

            if (response.data().updateTodo().size()>0) {
                Toast.makeText(UpdateEmployeeActivity.this, "Employee Updated Successfully !", 5000).show();
                editText2.setText("");
                editTextID.setText("");
            }

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }, uiHandler);





}
