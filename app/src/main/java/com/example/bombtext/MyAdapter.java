package com.example.bombtext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 李志军 on 2017/3/6.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Person> PersonList;
    private onDelListener listener;

    public MyAdapter(List<Person> PersonList,onDelListener listener) {
        this.PersonList = PersonList;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname, txtaddress, txtage;
        ImageView imgdel;

        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.txtname);
            txtaddress = (TextView) itemView.findViewById(R.id.txtaddress);
            txtage = (TextView) itemView.findViewById(R.id.txtage);
            imgdel = (ImageView) itemView.findViewById(R.id.imgdel);
        }

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        final Person person = PersonList.get(position);
        holder.txtname.setText(person.getName());
        holder.txtaddress.setText(person.getAddress());
        holder.txtage.setText(String.valueOf(person.getAge()));
        holder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        listener.refresh();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return PersonList.size();
    }
}
