package nz.pbomb.xposed.spoofmydevice.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nz.pbomb.xposed.spoofmydevice.R;

/**
 * A UI for the xposed module that allows the users to toggle preferences and features
 *
 * @author Prashant Bhikhu (PBombNZ)
 */
public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.activity_about_textView_author_1) protected TextView author1;
    @BindView(R.id.activity_about_textView_contributer_text_1) protected TextView contributor1;
    @BindView(R.id.activity_about_textView_contributer_text_2) protected TextView contributor2;
    @BindView(R.id.activity_about_textView_contributer_text_3) protected TextView contributor3;
    @BindView(R.id.activity_about_textView_contributer_text_4) protected TextView contributor4;
    @BindView(R.id.activity_about_textView_contributer_text_5) protected TextView contributor5;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        // Set all TextView's with uri's with a LinkMovementMethod instance to make them clickable.
        author1.setMovementMethod(LinkMovementMethod.getInstance());
        contributor1.setMovementMethod(LinkMovementMethod.getInstance());
        contributor2.setMovementMethod(LinkMovementMethod.getInstance());
        contributor3.setMovementMethod(LinkMovementMethod.getInstance());
        contributor4.setMovementMethod(LinkMovementMethod.getInstance());
        contributor5.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
