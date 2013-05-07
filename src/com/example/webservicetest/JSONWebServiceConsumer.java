package com.example.webservicetest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.classes.JSONParser;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;



public class JSONWebServiceConsumer extends ListActivity {

	private static final String TAG_NAME = "Nombre";
	private static final String TAG_DIRECCION = "Direccion";

	Button boton;
	EditText et;

	JSONArray results = null;

	private String tempUrl = "http://173.193.3.218/ServiceElDIrectorio.svc/busqueda/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jsonweb_service_consumer);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		boton = (Button) findViewById(R.id.startSearch);
		et = (EditText) findViewById(R.id.entrada);

		boton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doSearch(tempUrl + et.getText().toString());
			}
		});

	}

	private void doSearch(String url) {
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		JSONParser jp = new JSONParser();
		JSONArray ja = jp.getJSONFromUrl(url);
		try {
			results = ja;
			for (int i = 0; i < results.length(); i++) {
				JSONObject o = results.getJSONObject(i);
				String nombre = o.getString(TAG_NAME);
				String direccion = o.getString(TAG_DIRECCION);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_NAME, nombre);
				map.put(TAG_DIRECCION, direccion);

				resultList.add(map);
			}

			ListAdapter adapter = new SimpleAdapter(this, resultList,
					android.R.layout.simple_list_item_2, new String[] {
							TAG_NAME, TAG_DIRECCION }, new int[] {
							android.R.id.text1, android.R.id.text2 });

			setListAdapter(adapter);

			// ListView lv = getListView();
		} catch (Exception w) {
			Log.e("Error", w.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jsonweb_service_consumer, menu);
		return true;
	}

}
