package com.example;

import android.test.mock.MockContentResolver;
import android.util.Log;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.List;

/**
 * Test class for {@link ContactList} data structure.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
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
        mMockContactApi.addMockContact(1, "", fakePhoneArray, fakeEmailArray);
        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(1, contactList.size());
        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
    }

    public void testCanGetPhoneNumberForOneContact() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> phoneList = contactList.get(0).getPhoneNumbers();
        assertEquals("0000000000", phoneList.toArray()[0]);
    }

    public void testCanGetPhoneNumberForTwoContacts() {
        generateSampleContacts(2);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> phoneList0 = contactList.get(0).getPhoneNumbers();
        HashSet<String> phoneList1 = contactList.get(1).getPhoneNumbers();
        assertEquals("0000000000", phoneList0.toArray()[0]);
        assertEquals("1111111111", phoneList1.toArray()[0]);
    }

    public void testGetContactWithMultiplePhoneNumbers() {
        String[] fakePhoneArray = { "0000000000", "0000000001" };
        String[] fakeEmailArray = { "email1@test.com" };
        mMockContactApi.addMockContact(0, "Display Name 0", fakePhoneArray, fakeEmailArray);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> phoneList = contactList.get(0).getPhoneNumbers();
        assertEquals(2, phoneList.size());
        assertEquals("0000000000", phoneList.toArray()[0]);
        assertEquals("0000000001", phoneList.toArray()[1]);
    }

    public void testCanGetEmailAddressForOneContact() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> emailList = contactList.get(0).getEmailAddresses();
        assertEquals("email0@test.com", emailList.toArray()[0]);
    }

    public void testCanGetEmailAddressForTwoContacts() {
        generateSampleContacts(2);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> emailList0 = contactList.get(0).getEmailAddresses();
        HashSet<String> emailList1 = contactList.get(1).getEmailAddresses();
        assertEquals("email0@test.com", emailList0.toArray()[0]);
        assertEquals("email1@test.com", emailList1.toArray()[0]);
    }

    public void testGetContactWithMultipleEmailAddresses() {
        String[] fakePhoneArray = { "0000000000" };
        String[] fakeEmailArray = { "email@test01.com", "email@test02.com" };
        mMockContactApi.addMockContact(0, "Display Name 0", fakePhoneArray, fakeEmailArray);
        ContactList contactList = new ContactList(mMockContactApi);
        HashSet<String> emailList = contactList.get(0).getEmailAddresses();
        assertEquals(2, emailList.size());
        assertEquals("email@test01.com", emailList.toArray()[1]);
        assertEquals("email@test02.com", emailList.toArray()[0]);
    }

    public void testContactWithNoPhoneAndNoEmailIsExcludedFromList() {
        String[] fakePhoneArray0 = { "0000000000" };
        String[] fakeEmailArray0 = { "email0@test.com" };
        mMockContactApi.addMockContact(0, "Phone & Email", fakePhoneArray0, fakeEmailArray0);

        String[] fakePhoneArray1 = { "1111111111" };
        String[] fakeEmailArray1 = { };
        mMockContactApi.addMockContact(1, "Phone Only", fakePhoneArray1, fakeEmailArray1);

        String[] fakePhoneArray2 = { };
        String[] fakeEmailArray2 = { "email2@test.com" };
        mMockContactApi.addMockContact(2, "Email Only", fakePhoneArray2, fakeEmailArray2);

        String[] fakePhoneArray3 = { };
        String[] fakeEmailArray3 = { };
        mMockContactApi.addMockContact(3, "Neither", fakePhoneArray3, fakeEmailArray3);

        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals(3, contactList.size());
        for (ContactList.Contact contact : contactList) {
            assertNotSame("Neither", contact.getDisplayName());
        }
    }

    public void testNonNumericCharactersAreStrippedFromPhoneNumber() {
        String[] fakePhoneArray0 = { "123-4#56.78 90abc" };
        String[] fakeEmailArray0 = { "email0@test.com" };
        mMockContactApi.addMockContact(0, "Display Name 0", fakePhoneArray0, fakeEmailArray0);

        ContactList contactList = new ContactList(mMockContactApi);
        assertEquals("1234567890", contactList.get(0).getPhoneNumbers().toArray()[0]);
    }

    public void testGetStructuredName() {
        generateSampleContacts(1);
        ContactList contactList = new ContactList(mMockContactApi);
        ContactList.StructuredName structuredName = contactList.get(0).getStructuredName();
        Log.v("ContactListTest", structuredName.toString());

        assertEquals("FirstName", structuredName.givenName);
        assertEquals("LastName", structuredName.familyName);
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
            mMockContactApi.addMockContact(i, name, phoneArray, emailArray);
        }
    }

}
