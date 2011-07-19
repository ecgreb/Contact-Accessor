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
        String id, displayName, hasPhone;
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                id = c.getString(c.getColumnIndex(mApi.getColumnId()));
                displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
                hasPhone = c.getString(c.getColumnIndex(mApi.getColumnHasPhoneIndicator()));

                Log.v(TAG, "id=" + id + ", name=" + displayName + ", hasPhone=" + hasPhone);

                if (isContactValid(displayName, hasPhone)) {
                    add(new Contact(id, displayName));
                }
            }
        }
        c.close();
    }

    private boolean isContactValid(String displayName, String hasPhoneIndicator) {
        if (TextUtils.isEmpty(displayName)) {
            return false;
        } else if (TextUtils.isEmpty(hasPhoneIndicator)) {
            return false;
        } else if (Integer.parseInt(hasPhoneIndicator) == 0) {
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

        @Override
        public String toString() {
            return "Contact{" +
                    "displayName='" + mDisplayName + '\'' +
                    ", phoneList='" + getPhoneNumbers().toString() + '\'' +
                    '}';
        }
    }

}
