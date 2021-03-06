package com.mobileapp.demo1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HttpClientGETActivity extends Activity {
	private Handler handler;
	HttpClient client = new DefaultHttpClient();
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        handler = new Handler(new Handler.Callback() {			
			@Override
			public boolean handleMessage(Message msg) {
				if(msg.getData().containsKey("ERROR")){
					Toast.makeText(getBaseContext(), msg.getData().getString("ERROR"), Toast.LENGTH_LONG).show();
				} else{
					Toast.makeText(getBaseContext(), msg.getData().getString("RESULT"), Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(getData).start();
			}
		});        
    }
    
	private Runnable getData = new Runnable() {
		public void run() {
			Bundle bundle = new Bundle();
			Message msg = new Message();
			BufferedReader in = null;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", "Bob Smith"));
			params.add(new BasicNameValuePair("age", "25"));

			try {
				URI uri = URIUtils.createURI("http", "liispapps.uncc.edu", -1, "/mobileapps/index.php", 
						URLEncodedUtils.format(params, "UTF-8"), null);
				HttpGet request = new HttpGet(uri);

				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuffer sb = new StringBuffer("");
					String line = "";
					while((line = in.readLine()) != null){
						sb.append(line + "\n");
					}
					in.close();
					bundle.putString("RESULT",  sb.toString());
					
					//String data = EntityUtils.toString(response.getEntity());
					//bundle.putString("RESULT",  data);
				} else{
					bundle.putString("ERROR", "Problem with Response");
				}
			} 
			catch (URISyntaxException e1) {
				bundle.putString("ERROR", "Problem with Params");
			}
			catch (Exception e) {
				bundle.putString("ERROR", "Problem with URL");
			}
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	};
}
