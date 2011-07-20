package com.example;

import android.test.mock.MockContentResolver;

import junit.framework.TestCase;

import java.util.List;

/**
 * Test class for {@link ContactList} data structure. Includes tests of parsing and filtering logic
 * as it is applied to the {@link ContactApi}.
 *
 * @author cgreb
 * @since 2011-07-15
 */
public class ContactListTest extends TestCase {

    private MockContactApi mMockContactApi;

    public void setUp() {
        mMockContactApi = new MockContactApi();
        mMockContactApi.initContentResolver(new MockContentResolver());
    }

    public void testContactListNotNull() {
        ContactList contactList = new ContactList(mMockContactApi);
        assertNotNull(contactList);
    }

    public void testContactListIsEmptyForApiWithZeroContacts() {
        ContactList contactList = new ContactList(mMockContactApi);
        assertTrue(contactList.isEmpty());
    }

    public void testContactListIsNotEmptyForApiWithOneOrMoreContacts() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        assertFalse(contactList.isEmpty());
    }

    public void testApiWithZeroContactsReturnsContactListSizeZero() {
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(0, contactList.size());
    }

    public void testApiWithOneContactReturnsContactListSizeOne() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(1, contactList.size());
    }

    public void testApiWithTwoContactsReturnsContactListSizeTwo() {
        generateSampleContacts(2);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(2, contactList.size());
    }

    public void testGetOneContactHasCorrectDisplayName() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
    }

    public void testGetTwoContactsHaveCorrectDisplayName() {
        generateSampleContacts(2);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
        assertEquals("Display Name 1", contactList.get(1).getDisplayName());
    }

    public void testContactWithNoNameIsExcludedFromList() {
        generateSampleContacts(1);
        String[] fakePhoneArray = { "1111111111" };
        String[] fakeEmailArray = { "email1@test.com" };
        mMockContactApi.addMockContact("1", "", fakePhoneArray, fakeEmailArray);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(1, contactList.size());
        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
    }

    public void testCanGetPhoneNumberForOneContact() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList = contactList.get(0).getPhoneNumbers();
        assertEquals("0000000000", phoneList.get(0));
    }

    public void testCanGetPhoneNumberForTwoContacts() {
        generateSampleContacts(2);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList0 = contactList.get(0).getPhoneNumbers();
        List<String> phoneList1 = contactList.get(1).getPhoneNumbers();
        assertEquals("0000000000", phoneList0.get(0));
        assertEquals("1111111111", phoneList1.get(0));
    }

    public void testGetContactWithMultiplePhoneNumbers() {
        String[] fakePhoneArray = { "0000000000", "0000000001" };
        String[] fakeEmailArray = { "email1@test.com" };
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray, fakeEmailArray);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList = contactList.get(0).getPhoneNumbers();
        assertEquals(2, phoneList.size());
        assertEquals("0000000000", phoneList.get(0));
        assertEquals("0000000001", phoneList.get(1));
    }

    public void testCanGetEmailAddressForOneContact() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> emailList = contactList.get(0).getEmailAddresses();
        assertEquals("email0@test.com", emailList.get(0));
    }

    /**
     * Convenience method to add sample contacts to {@link MockContactApi} with one phone number
     * and one email address.
     *
     * @param count Number of contacts to be added.
     */
    private void generateSampleContacts(int count) {
        String id, name;
        String[] phoneArray = new String[1];
        String[] emailArray = new String[1];
        for (int i = 0; i < count; i++) {
            id = Integer.toString(i);
            name = "Display Name " + i;
            phoneArray[0] = id + id + id + id + id + id + id + id + id + id;
            emailArray[0] = "email" + id + "@test.com";
            mMockContactApi.addMockContact(id, name, phoneArray, emailArray);
        }
    }

}
