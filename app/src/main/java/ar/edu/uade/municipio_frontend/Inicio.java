package ar.edu.uade.municipio_frontend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends AppCompatActivity {
    int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton botonIzquierda = findViewById(R.id.tipoUsuarioIzquierda);
        ImageButton botonDerecha = findViewById(R.id.tipoUsuarioDerecha);
        c = 0;
        final ConstraintLayout[] actual = {findViewById(R.id.layoutVecino)};
        botonIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c > 0) {
                    c--;
                    actual[0] = cambiarUsuario(actual[0]);
                }
            }
        });
        botonDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c < 2) {
                    c++;
                    actual[0] = cambiarUsuario(actual[0]);
                }
            }
        });
        @SuppressLint("CutPasteId")
        ConstraintLayout vecino = findViewById(R.id.layoutVecino);
        Button botonOlvido = vecino.findViewById(R.id.botonOlvido);
        ConstraintLayout olvido = findViewById(R.id.layoutOlvido);
        botonOlvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vecino.setVisibility(View.INVISIBLE);
                olvido.setVisibility(View.VISIBLE);
            }
        });

        Button botonRegistro = vecino.findViewById(R.id.botonRegistro);
        ConstraintLayout registro = findViewById(R.id.layoutRegistro);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vecino.setVisibility(View.INVISIBLE);
                registro.setVisibility(View.VISIBLE);
            }
        });
        Button botonEnviarOlvido = olvido.findViewById(R.id.botonEnviarOlvido);
        botonEnviarOlvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                olvido.setVisibility(View.INVISIBLE);
                vecino.setVisibility(View.VISIBLE);
            }
        });
        Button botonEnviarRegistro = registro.findViewById(R.id.botonEnviar);
        botonEnviarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro.setVisibility(View.INVISIBLE);
                vecino.setVisibility(View.VISIBLE);
            }
        });
    }
    private ConstraintLayout cambiarUsuario(ConstraintLayout usuarioActual) {
        usuarioActual.setVisibility(View.INVISIBLE);
        TextView tipoUsuario = findViewById(R.id.tipoUsuario);
        String[] strings = getResources().getStringArray(R.array.tipoUsuario);
        tipoUsuario.setText(strings[c]);
        switch (c) {
            case 0:
                usuarioActual = findViewById(R.id.layoutVecino);
                break;
            case 1:
                usuarioActual = findViewById(R.id.layoutInspector);
                break;
            case 2:
                usuarioActual = findViewById(R.id.layoutInvitado);
                break;
        }
        usuarioActual.setVisibility(View.VISIBLE);
        return usuarioActual;
    }
}