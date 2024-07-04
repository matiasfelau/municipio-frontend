package ar.edu.uade.municipio_frontend.Database.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Database.DataContract;
import ar.edu.uade.municipio_frontend.Models.Empleado;

public class EmpleadoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Empleados.db";

    public EmpleadoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_EMPLEADOS = "CREATE TABLE " + DataContract.EmpleadosEntry.TABLE_NAME + "("
                + DataContract.EmpleadosEntry.LEGAJO + " INT NOT NULL PRIMARY KEY,"
                + DataContract.EmpleadosEntry.NOMBRE +" TEXT NOT NULL,"
                + DataContract.EmpleadosEntry.APELLIDO +" TEXT NOT NULL,"
                + DataContract.EmpleadosEntry.DOCUMENTO+" TEXT NOT NULL,"
                + DataContract.EmpleadosEntry.PASSWORD+" TEXT NOT NULL,"
                + DataContract.EmpleadosEntry.SECTOR+" TEXT NOT NULL)";

        db.execSQL(CREATE_EMPLEADOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveEmpleado(Empleado empleado) {
        SQLiteDatabase db = getWritableDatabase();

        db.insert(DataContract.EmpleadosEntry.TABLE_NAME, null, empleado.toContentValues());

    }

    public Empleado getEmpleadoByLegajo(int legajo) {
        Empleado aux = new Empleado();

        SQLiteDatabase db = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.EmpleadosEntry.TABLE_NAME,
                null,
                DataContract.EmpleadosEntry.LEGAJO+" = ? ", new String[] {
                        String.valueOf(legajo)
                },
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            aux.setLegajo(cursor.getInt(0));

            aux.setNombre(cursor.getString(1));

            aux.setApellido(cursor.getString(2));

            aux.setDocumento(cursor.getString(3));

            aux.setPassword(cursor.getString(4));

            aux.setSector(cursor.getString(5));

        }
        return aux;

    }

    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(DataContract.EmpleadosEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Empleado aux = new Empleado();

                aux.setLegajo(cursor.getInt(0));

                aux.setNombre(cursor.getString(1));

                aux.setApellido(cursor.getString(2));

                aux.setDocumento(cursor.getString(3));

                aux.setPassword(cursor.getString(4));

                aux.setSector(cursor.getString(5));

                empleados.add(aux);

            } while (cursor.moveToNext());

        }
        return empleados;

    }

    public void deleteEmpleados() {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DataContract.EmpleadosEntry.TABLE_NAME, null, null);
    }

}
