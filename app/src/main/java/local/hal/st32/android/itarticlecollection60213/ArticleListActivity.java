package local.hal.st32.android.itarticlecollection60213;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ArticleListActivity extends AppCompatActivity {

    private static final String ACCESS_URL = "http://hal.architshin.com/st32/getItArticlesList.php";
    private static ArticleListActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Log.d("検証","検証1");
        instance = this;

        ListView lv = findViewById(R.id.listView);

        GetListArticles getListArticles = new GetListArticles(lv);
        getListArticles.execute(ACCESS_URL);
    }

    public static ArticleListActivity getInstance() {
        return instance;
    }
}
