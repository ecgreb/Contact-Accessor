package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;

@SuppressWarnings("deprecation")
public class ContactApiSdk3 extends ContactApi {

    @Override
    public String getColumnId() {
        return Contacts.People._ID;
    }

    @Override
    public String getColumnDisplayName() {
        return Contacts.People.DISPLAY_NAME;
    }

    @Override
    public String getColumnPhoneNumber() {
        return Contacts.Phones.NUMBER;
    }

    @Override
    public String getColumnEmailAddress() {
        return Contacts.ContactMethods.DATA;
    }

    @Override
    public Cursor queryContacts() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.People.CONTENT_URI;
        return mResolver.query(uri, null, null, null, null);
    }

    @Override
    public Cursor queryPhoneNumbers(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.Phones.CONTENT_URI;
        final String query = Contacts.Phones.PERSON_ID + " = ?";
        return mResolver.query(uri, null, query, new String[] { contactId }, null);
    }

    @Override
    public Cursor queryEmailAddresses(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.ContactMethods.CONTENT_EMAIL_URI;
        final String query = Contacts.ContactMethods.PERSON_ID + " = ?";
        return mResolver.query(uri, null, query, new String[] { contactId }, null);
    }

}