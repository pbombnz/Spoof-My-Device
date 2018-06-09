package nz.pbomb.xposed.spoofmydevice.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;

public class ContactActivity extends AppCompatActivity {

    //@BindView(R.id.contact_twitter_button) Button twitterButton;
    //@BindView(R.id.contact_email_button) Button emailButton;
    //@BindView(R.id.contact_xda_button) Button xdaThreadButton;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.contact_twitter_button, R.id.contact_email_button})
    public void onButtonClick(Button button) {
        Intent intent;
        Log.d(Common.TAG, String.valueOf(button.getId()));
        switch(button.getId()) {
            case R.id.contact_twitter_button:
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=528317895"));
                    startActivity(intent);
                    return;
                }catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PBombNZ"));
                }
                break;
            case R.id.contact_email_button:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:pbomb.nz@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "[Xposed][SpoofMyDevice] Application Feedback");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Build Fingerprint: "+ Build.FINGERPRINT + "\n" +
                        "Build Manufacturer: "+ Build.MANUFACTURER + "\n" +
                        "Build Brand: "+ Build.BRAND + "\n" +
                        "Build Model: "+ Build.MODEL + "\n" +
                        "Build Product: "+ Build.PRODUCT + "\n" +
                        "\n" +
                        "Android OS Information: " + Build.VERSION.RELEASE + "\n" +
                        "\n" +
                        "Additional Text (Insert Feedback/Bug Report/Feature Request Here):\n");

                intent = Intent.createChooser(intent, "Chooser Email Client");
                break;
            /*case R.id.contact_xda_button:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/xposed/modules/xposed-anz-gomoney-zealand-mods-bypass-t3270623"));
                break;
            */
            default:
                return;
        }
        startActivity(intent);
    }
}
