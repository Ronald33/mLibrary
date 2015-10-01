package com.atik.elibrary;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class RestLibros extends AsyncTask<Object, Void, Object>{

	private String url="http://10.0.2.2:8090/elibrary/api/alibro"; //URL a cambiar con gusto
	//private String url="http://192.168.1.237:80/cellar/api/wines";
	private String opcion="";	
	private int id;
	private GridLayout grilla;
	private Object resultados;
	private String[][] datos;
	static String obtenerTodos="todo";
	static String obtenerUno="uno";
	
	
	public RestLibros(String opcion){
		this.opcion=opcion;		
	}
	public RestLibros(String opcion, int id){
		this.opcion=opcion;	
		this.id=id;
	}
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub		
		HttpClient httpClient = new DefaultHttpClient();
		if(opcion==obtenerTodos){
			HttpGet del = new HttpGet(url);		
			del.setHeader("content-type", "application/json");
			try
			{	
				HttpResponse resp = httpClient.execute(del);				
				String respStr = EntityUtils.toString(resp.getEntity());
				
				JSONArray respJSON = new JSONArray(respStr);
				String[][] datos = new String[respJSON.length()][3];
		        for(int i=0; i<respJSON.length(); i++)
		        {
		            JSONObject obj = respJSON.getJSONObject(i);	//otro objetc, para sacar cada registro. 	 		            		           
		            datos[i][0]= ""+obj.getInt("id");
		            datos[i][1]= obj.getString("portada");
		            datos[i][2]= obj.getString("titulo");		            		            
		        }	        		        
		        return datos;
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error TODO! nose a cual no entro XD", ex);								
				return null;
			}
		}				
		else if(opcion==obtenerUno){
			HttpGet del = new HttpGet(url+"/"+id);		
			del.setHeader("content-type", "application/json");
			try			
			{	HttpResponse resp = httpClient.execute(del);				
				String respStr = EntityUtils.toString(resp.getEntity());								
				JSONObject respJSON = new JSONObject(respStr);							 	
		        return respJSON;
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error Obtener Uno! nose a cual no entro XD", ex);
				return null;
			}			

		}
		else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		
		if(opcion==obtenerTodos){
			//String [][] rpt = (String[][]) result;
			
		}
		else if(opcion==obtenerUno){
			
		}
		super.onPostExecute(result);
	}
	
}
