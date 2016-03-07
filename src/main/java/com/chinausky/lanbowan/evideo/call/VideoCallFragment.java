package com.chinausky.lanbowan.evideo.call;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.chinausky.lanbowan.R;
import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.core.EvideoVoipCore;
import com.evideo.voip.mediastream.video.AndroidVideoWindowImpl;

/**
 * @author Evideo Voip Team
 */
public class VideoCallFragment extends Fragment {
	private SurfaceView mVideoView;
	private SurfaceView mCaptureView;
	private AndroidVideoWindowImpl androidVideoWindowImpl;
	private InCallActivity inCallActivity;
	
	@SuppressWarnings("deprecation") 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {		
        View view = inflater.inflate(R.layout.call_video, container, false);
        
		mVideoView = (SurfaceView) view.findViewById(R.id.videoSurface);
		mCaptureView = (SurfaceView) view.findViewById(R.id.videoCaptureSurface);
		mCaptureView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 

		fixZOrder(mVideoView, mCaptureView);
		
		androidVideoWindowImpl = new AndroidVideoWindowImpl(mVideoView, mCaptureView, new AndroidVideoWindowImpl.VideoWindowListener() {
			public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
				EvideoVoipFunctions.getEVCore().setVideoWindow(vw);
				mVideoView = surface;
			}

			public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl vw) {
				EvideoVoipCore evCore = EvideoVoipFunctions.getEVCore(); 
				if (evCore != null) {
					evCore.setVideoWindow(null);
				}
			}

			public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl vw, SurfaceView surface) {
				mCaptureView = surface;
				EvideoVoipFunctions.getEVCore().setPreviewWindow(mCaptureView);
			}

			public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl vw) {
				// Remove references kept in jni code and restart camera
				EvideoVoipFunctions.getEVCore().setPreviewWindow(null);
			}
		});
		
		return view;
    }

	private void fixZOrder(SurfaceView video, SurfaceView preview) {
		video.setZOrderOnTop(false);
		preview.setZOrderOnTop(true);
		preview.setZOrderMediaOverlay(true); // Needed to be able to display control layout over
	}
	
	@Override
	public void onResume() {		
		super.onResume();
		
		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onResume();
		}
		
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				EvideoVoipFunctions.getEVCore().setVideoWindow(androidVideoWindowImpl);
			}
		}
	}

	@Override
	public void onPause() {	
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				EvideoVoipFunctions.getEVCore().setVideoWindow(null);
			}
		}
		
		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onPause();
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		inCallActivity = null;
		
		mCaptureView = null;
		if (mVideoView != null) {
			mVideoView.setOnTouchListener(null);
			mVideoView = null;
		}
		if (androidVideoWindowImpl != null) { 
			// Prevent linphone from crashing if correspondent hang up while you are rotating
			androidVideoWindowImpl.release();
			androidVideoWindowImpl = null;
		}
		
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		inCallActivity = (InCallActivity) activity;
		if (inCallActivity != null) {
			inCallActivity.bindVideoFragment(this);
		}
	}
	
}
