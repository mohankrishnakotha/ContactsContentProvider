package vvit.contactscontentprovider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView output = (TextView) findViewById(R.id.output);

        // Fetch emails from contact list
        String emailStr = refreshData();
        // Show emails on screen
        output.setText(emailStr);

    }


    private String refreshData() {
        String emaildata = "";

        try {

            /**************************************************/

            ContentResolver cr = getBaseContext().getContentResolver();
            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            if (cur.getCount() > 0) {

                Log.i("Content provider", "Reading contact  emails");

                while (cur.moveToNext()) {

                    String contactId = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));

                    // Create query to use CommonDataKinds classes to fetch emails
                    Cursor emails = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = " + contactId, null, null);

                            /*
                            //You can use all columns defined for ContactsContract.Data
                            // Query to get phone numbers by directly call data table column

                            Cursor c = getContentResolver().query(Data.CONTENT_URI,
                                      new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
                                      Data.CONTACT_ID + "=?" + " AND "
                                              + Data.MIMETYPE + "= + Phone.CONTENT_ITEM_TYPE + ",
                                      new String[] {String.valueOf(contactId)}, null);
                            */

                    while (emails.moveToNext()) {

                        // This would allow you get several email addresses
                        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        //Log.e("email==>", emailAddress);

                        emaildata +=""+emailAddress+""+"--------------------------------------";
                    }

                    emails.close();
                }

            }
            else
            {
                emaildata +="Data not found.";

            }
            cur.close();


        } catch (Exception e) {

            emaildata +="Exception : "+e+"";
        }

        return emaildata;
    }

}