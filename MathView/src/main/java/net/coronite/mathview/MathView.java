package net.coronite.mathview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

import org.json.JSONObject;

public class MathView extends WebView {

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
            setData(mTypeArray.getString(R.styleable.MathView_data),
                    mTypeArray.getString(R.styleable.MathView_definition),
                    mTypeArray.getBoolean(R.styleable.MathView_show_term, true));
        } finally {
            mTypeArray.recycle();
        }
    }

    private Chunk getChunk() {
        String TEMPLATE_MATHJAX = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());
        return new Theme(loader).makeChunk(TEMPLATE_MATHJAX);
    }

    public void setData(String data, String definition, Boolean showTerm) {
        data = data.replace("\n", "");
        definition = definition.replace("\n", "");
        //Log.v("STRINGDATA", data);
        //Log.v("STRINGDEFINITION", definition);

        Chunk chunk = getChunk();
        String TAG_DATA = "data";
        String TAG_DEFINITION = "definition";
        String TAG_SHOW_TERM = "show_term";
        try {
            JSONObject jsonData = new JSONObject(data);
            chunk.set(TAG_DATA, jsonData);
        } catch(Exception e){
            chunk.set(TAG_DATA, "\"" + data + "\"");
        }
        try {
            JSONObject jsonDef = new JSONObject(definition);
            chunk.set(TAG_DEFINITION, jsonDef);
        } catch (Exception e) {
            chunk.set(TAG_DEFINITION, "\"" + definition + "\"");
        }
        chunk.set(TAG_SHOW_TERM, showTerm.toString());

        this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
    }


}