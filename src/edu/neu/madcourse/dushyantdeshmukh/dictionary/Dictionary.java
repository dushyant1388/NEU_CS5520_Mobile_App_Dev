package edu.neu.madcourse.dushyantdeshmukh.dictionary;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import edu.neu.madcourse.dushyantdeshmukh.About;
import edu.neu.madcourse.dushyantdeshmukh.MainActivity;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.sudoku.Sudoku;
import edu.neu.madcourse.dushyantdeshmukh.util.BloomFilter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class Dictionary extends Activity implements OnClickListener {

	BloomFilter<String> bloomFilter = new BloomFilter<String>(0.0001, 450000);
	HashSet<String> wordList = new HashSet<String>();
	
	public Dictionary() {
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadBitsetFromFile("compressedWordlist.txt");
		renderWorList();
	}
	
	private void renderWorList() {
		TextView textView = (TextView) findViewById(R.id.dictionary_wordlist);
		textView.setText(getWordListCharSeq());
	}

	private CharSequence getWordListCharSeq() {
		String wordListSeq = "WORD LIST: ";
		
		Iterator<String> iterator = wordList.iterator();
        while (iterator.hasNext()) {
        	wordListSeq += iterator.next() + ", ";
        }
		return wordListSeq;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary_main);

		// Set up click listeners for all the buttons
		EditText editText = (EditText) findViewById(R.id.dictionary_editText);
		 
		// add a keylistener to keep track user input
		editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.length() > 2) {
					checkWord(arg0.toString());
				}
			}
			
		});
		
		View clearButton = findViewById(R.id.dictionary_clear_button);
		clearButton.setOnClickListener(this);

		View returnButton = findViewById(R.id.dictionary_return_button);
		returnButton.setOnClickListener(this);

		View ackButton = findViewById(R.id.dictionary_ack_button);
		ackButton.setOnClickListener(this);
	}

	protected void checkWord(String ipWord) {
		if (bloomFilter.contains(ipWord)) {
			addWord(ipWord);
		}
	}

	private void addWord(String ipWord) {
		wordList.add(ipWord);
		renderWorList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dictionary_clear_button:
			EditText editText = (EditText) findViewById(R.id.dictionary_editText);
			editText.setText("");
			break;
		case R.id.dictionary_return_button:
//			Intent i = new Intent(this, MainActivity.class);
//			startActivity(i);
			finish();
			break;
		case R.id.dictionary_ack_button:
//			Intent i3 = new Intent(this, Dictionary.class);
//			startActivity(i3);
			break;
		}
	}

	public BloomFilter<String> loadBitsetFromFile(String filepath) {
		try {
			AssetManager am = this.getAssets();
			InputStream is = am.open(filepath);
			
		    byte[] fileData = new byte[1110000];
		    DataInputStream dis = new DataInputStream(is);
		    dis.readFully(fileData);
		    dis.close();
		    bloomFilter = BloomFilter.loadBitsetWithByteArray(fileData, bloomFilter);		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bloomFilter;
	}
}
