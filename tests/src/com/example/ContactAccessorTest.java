package com.example;

import android.os.Build;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author cgreb
 * @since 2011-07-12 14:03 ET
 */
public class ContactAccessorTest extends TestCase {

    protected ContactAccessor mContactAccessor;

    public void setUp() {
        mContactAccessor = ContactAccessor.getInstance();
    }

    public void testContactAccessorNotNull() {
        assertNotNull(mContactAccessor);
    }

    public void testCorrectImplementationForSdkVersion() {
        String className;
        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            className = "ContactAccessorSdk3_4";
        } else {
            className = "ContactAccessorSdk5";
        }

        assertEquals(className, mContactAccessor.getClass().getSimpleName());
    }

    public void testContactListNotNull() {
        List<ContactAccessor.Contact> contactList = mContactAccessor.queryContacts();
        assertNotNull(contactList);
    }

}
