package com.example.androidlabs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidlabs.R;
import com.example.androidlabs.model.Message;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;

    public MessageListAdapter(List<Message> list, Context context) {
        setMessages(list);
        setContext(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_chat_message, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.message_content);
        ImageView imgView = convertView.findViewById(R.id.avatar);
        textView.setText(getItem(position).getText());

        configureMessageLayout(imgView, textView, getItem(position).isSend());

        return convertView;
    }

    private void configureMessageLayout(ImageView imgView, TextView textView, boolean isSend) {
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imgParams.addRule(isSend ? RelativeLayout.ALIGN_PARENT_END : RelativeLayout.ALIGN_PARENT_START);

        RelativeLayout.LayoutParams msgInputparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        msgInputparams.addRule(isSend ? RelativeLayout.START_OF : RelativeLayout.END_OF, R.id.avatar);
        msgInputparams.addRule(RelativeLayout.CENTER_VERTICAL);

        imgView.setLayoutParams(imgParams);
        imgView.setImageResource(isSend ? R.drawable.send_avatar : R.drawable.receive_avatar);
        textView.setLayoutParams(msgInputparams);
        textView.setTextAlignment(isSend ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);
    }
}
