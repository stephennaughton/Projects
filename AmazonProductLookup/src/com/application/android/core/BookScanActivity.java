package com.application.android.core;

import com.amazon.prodadvertising.api.BookInfoRetriever;
import com.amazon.prodadvertising.api.ProductLookup;
import com.application.android.data.BooksDBAdapter;
import com.application.android.data.vo.BookInfo;
import com.google.zxing.integration.IntentIntegrator;
import com.google.zxing.integration.IntentResult;

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
		
		String isbn = "9780131479418";
		
		Log.d("BOOKINFO", "Retrieving book information");
		BookInfoRetriever bookLookup = new BookInfoRetriever();
		BookInfo bookInfo = bookLookup.lookupAmazonProductInfo(isbn);
		
		// Add to the database
		BooksDBAdapter adapter = new BooksDBAdapter(this);
		adapter.open();
		adapter.createBook(bookInfo);
		adapter.close();
		
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
