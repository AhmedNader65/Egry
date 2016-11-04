package mrerror.egry;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TinyDB tiny;
    TextView allcalView ;
    TextView alldisView;
    TextView alltimeView ;
    TextView allstepsView;
    String allCal ;
    String allDis ;
    String allSteps;
    String allTime;
    startserv startserv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tiny = new TinyDB(this);

        setContentView(R.layout.activity_main);

        allCal = String.valueOf(tiny.getInt("cal2"));
        allDis = String.format("%.3g", (tiny.getFloat("dis")));
        allSteps = String.valueOf(tiny.getInt("steps2"));
        allTime = String.format("%2d", (tiny.getInt("time")));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface type = Typeface.createFromAsset(this.getAssets(),"fonts/aokay.ttf");
        allstepsView = (TextView)findViewById(R.id.Allsteps);
        allstepsView.setTypeface(type);
        allstepsView.setText(allSteps);
        listView = (ListView)findViewById(R.id.lis);
         allcalView = (TextView)findViewById(R.id.cal_num);
         alldisView = (TextView)findViewById(R.id.dis_num);
        alltimeView = (TextView)findViewById(R.id.time_num);
        allcalView.setText(allCal);
        alldisView.setText(allDis);
        alltimeView.setText(allTime);
        if(listView!=null){
            listView.setAdapter(new HistoryAdapter(this,R.layout.history_item));
        }
        TextView textView = (TextView)findViewById(R.id.tt);
        listView.setEmptyView(textView);
    }
    private String getDate(){
        String s;
        Format formatter;
        Date date = new Date();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        s = formatter.format(date);
        return s;
    }
    ListView listView;
    @Override
    protected void onResume() {
        super.onResume();


        allCal = String.valueOf(tiny.getInt("cal2"));
        allDis = String.format("%.3f", (tiny.getFloat("dis")));
        allTime = String.format("%2d",(tiny.getInt("time")));
        allcalView.setText(allCal);
        alldisView.setText(allDis);
        alltimeView.setText(allTime);
        allSteps = String.valueOf(tiny.getInt("steps2"));
        allstepsView.setText(allSteps);
            ////////////// add history
        String date = getDate();
        if(!date.equals(tiny.getString("date"))){
            if(tiny.getString("date").length()<1){

                tiny.putString("date", getDate());
                tiny.putListString("DateList", new ArrayList<String>());
                tiny.putListString("TimeList",new ArrayList<String>());
                tiny.putListString("DisList",new ArrayList<String>());
            }else{
                ArrayList<String> datemod = tiny.getListString("DateList");
                datemod.add(tiny.getString("date"));
                tiny.putListString("DateList",datemod);
                ArrayList<String> timemod = tiny.getListString("TimeList");
                timemod.add(allTime);
                tiny.putListString("TimeList", timemod);
                ArrayList<String> dismod = tiny.getListString("DisList");
                dismod.add(allDis);
                tiny.putListString("DisList", dismod);
                tiny.putBoolean("finished", true);

                ////////////////////////////////////////////
                tiny.putInt("cal2", 0);
                allCal = String.valueOf(tiny.getInt("cal2"));
                tiny.putFloat("dis", 0.00f);
                allDis = String.format("%.3g", (tiny.getFloat("dis")));
                tiny.putInt("time",0);
                 allTime = String.format("%2d", (tiny.getInt("time")));
                allcalView.setText(allCal);
                alldisView.setText(allDis);
                alltimeView.setText(allTime);
                tiny.putInt("steps2", 0);
                allSteps = String.valueOf(tiny.getInt("steps2"));
                allstepsView.setText(allSteps);

                tiny.putString("date", getDate());

            }
            if(listView!=null){

                Log.e("oooh","fuck");
                listView.setAdapter(new HistoryAdapter(this,R.layout.history_item));
            }
            TextView textView = (TextView)findViewById(R.id.tt);
            listView.setEmptyView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start(View view) {
        Intent intent = new Intent(this,FullscreenActivity.class);
        startActivity(intent);
    }
}
