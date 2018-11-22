package local.hal.st32.android.itarticlecollection60213;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class PostMyArticle extends AsyncTask<PostItem, Void, String> {

    private static final String DEBUG_TAG = "PostMyArticle";

    // 通信の状況を管理するフラグ
    private boolean _success = false;

    // 引数の構造体(PostItem)から必要な情報を取得しサーバーへ送信
    @Override
    public String doInBackground(PostItem... params) {
        String postData = "title=" + params[0].title
                + "&url=" + params[0].articleUrl
                + "&comment=" + params[0].comment
                + "&lastname=" + params[0].lastName
                + "&firstname=" + params[0].firstName
                + "&studentid=" + params[0].studentId
                + "&seatno=" + params[0].seatNo;

        HttpURLConnection con = null;
        InputStream is = null;
        String result = "";

        try {
            java.net.URL url = new URL(params[0].postUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes());
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

    // 正常にサーバーへ送信できたらfinish()してArticleListActivityへ遷移
    @Override
    public void onPostExecute(String result) {
        getToast();
        if (_success) {
            ArticleAddActivity.getInstance().finish();
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

    // 通信状況に合ったToastを表示
    private void getToast() {
        if (_success){
            Toast.makeText(ArticleAddActivity.getInstance().getApplicationContext(), "データの送信に成功しました", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ArticleAddActivity.getInstance().getApplicationContext(), "データの送信に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }
}
