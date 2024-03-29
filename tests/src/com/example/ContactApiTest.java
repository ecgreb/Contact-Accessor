package com.example;

import android.os.Build;

import junit.framework.TestCase;

/**
 * Test class for {@link ContactApi}.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
public class ContactApiTest extends TestCase {

    protected ContactApi mContactApi;

    public void setUp() {
        mContactApi = ContactApi.instance;
        mContactApi.initContext(null);
        mContactApi.initContentResolver(null);
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

    public void testContactQueryBeforeInitializingContentResolverThrowsException() {
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

    public void testPhoneQueryBeforeInitializingContentResolverThrowsException() {
        Class type = null;
        String message = null;

        try {
            mContactApi.queryPhoneNumbers();
        } catch (IllegalStateException e) {
            type = e.getClass();
            message = e.getMessage();
        }

        assertEquals(IllegalStateException.class, type);
        assertEquals("Content resolver has not been initialized", message);
    }

    public void testEmailQueryBeforeInitializingContentResolverThrowsException() {
        Class type = null;
        String message = null;

        try {
            mContactApi.queryEmailAddresses();
        } catch (IllegalStateException e) {
            type = e.getClass();
            message = e.getMessage();
        }

        assertEquals(IllegalStateException.class, type);
        assertEquals("Content resolver has not been initialized", message);
    }

    public void testStructuredNameQueryBeforeInitializingContentResolverThrowsException() {
        Class type = null;
        String message = null;

        try {
            mContactApi.queryStructuredNames();
        } catch (IllegalStateException e) {
            type = e.getClass();
            message = e.getMessage();
        }

        assertEquals(IllegalStateException.class, type);
        assertEquals("Content resolver has not been initialized", message);
    }

}
