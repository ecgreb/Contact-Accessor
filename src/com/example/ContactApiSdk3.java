package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;

@SuppressWarnings("deprecation")
public class ContactApiSdk3 extends ContactApi {

    public ContactApiSdk3() {
        super();
    }

    @Override
    public String getColumnId() {
        return Contacts.People._ID;
    }

    @Override
    public String getColumnDisplayName() {
        return Contacts.People.DISPLAY_NAME;
    }

    @Override
    public String getColumnHasPhoneIndicator() {
        return Contacts.People.PRIMARY_PHONE_ID;
    }

    @Override
    public String getColumnPhoneNumber() {
        return Contacts.Phones.NUMBER;
    }

    @Override
    public String getColumnEmailIndicator() {
        return Contacts.People.PRIMARY_EMAIL_ID;
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
        return null;
    }

//    public List<String> queryPhoneNumbers(String id) {
//        final Uri uri = Contacts.Phones.CONTENT_URI;
//        final String query = Contacts.Phones.PERSON_ID + " = ?";
//        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);
//
//        String phoneNumber;
//        ArrayList<String> phoneList = new ArrayList<String>();
//
//        if (c.getCount() > 0) {
//            while (c.moveToNext()) {
//                phoneNumber = c.getString(c.getColumnIndex(Contacts.Phones.NUMBER));
//                phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
//                phoneList.add(phoneNumber);
//            }
//        }
//
//        c.close();
//        return phoneList;
//    }
//
//    public List<String> queryEmailAddresses(String id) {
//        final Uri uri = Contacts.ContactMethods.CONTENT_EMAIL_URI;
//        final String query = Contacts.ContactMethods.PERSON_ID + " = ?";
//        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);
//
//        String emailAddress;
//        ArrayList<String> emailList = new ArrayList<String>();
//
//        if(c.getCount() > 0) {
//            while (c.moveToNext()) {
//                emailAddress = c.getString(c.getColumnIndex(Contacts.ContactMethods.DATA));
//                emailList.add(emailAddress);
//            }
//        }
//
//        c.close();
//        return emailList;
//    }

}