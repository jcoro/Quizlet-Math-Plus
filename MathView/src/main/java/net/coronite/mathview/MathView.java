package net.coronite.mathview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class MathView extends WebView {
    private String mConfig;
    private String mData;
    private String mDefinition;


    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);
        WebClientClass webViewClient = new WebClientClass(context);
        setWebViewClient(webViewClient);

        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0
        );

        try {
            setData(mTypeArray.getString(R.styleable.MathView_data), mTypeArray.getString(R.styleable.MathView_definition));
        } finally {
            mTypeArray.recycle();
        }
    }

    private Chunk getChunk() {
        String TEMPLATE_MATHJAX = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());
        return new Theme(loader).makeChunk(TEMPLATE_MATHJAX);
    }

    public void setData(String data, String definition) {
        mData = data;
        mDefinition = definition;
        Chunk chunk = getChunk();
        String TAG_DATA = "data";
        String TAG_CONFIG = "config";
        String TAG_DEFINITION = "definition";
        chunk.set(TAG_DATA, mData);
        chunk.set(TAG_CONFIG, mConfig);
        chunk.set(TAG_DEFINITION, mDefinition);
        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }

    public void config(String config) {
            this.mConfig = config;
    }

}