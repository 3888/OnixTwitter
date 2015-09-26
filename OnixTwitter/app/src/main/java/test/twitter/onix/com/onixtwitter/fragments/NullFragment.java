package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NullFragment extends Fragment {

    public NullFragment() {
    }

    public static NullFragment newInstance() {
        return new NullFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return null;
    }
}