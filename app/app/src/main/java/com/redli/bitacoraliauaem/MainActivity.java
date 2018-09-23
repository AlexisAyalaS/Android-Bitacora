package com.redli.bitacoraliauaem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public String matricula;
    public String name;
    public String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mandamos a llamar los elementos de la vista
        final EditText etUser = (EditText) findViewById(R.id.etUser);
        final EditText etAccount = (EditText) findViewById(R.id.etPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        Button btSingIn = (Button) findViewById(R.id.btSingIn);

        //Capturamos el evento del boton
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Buscamos si existen Usuarios

                //Comprobamos si existe o no el usuario
                String user = etUser.getText().toString();
                String pass = etAccount.getText().toString();

                if (Login(user, pass).equals(true)) {
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", username);
                    i.putExtra("name", name);
                    i.putExtra("matr", matricula);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o Matricula no Validos", Toast.LENGTH_LONG).show();
                }

            }
        });
        btSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se abre la segunda Ventana
                Intent i = new Intent(getApplicationContext(), SingInActivity.class);
                startActivity(i);
            }
        });
    }


    public Boolean Login(String user, String matr) {
        Boolean exist = false;
        UsuariosSQLiteHelper admin = new UsuariosSQLiteHelper(this,
                "DBUsuarios", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = "nombre";
        Cursor fila = bd.rawQuery(
                "select * from usuarios", null);
        if (fila.moveToFirst()) {
            do {
                if (fila.getString(2).equals(user) && fila.getString(0).equals(matr)) {
                    exist = true;
                    matricula = fila.getString(0).toString();
                    name = fila.getString(1).toString();
                    username = fila.getString(2).toString();
                    Toast.makeText(this, "Bienvenid@: " + fila.getString(2), Toast.LENGTH_SHORT).show();
                } else {
                }
            } while (fila.moveToNext());

        }
        bd.close();
        return exist;
    }
}
