package net.coronite.mathview;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClientClass extends WebViewClient {

    ProgressDialog pd = null;
    Context mContext;

    public WebClientClass(Context context){
        mContext = context;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(pd == null) {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(pd.isShowing()) {
            pd.dismiss();
        }
    }

}
