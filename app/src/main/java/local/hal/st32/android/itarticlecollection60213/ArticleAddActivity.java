package local.hal.st32.android.itarticlecollection60213;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class ArticleAddActivity extends AppCompatActivity {

    // POSTするACCESS先URL
    private static final String POST_ACCESS_URL = "http://hal.architshin.com/st32/insertItArticle.php";

    // このActivityのinstanceを宣言(PostMyArticleクラスにてこのActivityのinstanceを使用する)
    private static ArticleAddActivity instance = null;

    private EditText _etTitle;
    private EditText _etUrl;
    private EditText _etComment;
    private EditText _etLastName;
    private EditText _etFirstName;
    private EditText _etStudentId;
    private EditText _etSeatNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_add);

        // このActivityのinstanceを入れる
        instance = this;

        // 戻るボタン追加
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        _etTitle = findViewById(R.id.etTitle);
        _etUrl = findViewById(R.id.etUrl);
        _etComment = findViewById(R.id.etComment);
        _etLastName = findViewById(R.id.etLastName);
        _etFirstName = findViewById(R.id.etFirstName);
        _etStudentId = findViewById(R.id.etStudentId);
        _etSeatNo = findViewById(R.id.etSeatNo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // アクションバーに登録マークのメニューを追加
        inflater.inflate(R.menu.article_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 戻るボタンが押されたらfinish()する
            case android.R.id.home:
                finish();
                break;
            // 登録ボタンが押されたら構造体(PostItem)へURL情報と入力情報をセットし、
            // PostMyArticleクラスにて非同期でサーバーへ構造体(PostItem)の内容を送信する
            case R.id.addButton:
                PostItem p = new PostItem();
                p.postUrl = POST_ACCESS_URL;
                p.title = _etTitle.getText().toString();
                p.articleUrl = _etUrl.getText().toString();
                p.comment = _etComment.getText().toString();
                p.lastName = _etLastName.getText().toString();
                p.firstName = _etFirstName.getText().toString();
                p.studentId = _etStudentId.getText().toString();
                p.seatNo = _etSeatNo.getText().toString();

                PostMyArticle postMyArticle = new PostMyArticle();
                postMyArticle.execute(p);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArticleAddActivity getInstance() {
        return instance;
    }
}
