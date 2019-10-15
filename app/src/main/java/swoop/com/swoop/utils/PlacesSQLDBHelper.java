package swoop.com.swoop.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlacesSQLDBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public PlacesSQLDBHelper(Context context) {
        super(context, Constants.DB1_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Constants.TB1_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, place_name TEXT, place_address TEXT, latitude TEXT, longitude TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TB1_NAME);
    }

    public boolean insertData(ContentValues contentValues){
        db = this.getWritableDatabase();
        long result = db.insert(Constants.TB1_NAME,null,contentValues);

        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor readAllData(){
        db = this.getWritableDatabase();
        return db.rawQuery("Select * from " + Constants.TB1_NAME,null);
    }

    public void deleteRow(String id){
        db = this.getWritableDatabase();
        db.delete(Constants.TB1_NAME,"ID = ?", new String[]{id});
    }

}
