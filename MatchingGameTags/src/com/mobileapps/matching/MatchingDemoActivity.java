package com.mobileapps.matching;

import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchingDemoActivity extends Activity {

	public int[] imageViews= {R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3,R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7,
			R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11 ,R.id.imageView12, R.id.imageView13, R.id.imageView14, R.id.imageView15};

	public int[] iconIds = new int[] {R.drawable.icon0, R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7,
			R.drawable.icon0, R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7};	
	public ArrayList<Integer> iconIdsList = new ArrayList<Integer>();
	private final Handler handler = new Handler();	
	private boolean touchEnabled = true;
	private int count;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		for(int i: iconIds){
			iconIdsList.add(i);
		}
		setupGame();
		
		Button b = (Button) findViewById(R.id.button_exit);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		b = (Button) findViewById(R.id.button_newgame);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setupGame();
			}
		});
	}

	public ImageView iv1, iv2;

	public void setupGame(){
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText("");
		count =imageViews.length;
		iv1 = null;
		Collections.shuffle(iconIdsList); 	
		for(int i=0; i<imageViews.length; i++){    		
			ImageView iv = (ImageView) findViewById(imageViews[i]);
			iv.setImageResource(R.drawable.empty);
			iv.setTag(R.id.blinded, true);
			iv.setTag(R.id.done, false);
			iv.setTag(R.id.imageViewID, iconIdsList.get(i).intValue());
		}
	}

	public void onClick(View v){		
		if(touchEnabled){
			ImageView iv = (ImageView) v;
			if((Boolean)iv.getTag(R.id.blinded)){
				iv.setImageResource(((Integer)iv.getTag(R.id.imageViewID)).intValue());
				iv.setTag(R.id.blinded, false);
				if(iv1 == null){    			
					iv1 = iv;
					Log.d("demo1", "first");
				} else{
					Log.d("demo1", "second");
					if(((Integer)iv1.getTag(R.id.imageViewID)).intValue() == ((Integer)iv.getTag(R.id.imageViewID)).intValue()){
						iv1.setTag(R.id.done, true);
						iv.setTag(R.id.done, true);    	
						iv1 = null;
						Log.d("demo1", "same");
						count = count - 2;
						if(count <=0){
							TextView tv = (TextView) findViewById(R.id.textView1);
							tv.setText("Well Done !!");
						}
					} else{					
						Log.d("demo1", "not same");
						touchEnabled = false;
						iv2 = iv;
						handler.postDelayed(new Runnable() {
							public void run() {
								iv1.setTag(R.id.blinded, true);
								iv2.setTag(R.id.blinded, true);
								iv1.setImageResource(R.drawable.empty);
								iv2.setImageResource(R.drawable.empty);
								iv1 = null;
								touchEnabled = true;
							}
						}, 1000);
					}
				}
			}
		}
	}
}