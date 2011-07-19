package com.example;

import android.os.Build;
import android.os.Message;

import junit.framework.TestCase;

/**
 * Test class for {@link ContactApi}.
 *
 * @author cgreb
 * @since 2011-07-12
 */
public class ContactApiTest extends TestCase {

    protected ContactApi mContactApi;

    public void setUp() {
        mContactApi = ContactApi.getInstance();
    }

    public void testContactApiNotNull() {
        assertNotNull(mContactApi);
    }

    public void testSdkVersion() {
        String className;
        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            className = "ContactApiSdk3";
        } else {
            className = "ContactApiSdk5";
        }

        assertEquals(className, mContactApi.getClass().getSimpleName());
    }

    public void testContactQueryBeforeInitializingContentResolverThrowsIllegalStateException() {
        Class type = null;
        String message = null;

        try {
            mContactApi.queryContacts();
        } catch (IllegalStateException e) {
            type = e.getClass();
            message = e.getMessage();
        }

        assertEquals(IllegalStateException.class, type);
        assertEquals("Content resolver has not been initialized", message);
    }

    public void testPhoneQueryBeforeInitializingContentResolverThrowsIllegalStateException() {
        Class type = null;
        String message = null;

        try {
            mContactApi.queryPhoneNumbers("");
        } catch (IllegalStateException e) {
            type = e.getClass();
            message = e.getMessage();
        }

        assertEquals(IllegalStateException.class, type);
        assertEquals("Content resolver has not been initialized", message);
    }

}
