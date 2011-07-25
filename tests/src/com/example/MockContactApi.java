package com.example;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Mock API object used for simulating the Android Contacts API. Can only be used on API Level 8 and
 * above due to use of the {@link MockCursor} class.
 *
 * @author cgreb
 * @since 2011-07-15 16:53 ET
 */
public class MockContactApi extends ContactApi {

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CONTACT_ID = "contact_id";
    private static final String COLUMN_DISPLAY_NAME = "display_name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_GIVEN_NAME = "given_name";
    private static final String COLUMN_FAMILY_NAME = "family_name";

    private MockContactCursor mContactCursor = new MockContactCursor();
    private MockPhoneCursor mPhoneCursor = new MockPhoneCursor();
    private MockEmailCursor mEmailCursor = new MockEmailCursor();
    private MockNameCursor mNameCursor = new MockNameCursor();

    public MockContactApi() {
        super();
    }

    @Override
    public String getColumnId() {
        return COLUMN_ID;
    }

    @Override
    public String getColumnContactId() {
        return COLUMN_CONTACT_ID;
    }

    @Override
    public String getColumnDisplayName() {
        return COLUMN_DISPLAY_NAME;
    }

    @Override
    public String getColumnPhoneNumber() {
        return COLUMN_NUMBER;
    }

    @Override
    public String getColumnEmailAddress() {
        return COLUMN_EMAIL;
    }

    @Override
    public String getColumnGivenName() {
        return COLUMN_GIVEN_NAME;
    }

    @Override
    public String getColumnFamilyName() {
        return COLUMN_FAMILY_NAME;
    }

    @Override
    public Cursor queryContacts() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mContactCursor;
    }

    @Override
    public Cursor queryPhoneNumbers() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mPhoneCursor;
    }

    @Override
    public Cursor queryEmailAddresses() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mEmailCursor;
    }

    @Override
    public Cursor queryStructuredNames() {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mNameCursor;
    }

    public void addMockContact(String id, String name, String[] phoneArray, String[] emailArray) {
        mContactCursor.addValues(id, name);

        for (String phone : phoneArray) {
            mPhoneCursor.addValues(id, phone);
        }

        for (String email : emailArray) {
            mEmailCursor.addValues(id, email);
        }

        mNameCursor.addValues(id, "FirstName", "LastName");
    }

    public class MockContactCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mIdArray = new ArrayList<String>();
        private ArrayList<String> mDisplayNameArray = new ArrayList<String>();

        public void addValues(String id, String displayName) {
            mIdArray.add(id);
            mDisplayNameArray.add(displayName);
        }

        @Override
        public int getCount() {
            return mDisplayNameArray.size();
        }

        @Override
        public boolean moveToNext() {
            int newPosition = mPosition + 1;
            if (newPosition == mDisplayNameArray.size()) {
                return false;
            }

            mPosition = newPosition;
            return true;
        }

        @Override
        public int getColumnIndex(String column) {
            if (COLUMN_ID.equals(column)) {
                return 0;
            } else if (COLUMN_DISPLAY_NAME.equals(column)) {
                return 1;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mIdArray.get(mPosition);
            } else if (index == 1) {
                return mDisplayNameArray.get(mPosition);
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    public class MockPhoneCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mContactIdArray = new ArrayList<String>();
        private ArrayList<String> mPhoneNumberArray = new ArrayList<String>();

        public void addValues(String contactId, String phoneNumber) {
            mContactIdArray.add(contactId);
            mPhoneNumberArray.add(phoneNumber);
        }

        @Override
        public int getCount() {
            return mContactIdArray.size();
        }

        @Override
        public boolean moveToNext() {
            int newPosition = mPosition + 1;

            if (newPosition == mContactIdArray.size()) {
                return false;
            }

            mPosition = newPosition;
            return true;
        }

        @Override
        public int getColumnIndex(String column) {
            if (COLUMN_CONTACT_ID.equals(column)) {
                return 0;
            } else if (COLUMN_NUMBER.equals(column)) {
                return 1;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mContactIdArray.get(mPosition);
            } else if (index == 1) {
                return mPhoneNumberArray.get(mPosition);
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    public class MockEmailCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mContactIdArray = new ArrayList<String>();
        private ArrayList<String> mEmailAddressesArray = new ArrayList<String>();

        public void addValues(String contactId, String emailAddress) {
            mContactIdArray.add(contactId);
            mEmailAddressesArray.add(emailAddress);
        }

        @Override
        public int getCount() {
            return mContactIdArray.size();
        }

        @Override
        public boolean moveToNext() {
            int newPosition = mPosition + 1;
            if (newPosition == mContactIdArray.size()) {
                return false;
            }

            mPosition = newPosition;
            return true;
        }

        @Override
        public int getColumnIndex(String column) {
            if (COLUMN_CONTACT_ID.equals(column)) {
                return 0;
            } else if (COLUMN_EMAIL.equals(column)) {
                return 1;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mContactIdArray.get(mPosition);
            } else if (index == 1) {
                return mEmailAddressesArray.get(mPosition);
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    public class MockNameCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mContactIdArray = new ArrayList<String>();
        private ArrayList<String> mGivenNameArray = new ArrayList<String>();
        private ArrayList<String> mFamilyNameArray = new ArrayList<String>();

        public void addValues(String contactId, String givenName, String familyName) {
            mContactIdArray.add(contactId);
            mGivenNameArray.add(givenName);
            mFamilyNameArray.add(familyName);
        }

        @Override
        public int getCount() {
            return mGivenNameArray.size();
        }

        @Override
        public boolean moveToNext() {
            int newPosition = mPosition + 1;
            if (newPosition == mGivenNameArray.size()) {
                return false;
            }

            mPosition = newPosition;
            return true;
        }

        @Override
        public int getColumnIndex(String column) {
            if (COLUMN_CONTACT_ID.equals(column)) {
                return 0;
            } else if (COLUMN_GIVEN_NAME.equals(column)) {
                return 1;
            } else if (COLUMN_FAMILY_NAME.equals(column)) {
                return 2;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mContactIdArray.get(mPosition);
            } else if (index == 1) {
                return mGivenNameArray.get(mPosition);
            } else if (index == 2) {
                return mFamilyNameArray.get(mPosition);
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

}
