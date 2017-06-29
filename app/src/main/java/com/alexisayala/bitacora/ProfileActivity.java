package com.alexisayala.bitacora;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.y;

public class ProfileActivity extends AppCompatActivity {

    public String lastDate, lastTimeIni, lastTimeEnd, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String name = (String) getIntent().getExtras().getSerializable("name");
        String user = (String) getIntent().getExtras().getSerializable("user");
        final String matr = (String) getIntent().getExtras().getSerializable("matr");
        try {
            getSesions(matr);
        } catch (Exception e) {

        }

        TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        final EditText etdate = (EditText) findViewById(R.id.etdate);
        final EditText etHini = (EditText) findViewById(R.id.etHini);
        final Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);

        simpleChronometer.start();
        simpleChronometer.setFormat("%m"); // set the format for a chronometer

        tvWelcome.setText("Bienvenid@: " + name);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final Calendar cal = Calendar.getInstance();


        etdate.setText(String.valueOf(dateFormat.format(cal.getTime())));
        etHini.setText(String.valueOf(timeFormat.format(cal.getTime())));


        Button btEndSesion = (Button) findViewById(R.id.btEndSesion);
        btEndSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DateFormat now = new SimpleDateFormat("HH:mm");
                    Calendar calen = Calendar.getInstance();

                    simpleChronometer.stop();

                    String stDate = etdate.getText().toString();
                    String stHini = etHini.getText().toString();
                    String stHFin = String.valueOf(now.format(calen.getTime()));
                    String stDuration = simpleChronometer.getText().toString() + "  mm/ss";


                    //Conexion con DB

                    //Abrimos la base de datos 'DBUsuarios' en modo escritura
                    UsuariosSQLiteHelper usdbh =
                            new UsuariosSQLiteHelper(ProfileActivity.this, "DBUsuarios", null, 1);

                    SQLiteDatabase db = usdbh.getWritableDatabase();

                    //Si hemos abierto correctamente la base de datos
                    if (db != null) {

                        //Insertamos los datos en la tabla Usuarios
                        db.execSQL("INSERT INTO bitacora (matricula, fecha, horaini, horafin, duracion) " +
                                "VALUES (" + matr + ", '" + stDate + "','" + stHini + "', '" + stHFin + "', '" + stDuration + "')");

                        //Cerramos la base de datos
                        db.close();

                        //Se abre la segunda Ventana
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                    Toast.makeText(getApplicationContext(), "Registro Guardado Exitosamente", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", e.getMessage().toString());
                }
            }
        });

    }

    public void getSesions(String matr) {

        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        TextView tvHoraIni = (TextView) findViewById(R.id.tvHoraIni);
        TextView tvHoraFin = (TextView) findViewById(R.id.tvHoraFin);
        TextView tvDuration = (TextView) findViewById(R.id.tvDuration);

        UsuariosSQLiteHelper admin = new UsuariosSQLiteHelper(this,
                "DBUsuarios", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from bitacora", null);
        if (fila.moveToFirst()) {
            do {
                if (fila.getString(0).equals(matr)) {
                    lastDate = fila.getString(1);
                    lastTimeIni = fila.getString(2);
                    lastTimeEnd = fila.getString(3);
                    duration = fila.getString(4);

                    tvDate.setText(lastDate + "\n");
                    tvHoraIni.setText(lastTimeIni + "\n");
                    tvHoraFin.setText(lastTimeEnd + "\n");
                    tvDuration.setText(duration + "\n");

                } else {
                }
            } while (fila.moveToNext());

        }
        bd.close();

    }

    public Boolean getName(String user) {
        Boolean exist = false;
        UsuariosSQLiteHelper admin = new UsuariosSQLiteHelper(this,
                "DBUsuarios", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = "nombre";
        Cursor fila = bd.rawQuery(
                "select * from usuarios", null);
        if (fila.moveToFirst()) {
            do {
                if (fila.getString(1).equals(user)) {
                    exist = true;
                } else {
                }
            } while (fila.moveToNext());

        }
        bd.close();
        return exist;
    }

    public String diferenciaFechas(String inicio, String llegada) {

        Date fechaInicio = null;
        Date fechaLlegada = null;

        // configuramos el formato en el que esta guardada la fecha en
        //  los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de
            // las Strings
            fechaInicio = (Date) formato.parse(inicio);
            fechaLlegada = (Date) formato.parse(llegada);

        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }

        // tomamos la instancia del tipo de calendario
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();

        // Configramos la fecha del calendatio, tomando los valores del date que
        // generamos en el parse
        calendarInicio.setTime(fechaInicio);
        calendarFinal.setTime(fechaLlegada);

        // obtenemos el valor de las fechas en milisegundos
        long milisegundos1 = calendarInicio.getTimeInMillis();
        long milisegundos2 = calendarFinal.getTimeInMillis();

        // tomamos la diferencia
        long diferenciaMilisegundos = milisegundos2 - milisegundos1;

        // Despues va a depender en que formato queremos  mostrar esa
        // diferencia, minutos, segundo horas, dias, etc, aca van algunos
        // ejemplos de conversion

        // calcular la diferencia en segundos
        long diffSegundos = Math.abs(diferenciaMilisegundos / 1000);

        // calcular la diferencia en minutos
        long diffMinutos = Math.abs(diferenciaMilisegundos / (60 * 1000));
        long restominutos = diffMinutos % 60;
        Log.d("TAAG", String.valueOf(restominutos));

        // calcular la diferencia en horas
        long diffHoras = (diferenciaMilisegundos / (60 * 60 * 1000));
        Log.d("TAAG", String.valueOf(diffHoras));

        // calcular la diferencia en dias
        long diffdias = Math.abs(diferenciaMilisegundos / (24 * 60 * 60 * 1000));

        // devolvemos el resultado en un string
        return String.valueOf(diffHoras + "H " + restominutos + "m ");
    }

}
