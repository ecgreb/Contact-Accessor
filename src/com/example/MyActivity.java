package com.example;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        ContactAccessor contactAccessor = ContactAccessor.getInstance();
        contactAccessor.setContentResolver(getContentResolver());

        Log.v(TAG, "Got instance of " + contactAccessor.getClass().getSimpleName());

        List<ContactAccessor.Contact> contactList = contactAccessor.queryContacts();
        List<String> phoneList, emailList;

        Log.v(TAG, "---------------------- Contacts -----------------------");
        for (ContactAccessor.Contact contact : contactList) {
            phoneList = contactAccessor.queryPhoneNumbers(contact.id);
            emailList = contactAccessor.queryEmailAddresses(contact.id);
            Log.v(TAG, contact.toString() +
                    " Phone: " + phoneList.toString() +
                    " Email: " + emailList.toString());
        }

        setListAdapter(new ContactArrayAdapter(this, R.layout.list_item, contactList));

    }

    private class ContactArrayAdapter extends ArrayAdapter<ContactAccessor.Contact>
            implements View.OnClickListener {

        private List<ContactAccessor.Contact> mContacts;

        public ContactArrayAdapter(Context context, int id, List<ContactAccessor.Contact> contacts) {
            super(context, id, contacts);
            mContacts = contacts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            ContactAccessor.Contact contact = mContacts.get(position);
            if (contact != null) {
                TextView txtName = (TextView) convertView.findViewById(R.id.name);
                txtName.setText(contact.displayName);
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
