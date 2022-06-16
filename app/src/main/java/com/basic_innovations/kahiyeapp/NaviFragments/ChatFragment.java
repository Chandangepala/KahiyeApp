package com.basic_innovations.kahiyeapp.NaviFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basic_innovations.kahiyeapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    RecyclerView recycVwChat;
    ArrayList<ChatModel> arrChats = new ArrayList<>();
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        recycVwChat = root.findViewById(R.id.recy_vw_chats);

        addChats("Chan", "Hi, How's you doing?","7:15PM","",R.mipmap.ic_launcher);
        addChats("Max", "Bye! see you.","8:15PM","",R.mipmap.ic_launcher);
        addChats("Alex", "Hi, New Song!","7:15AM","",R.mipmap.ic_launcher);
        addChats("Camelia", "Hi","7:15PM","",R.mipmap.ic_launcher);
        addChats("Sona", "Hello!","9:15PM","1",R.mipmap.ic_launcher);

        recycVwChat.setHasFixedSize(true);
        recycVwChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecycVwChatAdapter chatAdapter = new RecycVwChatAdapter(getContext(), arrChats);
        recycVwChat.setAdapter(chatAdapter);
        return root;
    }

    public void addChats(String name, String lastMsg, String time, String unreadCnt, int img){
        ChatModel chatModel = new ChatModel();
        chatModel.chatName = name;
        chatModel.chatLastmsg = lastMsg;
        chatModel.chatTime = time;
        chatModel.chatUnread = unreadCnt;
        chatModel.chatImg = img;

        arrChats.add(chatModel);
    }
}
