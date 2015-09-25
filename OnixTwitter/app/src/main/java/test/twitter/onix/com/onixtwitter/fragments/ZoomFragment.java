package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.activities.BaseActivity;

public class ZoomFragment extends Fragment {

    private static final String TAG = ZoomFragment.class.getSimpleName();

    public ZoomFragment() {
    }

    public static ZoomFragment newInstance() {
        return new ZoomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        Log.d(TAG, "onCreateView ");


        ImageView zoom = (ImageView) view.findViewById(R.id.zoom_image_view);
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ");
            }
        });
        return view;
    }
}