package test.twitter.onix.com.onixtwitter.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.twitter.onix.com.onixtwitter.R;

public class ZeroFragment extends Fragment {

    private static final String TAG = ZeroFragment.class.getSimpleName();

    public static ZeroFragment newInstance() {
        return new ZeroFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zero, container, false);

        return view;
    }
}