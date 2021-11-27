package com.example.courseorganiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ParticipantAdapter extends ArrayAdapter<ParticipantModel> {
    private Context mContext;
    private int mResource;
    public ParticipantAdapter(@NonNull Context context, int resource, @NonNull List<ParticipantModel> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String name=getItem(position).getName();
        boolean payed =getItem(position).isPayed();

        ParticipantModel participant=new ParticipantModel(name,payed);
        LayoutInflater inflater=LayoutInflater.from(mContext);

        convertView=inflater.inflate(mResource,parent,false);
        TextView tv_name=(TextView) convertView.findViewById(R.id.tv_participantName);
        TextView tv_isPayed=(TextView) convertView.findViewById(R.id.tv_participantIsPayed);

        tv_name.setText(name);
        String isPayed="";
        if(payed)
            isPayed="Payed";
        tv_isPayed.setText(isPayed);

        return convertView;
    }
    public void getParticipantNames(int position){

    }
}
