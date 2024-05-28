package ar.edu.uade.municipio_frontend.Database.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Database.DataContract;
import ar.edu.uade.municipio_frontend.Models.Invitado;

public class InvitadoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Invitados.db";

    public InvitadoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_INVITADOS = "CREATE TABLE " + DataContract.InvitadosEntry.TABLE_NAME + "("
                + DataContract.InvitadosEntry.ID + " INT NOT NULL PRIMARY KEY,"
                + DataContract.InvitadosEntry.NOMBRE + " TEXT NOT NULL)";

        db.execSQL(CREATE_INVITADOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveInvitado(Invitado invitado) {
        SQLiteDatabase db = getWritableDatabase();

        db.insert(DataContract.InvitadosEntry.TABLE_NAME, null, invitado.toContentValues());

    }

    public List<Invitado> getInvitados() {
        List<Invitado> invitados = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.InvitadosEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Invitado aux = new Invitado();

                aux.setId(cursor.getInt(0));

                aux.setNombre(cursor.getString(1));

                invitados.add(aux);

            } while (cursor.moveToNext());

        }
        return invitados;

    }

    public void deleteInvitados() {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DataContract.InvitadosEntry.TABLE_NAME, null, null);
    }
}
