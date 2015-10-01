package com.atik.elibrary;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class RestCategoria extends AsyncTask<Object, Void, Object>{
	
	private String url="http://10.0.2.2:8090/elibrary/api/categoria"; //URL a cambiar con gusto
	private String opcion="";
	private int id;
	private EditText nombreCategoria;
	private ArrayAdapter<String> adaptador ;
	static String obtenerTodos="todo";
	static String obtenerUno="uno";
	static String editar="editar";
	static String nuevo="nuevo";
	static String eliminar="eliminar";
	
	public RestCategoria(String opcion, int id, EditText nombreCategoria) {
	//	super();
		this.opcion = opcion;
		this.id = id;
		this.nombreCategoria = nombreCategoria;		
	}
	
	public RestCategoria(String opcion, ArrayAdapter<String> adapter) {
//		super();
		this.opcion = opcion;
		this.adaptador = adapter;
	}

	@Override	
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		Log.e("ServicioRest", "opcion: "+opcion);
		if(opcion==obtenerTodos){
			Log.e("ServicioRest", "aqui es "+opcion+"="+obtenerTodos);
			HttpGet del = new HttpGet(url+"");		
			del.setHeader("content-type", "application/json");
			String[] clientes ;
			try
			{	
				HttpResponse resp = httpClient.execute(del);				
				String respStr = EntityUtils.toString(resp.getEntity());			
				JSONArray respJSON = new JSONArray(respStr);	 				
		        clientes= new String[respJSON.length()];		 
		        for(int i=0; i<respJSON.length(); i++)
		        {
		            JSONObject obj = respJSON.getJSONObject(i);	 	 
		            int idCli = obj.getInt("id");
		            String nombCli = obj.getString("nombre");		            		            		
		            clientes[i] = idCli + ": " + nombCli;
		        }	        		        
		        return clientes;
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error TODO Categoria! nose a cual no entro XD", ex);
				clientes = new String[1];
				clientes[0]="0: esto lo llene en exception";
				return clientes;
			}
		}
		else if(opcion==obtenerUno){
			HttpGet del = new HttpGet(url+"/"+id);		
			del.setHeader("content-type", "application/json");
			try			
			{	HttpResponse resp = httpClient.execute(del);				
				String respStr = EntityUtils.toString(resp.getEntity());								
				JSONObject respJSON = new JSONObject(respStr);						        
				//nombre.setText(respJSON.getString("nombre").toString());													         	            
		        nombreCategoria.setText(respJSON.getString("nombre").toString());		       							 	
		        return true;
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error Obtener Uno! nose a cual no entro XD", ex);
				return null;
			}
		}
		else if(opcion==nuevo){
			String retorno="false";
			try			
			{	HttpPost post = new HttpPost(url);
				post.setHeader("content-type", "application/json");
				JSONObject dato = new JSONObject();
			 	dato.put("nombre", nombreCategoria.getText().toString());			 				 	
			 	StringEntity entity = new StringEntity(dato.toString());			 	
			 	post.setEntity(entity);
			 	
			 	HttpResponse resp = httpClient.execute(post);
			 	String respStr = EntityUtils.toString(resp.getEntity());
			 	JSONObject rpt = new JSONObject(respStr);
			 	retorno=rpt.getString("success");
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error en Nueva Categoria! nose a cual no entro", ex);								
			}
			return retorno;
		}
		else if(opcion==editar){
			String retorno="false";
			try			
			{	HttpPut put = new HttpPut(url+"/"+id);				
				put.setHeader("content-type", "application/json");
				JSONObject dato = new JSONObject();
				dato.put("id", id);
			 	dato.put("nombre", nombreCategoria.getText().toString());			 			 			 		 
			 	StringEntity entity = new StringEntity(dato.toString());			 	
			 	put.setEntity(entity);			 				 	
			 	HttpResponse resp = httpClient.execute(put);			 	
			 	String respStr = EntityUtils.toString(resp.getEntity());  			 	
			 	JSONObject rpt = new JSONObject(respStr); //capturemos rpt
			 	retorno=rpt.getString("success");
			 	Log.e("ServicioRest", "id: "+id);
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error en Editar Categoria! nose a cual no entro", ex);							
			}
			return retorno;
		}
		else if(opcion==eliminar){
			String rpt="false";
			HttpDelete del = new HttpDelete(url+"/"+id);				 
			del.setHeader("content-type", "application/json");			 
			try
			{
			        HttpResponse resp = httpClient.execute(del);
			        String respStr = EntityUtils.toString(resp.getEntity());
			        JSONObject r = new JSONObject(respStr);
				 	rpt=r.getString("success");			        
			}
			catch(Exception ex)
			{		
			        Log.e("ServicioRest","Error eliminando!", ex);			        
			}
			return rpt;
		}
		else{
			return null;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(opcion==obtenerTodos){
			adaptador.addAll((String[])result);
			adaptador.notifyDataSetChanged();
		}
		else if(opcion==obtenerUno){
			
		}else{
			
		}
	}
	
}
