package com.alexisayala.bitacora;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SingInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etNumberAccount = (EditText) findViewById(R.id.etNumberAccount);
        final EditText etUser = (EditText) findViewById(R.id.etUser);

        Button btSingIn = (Button) findViewById(R.id.btSingIn);

        btSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String stName = etName.getText().toString();
                    int stNumberAccount = Integer.valueOf(etNumberAccount.getText().toString());
                    String stUser = etUser.getText().toString();


                    //Conexion con DB

                    //Abrimos la base de datos 'DBUsuarios' en modo escritura
                    UsuariosSQLiteHelper usdbh =
                            new UsuariosSQLiteHelper(SingInActivity.this, "DBUsuarios", null, 1);

                    SQLiteDatabase db = usdbh.getWritableDatabase();

                    //Si hemos abierto correctamente la base de datos
                    if (db != null) {

                        //Generamos los datos
                        int matricula = stNumberAccount;
                        String nombre = stName;
                        String usuario = stUser;

                        //Insertamos los datos en la tabla Usuarios
                        db.execSQL("INSERT INTO Usuarios (matricula, nombre, user) " +
                                "VALUES (" + matricula + ", '" + nombre + "','" + usuario + "')");

                        //Cerramos la base de datos
                        db.close();

                        //Se abre la segunda Ventana
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                    }
                    Toast.makeText(getApplicationContext(), "Usuario Registrado Exitosamente", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Boolean getUser(String user) {
        Boolean exist = false;
        UsuariosSQLiteHelper admin = new UsuariosSQLiteHelper(this,
                "DBUsuarios", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = "nombre";
        Cursor fila = bd.rawQuery(
                "select * from usuarios", null);
        if (fila.moveToFirst()) {
            do {
                //Toast.makeText(this, fila.getString(0) + " " + fila.getString(1) + " " + fila.getString(2),
                //        Toast.LENGTH_SHORT).show();
                if (fila.getString(2).equals(user)) {
                    exist = true;
                } else {
                }
            } while (fila.moveToNext());

        }
        bd.close();
        return exist;
    }

}
