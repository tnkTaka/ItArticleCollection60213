package local.hal.st32.android.itarticlecollection60213;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ArticleListActivity extends AppCompatActivity {

    private static final String GET_ACCESS_URL = "http://hal.architshin.com/st32/getItArticlesList.php?limit=30";
    private static ArticleListActivity instance = null;
    private ListView _listView;
    private boolean _resumeFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        instance = this;
        _resumeFlg = false;

        _listView = findViewById(R.id.listView);

        GetListArticles getListArticles = new GetListArticles(_listView);
        getListArticles.execute(GET_ACCESS_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_resumeFlg){
            GetListArticles getListArticles = new GetListArticles(_listView);
            getListArticles.execute(GET_ACCESS_URL);
        }
        _resumeFlg = true;
    }

    public void onNewButtonClick(View view) {
        Intent intent = new Intent(ArticleListActivity.this, ArticleAddActivity.class);
        startActivity(intent);
    }

    public static ArticleListActivity getInstance() {
        return instance;
    }
}
