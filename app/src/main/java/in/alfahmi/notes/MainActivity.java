package in.alfahmi.notes;

import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.view.*;
import java.io.*;
import android.content.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.widget.*;
import java.text.*;
import java.util.*;
import android.widget.AdapterView.*;
import android.support.design.widget.*;


public class MainActivity extends AppCompatActivity {
	
	SQLiteHelper SQLITEHELPER;
    SQLiteDatabase SQLITEDATABASE;
    Cursor cursor;
    SQLiteListAdapter ListAdapter ;
	String SQLiteQuery;
	public static final String TABLE_NAME="transactionTable";
	public static final String KEY_ID="id";
	String test = "";
	private static String DB_PATH = "/data/data/in.alfahmi.notes/databases/";
	private static String DB_NAME = "AlfahmiDataBase";
	private FloatingActionButton fab;
	ArrayList<String> subject_list;
    ArrayList<String> ID_ArrayList = new ArrayList<String>();
    ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    ArrayList<String> PHONE_ArrayList = new ArrayList<String>();
    ArrayList<String> PRICE_ArrayList = new ArrayList<String>();
	ArrayList<String> DATETRX_ArrayList = new ArrayList<String>();
    ListView LISTVIEW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfahmi_penjualan);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_pencil_box_outline);
		
		LISTVIEW = (ListView) findViewById(R.id.alfahmi__list_penjualan);
        SQLITEHELPER = new SQLiteHelper(this);
		

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showDialogAdd();
				}
			});
			
	/**	
			
		LISTVIEW.setLongClickable(true);
		LISTVIEW.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {
				
				AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Delete");
                ad.setMessage("Sure you want to delete record ?");
                ad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							//SQLITEDATABASE.delete(SQLITEHELPER.TABLE_NAME, "id=? =" +  , null);
							//SQLiteDatabase db = this.getWritableDatabase();
							
							
						//	SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
							SQLITEDATABASE.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+position+"='"+position+"'");
						
							RestartActivity();
							
						}
					});
                ad.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Toast toast=Toast.makeText(getApplicationContext(),String.valueOf(ID_ArrayList),Toast.LENGTH_SHORT); 
							toast.show();
						}
					});
                ad.show();
		
                return false;
           
		
		}
		});
	*/
	}

	
    @Override
    protected void onResume() {

    	ShowSQLiteDBdata() ;
        super.onResume();
    }

    private void ShowSQLiteDBdata() {
    	
    	SQLITEDATABASE = SQLITEHELPER.getWritableDatabase();
    	
        cursor = SQLITEDATABASE.rawQuery("SELECT * FROM transactionTable", null);

        ID_ArrayList.clear();
		NAME_ArrayList.clear();
        PHONE_ArrayList.clear();
        PRICE_ArrayList.clear();
		DATETRX_ArrayList.clear();
        
        if (cursor.moveToFirst()) {
            do {
            	ID_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID)));
         
				NAME_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_NAME)));

            	PHONE_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PHONE)));
            	
            	PRICE_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PRICE)));

				DATETRX_ArrayList.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATETRX)));
				
            } while (cursor.moveToNext());
        }
        
        ListAdapter = new SQLiteListAdapter(MainActivity.this,
        		
        		ID_ArrayList,
				NAME_ArrayList,
        		PHONE_ArrayList,
        		PRICE_ArrayList,
				DATETRX_ArrayList
        		
        		);
        
        LISTVIEW.setAdapter(ListAdapter);
        cursor.close();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alfahmi__menu_penjualan_hapus, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			case R.id.alfahmi__action_delete:
				AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
				LayoutInflater inflater = this.getLayoutInflater();
				final View dialogView = inflater.inflate(R.layout.alfahmi__delete_dialog, null);
				ad.setView(dialogView);
				final EditText edtDelete = (EditText)dialogView.findViewById(R.id.alfahmi__edt_delete_input_number);
				
                ad.setTitle("Hapus");
                ad.setMessage("Silahkan masukan angka pada list yang ingin anda hapus, atau masukan angka 096 untuk menghapus semuanya. ");
				
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							String angka = edtDelete.getText().toString();
							if (angka.equals("096")) {
								SQLiteDatabase.deleteDatabase(new File(DB_PATH + DB_NAME));
							} else {
								SQLITEDATABASE.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+KEY_ID+"='"+angka+"'");
							}
							
							RestartActivity();
						}
					});
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
                ad.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		return false;
	}
	
	
	
	// Dialog Dimatikan
	
	
	public void showDialogAdd() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.alfahmi__add_dialog, null);
		dialogBuilder.setView(dialogView);
		final LinearLayout lytTest = (LinearLayout) dialogView.findViewById(R.id.alfahmi_layout);
		final EditText edtName = (EditText) dialogView.findViewById(R.id.alfahmi__edt_dialog_name);
		final EditText edtNominal = (EditText) dialogView.findViewById(R.id.alfahmi__edt_dialog_price);
		final Spinner spnrProduct = (Spinner)dialogView.findViewById(R.id.alfahmi__spnr_dialog_product);
		final Spinner spnrPackage = (Spinner)dialogView.findViewById(R.id.alfahmi__spnr_dialog_package);
		final Spinner spnrRegular = (Spinner)dialogView.findViewById(R.id.alfahmi__spnr_dialog_regular);
		final EditText edtQuantity = (EditText) dialogView.findViewById(R.id.alfahmi__edt_qty);
		final EditText edtNomor = (EditText) dialogView.findViewById(R.id.alfahmi__edt_dialog_phone);
		lytTest.setVisibility(View.GONE);
		spnrProduct.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> testalf, View view, int position, long id)
			{
	
				String SelectedItem = testalf.getItemAtPosition(position).toString();
				if (SelectedItem.equals("Pulsa")) {
					lytTest.setVisibility(View.GONE);
					edtNomor.setVisibility(View.VISIBLE);
				} else if (SelectedItem.equals("Paket")) {
					edtNomor.setVisibility(View.GONE);
					spnrRegular.setVisibility(View.GONE);
					spnrPackage.setVisibility(View.VISIBLE);
					lytTest.setVisibility(View.VISIBLE);
				} else if (SelectedItem.equals("Regular")) {
					edtNomor.setVisibility(View.GONE);
					spnrPackage.setVisibility(View.GONE);
					spnrRegular.setVisibility(View.VISIBLE);
					lytTest.setVisibility(View.VISIBLE);
				}
			}
			public void onNothingSelected(AdapterView<?> testalf) {
				
			}
		});
		
	
		dialogBuilder.setTitle("Tambah Catatan");
		dialogBuilder.setPositiveButton("Tambahkan", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int wichButton) {
				
				String SelectedItem = spnrProduct.getSelectedItem().toString();
				if (SelectedItem.equals("Pulsa")) {
					test = "Pulsa : " + edtNomor.getText().toString();
				} else if (SelectedItem.equals("Paket")) {
					String paket = spnrPackage.getSelectedItem().toString();
					test = edtQuantity.getText().toString()+"pcs "+paket;
				} else if (SelectedItem.equals("Regular")) {
					String regular = spnrRegular.getSelectedItem().toString();
					test = edtQuantity.getText().toString()+"pcs "+regular;
				}
				// Ngambil Data
				String dateTrx = DateFormat.getDateInstance().format(new Date());
				String strName = edtName.getText().toString();
				String strNominal = edtNominal.getText().toString();
			
				SQLiteQuery = "INSERT INTO transactionTable (NAME,PHONE,PRICE,DATETRX) VALUES('"+strName+"', '"+test+"', '"+strNominal+"', '"+dateTrx+"');";
				SQLITEDATABASE.execSQL(SQLiteQuery);
				cursor.requery();
				ListAdapter.notifyDataSetChanged();
				LISTVIEW.invalidateViews();
				LISTVIEW.refreshDrawableState();
				RestartActivity();
			}
		});
		
		dialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		
		AlertDialog b = dialogBuilder.create();
		b.show();
	}
	
	public void RestartActivity() {
		ListAdapter.notifyDataSetChanged();
		ListAdapter.notifyDataSetInvalidated();
		cursor.requery();
		Intent intent = getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
}
