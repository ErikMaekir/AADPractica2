package erikmaekir.aadpractica2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Cursor cursor;
    private boolean csv_status = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCSV();
    }

    private void createCSV() {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/contacto.csv"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String displayName;
        String number;
        long _id;
        String columns[] = new String[]{ ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                columns,
                null,
                null,
                ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        startManagingCursor(cursor);
        if(cursor.moveToFirst()) {
            do {
                _id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).trim();
                number = getPrimaryNumber(_id);
                writer.writeNext((displayName + "/" + number).split("/"));
            } while(cursor.moveToNext());
            csv_status = true;
        } else {
            csv_status = false;
        }
        try {
            if(writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            Log.w("Test", e.toString());
        }

    }

    private String getPrimaryNumber(long _id) {
        String primaryNumber = null;
        try {
            Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{Phone.NUMBER, Phone.TYPE}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ _id,
                    null, null);
            if(cursor != null) {
                while(cursor.moveToNext()){
                    switch(cursor.getInt(cursor.getColumnIndex(Phone.TYPE))){
                        case Phone.TYPE_MOBILE :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                            break;
                        case Phone.TYPE_HOME :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                            break;
                        case Phone.TYPE_WORK :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                            break;
                        case Phone.TYPE_OTHER :
                            primaryNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                            break;
                    }
                    if(primaryNumber != null)
                        break;
                }
            }
        } catch (Exception e) {
            Log.i("test", "Exception " + e.toString());
        } finally {
            if(cursor != null) {
                cursor.deactivate();
                cursor.close();
            }
        }
        return primaryNumber;
    }

    private void readFile() throws FileNotFoundException {
        InputStream is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/contacto.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split("\n");
                for (int a=0; a < RowData.length ; a++ ){
                    String[] FieldData = RowData[a].split(";");
                    
                }
            }
        }catch (IOException ex) {

        } finally {
            try {
                is.close();
            }
            catch (IOException e) {
            }
        }
    }
}