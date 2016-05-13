package com.example.bitjini.multiselectlistexample;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    Context context = null;
    ContactsAdapter objAdapter;
    ListView lv = null;
    EditText edtSearch = null;
    LinearLayout llContainer = null;
    RelativeLayout rl=null;
    String phoneImage;
    public static final ArrayList<ContactObject> phoneList = new ArrayList<ContactObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        edtSearch = (EditText) findViewById(R.id.input_search);
        llContainer = (LinearLayout) findViewById(R.id.data_container);



        edtSearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3)
            {
                String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                objAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        addContactsInList();
    }

    private void addContactsInList()
    {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        phoneList.clear();
        while (phones.moveToNext())
        {
            String phoneName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneImage = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            System.out.println("Phone Image"+phoneImage);
            ContactObject cp = new ContactObject();
            cp.setName(phoneName);
            cp.setNumber(phoneNumber);
            cp.setImage(phoneImage);
            phoneList.add(cp);
        }
        phones.close();
        lv = new ListView(context);
        llContainer.addView(lv);
        Collections.sort(phoneList, new Comparator<ContactObject>()
        {
            @Override
            public int compare(ContactObject lhs, ContactObject rhs)
            {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        objAdapter = new ContactsAdapter(MainActivity.this, phoneList);
        lv.setAdapter(objAdapter);
    }
//    public Bitmap Imageid()
//    {
//        Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(phoneImage));
//        InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri);
//        BufferedInputStream buf = new BufferedInputStream(photo_stream);
//        Bitmap my_btmp = BitmapFactory.decodeStream(buf);
//        return my_btmp;
//    }
}
