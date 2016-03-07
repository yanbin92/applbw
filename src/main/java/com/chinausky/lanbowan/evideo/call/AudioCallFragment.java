package com.chinausky.lanbowan.evideo.call;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinausky.lanbowan.R;

/**
 * @author Evideo Voip Team
 */
public class AudioCallFragment extends Fragment {	
	private InCallActivity incallActvityInstance;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_audio, container, false);
        return view;
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		incallActvityInstance = (InCallActivity) activity;
		
		if (incallActvityInstance != null) {
			incallActvityInstance.bindAudioFragment(this);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
}
