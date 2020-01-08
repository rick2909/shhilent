package com.example.shhilent;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import io.opencensus.resource.Resource;

public class Aantal_Meldingen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView aantalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aantal__meldingen);

        aantalTextView = findViewById(R.id.aantal);

        menu();
        showAmount();
        graph();
    }

    private void showAmount(){
        DbHandler db = new DbHandler(this);
        int amount = db.getAmount();
        aantalTextView.setText("Aantal meldingen vandaag: " + amount);
    }

    private void graph(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        int graphlineColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);
        int graphBgColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDarkTransparent, null);

        series.setColor(graphlineColor);
        series.setDrawBackground(true);
        series.setBackgroundColor(graphBgColor);

        graph.addSeries(series);
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Aantal_Meldingen.this, MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(Aantal_Meldingen.this, MainActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.meldingenList) {
            startActivity(new Intent(Aantal_Meldingen.this, meldingen.class));
            Log.d("MENU", "Back to meldingen ");
        } else if (id == R.id.meldingenAantal) {
            // niks
        } else if(id == R.id.logOut){
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void menu(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aantal meldingen");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getDisplayName().length() >  0) {
            navUsername.setText(user.getDisplayName());
        }
    }
}
