package test.twitter.onix.com.onixtwitter.views;

import android.widget.ListAdapter;

public interface TimelineView {

    void displayUserTweetList(ListAdapter adapter);

    void updateUserTweetList(ListAdapter adapter);

    void displayError(String string);
}
