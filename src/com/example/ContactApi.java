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

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;

/**
 * Abstract class that defines SDK-independent API for communication with the Contacts Provider.
 *
 * @author cgreb
 * @since 2011-07-12
 */
public abstract class ContactApi {

    /** Static singleton instance holding the SDK-specific implementation of the class. */
    private static ContactApi sInstance;

    /** Content resolver instance that will be used to execute a query. */
    protected ContentResolver mResolver;

    /**
     * Singleton accessor which returns existing {@link #sInstance} or creates new instance based
     * on current SDK.
     *
     * @return Instance of SDK-specific subclass.
     * @see <a href="http://developer.android.com/resources/articles/contacts.html">Using the Contacts API</a>
     */
    public static ContactApi getInstance() {
        if (sInstance == null) {
            @SuppressWarnings("deprecation")
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);

            String className;
            if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
                className = "com.example.ContactApiSdk3";
            } else {
                className = "com.example.ContactApiSdk5";
            }

            try {
                Class<? extends ContactApi> subclass =
                        Class.forName(className).asSubclass(ContactApi.class);
                sInstance = subclass.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return sInstance;
    }

    /**
     * Initialize the {@link ContentResolver} used to execute queries. The resolver instance  must
     * be initialized before calling {@link #queryContacts()}, {@link #queryPhoneNumbers(String)},
     * or {@link #queryEmailAddresses(String)}.
     *
     * @param contentResolver Reference to the parent application's {@link ContentResolver} obtained
     * by calling {@link android.app.Activity#getContentResolver()}
     */
    public void initContentResolver(ContentResolver contentResolver) {
        mResolver = contentResolver;
    }

    /**
     * Get the database _ID column.
     *
     * @return Cursor column name used to retrieve the row ID for the contact.
     */
    public abstract String getColumnId();

    /**
     * Get the database display name column.
     *
     * @return Cursor column name used to retrieve contact display name.
     */
    public abstract String getColumnDisplayName();

    /**
     * Get phone indicator column
     *
     * @return Cursor column used to determine if contact has at least one phone number
     */
    public abstract String getColumnPhoneIndicator();

    /**
     * Get the database phone number column.
     *
     * @return Cursor column name used to retrieve the phone number(s).
     */
    public abstract String getColumnPhoneNumber();

    /**
     * Get the database email address column.
     *
     * @return Cursor column name used to retrieve the email address(es).
     */
    public abstract String getColumnEmailAddress();

    /**
     * Get the database given name column.
     *
     * @return Cursor column name used to retrieve the given name.
     */
    public abstract String getColumnGivenName();

    /**
     * Get the database family name column.
     *
     * @return Cursor column name used to retrieve the family name.
     */
    public abstract String getColumnFamilyName();

    /**
     * Query all contacts on the device.
     *
     * @return Cursor used to iterate over all contacts.
     */
    public abstract Cursor queryContacts();

    /**
     * Query phone numbers for a single contact.
     *
     * @param contactId Contact row ID.
     * @return Cursor used to iterate over phone numbers for single contact.
     */
    public abstract Cursor queryPhoneNumbers(String contactId);

    /**
     * Query email addresses for a single contact.
     *
     * @param contactId Contact row ID.
     * @return Cursor used to iterate over email addresses for single contact.
     */
    public abstract Cursor queryEmailAddresses(String contactId);

    /**
     * Query structured name for a single contact.
     *
     * @param contactId Contact row ID.
     * @return Cursor used to iterate over structured name fields for single contact.
     */
    public abstract Cursor queryStructuredName(String contactId);

}
