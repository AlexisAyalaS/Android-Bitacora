package com.redli.bitacoraliauaem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joaquinmorales on 15/05/17.
 */

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {
    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Usuarios (" +
            "matricula INTEGER PRIMARY KEY, " +
            "nombre TEXT, " +
            "user TEXT )";

    String sqlCreateBitacora = "CREATE TABLE Bitacora (" +
            "matricula INTEGER, " +
            "fecha CURRENT_DATE, " +
            "horaini CURRENT_TIME," +
            "horafin CURRENT_TIME," +
            "duracion TEXT )";

    public UsuariosSQLiteHelper(Context contexto, String nombre,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreateBitacora);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Bitacora");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreateBitacora);
    }

}
