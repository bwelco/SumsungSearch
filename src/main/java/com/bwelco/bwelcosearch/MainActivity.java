package com.bwelco.bwelcosearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity implements TextWatcher, View.OnClickListener {

    ImageView close_image;
    ImageView search_image;
    EditText editText;
    ListView listView;
    RelativeLayout layout;
    ArrayList<String> lists;
    String TAG = MainActivity.class.getSimpleName();
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) this.findViewById(R.id.searchText);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setOnKeyListener(onKey);
        editText.addTextChangedListener(this);
        close_image = (ImageView) this.findViewById(R.id.close_image);
        search_image = (ImageView) this.findViewById(R.id.search_image);

        layout = (RelativeLayout) this.findViewById(R.id.listViewLayout);
        listView = (ListView) this.findViewById(R.id.listview);
        lists = new ArrayList<String>();

        this.adapter = new MyListAdapter(
                MainActivity.this, R.layout.adapter_item, lists, this);
        listView.setAdapter(adapter);

        search_image.setVisibility(View.VISIBLE);
        close_image.setVisibility(View.GONE);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "http://m5.baidu.com/s?from=124n&word="+lists.get(position);
                // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //打开软键盘
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public String getTextString(){
        return this.editText.getText().toString();
    }

    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String s = null;
            try {
                s = new String(responseBody, "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "返回：" + s);

            try {
                JSONArray array = new JSONArray(s);
                String name = array.getString(0);
                JSONArray listsJson = (JSONArray) array.get(1);
                Log.i(TAG, "NAME:" + name);

                lists.clear();
                for (int i = 0; i < listsJson.length(); i++) {
                    lists.add(listsJson.getString(i).toString());
                }

                for (String s2 : lists) {
                    Log.i(TAG, s2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MainActivity.this.adapter.notifyDataSetChanged();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //这里写发送信息的方法
                // Toast.makeText(getApplicationContext(), "回车", Toast.LENGTH_SHORT).show();
                String url = "http://m5.baidu.com/s?from=124n&word="+editText.getText().toString();
                // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

                return true;
            }
            return false;
        }
    };


    // TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText("");
        lists.clear();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (editText.getText().toString().equals("")) {
            search_image.setVisibility(View.VISIBLE);
            close_image.setVisibility(View.GONE);
            search_image.setImageResource(R.drawable.search);

        } else {
            search_image.setVisibility(View.GONE);
            close_image.setVisibility(View.VISIBLE);
            close_image.setImageResource(R.drawable.close);
            close_image.setOnClickListener(this);
            getLists();
        }
    }


    public void getLists() {
        RequestParams params = new RequestParams();
        params.add("wd", MainActivity.this.editText.getText().toString());
        params.add("action", "opensearch");

        MyHttpUtil.get("http://suggestion.baidu.com/su", params, handler);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_image:{
                editText.setText("");
                break;
            }

            default:break;
        }
    }
}
