package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import ar.edu.uade.municipio_frontend.R;

public class ReclamoExitosoDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reclamo_exitoso, null);

        // Set the custom view to the dialog
        builder.setView(view);

        // Configure the button
        view.findViewById(R.id.buttonVolverInicio).setOnClickListener(v -> {
            // Dismiss the dialog
            dismiss();
            // Implementar navegación al inicio (actividad principal)
            requireActivity().finish();
        });

        return builder.create();
    }
}
