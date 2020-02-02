package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlabs.adapters.MessageListAdapter;
import com.example.androidlabs.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        List<Message> messages = new ArrayList<>();

        ListView listView =  findViewById(R.id.list_view);

        BaseAdapter messageListAdapter = new MessageListAdapter(messages, getBaseContext());
        listView.setAdapter(messageListAdapter);

        Button sendBtn = findViewById(R.id.send_btn);
        EditText input = findViewById(R.id.message_input);

        sendBtn.setOnClickListener(getSendOrReceiveListener(true, messages, input, messageListAdapter));

        Button receiveBtn = findViewById(R.id.receive_btn);
        receiveBtn.setOnClickListener(getSendOrReceiveListener(false, messages, input, messageListAdapter));

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(getString(R.string.delete_confirm_msg))
                    .setMessage(getString(R.string.the_selected_row_is) + " " + position + "\n" +
                        getString(R.string.the_database_id_is) + " " + id)
                    .setPositiveButton(R.string.yes, (DialogInterface dialog, int which) -> {
                        messages.remove(position);
                        messageListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });
    }

    private View.OnClickListener getSendOrReceiveListener(boolean isSend, List<Message> messages, EditText input, BaseAdapter adapter) {
        return e -> {
            Message msg = new Message(input.getText().toString(), isSend);
            messages.add(msg);
            adapter.notifyDataSetChanged();
            input.setText("");
        };
    }
}
