package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactApiSdk5 extends ContactApi {

    @Override
    public String getColumnId() {
        return ContactsContract.Contacts._ID;
    }

    @Override
    public String getColumnDisplayName() {
        return ContactsContract.Contacts.DISPLAY_NAME;
    }

    @Override
    public String getColumnPhoneNumber() {
        return ContactsContract.CommonDataKinds.Phone.NUMBER;
    }

    @Override
    public String getColumnEmailAddress() {
        return ContactsContract.CommonDataKinds.Email.DATA;
    }

    @Override
    public Cursor queryContacts() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Contacts.CONTENT_URI;
        return mResolver.query(uri, null, null, null, null);
    }

    @Override
    public Cursor queryPhoneNumbers(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        return mResolver.query(uri, null, query, new String[] { contactId }, null);
    }

    @Override
    public Cursor queryEmailAddresses(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        final String query = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        return mResolver.query(uri, null, query, new String[] { contactId }, null);
    }
}