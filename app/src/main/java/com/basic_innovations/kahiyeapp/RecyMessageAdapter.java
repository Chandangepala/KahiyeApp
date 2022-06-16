package com.basic_innovations.kahiyeapp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyMessageAdapter extends RecyclerView.Adapter<RecyMessageAdapter.MessageViewHolder> {

    Context context;
    ArrayList<ChatMessage> arrMessages;

    public RecyMessageAdapter(Context context, ArrayList<ChatMessage> arrMessages) {
        this.context = context;
        this.arrMessages = arrMessages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            holder.nameTxt.setText(arrMessages.get(position).getName());
            holder.messageTxt.setText(arrMessages.get(position).getText());

        ChatMessage message = arrMessages.get(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            // Toast.makeText(getContext(),"Photo",Toast.LENGTH_SHORT).show();
            holder.messageTxt.setVisibility(View.GONE);
            holder.imgVw.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(message.getPhotoUrl())
                    .placeholder(R.drawable.always_logo)
                    .into(holder.imgVw);
            //.transform(new RoundedTransformation(50, 4))
        } else {
            //Toast.makeText(getContext(),"NO",Toast.LENGTH_SHORT).show();
            holder.messageTxt.setVisibility(View.VISIBLE);
            holder.imgVw.setVisibility(View.GONE);
            holder.messageTxt.setText(message.getText());
        }
        holder.nameTxt.setText(message.getName());
        String author = (String) message.getName();
        ///current user name
        String mUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (author != null && author.equals(mUserName)) {
            //messageTextView.setGravity(Gravity.RIGHT | Gravity.END);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            holder.messageTxt.setLayoutParams(params);
            holder.messageTxt.setBackgroundResource(R.drawable.msgbox);
            holder.imgVw.setLayoutParams(params);

            holder.nameTxt.setGravity(Gravity.RIGHT | Gravity.END);
            holder.nameTxt.setTextColor(Color.parseColor("#FFFFFF"));
            holder.messageTxt.setTextColor(Color.parseColor("#000000"));
        } else{
            // messageTextView.setGravity(Gravity.LEFT | Gravity.START);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            holder.messageTxt.setLayoutParams(params);
            holder.messageTxt.setBackgroundResource(R.drawable.msgbox_a);
            holder.messageTxt.setTextColor(Color.parseColor("#000000"));

            holder.imgVw.setLayoutParams(params);
            holder.nameTxt.setGravity(Gravity.RIGHT | Gravity.START);
            holder.nameTxt.setTextColor(Color.parseColor("#FFFFFF"));

        }

    }

    @Override
    public int getItemCount() {
        return arrMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTxt, nameTxt;
        ImageView imgVw;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTxt = itemView.findViewById(R.id.messageTextView);
            nameTxt = itemView.findViewById(R.id.nameTextView);
            imgVw = itemView.findViewById(R.id.photoImageView);
        }
    }

    public void msgBoxAlign(){


    }
}
