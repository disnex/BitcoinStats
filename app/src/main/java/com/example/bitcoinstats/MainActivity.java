package com.example.bitcoinstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.navigationView);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.marketPrice) {
            startActivity(new Intent(MainActivity.this, MarketPriceActivity.class));
        } else if (id == R.id.txPerDay) {
            startActivity(new Intent(MainActivity.this, TxPerDayActivity.class));
        } else if (id == R.id.totalTxFees) {
            startActivity(new Intent(MainActivity.this, TotalTxFeesActivity.class));
        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}