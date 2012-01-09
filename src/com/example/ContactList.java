package com.example;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Data structure to parse and store contacts on the device. Uses SDK specific implementation of
 * {@link ContactApi} to access database.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
public class ContactList extends ArrayList<ContactList.Contact> {

    /** Reference to SDK-specific implementation of the Contacts API */
    private ContactApi mApi;

    /** Map used to lookup phone numbers by contact ID */
    HashMap<Integer, HashSet<String>> mPhoneMap = new HashMap<Integer, HashSet<String>>();

    /** Map used to lookup email addresses by contact ID */
    HashMap<Integer, HashSet<String>> mEmailMap = new HashMap<Integer, HashSet<String>>();

    /** Map used to lookup given/family name by contact ID */
    HashMap<Integer, StructuredName> mNameMap = new HashMap<Integer, StructuredName>();

    /**
     * Create new {@link ContactList} object. Executes database queries to populate all data fields
     * on init.
     *
     * @param api SDK-specific implementation of {@link ContactApi}.
     */
    public ContactList(ContactApi api) {
        super();
        mApi = api;
        importPhoneNumbers();
        importEmailAddresses();
        importStructuredNames();
        importContacts();
    }

    /**
     * Imports phone numbers from device DB and stores as map of ArrayLists keyed by contact ID.
     */
    private void importPhoneNumbers() {
        Cursor c = mApi.queryPhoneNumbers();
        if (c != null) {
            int id;
            String phone;
            int count = c.getCount();
            if (count > 0) {
                while (c.moveToNext()) {
                    id = c.getInt(c.getColumnIndex(mApi.getColumnContactId()));
                    phone = c.getString(c.getColumnIndex(mApi.getColumnPhoneNumber()));
                    phone = phone.replaceAll("[^\\d]", "");
                    HashSet<String> entry = (HashSet<String>) mPhoneMap.get(id);
                    if (entry == null) {
                        entry = new HashSet<String>();
                    }
                    entry.add(phone);
                    mPhoneMap.put(id, entry);
                }
            }
            c.close();
        }
    }

    /**
     * Imports email addresses from device DB and stores as map of ArrayLists keyed by contact ID.
     */
    private void importEmailAddresses() {
        Cursor c = mApi.queryEmailAddresses();
        if (c != null) {
            int id;
            String email;
            int count = c.getCount();
            if (count > 0) {
                while (c.moveToNext()) {
                    id = c.getInt(c.getColumnIndex(mApi.getColumnContactId()));
                    email = c.getString(c.getColumnIndex(mApi.getColumnEmailAddress()));
                    HashSet<String> entry = (HashSet<String>) mEmailMap.get(id);
                    if (entry == null) {
                        entry = new HashSet<String>();
                    }
                    entry.add(email);
                    mEmailMap.put(id, entry);
                }
            }
            c.close();
        }
    }

    /**
     * Imports given/family names from device DB and stores as map of {@link StructuredName} objects
     * keyed by contact ID.
     */
    private void importStructuredNames() {
        Cursor c = mApi.queryStructuredNames();
        if (c != null) {
            int id;
            StructuredName name;
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    name = new StructuredName();
                    name.givenName = c.getString(c.getColumnIndex(mApi.getColumnGivenName()));
                    name.familyName = c.getString(c.getColumnIndex(mApi.getColumnFamilyName()));
                    id = c.getInt(c.getColumnIndex(mApi.getColumnContactId()));
                    mNameMap.put(id, name);
                }
            }
            c.close();
        }
    }

    /**
     * Imports all contacts in device DB and adds them to {@link ContactList}. Invalid contacts
     * are not added.
     *
     * @see {@link #isValidContact(int, String)}
     */
    private void importContacts() {
        Cursor c = mApi.queryContacts();
        if (c != null) {
            int id;
            String displayName;
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    id = c.getInt(c.getColumnIndex(mApi.getColumnId()));
                    displayName = c.getString(c.getColumnIndex(mApi.getColumnDisplayName()));
                    if (isValidContact(id, displayName)) {
                        add(new Contact(id, displayName));
                    }
                }
            }
            c.close();
        }
    }

    /**
     * Determines validity of an individual contact. A contact is invalid if
     * <ol>
     *     <li>Contact display name is empty.</li>
     *     <li>Contact does not have at least one phone number OR one email address.</li>
     * </ol>
     * @param id Contact database row ID.
     * @param displayName Contact display name.
     * @return False if contact fails either test, otherwise true.
     */
    private boolean isValidContact(int id, String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return false;
        }

        boolean hasPhoneNumber = false;
        if (mPhoneMap != null) {
            hasPhoneNumber = (mPhoneMap.get(id) != null);
        }

        boolean hasEmailAddress = false;
        if (mEmailMap != null) {
            hasEmailAddress = (mEmailMap.get(id) != null);
        }

        return hasPhoneNumber || hasEmailAddress;
    }

    /**
     * Import bitmap from database if available.
     *
     * @param id Contact row ID.
     * @return Contact photo or default bitmap.
     */
    Bitmap importPhotoById(int id) {
        return mApi.queryPhotoById(id);
    }

    /**
     * Class to store primary data for a single contact and provides methods to lookup secondary
     * data including phone numbers, email address, and given/family names.
     */
    public class Contact {
        private int mId;
        private String mDisplayName;

        public Contact(int id, String displayName) {
            mId = id;
            mDisplayName = displayName;
        }

        public int getId() {
            return mId;
        }

        public String getDisplayName() {
            return mDisplayName;
        }

        public HashSet<String> getPhoneNumbers() {
            return mPhoneMap.get(mId);
        }

        public HashSet<String> getEmailAddresses() {
            return mEmailMap.get(mId);
        }

        public StructuredName getStructuredName() {
            return mNameMap.get(mId);
        }

        public Bitmap getPhotoBitmap() {
            return importPhotoById(mId);
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "id='" + mId + '\'' +
                    ", displayName='" + mDisplayName + '\'' +
                    ", phoneNumbers='" + getPhoneNumbers() + '\'' +
                    ", emailAddresses='" + getEmailAddresses() + '\'' +
                    ", structuredName='" + getStructuredName() + '\'' +
                    '}';
        }
    }

    /**
     * Simple data class used to store given and family name for a single contact.
     */
    public static class StructuredName {
        public String givenName = "";
        public String familyName = "";

        @Override
        public String toString() {
            return "StructuredName{" +
                    "givenName='" + givenName + '\'' +
                    ", familyName='" + familyName + '\'' +
                    '}';
        }
    }

}
