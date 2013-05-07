package com.example.webservicetest;


import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity {

	RadioGroup rg;
	RadioButton cel, far;
	String PROPERTY_NAME;
	EditText input, result;
	Button boton;
	Button JSON;

	String SOAP_ACTION;
	String METHOD_NAME;
	final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	final String SOAP_ADDRESS = "http://173.193.3.218/WebService1.asmx";
	final String INITIALIZED = "initialized";
	final static String MENSAJE = "Bienvenido a la aplicación!!!";

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		final Intent intent = new Intent(this, WS.class);
		final Intent intent2 = new Intent(this, JSONWebServiceConsumer.class);

		SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
		boolean hasPreferences = myPrefs.getBoolean(INITIALIZED, false);

		if (hasPreferences) {
			Log.v("Preferences", "Ya habias entrado antes");
		} else {
			Log.v("Preferences", "La primera vez :D");
			
			Editor editor = myPrefs.edit();
			editor.putBoolean(INITIALIZED, true);
			editor.commit();
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
				generateNotification(this, MENSAJE);

			} else {
				newNotification(MENSAJE, intent);
			}
		}

		input = (EditText) findViewById(R.id.text_in);
		rg = (RadioGroup) findViewById(R.id.rgTemp);
		cel = (RadioButton) findViewById(R.id.radioButton1);
		far = (RadioButton) findViewById(R.id.radioButton2);
		result = (EditText) findViewById(R.id.editText2);
		boton = (Button) findViewById(R.id.boton);
		JSON = (Button) findViewById(R.id.JSONButton);
		
		JSON.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(intent2);
				
			}
		});

		boton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(intent);
			}
		});

		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup rGroup, int checkedId) {

				

				if (cel.isChecked()) {
					PROPERTY_NAME = "far";
					METHOD_NAME = "FarenheitCelcius";
					SOAP_ACTION = "http://tempuri.org/FarenheitCelcius";
				} else {
					PROPERTY_NAME = "cel";
					METHOD_NAME = "CelciusFarenheit";
					SOAP_ACTION = "http://tempuri.org/CelciusFarenheit";
				}

				

			}
		});
	}

	@SuppressWarnings("deprecation")
	private void generateNotification(Context arg0, String hola) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) arg0
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, hola, when);
		String title = arg0.getString(R.string.app_name);
		Intent notificationIntent = new Intent(arg0, WS.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(arg0, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(arg0, title, hola, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}
	
	protected void newNotification (String mensaje, Intent intent){
		Notification.Builder mBuilder = new Notification.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("My Notification")
				.setContentText(mensaje);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(WS.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}

	

}
