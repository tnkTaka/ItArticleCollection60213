package local.hal.st32.android.itarticlecollection60213;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ArticleListActivity extends AppCompatActivity {

    // GETするACCESS先URL
    private static final String GET_ACCESS_URL = "http://hal.architshin.com/st32/getItArticlesList.php?limit=30";

    // このActivityのinstanceを宣言(GetListArticlesクラスにてこのActivityのinstanceを使用する)
    private static ArticleListActivity instance = null;

    private ListView _listView;

    // onResume()とonCreate()が同時にGetListArticlesクラスを呼ばないようにするフラグ
    private boolean _resumeFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        // このActivityのinstanceを入れる
        instance = this;
        // onCreate()が呼ばれたらフラグをfalse
        _resumeFlg = false;

        _listView = findViewById(R.id.listView);

        // GetListArticlesクラスにて非同期でURLからJSONを取得しListViewにセット
        GetListArticles getListArticles = new GetListArticles(_listView);
        getListArticles.execute(GET_ACCESS_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_resumeFlg){
            // GetListArticlesクラスにて非同期でURLからJSONを取得しListViewにセット
            GetListArticles getListArticles = new GetListArticles(_listView);
            getListArticles.execute(GET_ACCESS_URL);
        }
        _resumeFlg = true;
    }

    // FABボタン
    public void onNewButtonClick(View view) {
        Intent intent = new Intent(ArticleListActivity.this, ArticleAddActivity.class);
        startActivity(intent);
    }

    // このActivityのinstanceを返す
    public static ArticleListActivity getInstance() {
        return instance;
    }
}
