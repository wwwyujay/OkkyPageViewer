package com.wwwyujay.sample.pageviewer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav;
    private Toolbar toolbar;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        replaceBoardFragment(R.id.board_community_all);   // Fragment shown by default
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Replace the fragment by the menu item id
        replaceBoardFragment(menuItem.getItemId());
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize layout: binding view references, setting up views, adapting listeners, etc.
     */
    private void initLayout() {
        /* Binding view references */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        nav = (NavigationView)findViewById(R.id.navigation_drawer);

        /* Initialize toolbar */
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        /* Initialize navigation drawer toggle */
        toggle = new ActionBarDrawerToggle(
                this,   /* Activity */
                drawerLayout,    /* DrawerLayout */
                toolbar,    /* Toolbar */
                R.string.open_drawer,   /* Drawer open description */
                R.string.close_drawer   /* Drawer close description */
        ){
            @Override
            public void onDrawerOpened(View drawerView) {   // Show back button when drawer opened
                showBackButton(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {   // Hide back button when drawer closed
                showBackButton(false);
            }
        };

        /* Adapting listeners and etc. */
        drawerLayout.addDrawerListener(toggle);
        nav.setNavigationItemSelectedListener(this);
        menu = nav.getMenu();
    }

    /**
     * Replace current fragment (placeholder) with another fragment.
     *
     * @param itemId Id of the selected menu item.
     */
    private void replaceBoardFragment(int itemId) {
        /* Initialize fragment */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BoardFragment boardFragment = new BoardFragment();

        /* Attach data bundle to fragment */
        if (menu != null) {
            Bundle data = new Bundle();
            data.putString("boardName", menu.findItem(itemId).getTitle().toString());   // Title of the menu item
            data.putInt("itemId", itemId);  // Id of the menu item
            boardFragment.setArguments(data);
        }

        /* Execute replacement */
        transaction.replace(R.id.fragment_placeholder, boardFragment);
        transaction.commit();

        /* Close navigation drawer after replacement */
        drawerLayout.closeDrawer(GravityCompat.START);
        nav.setCheckedItem(itemId);
    }

    /**
     * Switch navigation button to back button and reverse.
     * Copied from: https://stackoverflow.com/questions/36579799
     *
     * @param show Whether you want to show or hide the back button.
     */
    public void showBackButton(boolean show) {

        if (show) {
            toggle.setDrawerIndicatorEnabled(false);    // 1. Remove hamburger button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 2. Show back button

            /* Add click listener to back button */
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 1. Remove back button
            toggle.setDrawerIndicatorEnabled(true); // 2. Show hamburger button

            /* Clear existing click listener */
            toggle.setToolbarNavigationClickListener(null);
        }
    }
}
