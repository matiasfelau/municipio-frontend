package ar.edu.uade.municipio_frontend.Utilities;

import android.provider.BaseColumns;

public class DataContract {
    public static abstract class VecinosEntry implements BaseColumns {
        public static final String TABLE_NAME = "Vecino";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO ="apellido";
        public static final String DOCUMENTO = "documento";
        public static final String EMAIL = "email";
    }
}
