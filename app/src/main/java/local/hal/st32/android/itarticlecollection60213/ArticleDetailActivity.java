package local.hal.st32.android.itarticlecollection60213;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        HashMap<String, String> hashTmp = (HashMap<String, String>) getIntent().getExtras().get("hashMapKey");

        TextView title = findViewById(R.id.tvTitle);
        title.setText(hashTmp.get("title"));

        TextView url = findViewById(R.id.tvUrl);
        url.setText(hashTmp.get("url"));

        TextView comment = findViewById(R.id.tvComment);
        comment.setText(hashTmp.get("comment"));

        TextView studentId = findViewById(R.id.tvStudentId);
        studentId.setText(hashTmp.get("studentId"));

        TextView seatNo = findViewById(R.id.tvSeatNo);
        seatNo.setText(hashTmp.get("seatNo"));

        TextView name = findViewById(R.id.tvName);
        name.setText(hashTmp.get("name"));

        TextView createDate = findViewById(R.id.tvCreateDate);
        createDate.setText(hashTmp.get("createdAt"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case  android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
