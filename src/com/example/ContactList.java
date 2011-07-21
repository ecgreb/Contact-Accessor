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

    private ContactApi mApi;

    public ContactList(ContactApi api) {
        super();
        mApi = api;
        importContacts();
    }

    private void importContacts() {
        Cursor c = mApi.queryContacts();
        String id, displayName, hasPhone;
        Log.v("ContactList", "Contacts Base Count (pre-filter) = " + c.getCount());
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                id = c.getString(c.getColumnIndex(mApi.getColumnId()));
                displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
                hasPhone = c.getString(c.getColumnIndex(mApi.getColumnPhoneIndicator()));
                if (isValidContact(id, displayName, hasPhone)) {
                    add(new Contact(id, displayName));
                }
            }
        }
        c.close();
    }

    private boolean isValidContact(String id, String displayName, String hasPhone) {
        if (TextUtils.isEmpty(displayName)) {
            return false;
        }

        boolean hasPhoneNumber = (hasPhone != null && Integer.parseInt(hasPhone) > 0);

        Cursor emailCursor = mApi.queryEmailAddresses(id);
        boolean hasEmailAddress = (emailCursor.getCount() > 0);
        emailCursor.close();

        return hasPhoneNumber || hasEmailAddress;
    }

    List<String> importPhoneNumbersById(String id) {
        Cursor c = mApi.queryPhoneNumbers(id);
        ArrayList<String> phoneList = new ArrayList<String>();
        String phoneNumber;
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    phoneNumber = c.getString(c.getColumnIndex(mApi.getColumnPhoneNumber()));
                    phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        phoneList.add(phoneNumber);
                    }
                }
            }
            c.close();
        }
        return phoneList;
    }

    List<String> importEmailAddressesById(String id) {
        Cursor c = mApi.queryEmailAddresses(id);
        ArrayList<String> emailList = new ArrayList<String>();
        String emailAddress;
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    emailAddress = c.getString(c.getColumnIndex(mApi.getColumnEmailAddress()));
                    if (!TextUtils.isEmpty(emailAddress)) {
                        emailList.add(emailAddress);
                    }
                }
            }
            c.close();
        }
        return emailList;
    }

    StructuredName importStructuredName(String id) {
        Cursor c = mApi.queryStructuredName(id);
        StructuredName name = new StructuredName();
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    name.givenName = c.getString(c.getColumnIndex(mApi.getColumnGivenName()));
                    name.familyName = c.getString(c.getColumnIndex(mApi.getColumnFamilyName()));
                }
            }
            c.close();
        }
        return name;
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

        public StructuredName getStructuredName() {
            return importStructuredName(mId);
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "id='" + mId + '\'' +
                    ", displayName='" + mDisplayName + '\'' +
                    ", phoneNumbers='" + getPhoneNumbers().toString() + '\'' +
                    ", emailAddresses='" + getEmailAddresses().toString() + '\'' +
                    ", structuredName='" + getStructuredName().toString() + '\'' +
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
