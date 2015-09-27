package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.SectionsPagerAdapter;
import test.twitter.onix.com.onixtwitter.activities.BaseActivity;

public class VerticalViewPagerFragment extends Fragment {

    private static final String TAG = VerticalViewPagerFragment.class.getSimpleName();

    private ViewPager mViewPager;

    public static VerticalViewPagerFragment newInstance() {
        return new VerticalViewPagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_vertical_view_pager, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(Constants.VIEW_PAGER_DEFAULT_OFF_SCREEN_LIMIT);

        return view;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}