package com.chinausky.lanbowan.evideo;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.chinausky.lanbowan.R;

/**
 * @author Evideo Voip Team
 */
public class AddressText extends EditText {

    private String displayedName;
    private Uri pictureUri;

    public AddressText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPictureUri(Uri uri) {
        pictureUri = uri;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void clearDisplayedName() {
        displayedName = null;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setContactAddress(String uri, String displayedName) {
        setText(uri);
        this.displayedName = displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before,
                                 int after) {
        clearDisplayedName();
        pictureUri = null;

        if (!isSipAddress(text.toString()) && text.length() > 50) {
            setText(text.subSequence(0, 50));
            setSelection(50);
            Toast.makeText(getContext(), R.string.home_input_error, Toast.LENGTH_SHORT).show();
        }

        setCursorVisible(getText().toString().length() == 0 ? false : true);

        super.onTextChanged(text, start, before, after);
    }

    private boolean isSipAddress(String address) {
        return address.indexOf("@") != -1;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (width != oldWidth) {
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();

        setMeasuredDimension(parentWidth, height);
    }

}
