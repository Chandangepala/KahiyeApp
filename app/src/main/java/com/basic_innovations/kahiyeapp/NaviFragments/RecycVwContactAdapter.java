package com.basic_innovations.kahiyeapp.NaviFragments;

import android.content.Context;
import android.content.Intent;
import android.telephony.RadioAccessSpecifier;
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

public class RecycVwContactAdapter extends RecyclerView.Adapter<RecycVwContactAdapter.ContactViewHolder> {

    Context context;
    ArrayList<ContactsModel> arrContacts;

    public RecycVwContactAdapter(Context context, ArrayList<ContactsModel> arrContacts) {
        this.context = context;
        this.arrContacts = arrContacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
            holder.nameTxt.setText(arrContacts.get(position).getContactName());
            holder.statusTxt.setText(arrContacts.get(position).getContactStatus());
            holder.contImg.setImageResource(R.drawable.always_logo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Clicked: "+arrContacts.get(position).contactName, Toast.LENGTH_SHORT).show();
                    Intent chatIntent = new Intent(context, Chats_Activity.class);
                    chatIntent.putExtra("userID", arrContacts.get(position).getContactID());
                    chatIntent.putExtra("recieverName", arrContacts.get(position).getContactName());
                    context.startActivity(chatIntent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        CircleImageView contImg;
        TextView nameTxt, statusTxt;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contImg = itemView.findViewById(R.id.contact_img);
            nameTxt = itemView.findViewById(R.id.cont_name_txt);
            statusTxt = itemView.findViewById(R.id.cont_status_txt);
        }
    }
}
