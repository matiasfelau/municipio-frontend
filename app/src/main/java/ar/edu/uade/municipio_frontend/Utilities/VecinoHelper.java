package ar.edu.uade.municipio_frontend.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ar.edu.uade.municipio_frontend.POJOs.Vecino;

public class VecinoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Vecinos.db";
    public VecinoHelper (Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_VECINOS = "CREATE TABLE " + DataContract.VecinosEntry.TABLE_NAME + "("
                + DataContract.VecinosEntry.DOCUMENTO + " TEXT NOT NULL,"
                + DataContract.VecinosEntry.NOMBRE +" TEXT NOT NULL,"
                + DataContract.VecinosEntry.EMAIL +" TEXT NOT NULL,"
                + DataContract.VecinosEntry.APELLIDO+" TEXT NOT NULL)";

        db.execSQL(CREATE_VECINOS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long saveVecino (Vecino vecino){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(DataContract.VecinosEntry.TABLE_NAME,null,vecino.toConentValues());
    }
}
