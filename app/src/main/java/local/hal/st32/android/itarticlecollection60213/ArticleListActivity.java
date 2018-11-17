package local.hal.st32.android.itarticlecollection60213;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ArticleListActivity extends AppCompatActivity {

    private static final String ACCESS_URL = "http://hal.architshin.com/st32/getItArticlesList.php";
    private static ArticleListActivity instance = null;
    private ListView _listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Log.d("検証","CR");
        instance = this;

        _listView = findViewById(R.id.listView);

        GetListArticles getListArticles = new GetListArticles(_listView);
        getListArticles.execute(ACCESS_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("検証","RS");

        GetListArticles getListArticles = new GetListArticles(_listView);
        getListArticles.execute(ACCESS_URL);
    }

    public void onNewButtonClick(View view) {
        Intent intent = new Intent(ArticleListActivity.this, ArticleAddActivity.class);
        startActivity(intent);
    }

    public static ArticleListActivity getInstance() {
        return instance;
    }
}
