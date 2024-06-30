package ar.edu.uade.municipio_frontend.Utilities.Container;

import ar.edu.uade.municipio_frontend.Models.Profesional;

public class ProfesionalStoraged {
    private static Profesional profesional;

    public ProfesionalStoraged() {
    }

    public static Profesional getProfesional() {
        return profesional;
    }

    public static void setProfesional(Profesional profesional) {
        ProfesionalStoraged.profesional = profesional;
    }
}
