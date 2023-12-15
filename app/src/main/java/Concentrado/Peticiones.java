package Concentrado;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.DatosDTO;

public class Peticiones {

    Context contexto;
    String URL = "http://10.0.2.2/Hugoweb/API/peticion/server.php";
    String URLD = "http://10.0.2.2/Hugoweb/API/peticion/delete.php";
    GridView gvdatos;
    List<DatosDTO> ldatos = new ArrayList<>();
    List<String> sdatos = new ArrayList<>();

    public Peticiones(Context contexto) {
        this.contexto = contexto;
    }

    public void regilla(GridView gridView){
        gvdatos=gridView;
    }

    public void CargarDatos(){
        JsonArrayRequest respuesta = new JsonArrayRequest(GET, URL, null, new Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                llenarlista(response);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, "VolleyError" + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("VolleyError", "Error en la solicitud Volley: " + error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(respuesta);
    }

    public void cargarRegistroact(String id){
        String Ruta = Uri.parse(URL)
                .buildUpon()
                .appendQueryParameter("id",id)
                .build().toString();
        JsonObjectRequest respuesta = new JsonObjectRequest(GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("solo id", "solo1 registro: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error solo 1", "onErrorResponse: no se puede cargar"+error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(respuesta);
    }

    public void RegistraNuevo(String nombre, String edad, String correo){
        StringRequest respuesta = new StringRequest(POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("nuevo", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("edad", edad);
                parametros.put("correo", correo);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(respuesta);
    }

    public void ActualizaRegistro(String id,String nombre, String edad, String correo){
        StringRequest respuesta = new StringRequest(PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Actualiza", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error actualiza","onErrorResponse: "+error.getMessage());
            }
        }){
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("ida", id);
                parametros.put("nombre", nombre);
                parametros.put("edad", edad);
                parametros.put("correo", correo);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(respuesta);
    }

    public void EliminarRegistro(String id){
        StringRequest respuesta = new StringRequest(POST, URLD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Borrado Ok", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error delete","onErrorResponse: "+error.getMessage());
            }
        }){
            @org.jetbrains.annotations.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("id",id);
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(respuesta);
    }
    private void llenarlista(JSONArray respuesta){
        DatosDTO datos;
        sdatos.add("ID");
        sdatos.add("Nombre");
        for(int a=0;a<respuesta.length();a++){
            try {
                datos = new DatosDTO(respuesta.getJSONObject(a).getInt("id"),
                        respuesta.getJSONObject(a).getString("nombre"),
                        respuesta.getJSONObject(a).getInt("edad"),
                        respuesta.getJSONObject(a).getString("correo"));
                ldatos.add(datos);
                sdatos.add(String.valueOf(respuesta.getJSONObject(a).getInt("id")));
                sdatos.add(respuesta.getJSONObject(a).getString("nombre"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(contexto, android.R.layout.simple_list_item_1,sdatos);
        gvdatos.setAdapter(adaptador);
    }

    public List<DatosDTO> getLdatos(){
        return ldatos;
    }
    public List<String> getSdatos(){
        return sdatos;
    }
}
