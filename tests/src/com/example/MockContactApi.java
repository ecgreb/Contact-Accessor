package com.example;

import android.database.Cursor;
import android.test.mock.MockCursor;
import android.util.Log;

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

    private static final String TAG = "MockContactApi";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DISPLAY_NAME = "displayName";
    private static final String COLUMN_PHONE_INDICATOR = "phoneIndicator";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_EMAIL_INDICATOR = "emailIndicator";
    private static final String COLUMN_EMAIL = "email";

    private MockContactCursor mContactCursor = new MockContactCursor();
    private HashMap<String, MockPhoneCursor> mPhoneCursorMap =
            new HashMap<String, MockPhoneCursor>();

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
    public String getColumnHasPhoneIndicator() {
        return COLUMN_PHONE_INDICATOR;
    }

    @Override
    public String getColumnPhoneNumber() {
        return COLUMN_NUMBER;
    }

    @Override
    public String getColumnEmailIndicator() {
        return COLUMN_EMAIL_INDICATOR;
    }

    @Override
    public String getColumnEmailAddress() {
        return COLUMN_EMAIL;
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

        return mPhoneCursorMap.get(contactId);
    }

    @Override
    public Cursor queryEmailAddresses(String contactId) {
        return null;
    }

    public void addMockContact(String id, String name, String[] phoneArray) {
        Log.v(TAG, "addContact: " + name);
        if (phoneArray.length == 0) {
            mContactCursor.addValues(id, name, false);
        } else {
            mContactCursor.addValues(id, name, true);
            MockPhoneCursor mockPhoneCursor = new MockPhoneCursor();
            for (String number : phoneArray) {
                mockPhoneCursor.addValue(number);
            }
            mPhoneCursorMap.put(id, mockPhoneCursor);
        }
    }

    public class MockContactCursor extends MockCursor {

        private static final String TAG = "MockContactCursor";

        private int mPosition = -1;
        private ArrayList<String> mIdArray = new ArrayList<String>();
        private ArrayList<String> mDisplayNameArray = new ArrayList<String>();
        private ArrayList<Boolean> mHasPhoneNumberArray = new ArrayList<Boolean>();

        public MockContactCursor() {
            super();
            Log.v(TAG, "MockContactCursor");
        }

        public void addValues(String id, String displayName, boolean hasPhoneNumber) {
            Log.v(TAG, "addValues: " + id + " " + displayName);
            mIdArray.add(id);
            mDisplayNameArray.add(displayName);
            mHasPhoneNumberArray.add(hasPhoneNumber);
        }

        @Override
        public int getCount() {
            Log.v(TAG, "getCount: " + mDisplayNameArray.size());
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
            } else if (COLUMN_PHONE_INDICATOR.equals(column)) {
                return 2;
            }

            return -1;
        }

        @Override
        public String getString(int index) {
            if (index == 0) {
                return mIdArray.get(mPosition);
            } else if (index == 1) {
                return mDisplayNameArray.get(mPosition);
            } else if (index == 2) {
                return mHasPhoneNumberArray.get(mPosition) ? "1" : "0";
            }

            return null;
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

    public class MockPhoneCursor extends MockCursor {

        private static final String TAG = "MockPhoneCursor";

        private int mPosition = -1;
        private ArrayList<String> mValues = new ArrayList<String>();

        public MockPhoneCursor() {
            super();
            Log.v(TAG, "MockPhoneCursor");
        }

        public void addValue(String value) {
            Log.v(TAG, "addValue: " + value);
            mValues.add(value);
        }

        @Override
        public int getCount() {
            Log.v(TAG, "getCount: " + mValues.size());
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
