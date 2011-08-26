package com.application.android.amazon;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BookScanActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);        
		TextView textview = new TextView(this);        
		textview.setText("This is the BookScan Activity! There will be a button.");        
		setContentView(textview);    
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult =
						IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String upc = scanResult.getContents();

					Log.d("DEBUG", "Done with scan. ISBN is: " + upc);

					Log.d("DEBUG", "Kicking off Lookup Activity.");

					// Got barcode... So will kick off the Intent to lookup Amazon for Book ISBN
					Intent amazonLookupIntent = new Intent(this, AmazonRetrieveDetail.class);					
					amazonLookupIntent.putExtra("BOOK_ISBN", upc);					
					startActivity(amazonLookupIntent);
				}
			}
			break;
		}
		}

	}


}
