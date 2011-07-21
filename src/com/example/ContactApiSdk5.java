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
    public String getColumnPhoneIndicator() {
        return ContactsContract.Contacts.HAS_PHONE_NUMBER;
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
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER };
        return mResolver.query(uri, projection, null, null, null);
    }

    @Override
    public Cursor queryPhoneNumbers(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        return mResolver.query(uri, projection, query, new String[] { contactId }, null);
    }

    @Override
    public Cursor queryEmailAddresses(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        final String[] projection = new String[] { ContactsContract.CommonDataKinds.Email.DATA };
        final String query = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?";
        return mResolver.query(uri, projection, query, new String[] { contactId }, null);
    }

    @Override
    public Cursor queryStructuredName(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = ContactsContract.Data.CONTENT_URI;
        final String[] projection = new String[] {
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME };
        final String query = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'";

        return mResolver.query(uri, projection, query, new String[] { contactId }, null);
    }

}