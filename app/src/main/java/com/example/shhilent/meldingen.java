package com.example.shhilent;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

public class meldingen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView simpleListView;
    SimpleAdapter simpleAdapter;
    Context context = this;

    Activity activity = this;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meldingen);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meldingen");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //end toolbar

        fillList();
    }

    public void fillList(){
        // get all melding from database
        DbHandler db = new DbHandler(this);
        List<HashMap<String,String>> aList = db.getMeldingen();
        for (HashMap<String,String> temp : aList) {
            temp.put("Img", Integer.toString(R.drawable.ic_14121_warning_icon));
        }
        System.out.println("Dit is de Lijst!\n"+aList);

        String[] from = {"Img","Title","Beschrijving","Tijd","Datum"};

        int[] to = {R.id.list_images,R.id.Title,R.id.Description,R.id.tijdText,R.id.dagTextHidden};

        simpleAdapter = new SimpleAdapter(getBaseContext(),aList,R.layout.meldingen_list,from,to);
        simpleListView = (ListView)findViewById(R.id.list_view);
        simpleListView.setAdapter(simpleAdapter);

        // maak onclick aan
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                TextView titelView = v.findViewById(R.id.Title);
                TextView beschrijvingView = v.findViewById(R.id.Description);
                TextView dagView = v.findViewById(R.id.dagTextHidden);
                TextView tijdView = v.findViewById(R.id.tijdText);

                String titel = titelView.getText().toString();
                String beschrijving = beschrijvingView.getText().toString();
                String dag = dagView.getText().toString();
                String tijd = tijdView.getText().toString();

                messageDialog alert = new messageDialog();
                alert.showDialog(activity, titel, beschrijving, dag, tijd);

                // Display a messagebox.
                //Toast.makeText(getApplicationContext(),"Titel: " + titel + " Datum: " + dag,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(meldingen.this, MainActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        context.unregisterReceiver(mMessageReceiver);
    }


    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    private void updateUI() {
        // do what you need to do
        Toast.makeText(getApplicationContext(),"Nieuwe melding",Toast.LENGTH_SHORT).show();
        fillList();

    }

    //menu functions
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(meldingen.this, MainActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.meldingenList) {
           //niks
        } else if (id == R.id.meldingenAantal) {
            startActivity(new Intent(meldingen.this, Aantal_Meldingen.class));
        } else if(id == R.id.logOut){
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
