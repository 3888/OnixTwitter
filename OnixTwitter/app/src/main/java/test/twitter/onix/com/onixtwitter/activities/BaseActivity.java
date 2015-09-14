package test.twitter.onix.com.onixtwitter.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.Twitter;

import test.twitter.onix.com.onixtwitter.PreferencesHelper;
import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.callbacks.TweetComposerCallback;
import test.twitter.onix.com.onixtwitter.fragments.HomeTimelineFragment;
import test.twitter.onix.com.onixtwitter.fragments.ProfileFragment;
import test.twitter.onix.com.onixtwitter.fragments.SettingsFragment;
import test.twitter.onix.com.onixtwitter.fragments.TweetComposerFragment;

public class BaseActivity extends AppCompatActivity implements TweetComposerCallback {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private PreferencesHelper mSPHelper;
    private TextView mToolbarTopName;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mRightDrawer;

    private ImageView mDrawerLogo;
    private ImageView mViewPagerIcon;
    private MapFragment mMapFragment;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle != null) {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mSPHelper = new PreferencesHelper(getApplicationContext());
        mRightDrawer = (RelativeLayout) findViewById(R.id.base_right_drawer);

        Log.d(TAG, "isL = " + mSPHelper.getBoolean("IS_LOGGED", false));
        showUpdatedTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_pager, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(int requestCode, int resultCode, Intent data)");
        Log.d(TAG, "requestCode: " + requestCode);
        Log.d(TAG, "resultCode: " + resultCode);

        // Pass the activity result to the fragment,
        // which will then pass the result to the login button.
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            Log.d(TAG, "Passing result back to the sign in fragment");
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void displayTweetComposer() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                TweetComposerFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void showUpdatedTimeline() {
        Log.d(TAG, "Show updated timeline");
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                getHomeTimelineFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.fragment_container);
    }

    HomeTimelineFragment getHomeTimelineFragment() {
        setToolbarTop();
        setPanelBottom();
        return HomeTimelineFragment.newInstance();
    }

    private void setToolbarTop() {
        ListView drawerList = (ListView) findViewById(R.id.list_view_drawer);
        String[] drawerMenuItems = getResources().getStringArray(R.array.drawer_array);
        mViewPagerIcon = (ImageView) findViewById(R.id.toolbar_left_icon);
        showViewPagerIcon();
        mDrawerLogo = (ImageView) findViewById(R.id.drawer_list_logo);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, drawerMenuItems));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                        startActivity(browserIntent);
                        break;
                    case 1:
                        mSPHelper.setBoolean("IS_LOGGED", false);

                        CookieSyncManager.createInstance(getApplicationContext());
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.removeSessionCookie();
                        Twitter.getSessionManager().clearActiveSession();
                        Twitter.logOut();
                        finish();
                        Intent singInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                        singInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(singInIntent);
                        break;
                    case 2:
                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                TweetComposerFragment.newInstance()).addToBackStack(null).commit();
                        break;
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null,
                R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerLogo.setImageResource(0);
            }

            // Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerLogo.setImageResource(R.mipmap.ic_launcher);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mToolbarTopName = (TextView) findViewById(R.id.toolbar_top_tab_name);
        mToolbarTopName.setText(getString(R.string.toolbar_tab_name_home));

        ImageView menuButton = (ImageView) findViewById(R.id.toolbar_menu_icon);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
                    mDrawerLayout.closeDrawer(mRightDrawer);
                } else {
                    mDrawerLayout.openDrawer(mRightDrawer);
                }
            }
        });
    }

    private void setPanelBottom() {
        ImageButton showMap = (ImageButton) findViewById(R.id.toolbar_bottom_map);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideViewPagerIcon();
                mToolbarTopName.setText(getString(R.string.toolbar_tab_name_map));

                mMapFragment = MapFragment.newInstance();
                mMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setZoomControlsEnabled(true);
                        map.getUiSettings().setAllGesturesEnabled(true);
                        map.getUiSettings().setCompassEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                        map.getUiSettings().setIndoorLevelPickerEnabled(true);
                        map.getUiSettings().setMapToolbarEnabled(true);

                        LatLng onix = new LatLng(48.50333, 32.26);
                        map.addMarker(new MarkerOptions().position(onix).title("Onix"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(onix));
                    }
                });
                getFragmentManager().beginTransaction().add(R.id.fragment_container,
                        mMapFragment).commit();
            }
        });

        ImageButton profile = (ImageButton) findViewById(R.id.toolbar_bottom_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideViewPagerIcon();
                mToolbarTopName.setText(getString(R.string.toolbar_tab_name_profile));
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ProfileFragment.newInstance()).commit();
            }
        });

        ImageButton tweetsList = (ImageButton) findViewById(R.id.toolbar_bottom_list);
        tweetsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewPagerIcon();
                mToolbarTopName.setText(getString(R.string.toolbar_tab_name_home));
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        getHomeTimelineFragment()).commit();
            }
        });

        ImageButton settings = (ImageButton) findViewById(R.id.toolbar_bottom_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideViewPagerIcon();
                mToolbarTopName.setText("");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        SettingsFragment.newInstance()).commit();
            }
        });
    }

    private void hideViewPagerIcon() {
        mViewPagerIcon.setImageResource(0);
    }

    private void showViewPagerIcon() {
        mViewPagerIcon.setImageResource(R.drawable.ic_assignment_white_24dp);
    }
}