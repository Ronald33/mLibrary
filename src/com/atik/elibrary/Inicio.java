package com.atik.elibrary;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Inicio extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_inicio);
		GridLayout grilla = (GridLayout) findViewById(R.id.gridContainer);
		
		RestLibros restL= new RestLibros(RestLibros.obtenerTodos);
		restL.execute();
		String[][] rr=null;
		try {
			rr= (String[][]) restL.get();
			for(int i=0; i<rr.length; i++){				
				ImageView imgLibro = new ImageView(this);
				imgLibro.setLayoutParams(new FrameLayout.LayoutParams(80,80));
				//imgLibro.getLayoutParams().width = 80; 
				CargarImg cargarimg = new CargarImg(imgLibro);
				cargarimg.execute("http://10.0.2.2:8090/elibrary/"+rr[i][1]);				
				
				final TextView tex= new TextView(this);
				tex.setLayoutParams(new FrameLayout.LayoutParams(80, LayoutParams.WRAP_CONTENT));				
				tex.setText(rr[i][2]);
				tex.setGravity(Gravity.CENTER_HORIZONTAL);
				LinearLayout cuadro = new LinearLayout(this);
				
				cuadro.setOrientation(LinearLayout.VERTICAL);				
				cuadro.addView(imgLibro);
				cuadro.addView(tex);				
				final String id = rr[i][0];
				cuadro.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Log.e("ServicioRest", "click en: "+id);							
						AbrirDetalles(Integer.parseInt(id));
					}
				});
				grilla.addView(cuadro, Math.min(1, grilla.getChildCount()));								
			}
			
		} catch (InterruptedException e) {														
		} catch (ExecutionException e) {	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		int sMenuExampleResources[] = { R.menu.menu };
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(sMenuExampleResources[0], menu);        
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mLibros:			
			return true;
		case R.id.mCategoria:
			Intent intent= new Intent();
			intent.setComponent(new ComponentName(this, Categoria.class));
			startActivity(intent);
			return true;
		}
		return false;
	}
	private void AbrirDetalles(int id){
		Intent ii= new Intent();	
		ii.setComponent(new ComponentName(this, DetalleLibro.class));
		ii.putExtra("id", id);
		//ii.putExtra("opcion", "editar");
		startActivity(ii);
	}
	
}
