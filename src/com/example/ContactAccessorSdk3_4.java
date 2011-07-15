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
import android.provider.Contacts;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link ContactAccessor} that uses legacy Contacts API.
 * These APIs are deprecated and should not be used unless we are running on a
 * pre-Eclair SDK.
 * <p>
 * There are several reasons why we wouldn't want to use this class on an Eclair device:
 * <ul>
 * <li>It would see at most one account, namely the first Google account created on the device.
 * <li>It would work through a compatibility layer, which would make it inherently less efficient.
 * <li>Not relevant to this particular example, but it would not have access to new kinds
 * of data available through current APIs.
 * </ul>
 */
@SuppressWarnings("deprecation")
public class ContactAccessorSdk3_4 extends ContactAccessor {

    public ContactAccessorSdk3_4() {
        super();
    }

    public List<Contact> queryContacts() {
        final Uri uri = Contacts.People.CONTENT_URI;
        final Cursor c = mContentResolver.query(uri, null, null, null, null);

        String id, displayName;
        Contact contact;
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                id = c.getString(c.getColumnIndex(Contacts.People._ID));
                displayName = c.getString(c.getColumnIndex(Contacts.People.DISPLAY_NAME));
                contact = new Contact(id, displayName);
                contactList.add(contact);
            }
        }

        c.close();
        return contactList;
    }

    public List<String> queryPhoneNumbers(String id) {
        final Uri uri = Contacts.Phones.CONTENT_URI;
        final String query = Contacts.Phones.PERSON_ID + " = ?";
        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);

        String phoneNumber;
        ArrayList<String> phoneList = new ArrayList<String>();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                phoneNumber = c.getString(c.getColumnIndex(Contacts.Phones.NUMBER));
                phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
                phoneList.add(phoneNumber);
            }
        }

        c.close();
        return phoneList;
    }

    public List<String> queryEmailAddresses(String id) {
        final Uri uri = Contacts.ContactMethods.CONTENT_EMAIL_URI;
        final String query = Contacts.ContactMethods.PERSON_ID + " = ?";
        final Cursor c = mContentResolver.query(uri, null, query, new String[] { id }, null);

        String emailAddress;
        ArrayList<String> emailList = new ArrayList<String>();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                emailAddress = c.getString(c.getColumnIndex(Contacts.ContactMethods.DATA));
                emailList.add(emailAddress);
            }
        }

        c.close();
        return emailList;
    }

}