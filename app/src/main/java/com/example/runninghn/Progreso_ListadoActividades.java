package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.Actividad;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Progreso_ListadoActividades extends AppCompatActivity {

    private final ArrayList<Actividad> listaActividades = new ArrayList<>();
    ListView listViewCustomAdapter;
    AdaptadorListaActividad adaptador;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso_listado_actividades);

        listViewCustomAdapter = findViewById(R.id.listadoActividades);
        adaptador = new Progreso_ListadoActividades.AdaptadorListaActividad(this);

        listadoActividad(ActivityTablero.tablero_codigo_usuario);


    }

    class AdaptadorListaActividad extends ArrayAdapter<Actividad> {

        AppCompatActivity appCompatActivity;

        AdaptadorListaActividad(AppCompatActivity context) {
            super(context, R.layout.adapterlistadoactividades, listaActividades);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.adapterlistadoactividades, null);
            Button btnverRecorrido = item.findViewById(R.id.adapbtnverRecorrido);
            TextView fecha = item.findViewById(R.id.adaplblfecha);
            TextView kilometros = item.findViewById(R.id.adaplblkm);
            TextView tiempo = item.findViewById(R.id.adaplbltiempo);
            TextView kcal = item.findViewById(R.id.adaplblkcal);
            fecha.setText(listaActividades.get(position).getFecha());
            kilometros.setText(listaActividades.get(position).getKilometro());
            tiempo.setText(listaActividades.get(position).getTiempo());
            kcal.setText(listaActividades.get(position).getKcal());

            btnverRecorrido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("codigo_actividad",listaActividades.get(position).getCodigo_actividad());
                    startActivity(intent);
                    finish();
                }
            });
            return (item);
        }
    }

    //-----traer el listado de las actividades
    private void listadoActividad(String codigo_usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigo_usuario);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.ListarActividades,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usuarioArray = response.getJSONArray("actividades");

                    listaActividades.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject RowActividad = usuarioArray.getJSONObject(i);
                        Actividad actividad = new Actividad(RowActividad.getString("codigo_actividad"),
                                RowActividad.getString("fechahora"),
                                RowActividad.getString("distancia"),
                                RowActividad.getString("tiempo"),
                                RowActividad.getString("kcal")
                        );
                        listaActividades.add(actividad);

                    }
                    listViewCustomAdapter.setAdapter(adaptador);

                }catch (JSONException ex){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Mensaje: ");
                    alertDialogBuilder
                            .setMessage("No hay listas de actividades")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }




}



