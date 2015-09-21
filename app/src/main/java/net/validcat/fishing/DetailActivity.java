package net.validcat.fishing;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import net.validcat.fishing.db.DB;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
public class DetailActivity extends AppCompatActivity {
    //	public static final String LOG_TAG = DetailedFishing.class.getSimpleName();
//	@Bind(R.id.tv_place) TextView tvPlace;
//	@Bind(R.id.tv_date) TextView tvDate;
//	@Bind(R.id.tv_weather) TextView tvWeather;
//	@Bind(R.id.tv_description) TextView tvDescription;
//	@Bind(R.id.tv_catch) TextView tvCatch;
//	private DB db;
//
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

//        Intent intent = this.getIntent();
//        long id = intent.getLongExtra("id", -1);
//        DetailFragment df = new DetailFragment();
//        df.updateUiByItemId(id);
    }
}
//		ButterKnife.bind(this);
//
//		Intent intent = getIntent();
//		long id = intent.getLongExtra("id", -1);
//		if (id == -1) {
//			Toast.makeText(this, "Wrong id", Toast.LENGTH_SHORT).show();
//			finish();
//			return;
//		}
//
//		db = new DB(this);
//		db.open();
//		FishingItem item = db.getFishingItemById(id);
//		db.close();
//		// cursor = mDB.rawQuery("SELECT * FROM mytab WHERE _id = 'ID'", null);
//		// cursor = db.getAllData();
//		tvPlace.setText("Place: " + item.getPlace());
//		tvDate.setText("Date: " + item.getDate());
//		tvWeather.setText("Weather: " + item.getWeather());
//		tvDescription.setText("Description: " + item.getDescription());
//		tvCatch.setText("Price: " + item.getPrice());
//	}
//
//	    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getGroupId();
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            startActivity(new Intent(this, SettingsActivity.class));
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}