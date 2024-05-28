package ar.edu.uade.municipio_frontend.Database.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Database.DataContract;
import ar.edu.uade.municipio_frontend.Models.Vecino;

public class VecinoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Vecinos.db";
    public VecinoHelper (Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_VECINOS = "CREATE TABLE " + DataContract.VecinosEntry.TABLE_NAME + "("
                + DataContract.VecinosEntry.DOCUMENTO + " TEXT NOT NULL PRIMARY KEY,"
                + DataContract.VecinosEntry.NOMBRE +" TEXT NOT NULL,"
                + DataContract.VecinosEntry.EMAIL +" TEXT NOT NULL,"
                + DataContract.VecinosEntry.APELLIDO+" TEXT NOT NULL,"
                + DataContract.VecinosEntry.PASSWORD+" TEXT NOT NULL)";

        db.execSQL(CREATE_VECINOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveVecino (Vecino vecino){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DataContract.VecinosEntry.TABLE_NAME, null, vecino.toContentValues());
    }

    public Vecino getVecinoByDocumento(String documento) {
        Vecino aux = new Vecino();

        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.VecinosEntry.TABLE_NAME,
                null,
                DataContract.VecinosEntry.DOCUMENTO+" = ? ",new String[] {String.valueOf(documento)},
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            aux.setDocumento(cursor.getString(0));
            aux.setNombre(cursor.getString(1));
            aux.setEmail(cursor.getString(2));
            aux.setApellido(cursor.getString(3));
            aux.setPassword(cursor.getString(4));
        }
        return aux;
    }

    public List<Vecino> getVecinos() {
        List<Vecino> vecinos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.VecinosEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Vecino aux = new Vecino();
                aux.setDocumento(cursor.getString(0));
                aux.setNombre(cursor.getString(1));
                aux.setEmail(cursor.getString(2));
                aux.setApellido(cursor.getString(3));
                aux.setPassword(cursor.getString(4));
                vecinos.add(aux);
            } while (cursor.moveToNext());
        }
        return vecinos;
    }

    public void deleteVecinos() {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DataContract.VecinosEntry.TABLE_NAME, null, null);
    }
}
