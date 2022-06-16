package com.basic_innovations.kahiyeapp.NaviFragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic_innovations.kahiyeapp.Chats_Activity;
import com.basic_innovations.kahiyeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycVwAlwaysAdapter extends RecyclerView.Adapter<RecycVwAlwaysAdapter.AlwaysViewHolder> {

    Context context;
    ArrayList<AlwaysModel> arrAlways;

    public RecycVwAlwaysAdapter(Context context, ArrayList<AlwaysModel> arrAlways) {
        this.context = context;
        this.arrAlways = arrAlways;
    }

    @NonNull
    @Override
    public AlwaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlwaysViewHolder(LayoutInflater.from(context).inflate(R.layout.always_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlwaysViewHolder holder, final int position) {
        final AlwaysModel alwaysModel = arrAlways.get(position);
        holder.nameTxt.setText(alwaysModel.getAlwName());
        holder.timeTxt.setText(alwaysModel.getAlwTime());

        Picasso.with(context).load(arrAlways.get(position).getAlwImg())
                .resize(200,300)
                .into(holder.alwImg);

        holder.alwProfImg.setImageResource(R.mipmap.ic_launcher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent chatIntent = new Intent(context, AlwaysFull_ImgActivity.class);
            chatIntent.putExtra("imgUrl", arrAlways.get(position).getAlwImg());
            context.startActivity(chatIntent);
               // Toast.makeText(context, alwaysModel.alwName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrAlways.size();
    }

    public class AlwaysViewHolder extends RecyclerView.ViewHolder {

        CircleImageView alwProfImg;
        ImageView alwImg;
        TextView nameTxt,timeTxt;
        public AlwaysViewHolder(@NonNull View itemView) {
            super(itemView);
            alwImg = itemView.findViewById(R.id.always_img);
            alwProfImg = itemView.findViewById(R.id.alw_profile_img);
            nameTxt = itemView.findViewById(R.id.always_name);
            timeTxt = itemView.findViewById(R.id.always_time);

        }
    }
}
