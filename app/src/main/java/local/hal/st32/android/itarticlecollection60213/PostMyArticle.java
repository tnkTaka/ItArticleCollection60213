package local.hal.st32.android.itarticlecollection60213;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

public class PostMyArticle extends AsyncTask<PostItem, String, String> {

    private static final String DEBUG_TAG = "PostMyArticle";

    private boolean _success = false;

    private TextView _tvProcess;
    private TextView _tvResult;

    public PostMyArticle(TextView tvProcess, TextView tvResult) {
        _tvProcess = tvProcess;
        _tvResult = tvResult;
    }

    @Override
    public String doInBackground(PostItem... params) {

        Log.d(DEBUG_TAG,""
                +params[0].postUrl+"::"
                +params[0].title+"::"
                +params[0].articleUrl+"::"
                +params[0].comment+"::"
                +params[0].lastName+"::"
                +params[0].firstName+"::"
                +params[0].studentId+"::"
                +params[0].seatNo+"::"
        );

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
            publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_send_before));
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
            publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_send_after));
            is = con.getInputStream();

            result = is2String(is);
            _success = true;
        } catch (SocketTimeoutException ex) {
            publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_err_timeout));
            Log.e(DEBUG_TAG, "タイムアウト", ex);
        } catch (MalformedURLException ex) {
            publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_err_send));
            Log.e(DEBUG_TAG, "URL変換失敗", ex);
        } catch (IOException ex) {
            publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_err_send));
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
                publishProgress(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_err_parse));
                Log.e(DEBUG_TAG, "InputStream解析失敗", ex);
            }
        }

        return result;
    }

    @Override
    public void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String message = _tvProcess.getText().toString();
        if (!message.equals("")) {
            message += "\n";
        }
        message += values[0];
        _tvProcess.setText(message);
    }

    @Override
    public void onPostExecute(String result) {
        if (_success) {
            String title = "";
            String url = "";
            String comment = "";
            String name = "";
            String studentId = "";
            String seatNo = "";
            String status = "";
            String statusMessage = "";
            String timestamp = "";

            onProgressUpdate(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_parse_before));
            try {
                JSONObject rootJSON = new JSONObject(result);
                title = rootJSON.getString("title");
                url = rootJSON.getString("url");
                comment = rootJSON.getString("comment");
                name = rootJSON.getString("name");
                studentId = rootJSON.getString("studentid");
                seatNo = rootJSON.getString("seatno");
                status = rootJSON.getString("status");
                statusMessage = rootJSON.getString("msg");
                timestamp = rootJSON.getString("timestamp");


            } catch (JSONException ex) {
                onProgressUpdate(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_err_parse));
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }
            onProgressUpdate(ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.msg_parse_after));

            String message = ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_title) + title
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_url) + url
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_comment) + comment
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_name) + name
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_student_id) + studentId
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_seat_no) + seatNo
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_status) + status
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_status_message) + statusMessage
                    + "\n" + ArticleAddActivity.getInstance().getApplicationContext().getString(R.string.dig_msg_timestamp) + timestamp;
            _tvResult.setText(message);
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
