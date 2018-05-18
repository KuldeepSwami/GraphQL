package com.pandodev.prixmdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {
    Context context;
    List<MyTestData.GetTodo> chatModel = new ArrayList<>();


    public EmployeeListAdapter(Context context) {
        this.context = context;
    }

    public void setFeed(List<MyTestData.GetTodo> todo) {
        this.chatModel = todo;
        this.notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.empId)
        TextView empId;
        @BindView(R.id.empName)
        TextView empName;
        @BindView(R.id.isJoined)
        TextView isJoined;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_emp_lits_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.empId.setText("Emp ID : "+chatModel.get(position).itemId());
        holder.empName.setText("Name : "+chatModel.get(position).item());
        holder.isJoined.setText("Joining Status : "+chatModel.get(position).completed());

    }

    @Override
    public int getItemCount() {
        return chatModel.size();
    }
}
