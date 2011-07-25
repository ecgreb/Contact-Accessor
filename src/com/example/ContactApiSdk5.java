package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.lang.reflect.Array;

public class ContactApiSdk5 extends ContactApi {

    @Override
    public String getColumnId() {
        return ContactsContract.Contacts._ID;
    }

    @Override
    public String getColumnContactId() {
        return ContactsContract.Data.CONTACT_ID;
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
    public String getColumnGivenName() {
        return ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME;
    }

    @Override
    public String getColumnFamilyName() {
        return ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME;
    }

    @Override
    public Cursor queryContacts() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Contacts.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME };
        return mResolver.query(uri, projection, null, null, null);
    }

    @Override
    public Cursor queryPhoneNumbers() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Data.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        final String query = ContactsContract.Data.MIMETYPE + " = ?";
        final String selection = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
        return mResolver.query(uri, projection, query, new String[] { selection }, null);
    }

    @Override
    public Cursor queryEmailAddresses() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Data.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Email.DATA };
        final String query = ContactsContract.Data.MIMETYPE + " = ?";
        final String selection = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
        return mResolver.query(uri, projection, query, new String[] { selection }, null);
    }

    @Override
    public Cursor queryStructuredNames() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Data.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME };
        final String query = ContactsContract.Data.MIMETYPE + " = ?";
        final String selection = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
        return mResolver.query(uri, projection, query, new String[] { selection }, null);
    }

}