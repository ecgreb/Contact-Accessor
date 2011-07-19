package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactApiSdk5 extends ContactApi {

    public ContactApiSdk5() {
        super();
    }

    @Override
    public String getColumnId() {
        return ContactsContract.Contacts._ID;
    }

    @Override
    public String getColumnDisplayName() {
        return ContactsContract.Contacts.DISPLAY_NAME;
    }

    @Override
    public String getColumnHasPhoneIndicator() {
        return ContactsContract.Contacts.HAS_PHONE_NUMBER;
    }

    @Override
    public String getColumnPhoneNumber() {
        return ContactsContract.CommonDataKinds.Phone.NUMBER;
    }

    @Override
    public String getColumnEmailIndicator() {
        return null; // Shit there is none!
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
        return null;
    }

//    public List<String> queryPhoneNumbers(String id) {
//        ArrayList<String> phoneList = new ArrayList<String>();
//        final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
//        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);
//
//        if (c.getCount() > 0) {
//            String phoneNumber;
//            while (c.moveToNext()) {
//                phoneNumber = c.getString(c.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.NUMBER));
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
//        ArrayList<String> emailList = new ArrayList<String>();
//        final Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
//        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
//        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);
//
//        if (c.getCount() > 0) {
//            String emailAddress;
//            while (c.moveToNext()) {
//                emailAddress = c.getString(c.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Email.DATA));
//                emailList.add(emailAddress);
//            }
//        }
//
//        c.close();
//        return emailList;
//    }

}