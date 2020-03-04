package com.example.leslie.monnyfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leslie.monnyfree.common.MonnyBaseFragmentActivity;
import com.example.leslie.monnyfree.core.RequestCodes;

public class MainActivity extends MonnyBaseFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean mIsDualPanel = false;
    DrawerLayout mDrawer;
    Toolbar mToolbar;
    ActionBarDrawerToggle toggle;
    static Account account = new Account();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initDrawer();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeToolbar();
        initHomeFragment();
        Account.setGlobalAccountId();
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
        // automatically handle clicks on the Home/Up button_default_red, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                HomeFragment myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
                if (myFragment == null && !myFragment.isVisible()) {
                    showFragment(HomeFragment.class);
                }

                break;
            case R.id.nav_recurring:
                    Intent intent = new Intent(this, RecurringTransactionsActivity.class);
                    startActivityForResult(intent, RequestCodes.RECURRING);
                break;
            case R.id.nav_category:
                    startActivity(new Intent(this, CategoryActivity.class));
                    break;
            case R.id.nav_account:
                startActivity(new Intent(this, AccountActivity.class));

                break;
            case R.id.nav_report:

                break;
            case R.id.nav_chart:

                break;
            case R.id.nav_topten:

                break;
            case R.id.nav_trend:
                break;
            case R.id.nav_backup:
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, AppCompatPreferenceActivity.class));
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeToolbar(){
        if (mToolbar != null) {
            mToolbar.setTitle(account.getAccountName());
        }
    }

    private void initDrawer(){
        mDrawer= findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void initHomeFragment() {
        String tag = HomeFragment.class.getSimpleName();

        // See if the fragment is already there.
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (existingFragment != null) return;

        // Create new Home fragment.
        HomeFragment fragment = new HomeFragment();

        int containerId = isDualPanel() ? getNavigationId() : getContentId();
        Log.d(TAG, "initHomeFragment: " + containerId);
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();
//                .commitAllowingStateLoss();
    }

    //region Tablet Dual Pane
    public void showFragment(Class<?> fragmentClass){
        if (fragmentClass == null) return;

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentClass.getName());
        if (fragment == null){
            ClassLoader loader = getClassLoader();
            if (loader != null){
                try{
                    Class<?> classFragment = loader.loadClass(fragmentClass.getName());
                    fragment = (Fragment) classFragment.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (fragment != null){
            showFragment(fragment);
        }
    }
    public void showFragment(Fragment fragment, String tag) {
        try {
            showFragment_Internal(fragment, tag);
        } catch (Exception e) {
            Log.e(TAG, "showing fragment with tag");
        }
    }
    public void showFragment(Fragment fragment){
        showFragment(fragment, fragment.getClass().getName());
    }

    private void showFragment_Internal(Fragment fragment, String tag) {
        // Check if fragment is already added.
        if (fragment.isAdded()) return;

        // In tablet layout, do not try to display the Home Fragment again. Show empty fragment.
        /*if (isDualPanel() && tag.equalsIgnoreCase(HomeFragment.class.getName())) {
            fragment = new Fragment();
            tag = "Empty";
        }*/

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
        // Replace whatever is in the fragment_container view with this fragment.
        if (isDualPanel()) {
            transaction.replace(R.id.fragmentDetail, fragment, tag);
        } else {
            transaction.replace(R.id.fragmentMain, fragment, tag);
        }
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        // use this call to prevent exception in some cases -> commitAllowingStateLoss()
        // The exception is "fragment already added".
//        transaction.commitAllowingStateLoss();
    }


    /**
     * @return the mIsDualPanel
     */
    public boolean isDualPanel() {
        return mIsDualPanel;
    }

    public int getContentId() {
        return isDualPanel()
                ? R.id.fragmentDetail
                : R.id.fragmentMain;
    }

    public int getNavigationId() {
        return isDualPanel()
                ? R.id.fragmentMain
                : R.id.fragmentDetail;
    }

    //endregion
}
