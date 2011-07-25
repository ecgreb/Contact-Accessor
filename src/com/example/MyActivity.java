package com.example;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyActivity extends ListActivity {

    private static final String TAG = "MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        Debug.startMethodTracing("contactapi");

        Void[] params = new Void[] { };
        new LoadContactsTask().execute(params);

    }

    private class LoadContactsTask extends AsyncTask<Void, Void, ContactList> {
        long startTime, endTime, logTime, uiTime;
        private final ProgressDialog dialog = new ProgressDialog(MyActivity.this);

        protected void onPreExecute() {
            dialog.setMessage("Loading contacts...");
            dialog.show();
        }

        protected ContactList doInBackground(Void... params) {
            startTime = System.currentTimeMillis();

            ContactApi api = ContactApi.getInstance();
            api.initContentResolver(getContentResolver());
            return new ContactList(api);
        }

        protected void onPostExecute(ContactList contactList) {
            endTime = System.currentTimeMillis();

            Log.v(TAG, "---------------------- Contacts -----------------------");
            for (ContactList.Contact contact : contactList) {
                Log.v(TAG, contact.toString());
            }

            logTime = System.currentTimeMillis();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            setListAdapter(new ContactArrayAdapter(MyActivity.this.getApplicationContext(),
                    R.layout.list_item, contactList));

            uiTime = System.currentTimeMillis();
//            Debug.stopMethodTracing();

            Log.v(TAG, "---------------------- Benchmarks -----------------------");
            Log.v(TAG, "Generate List: " + (endTime - startTime));
            Log.v(TAG, "Log Details: " + (logTime - endTime));
            Log.v(TAG, "Populate UI: " + (uiTime - logTime));
        }
    }

    private class ContactArrayAdapter extends ArrayAdapter<ContactList.Contact>
            implements View.OnClickListener {

        private List<ContactList.Contact> mContacts;

        public ContactArrayAdapter(Context context, int id, List<ContactList.Contact> contacts) {
            super(context, id, contacts);
            mContacts = contacts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            ContactList.Contact contact = mContacts.get(position);
            if (contact != null) {
                TextView txtName = (TextView) convertView.findViewById(R.id.name);
                txtName.setText(contact.getDisplayName());
            }

            convertView.setOnClickListener(this);

            return convertView;
        }

        public void onClick(View view) {
            Log.v(TAG, "onCLick!");

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "onClick!", Toast.LENGTH_SHORT);
                }
            });
        }
    }
}
