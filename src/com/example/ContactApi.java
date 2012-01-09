package com.example;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;

/**
 * Abstract class that defines SDK-independent API for communication with the Contacts Provider.
 *
 * @author Chuck Greb <charles.greb@gmail.com>
 */
public abstract class ContactApi {

    /** Static singleton instance holding the SDK-specific implementation of the class. */
    public static final ContactApi instance;

    static {
        int sdkVersion = Integer.parseInt(Build.VERSION.SDK);

        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            instance = new ContactApiSdk3();
        } else {
            instance = new ContactApiSdk5();
        }
    }

    /** Calling application's context */
    protected Context mContext;

    /** Content resolver instance that will be used to execute a query. */
    protected ContentResolver mResolver;

    /**
     * Initialize reference to {@link android.content.Context} calling application. Required for
     * {@link ContactApiSdk3#queryPhotoById(long)}.
     *
     * @param context Context of calling application.
     */
    public void initContext(Context context) {
        mContext = context;
    }

    /**
     * Initialize the {@link android.content.ContentResolver} used to execute queries. The resolver
     * instance must be initialized before calling {@link #queryContacts()},
     * {@link #queryPhoneNumbers()}, {@link #queryEmailAddresses()},
     * or {@link #queryStructuredNames()}.
     *
     * @param contentResolver Reference to the parent application's {@link android.content.ContentResolver}
     * obtained by calling {@link android.app.Activity#getContentResolver()}.
     */
    public void initContentResolver(ContentResolver contentResolver) {
        mResolver = contentResolver;
    }

    /**
     * Get the database row ID.
     *
     * @return Cursor column name used to retrieve the row ID for the contact.
     * @see {@link android.provider.BaseColumns#_ID}
     */
    public abstract String getColumnId();

    /**
     * Get the database contact ID column.
     *
     * @return Cursor column name used to retrieve the row ID for the contact.
     * @see {@link android.provider.Contacts.ContactMethods#PERSON_ID}
     * @see {@link android.provider.ContactsContract.RawContactsColumns#CONTACT_ID}
     */
    public abstract String getColumnContactId();

    /**
     * Get the database display name column.
     *
     * @return Cursor column name used to retrieve contact display name.
     */
    public abstract String getColumnDisplayName();

    /**
     * Get the database phone number column.
     *
     * @return Cursor column name used to retrieve phone numbers.
     */
    public abstract String getColumnPhoneNumber();

    /**
     * Get the database email address column.
     *
     * @return Cursor column name used to retrieve email addresses.
     */
    public abstract String getColumnEmailAddress();

    /**
     * Get the database given name column.
     *
     * @return Cursor column name used to retrieve the given name.
     */
    public abstract String getColumnGivenName();

    /**
     * Get the database family name column.
     *
     * @return Cursor column name used to retrieve the family name.
     */
    public abstract String getColumnFamilyName();

    /**
     * Query all contacts on the device.
     *
     * @return Cursor used to iterate over all contacts.
     */
    public abstract Cursor queryContacts();

    /**
     * Query phone numbers for all contacts.
     *
     * @return Cursor used to iterate over phone numbers.
     */
    public abstract Cursor queryPhoneNumbers();

    /**
     * Query email addresses for all contacts.
     *
     * @return Cursor used to iterate over email addresses.
     */
    public abstract Cursor queryEmailAddresses();

    /**
     * Query structured name for all contacts.
     * <p />
     * Note: Structured names are only supported in SDK 5+. Implementations for SDK < 5 should
     * always return null.
     *
     * @return Cursor used to iterate over structured name fields.
     */
    public abstract Cursor queryStructuredNames();

    /**
     * Query photo bitmap database for a single contact.
     *
     * @param id Contact row ID.
     * @return Bitmap of contact photo or default bitmap if none was found.
     */
    public abstract Bitmap queryPhotoById(long id);

}
