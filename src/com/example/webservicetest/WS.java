package com.example.webservicetest;

import java.util.ArrayList;
import java.util.StringTokenizer;

import java.lang.reflect.Type;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.classes.Negocio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;


public class WS extends Activity {

	Button search;
	EditText busqueda;
	TextView resErr;
	ListView lv;

	public String PROPERTY_NAME = "search";
	public String SOAP_ACTION = "http://tempuri.org/NormalSearch";
	public String METHOD_NAME = "NormalSearch";
	public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	public final String SOAP_ADDRESS = "http://173.193.3.218/WebService1.asmx";
	public static String TAG = "WS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ws);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		busqueda = (EditText) findViewById(R.id.lookingText);
		resErr = (TextView) findViewById(R.id.resultError);
		search = (Button) findViewById(R.id.consumeWS);
		lv = (ListView) findViewById(R.id.resultados);

		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				resErr.setText("");
				String term = busqueda.getText().toString();
				Convert(term);
			}
		});

	}
	
	public void returnResults(String s){

		StringTokenizer st = new StringTokenizer(s, "\n");
		final ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i < st.countTokens(); i++){
			list.add(st.nextToken());
		}
		
		//ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		//lv.setAdapter(aa);
	}
	
	public void manageJSON (String s){
		Gson gson = new Gson();
		Type lstT = new TypeToken< ArrayList<Negocio>>(){}.getType();
		ArrayList<Negocio> aln = new ArrayList<Negocio>();
		aln=gson.fromJson(s, lstT);
		lv.setAdapter(new ArrayAdapter<Negocio>
		(getApplication(), android.R.layout.simple_list_item_1, aln));
		
	}

	public void Convert(String val) {
		
			SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,METHOD_NAME);
			
			
			
			PropertyInfo p = new PropertyInfo();
			p.setName(PROPERTY_NAME);
			p.setValue(val.toString());
			p.setType(String.class);
			request.addProperty(p);
			//request.addProperty(PROPERTY_NAME,val);
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			//envelope.addMapping(WSDL_TARGET_NAMESPACE, "Negocios", String.class);
			
			
			HttpTransportSE ahs = new HttpTransportSE(SOAP_ADDRESS);
			
			
			try {
			ahs.call(SOAP_ACTION, envelope);
				
			
			
			//SoapObject response = (SoapObject)envelope.getResponse();
			SoapObject response = (SoapObject) envelope.bodyIn;
			String toTokenize = response.getPropertyAsString(0).toString();
			manageJSON(toTokenize);
			
			
			//returnResults(toTokenize);
			//resErr.setText(response.getProperty(0).toString());
			
			//String str = ((SoapObject)response.getProperty(0)).getPropertyAsString(0);
			//resErr.setText(str);
			
			//int i = response.getPropertyCount();
			
			
			

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

}
