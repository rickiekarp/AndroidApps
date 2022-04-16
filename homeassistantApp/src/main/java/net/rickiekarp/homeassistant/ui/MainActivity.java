package net.rickiekarp.homeassistant.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.rickiekarp.homeassistant.R;
import net.rickiekarp.homeassistant.db.AppDatabase;
import net.rickiekarp.homeassistant.domain.ShoppingStoreList;
import net.rickiekarp.homeassistant.interfaces.IOnDialogClick;
import net.rickiekarp.homeassistant.interfaces.IOnGetStoreListResult;
import net.rickiekarp.homeassistant.model.MenuItemInformation;
import net.rickiekarp.homeassistant.net.communication.controller.GetStoreListController;
import net.rickiekarp.homeassistant.net.communication.vo.VONote;
import net.rickiekarp.homeassistant.preferences.Constants;
import net.rickiekarp.homeassistant.ui.dialog.LogoutDialog;
import net.rickiekarp.homeassistant.ui.fragments.InfoFragment;
import net.rickiekarp.homeassistant.ui.fragments.NotesFragment;
import net.rickiekarp.homeassistant.ui.fragments.NotesHistoryFragment;
import net.rickiekarp.homeassistant.utils.Util;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IOnDialogClick {

    private SharedPreferences sp;
    private AppDatabase database;
    private static String TAG;

    private final int MENU_NOTES = 1;
    private final int MENU_NOTES_HISTORY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(Constants.Preferences.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        database = AppDatabase.getDatabase(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationDrawer(navigationView);

        if (database.getStoreList() == null) {
            GetStoreListController call = new GetStoreListController(sp, new IOnGetStoreListResult() {
                @Override
                public void onGetAllNotesSuccess(ShoppingStoreList storeList) {
                    database.setStoreList(storeList);
                }

                @Override
                public void onGetAllNotesError() {
                    Log.e("tag", "error");
                }
            });
            call.start();
        }

        showFragment(new NotesFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case MENU_NOTES:
                fragment = new NotesFragment();
                TAG = "notes";
                break;
            case MENU_NOTES_HISTORY:
                fragment = new NotesHistoryFragment();
                TAG = "notes_history";
                break;
            case R.id.nav_info:
                fragment = new InfoFragment();
                TAG = "info";
                break;
            case R.id.nav_logout:
                fragment = null;
                doLogout();
                break;
            default:
                Log.w("warn", "Invalid fragment!");
        }

        if (fragment != null) {
            showFragment(fragment);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private MenuItemInformation getMenuItem(String key) {
        MenuItemInformation newItem = new MenuItemInformation();
        switch (key) {
            case "featureNotes":
                newItem.title = R.string.action_notes;
                newItem.itemId = MENU_NOTES;
                newItem.drawableId = R.drawable.ic_assignment_black_24dp;
                break;
            case "featureNotesHistory":
                newItem.title = R.string.action_notes_history;
                newItem.itemId = MENU_NOTES_HISTORY;
                newItem.drawableId = R.drawable.ic_assignment_black_24dp;
                break;
            default:
                Log.w("error", "No fitting key found!");
        }
        return newItem;
    }

    /**
     * Updates all information in the navigation drawer,
     * like username and menu items of enabled features
     *
     * @param navigationView Navigation view to update
     */
    private void updateNavigationDrawer(NavigationView navigationView) {

        // set user name
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.headerSubTitle);
        navUsername.setText(sp.getString(Constants.Preferences.PREF_USERNAME, null));

        Properties properties = database.getAppData().getFeatures();

        // create treemap of feature properties to sort the received properties
        // TODO: use an array (json) instead of Properties
        Map<String, String> sortedMap = new TreeMap(properties);
        for (String key : sortedMap.keySet()) {
            if (Boolean.parseBoolean(properties.getProperty(key))) {
                final MenuItemInformation navItem = getMenuItem(key);
                addNavigationDrawerItem(
                        navigationView.getMenu().findItem(R.id.submenu_1), navItem
                );
            }
        }

//        // set menu items of enabled features
//        Enumeration e = properties.propertyNames();
//        while (e.hasMoreElements()) {
//            String key = (String) e.nextElement();
//            if (Boolean.parseBoolean(properties.getProperty(key))) {
//                final MenuItemInformation navItem = getMenuItem(key);
//                addNavigationDrawerItem(
//                        navigationView.getMenu().findItem(R.id.submenu_1), navItem
//                );
//            }
//        }
    }

    private void addNavigationDrawerItem(final MenuItem menuItem, final MenuItemInformation newItem) {
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.add(0, newItem.itemId, Menu.NONE, newItem.title).setIcon(newItem.drawableId);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main, fragment).addToBackStack(TAG).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack("singleNote", 0);
            } else {
                LogoutDialog logoutDialog = LogoutDialog.newInstance(this);
                logoutDialog.show(getFragmentManager(), "LogoutDialog");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doLogout() {
        Util.setIsLoggedIn(sp, false);
        Util.removeToken(sp);
        Intent intent = new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onPositiveClick(VONote note, String type) {
        doLogout();

    }

    @Override
    public void onNegativeClick(int id) {

    }
}
