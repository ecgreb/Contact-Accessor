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
    private static final String COLUMN_DISPLAY_NAME = "displayName";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_GIVEN_NAME = "givenName";
    private static final String COLUMN_FAMILY_NAME = "familyName";

    private MockContactCursor mContactCursor = new MockContactCursor();
    private HashMap<String, MockDataCursor> mPhoneCursors = new HashMap<String, MockDataCursor>();
    private HashMap<String, MockDataCursor> mEmailCursors = new HashMap<String, MockDataCursor>();
    private HashMap<String, MockNameCursor> mNameCursors = new HashMap<String, MockNameCursor>();

    public MockContactApi() {
        super();
    }

    @Override
    public String getColumnId() {
        return COLUMN_ID;
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
    public Cursor queryPhoneNumbers(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mPhoneCursors.get(contactId);
    }

    @Override
    public Cursor queryEmailAddresses(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mEmailCursors.get(contactId);
    }

    @Override
    public Cursor queryStructuredName(String contactId) {
        if (mResolver == null) {
            throw new IllegalStateException("Content resolver has not been initialized");
        }
        return mNameCursors.get(contactId);
    }

    public void addMockContact(String id, String name, String[] phoneArray, String[] emailArray) {
        mContactCursor.addValues(id, name);

        MockDataCursor mockPhoneCursor = new MockDataCursor();
        for (String number : phoneArray) {
            mockPhoneCursor.addValue(number);
        }
        mPhoneCursors.put(id, mockPhoneCursor);

        MockDataCursor mockEmailCursor = new MockDataCursor();
        for (String email : emailArray) {
            mockEmailCursor.addValue(email);
        }
        mEmailCursors.put(id, mockEmailCursor);

        MockNameCursor mockNameCursor = new MockNameCursor();
        mockNameCursor.addValues("FirstName", "LastName");
        mNameCursors.put(id, mockNameCursor);
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

    public class MockNameCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mGivenNameArray = new ArrayList<String>();
        private ArrayList<String> mFamilyNameArray = new ArrayList<String>();

        public void addValues(String givenName, String familyName) {
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
            if (COLUMN_GIVEN_NAME.equals(column)) {
                return 0;
            } else if (COLUMN_FAMILY_NAME.equals(column)) {
                return 1;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mGivenNameArray.get(mPosition);
            } else if (index == 1) {
                return mFamilyNameArray.get(mPosition);
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    public class MockDataCursor extends MockCursor {

        private int mPosition = -1;
        private ArrayList<String> mValues = new ArrayList<String>();

        public void addValue(String value) {
            mValues.add(value);
        }

        @Override
        public int getCount() {
            return mValues.size();
        }

        @Override
        public boolean moveToNext() {
            int newPosition = mPosition + 1;

            if (newPosition == mValues.size()) {
                return false;
            }

            mPosition = newPosition;
            return true;
        }

        @Override
        public int getColumnIndex(String column) {
            return 0;
        }

        @Override
        public String getString(int index) {
            return mValues.get(mPosition);
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

}
