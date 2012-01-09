package com.example;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Sample activity for contact API.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
public class ContactActivity extends ListActivity {

    private static final String TAG = "ContactActivity";

    private LoadContactsTask mLoadContactsTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Debug.startMethodTracing("contactapi");

        ContactApi.instance.initContext(getApplicationContext());
        ContactApi.instance.initContentResolver(getContentResolver());
        ContactApi[] params = new ContactApi[] { ContactApi.instance };
        mLoadContactsTask = new LoadContactsTask();
        mLoadContactsTask.execute(params);
    }

    /**
     * Background task used to load contact data from device on non-UI thread.
     */
    private class LoadContactsTask extends AsyncTask<ContactApi, Void, ContactList> {
        long startTime, endTime;

        private final ProgressDialog dialog = new ProgressDialog(ContactActivity.this);

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
            Debug.startMethodTracing("InviteFriends");

            dialog.setMessage("Loading contacts...");
            dialog.show();
        }

        @Override
        protected ContactList doInBackground(ContactApi... params) {
            return new ContactList(params[0]);
        }

        @Override
        protected void onPostExecute(final ContactList contacts) {
            endTime = System.currentTimeMillis();
            Debug.stopMethodTracing();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            setListAdapter(new ContactArrayAdapter(ContactActivity.this, R.layout.list_item, contacts));

            Log.v(TAG, "---------------------- Benchmarks -----------------------");
            Log.v(TAG, "Number of contacts: " + contacts.size());
            Log.v(TAG, "Time to import: " + (endTime - startTime) + " ms");
            Log.v(TAG, "----------------------- Contacts ------------------------");
            for (ContactList.Contact contact : contacts) {
                Log.v(TAG, contact.toString());
            }
        }
    }

    private class ContactArrayAdapter extends ArrayAdapter<ContactList.Contact> {

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

                ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.photo);
                Bitmap bitmap = contact.getPhotoBitmap();
                if (bitmap == null) {
                    imgPhoto.setImageResource(R.drawable.ic_contact_picture);
                } else {
                    imgPhoto.setImageBitmap(bitmap);
                }
            }

            return convertView;
        }
    }
}
