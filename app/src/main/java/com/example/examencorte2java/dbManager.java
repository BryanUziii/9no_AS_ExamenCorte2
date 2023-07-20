package com.example.examencorte2java;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dbManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "examen.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODIGO = "codigo";
    private static final String COLUMN_NOMBREPRODUCTO = "nombreProducto";
    private static final String COLUMN_MARCA = "marca";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_ESTADO = "estado";

    public dbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crea la tabla de usuarios en la base de datos
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " + TABLE_PRODUCTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CODIGO + " TEXT,"
                + COLUMN_NOMBREPRODUCTO + " TEXT,"
                + COLUMN_MARCA + " TEXT,"
                + COLUMN_PRECIO + " TEXT,"
                + COLUMN_ESTADO + " TEXT"
                + ")";
        db.execSQL(CREATE_USUARIOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Elimina la tabla de productos si existe y la vuelve a crear
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public boolean validarCodigo(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_CODIGO};
        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query(
                TABLE_PRODUCTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean existeProducto = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Si existeProducto es true, significa que ya existe un producto con el mismo código
        // Si existeProducto es false, significa que el código no está en uso y es válido para un nuevo producto
        return !existeProducto;
    }

    public boolean aggProducto(String codigo, String nombreProducto, String marca, String precio, String estado) {
        boolean productoAgregado = false;
        // Verificar si el código del producto es válido y no está duplicado en la base de datos
        if (!validarCodigo(codigo)) {
            // El código no es válido o ya existe en la base de datos, muestra un mensaje de error o toma otra acción según tus necesidades
            // Por ejemplo, puedes mostrar un Toast indicando que el código no es válido o ya está en uso

            return productoAgregado;
        }

        // Si el código es válido, procedemos a agregar el producto a la base de datos
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO, codigo);
        values.put(COLUMN_NOMBREPRODUCTO, nombreProducto);
        values.put(COLUMN_MARCA, marca);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_ESTADO, estado);
        long resultado = db.insert(TABLE_PRODUCTOS, null, values);
        if (resultado == -1) {
            // Si el resultado es -1, hubo un error al insertar el producto
            // Puedes manejar el error aquí
        } else {
            // La inserción fue exitosa, muestra un mensaje de éxito o realiza otra acción según tus necesidades
            productoAgregado = true;
        }
        db.close();
        return productoAgregado;
    }

    public void eliminarProducto(String codigo) {
        //Elimina un producto de la base de datos dado su codigo
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, COLUMN_CODIGO + " = ?", new String[]{String.valueOf(codigo)});
        db.close();
    }

    public Producto buscarProductoPorCodigo(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_CODIGO,
                COLUMN_NOMBREPRODUCTO,
                COLUMN_MARCA,
                COLUMN_PRECIO,
                COLUMN_ESTADO
        };

        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query(
                TABLE_PRODUCTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Producto producto = null;
        if (cursor != null && cursor.moveToFirst()) {
            // El cursor tiene datos, construye el objeto Producto
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String nombreProducto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBREPRODUCTO));
            String marca = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MARCA));
            String precio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRECIO));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO));

            producto = new Producto(id, codigo, nombreProducto, marca, precio, estado);

            cursor.close();
        }

        db.close();
        return producto;
    }

    public boolean actualizarProducto(String codigo, String nuevoNombreProducto, String nuevaMarca, String nuevoPrecio, String nuevoEstado) {
        boolean productoActualizado = false;
        // Verificar si el producto con el código proporcionado existe en la base de datos
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {COLUMN_ID};
        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query(
                TABLE_PRODUCTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // El producto con el código proporcionado existe en la base de datos, procedemos a actualizarlo
            int productoId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));

            ContentValues values = new ContentValues();
            values.put(COLUMN_NOMBREPRODUCTO, nuevoNombreProducto);
            values.put(COLUMN_MARCA, nuevaMarca);
            values.put(COLUMN_PRECIO, nuevoPrecio);
            values.put(COLUMN_ESTADO, nuevoEstado);

            int filasActualizadas = db.update(TABLE_PRODUCTOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(productoId)});
            if (filasActualizadas > 0) {
                // La actualización fue exitosa, muestra un mensaje de éxito o realiza otra acción según tus necesidades
                productoActualizado = true;
            } else {
                // Hubo un problema al actualizar el producto, muestra un mensaje de error o realiza otra acción según tus necesidades
            }
        } else {
            // El producto con el código proporcionado no existe en la base de datos
            // Muestra un mensaje indicando que el producto no fue encontrado
            productoActualizado = false;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return productoActualizado;
    }

}
