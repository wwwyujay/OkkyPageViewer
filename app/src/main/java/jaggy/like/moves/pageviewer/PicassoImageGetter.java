package jaggy.like.moves.pageviewer;
/*
* Copyright 2018 Ha Duy Trung
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
*
* You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* https://www.hidroh.com/2016/02/27/richtext-textview-versus-webview/
* */
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class PicassoImageGetter implements Html.ImageGetter {
    private final TextView mTextView;

    /**
     * Construct an instance of {@link android.text.Html.ImageGetter}
     * @param view  {@link android.widget.TextView} that holds HTML which contains $lt;img&gt; tag to loa
     */
    public PicassoImageGetter(TextView view) {
        mTextView = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        final Uri uri = Uri.parse(source);

        if (uri.isRelative()) {
            return null;
        }

        final CustomDrawable cDrawable = new CustomDrawable(mTextView.getResources(), null);
        new LoadFromUriAsyncTask(mTextView, cDrawable).execute(uri);

        return cDrawable;
    }
}

class LoadFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<TextView> mTextViewRef;
    private final CustomDrawable cDrawable;
    private final Picasso mImageUtils;

    public LoadFromUriAsyncTask(TextView textView, CustomDrawable cDrawable) {
        /*mImageUtils = Picasso.with(textView.getContext());*/
        mImageUtils = Picasso.get();
        mTextViewRef = new WeakReference<>(textView);
        this.cDrawable = cDrawable;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        try {
            return mImageUtils.load(uris[0]).get();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mTextViewRef.get() == null || bitmap == null) return;

        TextView textView = mTextViewRef.get();
        // change the reference of the current mDrawable to the result from the HTTP call
        cDrawable.mDrawable = new BitmapDrawable(textView.getResources(), bitmap);

        // set bound to scale image to fit width and keep aspect ratio
        // according to the result from HTTP call
        int width = textView.getWidth();
        int height = Math.round(1.0f * width *
                cDrawable.mDrawable.getIntrinsicHeight() /
                cDrawable.mDrawable.getIntrinsicWidth());

        cDrawable.setBounds(0, 0, 0 + width, 0 + height);
        cDrawable.mDrawable.setBounds(0, 0, 0 + width, 0 + height);
        // force redrawing bitmap by setting text
        textView.setText(textView.getText());

    }
}

class CustomDrawable extends BitmapDrawable {
    Drawable mDrawable;

    public CustomDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }
}
