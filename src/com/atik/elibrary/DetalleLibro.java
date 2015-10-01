package com.atik.elibrary;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.MessageQueue;
//import android.provider.Telephony.TextBasedSmsColumns;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetalleLibro extends Activity{
	//private static final int sMenuExampleResources[] = { R.menu.menu };
	private int id=0;
	private String url="http://10.0.2.2:8090/elibrary/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_detalle_libro);
		TextView titulo = (TextView)findViewById(R.id.idTitulo);
		TextView descripcion=(TextView)findViewById(R.id.idDescripcion);
		TextView autor = (TextView)findViewById(R.id.idAutor);
		TextView editorial = (TextView)findViewById(R.id.idEditorial);
		TextView categoria = (TextView)findViewById(R.id.idCategoria);
		TextView descargas = (TextView)findViewById(R.id.idDescargas);
		TextView link = (TextView) findViewById(R.id.idLink);		                
		ImageView imagen = (ImageView) findViewById(R.id.idImagen);
        Bundle bundle = getIntent().getExtras();
        try
        {			
			id= bundle.getInt("id");											
		}catch (NullPointerException n){			
		}
        
        RestLibros rl = new RestLibros(RestLibros.obtenerUno, id);
        rl.execute();
        
    	JSONObject json=null;
		try {
			json= (JSONObject) rl.get();
			titulo.setText(json.getString("titulo").toString());
			//autor.setText(json.getString("autor").toString());
			descripcion.setText(json.getString("descripcion").toString());
			editorial.setText(json.getString("edit_nombre").toString());			
			categoria.setText(json.getString("cate_nombre").toString());			
			link.setText(Html.fromHtml("<a href=\""+url+json.getString("link")+"\">"+url+R.string.contenLink+"</a>"));                
	        link.setMovementMethod(LinkMovementMethod.getInstance());
	        descargas.setText("("+json.getString("descargas").toString()+") "+getString(R.string.txtDescargas));
	        
	        CargarImg cargarimg = new CargarImg(imagen);
			cargarimg.execute(url+json.getString("portada"));
			
		} catch (InterruptedException e) {														
		} catch (ExecutionException e) {				
		} catch (JSONException e) {			
			e.printStackTrace();
		} 
        
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mLibros:
			Intent intentL= new Intent();
			intentL.setComponent(new ComponentName(this, Inicio.class));
			startActivity(intentL);
			return true;
		case R.id.mCategoria:
			Intent intent= new Intent();
			intent.setComponent(new ComponentName(this, Categoria.class));
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub	
		int sMenuExampleResources[] = { R.menu.menu };
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(sMenuExampleResources[0], menu);        
		return true;
	}

}
