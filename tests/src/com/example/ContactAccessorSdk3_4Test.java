package com.example;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author cgreb
 * @since 2011-07-14 10:23 ET
 */
public class ContactAccessorSdk3_4Test extends TestCase {

    protected ContactAccessorSdk3_4 mContactAccessorSdk3_4;

    public void setUp() {
        mContactAccessorSdk3_4 = new ContactAccessorSdk3_4();
    }

    public void testContactAccessorSdk3_4NotNull() {
        assertNotNull(mContactAccessorSdk3_4);
    }

    public void testContactListNotNull() {
        List<ContactAccessor.Contact> contactList = mContactAccessorSdk3_4.queryContacts();
        assertNotNull(contactList);
    }

}
