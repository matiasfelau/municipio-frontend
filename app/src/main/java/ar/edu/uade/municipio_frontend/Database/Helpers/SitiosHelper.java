package ar.edu.uade.municipio_frontend.Database.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Database.DataContract;
import ar.edu.uade.municipio_frontend.Models.Sitio;

public class SitiosHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME = "Sitios.db";
    public SitiosHelper (Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_SITIOS = "CREATE TABLE " + DataContract.SitiosEntry.TABLE_NAME + "("
                + DataContract.ReclamosEntry.IDPRELIMINAR + " TEXT NOT NULL PRIMARY KEY,"
                + DataContract.SitiosEntry.LATITUD + " TEXT NOT NULL,"
                + DataContract.SitiosEntry.LONGITUD + " TEXT NOT NULL,"
                + DataContract.SitiosEntry.DESCRIPCION + " TEXT NOT NULL)";
        db.execSQL(CREATE_SITIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void saveSitio (Sitio sitio){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DataContract.SitiosEntry.TABLE_NAME,null,sitio.toContentValues());
    }
    public Sitio getSitio(String id){
        Sitio aux = new Sitio();
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.SitiosEntry.TABLE_NAME,
                null,
                DataContract.SitiosEntry.IDPRELIMINAR+" = ? ",new String[] {String.valueOf(id)},
                null,
                null,
                null,
                null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            aux.setIdPreliminar(cursor.getString(0));
            aux.setLatitud(new BigDecimal(cursor.getString(1)));
            aux.setLongitud(new BigDecimal(cursor.getString(2)));
            aux.setDescripcion(cursor.getString(3));
        }
        return aux;
    }
    public List<Sitio> getSitios(){
        List<Sitio> sitios = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.SitiosEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                Sitio aux = new Sitio();
                aux.setIdPreliminar(cursor.getString(0));
                aux.setLatitud(new BigDecimal(cursor.getString(1)));
                aux.setLongitud(new BigDecimal(cursor.getString(2)));
                aux.setDescripcion(cursor.getString(3));
                sitios.add(aux);
            } while (cursor.moveToNext());
        }
        return sitios;
    }

    public void deleteSitios(){
        SQLiteDatabase db =getWritableDatabase();
        db.delete(DataContract.SitiosEntry.TABLE_NAME,null, null);
    }
}
