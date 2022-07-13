package sg.edu.rp.c346.id21025290.ndp_songs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ToggleButton btnStars;
    Spinner spnYear;
    ListView lv;
    ArrayList <Song> al;
    ArrayAdapter <Song> aa;
    ArrayList <String> alYears;
    ArrayAdapter <String> aaYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        btnStars = findViewById(R.id.toggleButtonStars);
        spnYear = findViewById(R.id.spinnerYear);
        lv = findViewById(R.id.listView);

        al = new ArrayList<Song>();
        aa = new ArrayAdapter<Song>(this,
                android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

        DBHelper dbh = new DBHelper(ListActivity.this);
        al.addAll(dbh.getAllSongs());

        // Year Filter
        alYears = dbh.getAllYears();
        alYears.add(0,"Filter by year");

        aaYears = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alYears);

        spnYear.setAdapter(aaYears);


        btnStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnStars.isChecked()){
                    for (int i = 0; i<al.size(); i++){
                        if(al.get(i).getStar() != 5){
                            al.remove(i);
                        }
                    }
                } else {
                    al.clear();
                    al.addAll(dbh.getAllSongs());
                }
                aa.notifyDataSetChanged();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song data = al.get(i);
                Toast.makeText(ListActivity.this,data.toString(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListActivity.this, EditActivity.class);
                intent.putExtra("songData", data);
                startActivity(intent);

            }
        });
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==0){
                    btnStars.setEnabled(true);
                    for (int j = 0; j < al.size(); j++) {
                        lv.getChildAt(j).setVisibility(View.VISIBLE);
                    }
                } else {
                    btnStars.setEnabled(false);
                    alYears.set(0, "View all songs");
                    for (int j = 0; j < al.size(); j++) {
                        if (al.get(j).getYear() == Integer.parseInt(adapterView.getItemAtPosition(i).toString())) {
                            lv.getChildAt(j).setVisibility(View.VISIBLE);
                        } else{
                            lv.getChildAt(j).setVisibility(View.GONE);
                        }
                    }
                    aa.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        DBHelper dbh = new DBHelper(ListActivity.this);
        al.clear();
        al.addAll(dbh.getAllSongs());
        aa.notifyDataSetChanged();

        alYears = dbh.getAllYears();
        alYears.add(0,"Filter by year");

        aaYears = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, alYears);

        spnYear.setAdapter(aaYears);


    }

}
