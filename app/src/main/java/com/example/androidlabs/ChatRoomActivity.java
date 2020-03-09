package com.example.androidlabs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidlabs.adapters.MessageListAdapter;
import com.example.androidlabs.model.Message;
import com.example.androidlabs.openers.ChatDatabaseOpener;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        FrameLayout frameLayout = findViewById(R.id.frame_layout);

        List<Message> messages = new ArrayList<>();

        ChatDatabaseOpener db = new ChatDatabaseOpener(this);
        Cursor cursor = db.getReadableDatabase().query(ChatDatabaseOpener.MESSAGE_TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                String msgText = cursor.getString(cursor.getColumnIndex(ChatDatabaseOpener.MESSAGE_TABLE_TEXT_COL));
                Integer msgIsSend = cursor.getInt(cursor.getColumnIndex(ChatDatabaseOpener.MESSAGE_TABLE_IS_SEND_COL));
                Long id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseOpener.MESSAGE_TABLE_ID_COL));
                messages.add(new Message(msgText, msgIsSend == 1, id));

                cursor.moveToNext();
            }
        }

        printCursor(cursor, db.getReadableDatabase().getVersion());

        ListView listView =  findViewById(R.id.list_view);

        BaseAdapter messageListAdapter = new MessageListAdapter(messages, getBaseContext());
        listView.setAdapter(messageListAdapter);

        Button sendBtn = findViewById(R.id.send_btn);
        EditText input = findViewById(R.id.message_input);

        sendBtn.setOnClickListener(getSendOrReceiveListener(true, messages, input, messageListAdapter, db.getWritableDatabase()));

        Button receiveBtn = findViewById(R.id.receive_btn);
        receiveBtn.setOnClickListener(getSendOrReceiveListener(false, messages, input, messageListAdapter, db.getWritableDatabase()));

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(getString(R.string.delete_confirm_msg))
                    .setMessage(getString(R.string.the_selected_row_is) + " " + position + "\n" +
                        getString(R.string.the_database_id_is) + " " + id)
                    .setPositiveButton(R.string.yes, (DialogInterface dialog, int which) -> {
                        Long msgId = messageListAdapter.getItemId(position);
                        db.getWritableDatabase().delete(ChatDatabaseOpener.MESSAGE_TABLE_NAME,
                                ChatDatabaseOpener.MESSAGE_TABLE_ID_COL + " = ?",
                                new String[]{Long.toString(msgId)});

                        if(frameLayout != null) {
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if(fragment.getArguments().getLong("id") == msgId) {
                                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                    break;
                                }
                            }
                        }

                        messages.remove(position);
                        messageListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        });

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("message", messages.get(position).getText());
            bundle.putLong("id", id);
            bundle.putBoolean("isSendMsg", messages.get(position).isSend());

            if(frameLayout == null) {
                Intent intent = new Intent(this, EmptyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Fragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, detailsFragment).commit();
            }
        });
    }

    private View.OnClickListener getSendOrReceiveListener(boolean isSend, List<Message> messages, EditText input, BaseAdapter adapter, SQLiteDatabase db) {
        return e -> {
            ContentValues cValues = new ContentValues();
            cValues.put(ChatDatabaseOpener.MESSAGE_TABLE_IS_SEND_COL, isSend);
            cValues.put(ChatDatabaseOpener.MESSAGE_TABLE_TEXT_COL, input.getText().toString());

            Long id = db.insert(ChatDatabaseOpener.MESSAGE_TABLE_NAME, ChatDatabaseOpener.MESSAGE_TABLE_ID_COL, cValues);

            Message msg = new Message(input.getText().toString(), isSend, id);
            messages.add(msg);

            adapter.notifyDataSetChanged();
            input.setText("");
        };
    }

    private void printCursor(Cursor c, int version) {
        Log.i(this.toString(),"Database version: " + version);
        Log.i(this.toString(), "Number of columns in the cursor: " + c.getColumnCount());
        StringBuilder colNamesBuilder = new StringBuilder();
        for(String col : c.getColumnNames()) {
            colNamesBuilder.append(col).append(", ");
        }
        Log.i(this.toString(), "The names of the columns in the cursor: " + colNamesBuilder.toString());
        Log.i(this.toString(), "The number of results in the cursor: " + c.getCount());

        if(c.moveToFirst()) {
            StringBuilder resBuilder = new StringBuilder();
            while(!c.isAfterLast()) {
                for(String col : c.getColumnNames()) {
                    resBuilder.append(c.getString(c.getColumnIndex(col)))
                        .append(", ");
                }
                resBuilder.append("\n");
                c.moveToNext();
            }
            Log.i(this.toString(), "Result: \n" + resBuilder.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
