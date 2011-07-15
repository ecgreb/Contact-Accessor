/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link ContactAccessor} that uses current Contacts API.
 * This class should be used on Eclair or beyond, but would not work on any earlier
 * release of Android.  As a matter of fact, it could not even be loaded.
 * <p>
 * This implementation has several advantages:
 * <ul>
 * <li>It sees contacts from multiple accounts.
 * <li>It works with aggregated contacts. So for example, if the contact is the result
 * of aggregation of two raw contacts from different accounts, it may return the name from
 * one and the phone number from the other.
 * <li>It is efficient because it uses the more efficient current API.
 * <li>Not obvious in this particular example, but it has access to new kinds
 * of data available exclusively through the new APIs. Exercise for the reader: add support
 * for nickname (see {@link android.provider.ContactsContract.CommonDataKinds.Nickname}) or
 * social status updates (see {@link android.provider.ContactsContract.StatusUpdates}).
 * </ul>
 */
public class ContactAccessorSdk5 extends ContactAccessor {

    public ContactAccessorSdk5() {
        super();
    }

    public List<Contact> queryContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        final Uri uri = ContactsContract.Contacts.CONTENT_URI;
        final Cursor c = mContentResolver.query(uri, null, null, null, null);

        if (c.getCount() > 0) {
            String id, displayName;
            Contact contact;
            while (c.moveToNext()) {
                id = c.getString(c.getColumnIndex(ContactsContract.Data._ID));
                displayName = c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                contact = new Contact(id, displayName);
                contactList.add(contact);
            }
        }

        c.close();
        return contactList;
    }

    public List<String> queryPhoneNumbers(String id) {
        ArrayList<String> phoneList = new ArrayList<String>();
        final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);

        if (c.getCount() > 0) {
            String phoneNumber;
            while (c.moveToNext()) {
                phoneNumber = c.getString(c.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
                phoneList.add(phoneNumber);
            }
        }

        c.close();
        return phoneList;
    }

    public List<String> queryEmailAddresses(String id) {
        ArrayList<String> emailList = new ArrayList<String>();
        final Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        final String query = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);

        if (c.getCount() > 0) {
            String emailAddress;
            while (c.moveToNext()) {
                emailAddress = c.getString(c.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.DATA));
                emailList.add(emailAddress);
            }
        }

        c.close();
        return emailList;
    }

}