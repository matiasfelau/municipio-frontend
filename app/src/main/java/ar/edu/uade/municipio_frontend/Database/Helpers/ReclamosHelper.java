package ar.edu.uade.municipio_frontend.Database.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Database.DataContract;
import ar.edu.uade.municipio_frontend.Models.Reclamo;

public class ReclamosHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME ="Reclamos.db";
    public ReclamosHelper (Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECLAMOS = "CREATE TABLE " + DataContract.ReclamosEntry.TABLE_NAME + "("
                + DataContract.ReclamosEntry.IDPRELIMINAR + " TEXT NOT NULL PRIMARY KEY,"
                + DataContract.ReclamosEntry.DESCRIPCION + " TEXT NOT NULL,"
                + DataContract.ReclamosEntry.ESTADO + " TEXT NOT NULL,"
                + DataContract.ReclamosEntry.DOCUMENTO + " TEXT NOT NULL,"
                + DataContract.ReclamosEntry.IDSITIO + " INTEGER,"
                + DataContract.ReclamosEntry.IDDESPERFECTO + " INTEGER NOT NULL)";

        db.execSQL(CREATE_RECLAMOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveReclamo (Reclamo reclamo){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(DataContract.ReclamosEntry.TABLE_NAME,null, reclamo.toContentValues());
    }

    public List<Reclamo> getReclamos(){
        List<Reclamo> reclamos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.ReclamosEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Reclamo aux = new Reclamo();
                aux.setIdPreliminar(cursor.getString(0));
                aux.setDescripcion(cursor.getString(1));
                aux.setEstado(cursor.getString(2));
                aux.setDocumento(cursor.getString(3));
                aux.setIdSitio(cursor.getInt(4));
                aux.setIdDesperfecto(cursor.getInt(5));
                reclamos.add(aux);
            }while (cursor.moveToNext());
        }
        return reclamos;
    }

    public void deleteReclamos(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DataContract.ReclamosEntry.TABLE_NAME, null, null);
    }
}
