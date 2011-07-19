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
        String[] fakePhoneArray = {"0000000000"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray);
        ContactList contactList = new ContactList(mMockContactApi);

        assertFalse(contactList.isEmpty());
    }

    public void testApiWithZeroContactsReturnsContactListSizeZero() {
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals(0, contactList.size());
    }

    public void testApiWithOneContactReturnsContactListSizeOne() {
        String[] fakePhoneArray = {"0000000000"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray);
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals(1, contactList.size());
    }

    public void testApiWithTwoContactsReturnsContactListSizeTwo() {
        String[] fakePhoneArray0 = {"0000000000"};
        String[] fakePhoneArray1 = {"1111111111"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        mMockContactApi.addMockContact("1", "Display Name 1", fakePhoneArray1);
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals(2, contactList.size());
    }

    public void testGetOneContactHasCorrectDisplayName() {
        String[] fakePhoneArray0 = {"0000000000"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
    }

    public void testGetTwoContactsHaveCorrectDisplayName() {
        String[] fakePhoneArray0 = {"0000000000"};
        String[] fakePhoneArray1 = {"1111111111"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        mMockContactApi.addMockContact("1", "Display Name 1", fakePhoneArray1);
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
        assertEquals("Display Name 1", contactList.get(1).getDisplayName());
    }

    public void testContactWithNoNameIsExcludedFromList() {
        String[] fakePhoneArray0 = {"0000000000"};
        String[] fakePhoneArray1 = {"1111111111"};
        String[] fakePhoneArray2 = {"2222222222"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        mMockContactApi.addMockContact("1", "", fakePhoneArray1);
        mMockContactApi.addMockContact("2", "Display Name 2", fakePhoneArray2);
        ContactList contactList = new ContactList(mMockContactApi);

        assertEquals(2, contactList.size());
        assertEquals("Display Name 0", contactList.get(0).getDisplayName());
        assertEquals("Display Name 2", contactList.get(1).getDisplayName());
    }

    public void testCanGetPhoneNumberForOneContact() {
        String[] fakePhoneArray0 = {"0000000000"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList = contactList.get(0).getPhoneNumbers();

        assertEquals("0000000000", phoneList.get(0));
    }

    public void testCanGetPhoneNumberForTwoContacts() {
        String[] fakePhoneArray0 = {"0000000000"};
        String[] fakePhoneArray1 = {"1111111111"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        mMockContactApi.addMockContact("1", "Display Name 1", fakePhoneArray1);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList0 = contactList.get(0).getPhoneNumbers();
        List<String> phoneList1 = contactList.get(1).getPhoneNumbers();

        assertEquals("0000000000", phoneList0.get(0));
        assertEquals("1111111111", phoneList1.get(0));
    }

    public void testContactWithoutPhoneNumberIsExcludedFromList() {
        String[] fakePhoneArray0 = {"0000000000"};
        String[] fakePhoneArray1 = {};
        String[] fakePhoneArray2 = {"2222222222"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray0);
        mMockContactApi.addMockContact("1", "Display NAme 1", fakePhoneArray1);
        mMockContactApi.addMockContact("2", "Display Name 2", fakePhoneArray2);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList0 = contactList.get(0).getPhoneNumbers();
        List<String> phoneList1 = contactList.get(1).getPhoneNumbers();

        assertEquals(2, contactList.size());
        assertEquals("0000000000", phoneList0.get(0));
        assertEquals("2222222222", phoneList1.get(0));
    }

    public void testGetContactWithMultiplePhoneNumbers() {
        String[] fakePhoneArray = {"0000000000", "0000000001"};
        mMockContactApi.addMockContact("0", "Display Name 0", fakePhoneArray);
        ContactList contactList = new ContactList(mMockContactApi);
        List<String> phoneList = contactList.get(0).getPhoneNumbers();

        assertEquals(2, phoneList.size());
        assertEquals("0000000000", phoneList.get(0));
        assertEquals("0000000001", phoneList.get(1));
    }

}
