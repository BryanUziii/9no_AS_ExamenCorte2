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

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        db = new dbManager(getApplicationContext());
        db.onConfigure(db.getWritableDatabase());
        db.onOpen(db.getWritableDatabase());

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtMarca = findViewById(R.id.txtMarca);
        txtPrecio = findViewById(R.id.txtPrecio);
        radioGroup = findViewById(R.id.radioGroup);


        habilitarCampos(false);


        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarCampos()){

                    String codigo = txtCodigo.getText().toString().trim();
                    String nombre = txtNombreProducto.getText().toString().trim();
                    String marca = txtMarca.getText().toString().trim();
                    String precio = txtPrecio.getText().toString().trim();

                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                    // Se ha seleccionado una opción, obtenemos el RadioButton seleccionado
                    RadioButton radioButtonSeleccionado = findViewById(selectedRadioButtonId);

                    // Aquí puedes realizar la acción que desees con el RadioButton seleccionado
                    String opcionSeleccionada = radioButtonSeleccionado.getText().toString();

                    if (db.aggProducto(codigo, nombre, marca, precio, opcionSeleccionada)){
                        Toast.makeText(getApplicationContext(), "Producto agregado", Toast.LENGTH_SHORT).show();
                        habilitarCampos(false);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ya existe un producto con ese codigo.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });


        Button btnLimpiar = findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarCampos(false);
            }
        });

        Button btnNuevo = findViewById(R.id.btnNuevo);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarCampos(true);
            }
        });

        Button btnEditar = findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicio de sesión exitoso, mostrar mensaje o realizar acciones adicionales
                Toast.makeText(getApplicationContext(), "Ventana 'Editar Productos'", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EditarProductoActivity.class); // Corrección aquí
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

    private void habilitarCampos(boolean habilitar) {
        txtCodigo.setEnabled(habilitar);
        txtNombreProducto.setEnabled(habilitar);
        txtMarca.setEnabled(habilitar);
        txtPrecio.setEnabled(habilitar);
        radioGroup.setEnabled(habilitar);

        // Si se deshabilitan los campos, también limpiamos los valores
        if (!habilitar) {
            txtCodigo.setText("");
            txtNombreProducto.setText("");
            txtMarca.setText("");
            txtPrecio.setText("");
            radioGroup.clearCheck();
        }
    }

}