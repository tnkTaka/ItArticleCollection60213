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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 非同期でサーバと通信し、ListVewに表示するJSONを取得 & SETするクラス
 */
public class GetListArticles extends AsyncTask<String, Void, String> {

    private static final String DEBUG_TAG = "GetListArticles";

    private boolean _success = false;

    private ListView _listView;

    public GetListArticles(ListView listView) {
        _listView = listView;
    }

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
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.flush();
            os.close();
            int status = con.getResponseCode();
            if (status != 200) {
                throw new IOException("ステータスコード:" + status);
            }
            is = con.getInputStream();

            result = is2String(is);
            _success = true;
        } catch (SocketTimeoutException ex) {
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
            Log.e(DEBUG_TAG, "タイムアウト", ex);
        } catch (MalformedURLException ex) {
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
            Log.e(DEBUG_TAG, "URL変換失敗", ex);
        } catch (IOException ex) {
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
                Log.e(DEBUG_TAG, "InputStream解析失敗", ex);
            }
        }

        return result;
    }

    @Override
    public void onPostExecute(String data) {
        if (_success) {
            final ArrayList<HashMap<String, String>> list_data = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hashTmp = new HashMap<String, String>();

            try {
                JSONObject rootJSON = new JSONObject(data);
                JSONArray lists = rootJSON.getJSONArray("list");
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject list = lists.getJSONObject(i);
                    hashTmp.put("title", list.getString("title"));
                    hashTmp.put("comment", list.getString("comment"));
                    hashTmp.put("url",list.getString("url"));
                    hashTmp.put("studentId",list.getString("student_id"));
                    hashTmp.put("seatNo",list.getString("seat_no"));
                    hashTmp.put("name", list.getString("last_name")+list.getString("first_name"));
                    hashTmp.put("createdAt",list.getString("created_at"));
                    list_data.add(new HashMap<String, String>(hashTmp));
                }
            } catch (JSONException ex) {
                Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に成功しました", Toast.LENGTH_SHORT).show();

            SimpleAdapter adapter = new SimpleAdapter(ArticleListActivity.getInstance().getApplicationContext(), list_data, R.layout.list_item,
                    new String[]{"name", "title", "comment"}, new int[]{R.id.item_right, R.id.item_main, R.id.item_sub});

            _listView.setAdapter(adapter);
            _listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ArticleListActivity.getInstance().getApplication(), ArticleDetailActivity.class);
                    intent.putExtra("hashMapKey",list_data.get(position));
                    ArticleListActivity.getInstance().startActivity(intent);
                }
            });

        } else {
            Toast.makeText(ArticleListActivity.getInstance().getApplicationContext(), "データの取得に失敗しました", Toast.LENGTH_SHORT).show();
        }
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
}
