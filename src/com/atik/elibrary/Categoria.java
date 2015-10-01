package com.atik.elibrary;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Categoria extends Activity{
	private EditText nombreEdit = null;
	private EditText idEdit = null;
	private Button btnGuardar = null;
	private Button btnEliminar = null;
	private int idCategoria = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_categoria);				
						
//				
		nombreEdit= (EditText) findViewById(R.id.nomCat);
		idEdit= (EditText) findViewById(R.id.idCat);
		btnGuardar = (Button)findViewById(R.id.btnGuardarCat);
		btnEliminar = (Button)findViewById(R.id.btnEliminarCat);
		btnEliminar.setEnabled(false);
					
		ListView lista = (ListView) findViewById(R.id.listCategoria);
		final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lista.setAdapter(adaptador);
		RestCategoria rC = new RestCategoria(RestCategoria.obtenerTodos, adaptador);
		rC.execute();				
		
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String texto=adaptador.getItem((int) id);				
				int idd = Integer.parseInt(texto.substring(0, texto.indexOf(":")));
				String nombre=texto.substring(texto.indexOf(":")+2);		
				idEdit.setText(""+idd);
				idCategoria=idd;
				nombreEdit.setText(nombre);
				btnEliminar.setEnabled(true);
				btnGuardar.setText(R.string.btnGuardarEdit);
			}			
		});
		btnGuardar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String opc="";
				if(idCategoria!=0){ //si esta vacio, es nuevo
					opc= RestCategoria.editar;					
				}
				else{
					opc= RestCategoria.nuevo;
				}
				RestCategoria rC = new RestCategoria(opc, idCategoria, nombreEdit);
				rC.execute();
				String rr="false";
				try {
					rr= rC.get().toString();
					if(rr=="true"){
						Exito("Guardado con Éxito");					
					}else if(rr=="false"){
						Fracaso("No se pudo guardar");
					}
				} catch (InterruptedException e) {														
				} catch (ExecutionException e) {					
				}
			}
		});
		btnEliminar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RestCategoria rcEli = new RestCategoria(RestCategoria.eliminar, idCategoria, null); 
				rcEli.execute();								
				String rr="false";
				try {
					rr= rcEli.get().toString();					
					if(rr=="true"){
						Exito("Eliminado con Éxito");					
					}else{
						Fracaso("No se pudo eliminar");
					}
				} catch (InterruptedException e) {														
				} catch (ExecutionException e) {	}																				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		int sMenuExampleResources[] = { R.menu.menu_categoria };
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(sMenuExampleResources[0], menu);        
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mLibros:
			Intent intent= new Intent();
			intent.setComponent(new ComponentName(this, Inicio.class));
			startActivity(intent);
			return true;
			
		case R.id.mCategoriaNuevo:
			idEdit.setText("");
			nombreEdit.setText("");
			nombreEdit.setFocusable(true);
			nombreEdit.setFocusableInTouchMode(true);
			btnEliminar.setEnabled(false);
			btnGuardar.setText(R.string.btnGuardar);
			return true;
		}
		return false;
	}
	public void Exito(String msn){
		Toast.makeText(this, msn, Toast.LENGTH_SHORT).show();
		Intent ii= new Intent();
		ii.setComponent(new ComponentName(this, Categoria.class));
		startActivity(ii);		
	}
	public void Fracaso(String msn){
		Toast.makeText(this, msn, Toast.LENGTH_SHORT).show();
	}
}
