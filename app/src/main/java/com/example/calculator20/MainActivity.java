package com.example.calculator20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView preguntaText;
    private EditText respuestaUsuario;
    private TextView contadorText;
    private TextView puntajeText;
    private Button responder;
    private Button intentar;

    private Pregunta preguntaActual;
    private int tiempoRestante;
    private int puntaje;
    private int presionadoTiempo;
    private int presion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preguntaText = findViewById(R.id.preguntaText);
        respuestaUsuario = findViewById(R.id.respuestaUsuario);
        contadorText = findViewById(R.id.contadorText);
        puntajeText = findViewById(R.id.puntajeText);
        responder = findViewById(R.id.responder);
        intentar = findViewById(R.id.intentar);

        puntaje = 0;
        puntajeText.setText("Puntaje:  " + puntaje);

        tiempoRestante = 30;
        contadorText.setText(" " + tiempoRestante);

        presion=0;

        generarNuevaPregunta();
        Timer();

        responder.setOnClickListener(
                (view) -> {

                    verificarRespuesta();

                }
        );

        intentar.setOnClickListener(
                (view) -> {
                    intentarDenuevo();


                }
        );

        preguntaText.setOnTouchListener(
                (view, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            presion = 1;
                            new Thread(() -> {
                                presionadoTiempo++;
                                while (presionadoTiempo < 1500) {
                                    try {
                                        Thread.sleep(150);
                                        presionadoTiempo+=150;
                                        if (presion==1) {
                                            return;
                                        }
                                    } catch (InterruptedException e) {
                                    }
                                }

                                runOnUiThread(() -> {
                                    intentarDenuevo();
                                    intentar.setVisibility(View.GONE);
                                    Timer();
                                });

                            }).start();
                            break;
                        case MotionEvent.ACTION_UP:
                            presion = 0;
                            break;
                    }
                    return true;
                }
        );

    }

    public void verificarRespuesta() {
        String respuestaTexto= respuestaUsuario.getText().toString();
        int respuestaInt= (int) Integer.parseInt(respuestaTexto);

        if(respuestaInt == preguntaActual.getRespuesta()){
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
            puntaje +=5;
            puntajeText.setText("Puntaje: "+ puntaje);
        } else {
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
            puntaje -=4;
            puntajeText.setText("Puntaje: "+ puntaje);
        }
        generarNuevaPregunta();
    }
    public void generarNuevaPregunta(){
        preguntaActual= new Pregunta();
        preguntaText.setText(preguntaActual.getPregunta());
    }

    public void intentarDenuevo(){

        intentar.setVisibility(View.GONE);

        puntaje= 0;
        puntajeText.setText("Puntaje:  "+ puntaje);

        tiempoRestante=30;
        contadorText.setText(" "+ tiempoRestante);

        Timer();
        generarNuevaPregunta();

    }
    public void Timer(){

        new Thread(
                () -> {
                    while (tiempoRestante > 0)
                        try {
                            tiempoRestante--;
                            runOnUiThread(
                                    () -> {
                                        contadorText.setText(" " + tiempoRestante);
                                        if (tiempoRestante > 0) {
                                            intentar.setVisibility(View.GONE);
                                        }
                                        if (tiempoRestante == 0) {
                                            intentar.setVisibility(View.VISIBLE);
                                        }
                                    }
                            );
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e("ERROR", e.toString());
                        }
                }
        ).start();

    }

}