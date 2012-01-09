package com.example;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts;

/**
 * Contact API implementation for SDK 3-4.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
@SuppressWarnings("deprecation")
public class ContactApiSdk3 extends ContactApi {

    @Override
    public String getColumnId() {
        return Contacts.People._ID;
    }

    @Override
    public String getColumnContactId() {
        return Contacts.Phones.PERSON_ID;
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
    public String getColumnGivenName() {
        return null;
    }

    @Override
    public String getColumnFamilyName() {
        return null;
    }

    @Override
    public Cursor queryContacts() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.People.CONTENT_URI;
        final String[] projection = new String[] {
                Contacts.People._ID,
                Contacts.People.DISPLAY_NAME };
        final String sort = Contacts.People.DISPLAY_NAME + " ASC";

        return mResolver.query(uri, projection, null, null, sort);
    }

    @Override
    public Cursor queryPhoneNumbers() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.Phones.CONTENT_URI;
        final String[] projection = new String[] {
                Contacts.Phones.PERSON_ID,
                Contacts.Phones.NUMBER };

        return mResolver.query(uri, projection, null, null, null);
    }

    @Override
    public Cursor queryEmailAddresses() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        final Uri uri = Contacts.ContactMethods.CONTENT_URI;
        final String[] projection = new String[] {
                Contacts.ContactMethods.PERSON_ID,
                Contacts.ContactMethods.DATA };

        return mResolver.query(uri, projection, null, null, null);
    }

    @Override
    public Cursor queryStructuredNames() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }

        // Structured name not supported in SDK < 5
        return null;
    }

    @Override
    public Bitmap queryPhotoById(long id) {
        Uri uri = ContentUris.withAppendedId(Contacts.People.CONTENT_URI, id);
        return Contacts.People.loadContactPhoto(mContext, uri, R.drawable.icon, null);
    }
}