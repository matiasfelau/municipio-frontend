package ar.edu.uade.municipio_frontend.Database;

import android.provider.BaseColumns;

public class DataContract {

    public static abstract class VecinosEntry implements BaseColumns {
        public static final String TABLE_NAME = "Vecino";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO = "apellido";
        public static final String DOCUMENTO = "documento";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public static abstract class EmpleadosEntry implements BaseColumns {
        public static final String TABLE_NAME = "Empleado";
        public static final String LEGAJO = "legajo";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO = "apellido";
        public static final String PASSWORD = "password";
        public static final String SECTOR = "sector";
    }

    public static abstract class InvitadosEntry implements BaseColumns {
        public static final String TABLE_NAME = "Invitado";
        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
    }
}
