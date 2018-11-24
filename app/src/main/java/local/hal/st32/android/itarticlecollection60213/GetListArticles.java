package local.hal.st32.android.itarticlecollection60213;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 非同期でサーバと通信し、ListVewに表示するJSONを取得 & SETするクラス
 */
public class GetListArticles extends AsyncTask<String, Void, String> {

    private static final String DEBUG_TAG = "GetListArticles";

    // 通信とJSON解析の状況を管理するフラグ
    private boolean _success = false;

    private ListView _listView;

    // ListViewに表示するArrayList、中身はハッシュマップ(key(String),value(String))
    private ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();

    // ArticleListActivityのListViewをこのクラスで引き継ぐコンストラクタ
    public GetListArticles(ListView listView) {
        _listView = listView;
    }

    // 引数からアクセス先URLを取得しJSONを取りに行く
    @Override
    public String doInBackground(String... params) {
        String urlStr = params[0];
        HttpURLConnection con = null;
        InputStream is = null;
        String result = "";
        try {
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.connect();

            int status = con.getResponseCode();
            if (status != 200) {
                throw new IOException("ステータスコード:" + status);
            }
            is = con.getInputStream();

            result = is2String(is);
            _success = true;
        } catch (SocketTimeoutException ex) {
            getToast();
            Log.e(DEBUG_TAG, "タイムアウト", ex);
        } catch (MalformedURLException ex) {
            getToast();
            Log.e(DEBUG_TAG, "URL変換失敗", ex);
        } catch (IOException ex) {
            getToast();
            Log.e(DEBUG_TAG, "通信失敗", ex);
        } finally {
            if (con != null) {
                con.disconnect();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                getToast();
                Log.e(DEBUG_TAG, "InputStream解析失敗", ex);
            }
        }
        return result;
    }

    // doInBackgroundからJSONを取得しSimpleAdapterへJSONデータをセット
    @Override
    public void onPostExecute(String jsonData) {
        if (_success) {
            try {
                JSONObject rootJSON = new JSONObject(jsonData);
                // JSON内のListをハッシュマップへ追加し、そのハッシュマップをArrayList(listData)へ追加する
                JSONArray lists = rootJSON.getJSONArray("list");
                for (int i = 0; i < lists.length(); i++) {
                    HashMap<String, String> articles = new HashMap<String, String>();
                    JSONObject list = lists.getJSONObject(i);
                    articles.put("id", list.getString("id"));
                    articles.put("title", list.getString("title"));
                    articles.put("comment", list.getString("comment"));
                    articles.put("url",list.getString("url"));
                    articles.put("studentId",list.getString("student_id"));
                    articles.put("seatNo",list.getString("seat_no"));
                    articles.put("name", list.getString("last_name")+list.getString("first_name"));
                    articles.put("createdAt",remakeDate(list.getString("created_at")));
                    listData.add(articles);
                }
            } catch (JSONException ex) {
                getToast();
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            // ArrayList(listData)の中身(ハッシュマップ化された"createdAt","name","title","comment")をSimpleAdapterへセットする
            SimpleAdapter adapter = new SimpleAdapter(ArticleListActivity.getInstance().getApplicationContext(), listData, R.layout.list_item,
                    new String[]{"createdAt","name", "title", "comment"}, new int[]{R.id.item_right_bottom,R.id.item_right_top, R.id.item_main, R.id.item_sub});

            _listView.setAdapter(adapter);
            _listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // クリックされたListViewの要素と同じindexのハッシュマップデータをArticleDetailActivityへ渡す
                    Intent intent = new Intent(ArticleListActivity.getInstance().getApplication(), ArticleDetailActivity.class);
                    intent.putExtra("Article",listData.get(position));
                    ArticleListActivity.getInstance().startActivity(intent);
                }
            });

        }
        getToast();
        _success = false;
    }

    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }

    // 通信とJSON解析の状況に合ったToastを表示
    private void getToast(){
        if (_success){
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に成功しました", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    // 日付を良い感じにして返す
    private String remakeDate(String _date) {
        String remakeDate = "";
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat strDate = new SimpleDateFormat("yyyy年MM月dd日 HH時");
        try {
            Date d = date.parse(_date);
            remakeDate = strDate.format(d);
        } catch (ParseException ex) {
            Log.e(DEBUG_TAG,"日付変換miss",ex);
        }
        return remakeDate;
    }
}
