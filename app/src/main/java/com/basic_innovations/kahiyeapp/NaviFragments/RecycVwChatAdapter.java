package com.basic_innovations.kahiyeapp.NaviFragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic_innovations.kahiyeapp.Chats_Activity;
import com.basic_innovations.kahiyeapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycVwChatAdapter extends RecyclerView.Adapter<RecycVwChatAdapter.ChatViewHolder> {

    Context context;
    ArrayList<ChatModel> arrChats;

    public RecycVwChatAdapter(Context context, ArrayList<ChatModel> arrChats) {
        this.context = context;
        this.arrChats = arrChats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        final ChatModel chatModel = arrChats.get(position);
        holder.nameTxt.setText(chatModel.chatName);
        holder.lastMsgTxt.setText(chatModel.chatLastmsg);
        holder.timeTxt.setText(chatModel.chatTime);
        holder.unreadTxt.setText(chatModel.chatUnread);
        holder.chatImg.setImageResource(R.mipmap.ic_launcher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(context, Chats_Activity.class);
                chatIntent.putExtra("userID", arrChats.get(position).chatUserID);
                chatIntent.putExtra("recieverName", arrChats.get(position).chatName);
                context.startActivity(chatIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrChats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        CircleImageView chatImg;
        TextView nameTxt, lastMsgTxt,timeTxt,unreadTxt;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatImg = itemView.findViewById(R.id.chat_img);
            nameTxt = itemView.findViewById(R.id.chat_name_txt);
            lastMsgTxt = itemView.findViewById(R.id.chat_lastMsg_txt);
            timeTxt = itemView.findViewById(R.id.chat_time_txt);
            unreadTxt = itemView.findViewById(R.id.chat_unreadCount_txt);
        }
    }
}
