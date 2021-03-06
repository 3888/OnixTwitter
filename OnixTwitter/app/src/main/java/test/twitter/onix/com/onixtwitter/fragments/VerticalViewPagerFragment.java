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

public class VerticalViewPagerFragment extends Fragment {

    private ViewPager mViewPager;
    private final int VIEW_PAGER_DEFAULT_OFF_SCREEN_LIMIT = 1;

    public static VerticalViewPagerFragment newInstance() {
        return new VerticalViewPagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_vertical_view_pager, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_DEFAULT_OFF_SCREEN_LIMIT);

        return view;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}