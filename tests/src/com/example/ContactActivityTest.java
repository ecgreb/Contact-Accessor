package com.example;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.MyActivityTest \
 * com.example.tests/android.test.InstrumentationTestRunner
 */
public class ContactActivityTest extends ActivityInstrumentationTestCase2<ContactActivity> {

    public ContactActivityTest() {
        super("com.example", ContactActivity.class);
    }

    public void testMyActivityNotNull() {
        ContactActivity myActivity = getActivity();
        assertNotNull(myActivity);
    }

}
