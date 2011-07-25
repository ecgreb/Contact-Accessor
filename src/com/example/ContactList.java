package com.example;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String, List<String>> mEmailMap = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> mPhoneMap = new HashMap<String, List<String>>();
    private HashMap<String, StructuredName> mNameMap = new HashMap<String, StructuredName>();

    public ContactList(ContactApi api) {
        super();
        mApi = api;
        importPhoneNumbers();
        importEmailAddresses();
        importStructuredNames();
        importContacts();
    }

    private void importPhoneNumbers() {
        Cursor c = mApi.queryPhoneNumbers();
        if (c != null) {
            String id, phone;
            int count = c.getCount();
            if (count > 0) {
                while (c.moveToNext()) {
                    id = c.getString(c.getColumnIndex(mApi.getColumnContactId()));
                    phone = c.getString(c.getColumnIndex(mApi.getColumnPhoneNumber()));
                    phone = phone.replaceAll("[^\\d]", "");
                    ArrayList<String> entry = (ArrayList<String>) mPhoneMap.get(id);
                    if (entry == null) {
                        entry = new ArrayList<String>();
                    }
                    entry.add(phone);
                    mPhoneMap.put(id, entry);
                }
            }
            Log.v(TAG, "Phone Map Size = " + mPhoneMap.size());
            Log.v(TAG, "Phone Map = " + mPhoneMap.toString());
            c.close();
        }
    }

    private void importEmailAddresses() {
        Cursor c = mApi.queryEmailAddresses();
        if (c != null) {
            String id, email;
            int count = c.getCount();
            if (count > 0) {
                while (c.moveToNext()) {
                    id = c.getString(c.getColumnIndex(mApi.getColumnContactId()));
                    email = c.getString(c.getColumnIndex(mApi.getColumnEmailAddress()));
                    ArrayList<String> entry = (ArrayList<String>) mEmailMap.get(id);
                    if (entry == null) {
                        entry = new ArrayList<String>();
                    }
                    entry.add(email);
                    mEmailMap.put(id, entry);
                }
            }
            Log.v(TAG, "Email Map Size = " + mEmailMap.size());
            Log.v(TAG, "Email Map = " + mEmailMap.toString());
            c.close();
        }
    }

    private void importStructuredNames() {
        Cursor c = mApi.queryStructuredNames();
        if (c != null) {
            String contactId;
            StructuredName name = new StructuredName();
            if (c.getCount() > 0) {

                while (c.moveToNext()) {
                    contactId = c.getString(c.getColumnIndex(mApi.getColumnContactId()));
                    name.givenName = c.getString(c.getColumnIndex(mApi.getColumnGivenName()));
                    name.familyName = c.getString(c.getColumnIndex(mApi.getColumnFamilyName()));
                    mNameMap.put(contactId, name);
                }
            }
            Log.v(TAG, "Name Map Size = " + mNameMap.size());
            Log.v(TAG, "Name Map = " + mNameMap.toString());
            c.close();
        }
    }

    private void importContacts() {
        Cursor c = mApi.queryContacts();
        if (c != null) {
            String id, displayName;
            Log.v(TAG, "Contacts Base Count = " + c.getCount());
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    id = c.getString(c.getColumnIndex(mApi.getColumnId()));
                    displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
                    if (isValidContact(id, displayName)) {
                        add(new Contact(id, displayName));
                    }
                }
            }
            Log.v(TAG, "Contact List Size = " + this.size());
            Log.v(TAG, "Contact List = " + this.toString());
            c.close();
        }
    }

    private boolean isValidContact(String id, String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return false;
        }

        boolean hasPhoneNumber = false;
        if (mPhoneMap != null) {
            hasPhoneNumber = (mPhoneMap.get(id) != null);
        }

        boolean hasEmailAddress = false;
        if (mEmailMap != null) {
            hasEmailAddress = (mEmailMap.get(id) != null);
        }

        return hasPhoneNumber || hasEmailAddress;
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
            return mPhoneMap.get(mId);
        }

        public List<String> getEmailAddresses() {
            return mEmailMap.get(mId);
        }

        public StructuredName getStructuredName() {
            return mNameMap.get(mId);
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "id='" + mId + '\'' +
                    ", displayName='" + mDisplayName + '\'' +
                    ", phoneNumbers='" + getPhoneNumbers() + '\'' +
                    ", emailAddresses='" + getEmailAddresses() + '\'' +
                    ", structuredName='" + getStructuredName() + '\'' +
                    '}';
        }
    }

    public static class StructuredName {
        public String givenName = "";
        public String familyName = "";

        @Override
        public String toString() {
            return "StructuredName{" +
                    "givenName='" + givenName + '\'' +
                    ", familyName='" + familyName + '\'' +
                    '}';
        }
    }

}
