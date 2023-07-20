package com.example.examencorte2java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditarProductoActivity extends AppCompatActivity {
    private dbManager db;
    private EditText txtCodigo;
    private EditText txtNombreProducto;
    private EditText txtMarca;
    private EditText txtPrecio;

    private RadioGroup radioGroup;

    private RadioButton rBtnPerecedero;
    private RadioButton rBtnNoPerecedero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        db = new dbManager(getApplicationContext());
        db.onConfigure(db.getWritableDatabase());
        db.onOpen(db.getWritableDatabase());

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtMarca = findViewById(R.id.txtMarca);
        txtPrecio = findViewById(R.id.txtPrecio);
        radioGroup = findViewById(R.id.radioGroup);
        rBtnPerecedero = findViewById(R.id.btnPerecedero);
        rBtnNoPerecedero = findViewById(R.id.btnNoPerecedero);


        Button btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = txtCodigo.getText().toString().trim();

                if (codigo.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese un codigo", Toast.LENGTH_SHORT).show();


                } else {

                    // Llamada a la función buscarProductoPorCodigo
                    //String codigoProductoABuscar = "codigo_producto"; // Reemplaza "codigo_producto" con el código que deseas buscar
                    Producto productoEncontrado = db.buscarProductoPorCodigo(codigo);

                    if (productoEncontrado != null) {

                        // Si se encontró el producto, muestra sus detalles en el TextView

                        txtNombreProducto.setText(productoEncontrado.getNombre());
                        txtMarca.setText(productoEncontrado.getMarca());
                        txtPrecio.setText(productoEncontrado.getPrecio());

                        String estado = productoEncontrado.getEstado();

                        Toast.makeText(getApplicationContext(), "Estado:" + productoEncontrado.getEstado(), Toast.LENGTH_SHORT).show();

                        if (estado.equals("Perecedero")){
                            rBtnPerecedero.setChecked(true);
                        } else {
                            rBtnNoPerecedero.setChecked(true);
                        }
                        //txtEstado.setText(productoEncontrado.getMarca());

//                       String detallesProducto = "Nombre: " + productoEncontrado.getNombreProducto() + "\n"
//                               + "Marca: " + productoEncontrado.getMarca() + "\n"
//                               + "Precio: " + productoEncontrado.getPrecio() + "\n"
//                               + "Estado: " + productoEncontrado.getEstado();
//                       resultTextView.setText(detallesProducto);
                    } else {
                        // Si no se encontró el producto, muestra un mensaje indicando que no se encontró
                        Toast.makeText(getApplicationContext(), "No existe producto con codigo: " + codigo, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        Button btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCampos()){

                    String codigo = txtCodigo.getText().toString().trim();
                    String nombre = txtNombreProducto.getText().toString().trim();
                    String marca = txtMarca.getText().toString().trim();
                    String precio = txtPrecio.getText().toString().trim();

                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                    // Se ha seleccionado una opción, obtenemos el RadioButton seleccionado
                    RadioButton radioButtonSeleccionado = findViewById(selectedRadioButtonId);

                    // Aquí puedes realizar la acción que desees con el RadioButton seleccionado
                    String opcionSeleccionada = radioButtonSeleccionado.getText().toString();

                    if (db.actualizarProducto(codigo, nombre, marca, precio, opcionSeleccionada)){
                        Toast.makeText(getApplicationContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    } else {
                        Toast.makeText(getApplicationContext(), "No existe producto con codigo: " + codigo, Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(getApplicationContext(), "Porfavor llene todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });


        Button btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = txtCodigo.getText().toString().trim();

                if (codigo.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese un codigo", Toast.LENGTH_SHORT).show();

                } else {
                    db.eliminarProducto(codigo);
                    limpiarCampos();

                    Toast.makeText(getApplicationContext(), "Producto " + codigo + " Eliminado", Toast.LENGTH_SHORT).show();

                }


            }
        });

        Button btnCerrar = findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicio de sesión exitoso, mostrar mensaje o realizar acciones adicionales
                Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditarProductoActivity.this, MainActivity.class); // Corrección aquí
                startActivity(intent);
            }
        });
    }

    // Función mejorada para limpiar los campos
    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombreProducto.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
        radioGroup.clearCheck(); // Desmarca todos los RadioButtons del RadioGroup
    }

    public boolean validarCampos() {
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtMarca = findViewById(R.id.txtMarca);
        txtPrecio = findViewById(R.id.txtPrecio);
        radioGroup = findViewById(R.id.radioGroup);

        String codigo = txtCodigo.getText().toString().trim();
        String nombre = txtNombreProducto.getText().toString().trim();
        String marca = txtMarca.getText().toString().trim();
        String precio = txtPrecio.getText().toString().trim();

        // Verificar si se ha seleccionado alguna opción en el RadioGroup
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1 || codigo.isEmpty() || nombre.isEmpty() || marca.isEmpty() || precio.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}