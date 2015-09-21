package test.twitter.onix.com.onixtwitter.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.presenters.HomeTimelinePresenter;
import test.twitter.onix.com.onixtwitter.presenters.HomeTimelinePresenterImpl;
import test.twitter.onix.com.onixtwitter.views.TimelineView;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

public class HomeTimelineFragment extends ListFragment implements TimelineView {

    private static final String TAG = HomeTimelineFragment.class.getSimpleName();

    private HomeTimelinePresenter mHomeTimelinePresenter;

    public static HomeTimelineFragment newInstance() {
        Log.d(TAG, "HomeTimelineFragment newInstance()");
        return new HomeTimelineFragment();
    }

    public HomeTimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

        mHomeTimelinePresenter = HomeTimelinePresenterImpl.newInstance(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        return view;
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        createHomeTimeline();
    }

    @Override
    public void displayUserTweetList(ListAdapter adapter) {
        Log.d(TAG, "displayUserTweetList(ListAdapter adapter)");
        setListAdapter(adapter);
    }

    @Override
    public void updateUserTweetList(ListAdapter adapter) {
        Log.d(TAG, "updateUserTweetList(ListAdapter adapter)");
        setListAdapter(adapter);

        TweetTimelineListAdapter tweetTimelineListAdapter = (TweetTimelineListAdapter) getListAdapter();
        tweetTimelineListAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayError(String error) {
        Log.d(TAG, "Display Error: " + error);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    void createHomeTimeline() {
        Log.d(TAG, "createHomeTimeline()");
        mHomeTimelinePresenter.createTimeline(this.getActivity());
    }
}