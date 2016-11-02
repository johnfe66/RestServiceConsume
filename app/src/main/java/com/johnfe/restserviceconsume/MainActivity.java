package com.johnfe.restserviceconsume;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.johnfe.restserviceconsume.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    private Button btnGetData;
    private Button btnImagen;
    private EditText txtNombre;
    private EditText txtId;
    private EditText txtClave;
    private EditText txtUsuario;
    private EditText txtCedula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);


        txtNombre=(EditText) findViewById(R.id.txtnombre);
        txtCedula=(EditText) findViewById(R.id.txtcedula);
        txtUsuario=(EditText) findViewById(R.id.txtusuario);
        txtClave=(EditText) findViewById(R.id.txtclave);
        txtId=(EditText) findViewById(R.id.txtid);
        btnGetData=(Button) findViewById(R.id.btnGetData);
        btnImagen=(Button) findViewById(R.id.btnImagen);

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Imagen.class);
                startActivity(intent);

            }
        });
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*new InsertData().execute("http://54.244.201.82:8080/RestfulService2/serviciosRest/usuario/nuevoUsuario"
                        +"?nombre="+txtNombre.getText()
                        +"&usuario="+txtUsuario.getText()
                        +"&clave="+txtClave.getText()
                        +"&cedula="+txtCedula.getText()
                        +"&id="+txtId.getText(),"hola soy el parametro 1"

                );*/
                new InsertData().execute("http://54.244.201.82:8080/RestfulService2/serviciosRest/usuario/listaUsuarios"
                        ,"hola soy el parametro 2"

                );
            }
        });
    }

    public class InsertData extends AsyncTask<String,String,String>{

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setMessage("insertando datos...");
            progreso.setCancelable(false);
            progreso.show();

        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection=null;
            BufferedReader reader=null;

            try {
                URL url = new URL(params[0]);
                System.out.println(params[1]);
                connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line="";

                while ((line=reader.readLine())!=null){
                    buffer.append(line);

                }
                System.out.println(buffer.toString());

                String respuestaJson = buffer.toString();

              //  JSONObject jsonRespuesta = new JSONObject(respuestaJson);
                //String mensaje = jsonRespuesta.getString("id");


                JSONArray jsonList = new JSONArray(respuestaJson);
                for (int i = 0; i < jsonList.length(); ++i){

                    JSONObject registro = jsonList.getJSONObject(i);
                    //System.out.println(registro.getString("nombre"));
                    System.out.println(registro.getString("id"));
                }



                //return mensaje;
                return "hola";




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }

                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String mensaje) {
            progreso.dismiss();
            super.onPostExecute(mensaje);
            Toast.makeText(MainActivity.this,"Termine", Toast.LENGTH_SHORT).show();

        }
    }
}


