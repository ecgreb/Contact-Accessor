package com.example;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author cgreb
 * @since 2011-07-15 14:29 ET
 */
public class ContactList extends ArrayList<ContactList.Contact> {

    private static final String TAG = "ContactList";

    private ContactApi mApi;

    public ContactList(ContactApi api) {
        super();
        mApi = api;
        importContacts();
    }

    private void importContacts() {
        Log.v(TAG, "Importing contacts...");
        Cursor c = mApi.queryContacts();
        String id, displayName;
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                if (isValidContact(c)) {
                    id = c.getString(c.getColumnIndex(mApi.getColumnId()));
                    displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
                    add(new Contact(id, displayName));
                }
            }
        }
        c.close();
    }

    private boolean isValidContact(Cursor c) {
        String displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
        if (TextUtils.isEmpty(displayName)) {
            return false;
        }

        return true;
    }

    List<String> importPhoneNumbersById(String id) {
        Cursor c = mApi.queryPhoneNumbers(id);
        ArrayList<String> phoneList = new ArrayList<String>();
        String phoneNumber;
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                phoneNumber = c.getString(c.getColumnIndex(mApi.getColumnPhoneNumber()));
                if (!TextUtils.isEmpty(phoneNumber)) {
                    phoneList.add(phoneNumber);
                }
            }
        }
        c.close();
        return phoneList;
    }

    List<String> importEmailAddressesById(String id) {
        Cursor c = mApi.queryEmailAddresses(id);
        ArrayList<String> emailList = new ArrayList<String>();
        String emailAddress;
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                emailAddress = c.getString(c.getColumnIndex(mApi.getColumnEmailAddress()));
                if (!TextUtils.isEmpty(emailAddress)) {
                    emailList.add(emailAddress);
                }
            }
        }
        c.close();
        return emailList;
    }

    public class Contact {
        private String mId;
        private String mDisplayName;

        public Contact(String id, String displayName) {
            mId = id;
            mDisplayName = displayName;
        }

        public String getDisplayName() {
            return mDisplayName;
        }

        public List<String> getPhoneNumbers() {
            return importPhoneNumbersById(mId);
        }

        public List<String> getEmailAddresses() {
            return importEmailAddressesById(mId);
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "displayName='" + mDisplayName + '\'' +
                    ", phoneList='" + getPhoneNumbers().toString() + '\'' +
                    ", emailList='" + getEmailAddresses().toString() + '\'' +
                    '}';
        }
    }

}
