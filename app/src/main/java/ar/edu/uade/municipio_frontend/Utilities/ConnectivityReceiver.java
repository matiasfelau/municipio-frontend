package ar.edu.uade.municipio_frontend.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.CrearReclamo;
import ar.edu.uade.municipio_frontend.Database.Helpers.ReclamosHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.SitiosHelper;
import ar.edu.uade.municipio_frontend.Models.AutenticacionReclamo;
import ar.edu.uade.municipio_frontend.Models.AutenticacionSitio;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Sitio;

public class ConnectivityReceiver extends BroadcastReceiver {
    private Context context;

    public ConnectivityReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // Conectado a Wi-Fi
                syncData();
            }
        }
    }

    private void syncData() {
        // Implementa la lógica de sincronización aquí
        ReclamosHelper reclamosHelper = new ReclamosHelper(context);
        SitiosHelper sitiosHelper = new SitiosHelper(context);

        // Obtener reclamos y sitios guardados localmente
        List<Reclamo> reclamosPendientes = reclamosHelper.getReclamos();
        List<Sitio> sitiosPendientes = sitiosHelper.getSitios();

        // Sincronizar sitios primero (para obtener sus IDs)
        for (Sitio sitio : sitiosPendientes) {
            AutenticacionSitio autenticacionSitio = new AutenticacionSitio();
            autenticacionSitio.setSitio(sitio);
            autenticacionSitio.setAutenticacion(/* Obtén la autenticación correspondiente */);

            nuevoSitio(autenticacionSitio, sitio, reclamosPendientes, reclamosHelper);//MAL
        }
    }
}


