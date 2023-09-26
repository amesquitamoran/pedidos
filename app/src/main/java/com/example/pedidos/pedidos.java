package com.example.pedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class pedidos extends AppCompatActivity {

    private EditText etCantidadProducto1, etCantidadProducto2, etCantidadProducto3, etDireccionEnvio;
    private List<String> productosSeleccionados = new ArrayList<>();
    private double totalPedido = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // Inicializar los elementos de la interfaz
        etCantidadProducto1 = findViewById(R.id.etCantidadProducto1);
        etCantidadProducto2 = findViewById(R.id.etCantidadProducto2);
        etCantidadProducto3 = findViewById(R.id.etCantidadProducto3);
        etDireccionEnvio = findViewById(R.id.etDireccionEnvio);
        TextView tvResumenPedido = findViewById(R.id.tvResumenPedido);
        LinearLayout llResumenProductos = findViewById(R.id.llResumenProductos);
        TextView tvTotalPedido = findViewById(R.id.tvTotalPedido);
        Button btnQuitarProducto1 = findViewById(R.id.btnQuitarProducto1);
        Button btnAgregarProducto1 = findViewById(R.id.btnAgregarProducto1);
        Button btnQuitarProducto2 = findViewById(R.id.btnQuitarProducto2);
        Button btnAgregarProducto2 = findViewById(R.id.btnAgregarProducto2);
        Button btnQuitarProducto3 = findViewById(R.id.btnQuitarProducto3);
        Button btnAgregarProducto3 = findViewById(R.id.btnAgregarProducto3);
        Button btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        Button btnAgregar = findViewById(R.id.btnagregar);

        // Agregar listeners a los botones para controlar las cantidades
        btnQuitarProducto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarCantidad(etCantidadProducto1);
            }
        });

        btnAgregarProducto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCantidad(etCantidadProducto1);
            }
        });

        btnQuitarProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarCantidad(etCantidadProducto2);
            }
        });

        btnAgregarProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCantidad(etCantidadProducto2);
            }
        });

        btnQuitarProducto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarCantidad(etCantidadProducto3);
            }
        });

        btnAgregarProducto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCantidad(etCantidadProducto3);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la cantidad de cada producto
                int cantidadProducto1 = obtenerCantidad(etCantidadProducto1);
                int cantidadProducto2 = obtenerCantidad(etCantidadProducto2);
                int cantidadProducto3 = obtenerCantidad(etCantidadProducto3);

                if (cantidadProducto1 == 0 && cantidadProducto2 == 0 && cantidadProducto3 == 0) {
                    Toast.makeText(pedidos.this, "Por favor, agregue al menos un producto", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Limpiar la lista de productos seleccionados
                productosSeleccionados.clear();

                // Calcular el total del pedido
                totalPedido = 0.0;

                // Agregar los productos seleccionados y calcular el total
                // Producto 1
                if (cantidadProducto1 > 0) {
                    productosSeleccionados.add("Pilsen de 650ml - Cantidad: " + cantidadProducto1);
                    totalPedido += cantidadProducto1 * 65.0;
                }

                // Producto 2
                if (cantidadProducto2 > 0) {
                    productosSeleccionados.add("Cristal de 650ml - Cantidad: " + cantidadProducto2);
                    totalPedido += cantidadProducto2 * 65.0;
                }

                // Producto 3
                if (cantidadProducto3 > 0) {
                    productosSeleccionados.add("Cusqueña de 650ml - Cantidad: " + cantidadProducto3);
                    totalPedido += cantidadProducto3 * 68.0;
                }

                // Mostrar el resumen del pedido
                mostrarResumenPedido(productosSeleccionados, totalPedido);

                etCantidadProducto1.setText("");
                etCantidadProducto2.setText("");
                etCantidadProducto3.setText("");
                etDireccionEnvio.setText("");
            }
        });

        // Agregar listener al botón de realizar pedido
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la dirección de envío
                String direccionEnvio = etDireccionEnvio.getText().toString().trim();

                // Validar que se haya ingresado una dirección de envío
                if (TextUtils.isEmpty(direccionEnvio)) {
                    Toast.makeText(pedidos.this, "Por favor, ingrese una dirección de envío", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el mensaje para enviar al dueño
                StringBuilder mensajeBuilder = new StringBuilder("Pedido:\n");
                for (String producto : productosSeleccionados) {
                    mensajeBuilder.append(producto).append("\n");
                }
                mensajeBuilder.append("Dirección de envío: ").append(direccionEnvio);
                String mensaje = mensajeBuilder.toString();

                // Enviar mensaje por WhatsApp al dueño
                enviarMensajeWhatsApp(mensaje);
            }
        });
    }

    private void quitarCantidad(EditText etCantidad) {
        int cantidad = obtenerCantidad(etCantidad);
        if (cantidad > 0) {
            cantidad--;
        }
        etCantidad.setText(String.valueOf(cantidad));
    }

    private void agregarCantidad(EditText etCantidad) {
        int cantidad = obtenerCantidad(etCantidad);
        cantidad++;
        etCantidad.setText(String.valueOf(cantidad));
    }

    private int obtenerCantidad(EditText etCantidad) {
        String cantidadString = etCantidad.getText().toString();
        if (!cantidadString.isEmpty()) {
            return Integer.parseInt(cantidadString);
        }
        return 0;
    }

    private void eliminarProductoSeleccionado(String producto) {
        // Verificar si el producto está presente en la lista de productos seleccionados
        if (productosSeleccionados.contains(producto)) {
            productosSeleccionados.remove(producto);
            mostrarResumenPedido(productosSeleccionados, totalPedido);
        }
    }

    private void mostrarResumenPedido(List<String> productosSeleccionados,double totalPedido) {
        TextView tvResumenPedido = findViewById(R.id.tvResumenPedido);
        LinearLayout llResumenProductos = findViewById(R.id.llResumenProductos);
        TextView tvTotalPedido = findViewById(R.id.tvTotalPedido);
        llResumenProductos.removeAllViews(); // Eliminar los productos mostrados anteriormente

        if (!productosSeleccionados.isEmpty()) {
            for (String producto : productosSeleccionados) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Crear el LinearLayout para contener el producto y el botón de eliminación
                LinearLayout llProducto = new LinearLayout(this);
                llProducto.setLayoutParams(layoutParams);
                llProducto.setOrientation(LinearLayout.HORIZONTAL);

                // Crear el ImageButton para eliminar el producto
                ImageButton btnEliminarProducto = new ImageButton(this);
                btnEliminarProducto.setImageResource(R.drawable.eliminar); // Asignar el ícono de eliminar
                btnEliminarProducto.setBackground(null); // Eliminar el fondo del botón
                btnEliminarProducto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                int iconSize = (int) getResources().getDimension(R.dimen.icon_size); // Obtener el tamaño del ícono desde los recursos
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(iconSize, iconSize);
                btnEliminarProducto.setLayoutParams(layoutParams2);
                btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarProductoSeleccionado(producto);
                    }
                });
                llProducto.addView(btnEliminarProducto);

                // Crear el TextView para mostrar el producto
                TextView tvProducto = new TextView(this);
                tvProducto.setText(producto);
                llProducto.addView(tvProducto);

                llResumenProductos.addView(llProducto);

                // Actualizar el total del pedido sumando el subtotal del producto actual
                totalPedido += obtenerSubtotalProducto(producto);
            }

            tvResumenPedido.setVisibility(View.VISIBLE);
            tvTotalPedido.setVisibility(View.VISIBLE);
            tvTotalPedido.setText("Total del Pedido: S/." + totalPedido);

        } else {
            tvResumenPedido.setVisibility(View.GONE);
            tvTotalPedido.setVisibility(View.GONE);
            tvTotalPedido.setText(""); // Limpiar el texto del total del pedido
        }
    }

    private double obtenerSubtotalProducto(String producto) {
        // Extraer la cantidad del producto
        int cantidad = obtenerCantidadProducto(producto);

        // Extraer el precio unitario del producto (asumiendo un formato "Producto - Precio")
        String[] parts = producto.split(" - ");
        if (parts.length >= 2) {
            String precioString = parts[1];
            try {
                double precio = Double.parseDouble(precioString);
                // Calcular el subtotal del producto
                double subtotal = cantidad * precio;
                return subtotal;
            } catch (NumberFormatException e) {
                // Manejar el error apropiadamente
                e.printStackTrace();
            }
        } else {
            // Manejar el error apropiadamente
        }
        return 0.0;
    }

    private double calcularTotalPedido() {
        double totalPedido = 0.0;
        for (String producto : productosSeleccionados) {
            double subtotalProducto = obtenerSubtotalProducto(producto); // Obtener el subtotal del producto
            totalPedido += subtotalProducto; // Sumar el subtotal al total del pedido
        }
        return totalPedido;
    }

    private int obtenerCantidadProducto(String producto) {
        // Extraer la cantidad del producto (asumiendo un formato "Producto - Cantidad")
        String cantidadString = producto.split(" - ")[1].replace("Cantidad: ", "");
        return Integer.parseInt(cantidadString);
    }

    private void enviarMensajeWhatsApp(String mensaje) {
        mensaje += "\nTotal del Pedido: S/." + totalPedido;

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+51962795407&text=" + mensaje);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}