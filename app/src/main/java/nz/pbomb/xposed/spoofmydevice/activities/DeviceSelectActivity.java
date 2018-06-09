package nz.pbomb.xposed.spoofmydevice.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;
import nz.pbomb.xposed.spoofmydevice.adapters.DeviceSelectFragment_RecyclerViewAdapter;
import nz.pbomb.xposed.spoofmydevice.adapters.RecyclerViewOnClickListener;
import nz.pbomb.xposed.spoofmydevice.fragments.DeviceSelectDialogFragment;
import nz.pbomb.xposed.spoofmydevice.preferences.SharedPreferencesKeys;
import nz.pbomb.xposed.spoofmydevice.utils.SpoofDevice;
import nz.pbomb.xposed.spoofmydevice.utils.SpoofDevices;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeviceSelectActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    SharedPreferences mSharedPref;

    @BindView(R.id.acitivity_device_select_fab_menu) protected FloatingActionMenu mFAM;
    //@BindView(R.id.acitivity_device_select_fab_clearall) protected FloatingActionButton mFAB_clearAll;
    //@BindView(R.id.acitivity_device_select_fab_templatechooser) protected FloatingActionButton mFAB_templateChooser;

    @BindView(R.id.board_editText)          protected EditText mEditText__build_board;
    @BindView(R.id.id_editText)             protected EditText mEditText__build_id;
    @BindView(R.id.display_editText)        protected EditText mEditText__build_display;
    @BindView(R.id.device_editText)         protected EditText mEditText__build_device;
    @BindView(R.id.product_editText)        protected EditText mEditText__build_product;
    @BindView(R.id.cpuAbi_radioGroup)       protected RadioGroup mRadioGroup__build_cpuAbi;
    @BindView(R.id.cpuAbi2_radioGroup)      protected RadioGroup mRadioGroup__build_cpuAbi2;
    @BindView(R.id.activity_device_select_textview_cpuAbiLabel)  protected TextView mTextView__cpuAbiLabel;
    @BindView(R.id.activity_device_select_textview_cpuAbi2Label) protected TextView mTextView__cpuAbi2Label;
    @BindView(R.id.manufacturer_editText)   protected EditText mEditText__build_manufacturer;
    @BindView(R.id.brand_editText)          protected EditText mEditText__build_brand;
    @BindView(R.id.model_editText)          protected EditText mEditText__build_model;
    @BindView(R.id.bootloader_editText)     protected EditText mEditText__build_bootloader;
    @BindView(R.id.radio_editText)          protected EditText mEditText__build_radio;
    @BindView(R.id.hardware_editText)       protected EditText mEditText__build_hardware;
    @BindView(R.id.serial_editText)         protected EditText mEditText__build_serial;

    //@BindView(R.id.supportedAbis32bit__armeabi_checkBox)    protected CheckBox mCheckBox__build_supportedAbis32bit__armeabi;
    //@BindView(R.id.supportedAbis32bit__armeabi_v7a_checkBox)protected CheckBox mCheckBox__build_supportedAbis32bit__armeabi_v7a;
    //@BindView(R.id.supportedAbis32bit__x86_checkBox)        protected CheckBox mCheckBox__build_supportedAbis32bit__x86;
    //@BindView(R.id.supportedAbis32bit__mips_checkBox)       protected CheckBox mCheckBox__build_supportedAbis32bit__mips;
    //@BindView(R.id.supportedAbis64bit__arm64_v8a_checkBox)  protected CheckBox mCheckBox__build_supportedAbis64bit__arm64_v8a;
    //@BindView(R.id.supportedAbis64bit__x86_64_checkBox)     protected CheckBox mCheckBox__build_supportedAbis64bit__x86_64;
    //@BindView(R.id.supportedAbis64bit__mips64_checkBox)     protected CheckBox mCheckBox__build_supportedAbis64bit__mips64;

    @BindViews({
            R.id.supportedAbis32bit__armeabi_checkBox,
            R.id.supportedAbis32bit__armeabi_v7a_checkBox,
            R.id.supportedAbis32bit__x86_checkBox,
            R.id.supportedAbis32bit__mips_checkBox
    })
    protected List<CheckBox> mCheckBoxes__build_supportedAbis32bit;

    @BindViews({
            R.id.supportedAbis64bit__arm64_v8a_checkBox,
            R.id.supportedAbis64bit__x86_64_checkBox,
            R.id.supportedAbis64bit__mips64_checkBox
    })
    protected List<CheckBox> mCheckBoxes__build_supportedAbis64bit;

    @BindView(R.id.type_editText)           protected EditText mEditText_build_type;
    @BindView(R.id.tags_editText)           protected EditText mEditText_build_tags;
    @BindView(R.id.fingerprint_editText)    protected EditText mEditText_build_fingerprint;

    @BindView(R.id.version_incremental_editText)    protected EditText mEditText__version_incremental;
    @BindView(R.id.version_release_editText)        protected EditText mEditText__version_release;
    @BindView(R.id.version_sdk_editText)            protected EditText mEditText__version_sdk;
    @BindView(R.id.version_sdkint_editText)         protected EditText mEditText__version_sdkint;
    @BindView(R.id.version_codename_editText)       protected EditText mEditText__version_codename;

    @BindViews({
            R.id.board_editText,
            R.id.id_editText,
            R.id.display_editText,
            R.id.device_editText,
            R.id.product_editText,
            R.id.cpuAbi_radioGroup,
            R.id.cpuAbi2_radioGroup,
            R.id.manufacturer_editText,
            R.id.brand_editText,
            R.id.model_editText,
            R.id.bootloader_editText,
            R.id.radio_editText,
            R.id.hardware_editText,
            R.id.serial_editText,
            R.id.type_editText,
            R.id.tags_editText,
            R.id.fingerprint_editText,
            R.id.version_incremental_editText,
            R.id.version_release_editText,
            R.id.version_sdkint_editText,
            R.id.version_sdk_editText,
            R.id.version_codename_editText,
    })
    protected List<View> mConfigViews;

    public DeviceSelectActivity() {
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mFAM.setClosedOnTouchOutside(true);

        mSharedPref = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        displayDeviceSelect(true, null);

        mRadioGroup__build_cpuAbi.setOnCheckedChangeListener(this);
        mRadioGroup__build_cpuAbi2.setOnCheckedChangeListener(this);

        List<CheckBox> allCheckBoxes = new ArrayList<>(mCheckBoxes__build_supportedAbis32bit);
        allCheckBoxes.addAll(mCheckBoxes__build_supportedAbis64bit);
        /*for(CheckBox checkBox : allCheckBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mTextView__supportedAbisLabel.setError(null);
                }
            });
        }*/
    }



    @OnClick({R.id.acitivity_device_select_fab_templatechooser,
            R.id.acitivity_device_select_fab_clearall,
            R.id.acitivity_device_select_fab_default})
    public void onSubFloatingActionButtonClicked(FloatingActionButton fab) {
        mFAM.close(true);
        switch(fab.getId()) {
            case R.id.acitivity_device_select_fab_templatechooser:
                onTemplate(fab);
                break;
            case R.id.acitivity_device_select_fab_clearall:
                clearAllErrors();
                clearAllSpoofDeviceFields();
                break;
            case R.id.acitivity_device_select_fab_default:
                clearAllErrors();
                clearAllSpoofDeviceFields();
                loadSpoofDeviceFromActualDevice();
                break;
        }

    }

    public void onTemplate(FloatingActionButton b) {
        final DeviceSelectDialogFragment deviceSelectDialogFragment = DeviceSelectDialogFragment.newInstance();
        DeviceSelectDialogFragment.show(this, deviceSelectDialogFragment);

        deviceSelectDialogFragment.getRecyclerView().setVisibility(View.GONE);
        deviceSelectDialogFragment.getNoDeviceFoundTextView().setVisibility(View.VISIBLE);


        File deviceJsonDirectory = new File(getFilesDir().getPath() + File.separator + "devices");
        if(!deviceJsonDirectory.exists()) {
            boolean dirCreation = deviceJsonDirectory.mkdir();
            if(!dirCreation) {
                throw new RuntimeException();
            }
        }

        List<File> jsonDeviceFiles = new ArrayList<>();
        List<SpoofDevice> spoofDevices = new ArrayList<>();

        File[] fileDir = deviceJsonDirectory.listFiles();

        for (File file : fileDir) {
            if (file.isFile()) {
                String fileExtension = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
                if (fileExtension.equals("json")) {
                    jsonDeviceFiles.add(new File(file.getPath()));
                }
            }
        }

        if(!jsonDeviceFiles.isEmpty()) {
            for (File file : jsonDeviceFiles) {
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file.getPath()));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    SpoofDevice spoofDevice = new SpoofDevice(new JSONObject(stringBuilder.toString()));
                    spoofDevices.add(spoofDevice);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            deviceSelectDialogFragment.getRecyclerView().setVisibility(View.VISIBLE);
            deviceSelectDialogFragment.getNoDeviceFoundTextView().setVisibility(View.GONE);
        }

        DeviceSelectFragment_RecyclerViewAdapter adapter = new DeviceSelectFragment_RecyclerViewAdapter(spoofDevices);
        deviceSelectDialogFragment.getRecyclerView().setAdapter(adapter);
        deviceSelectDialogFragment.getRecyclerView().setLayoutManager(new LinearLayoutManager(DeviceSelectActivity.this));

        adapter.setListener(new RecyclerViewOnClickListener() {
            @Override
            public void onRowClicked(RecyclerView.Adapter rva, int position) {
            }

            @Override
            public void onViewClicked(RecyclerView.Adapter rva, View view, int position) {
                DeviceSelectFragment_RecyclerViewAdapter adapter = (DeviceSelectFragment_RecyclerViewAdapter) rva;
                SpoofDevice sp = adapter.getSpoofDevices().get(position);
                clearAllErrors();
                clearAllSpoofDeviceFields();
                loadSpoofDeviceFromPreconfigured(sp);
                deviceSelectDialogFragment.dismiss();
            }
        });

        deviceSelectDialogFragment.getRefreshButtonTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RefreshAsyncTask(deviceSelectDialogFragment).execute();
            }
        });

        deviceSelectDialogFragment.getOkTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviceSelectDialogFragment.dismiss();
            }
        });
    }

    private class RefreshAsyncTask extends AsyncTask<Void, Void, List<SpoofDevice>> {
        private ProgressDialog pd;
        private DeviceSelectDialogFragment f;

        RefreshAsyncTask(DeviceSelectDialogFragment f) {
            this.f = f;
        }

        @Override
        protected void onPreExecute() {
            this.pd = new ProgressDialog(DeviceSelectActivity.this);
            this.pd.setTitle("Loading");
            this.pd.setMessage("Gathering device build information...");
            this.pd.show();

            if(!isNetworkAvailable() || !isOnline()) {
                return;
            }

            File fileJsonDir = new File(getFilesDir().getPath() + File.separator + "devices");
            File[] fileDir = fileJsonDir.listFiles();

            for (File file : fileDir) {
                if (file.isFile()) {
                    file.delete();
                }
            }

            DeviceSelectFragment_RecyclerViewAdapter adapter = (DeviceSelectFragment_RecyclerViewAdapter) f.getRecyclerView().getAdapter();
            adapter.getSpoofDevices().clear();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected List<SpoofDevice> doInBackground(Void... voids) {
            List<SpoofDevice> spoofDevices = new ArrayList<>();

            if(!isNetworkAvailable() || !isOnline()) {
                return null;
            }


            // should be a singleton
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/pbombnz/AndroidDeviceBuildInfo/contents/")
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();

                if(!response.isSuccessful()) {
                    throw new IOException("unssucessful");
                }

                String responseData = response.body().string();
                JSONArray json = new JSONArray(responseData);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    String name = obj.getString("name");
                    Log.d(Common.TAG, "Github file name: "+name);

                    int extIndx = name.lastIndexOf(".");
                    if(extIndx > 0) {
                        String extension = name.substring(name.lastIndexOf(".") + 1);
                        if(extension.equalsIgnoreCase("json")) {
                            String download_url = obj.getString("download_url");

                            Request request2 = new Request.Builder()
                                    .url(download_url)
                                    .build();

                            Response response2;
                            response2 = client.newCall(request2).execute();

                            String x = response2.body().string();
                            Log.d(Common.TAG, "x: "+x);

                            JSONObject s = new JSONObject(x);

                            SpoofDevice sp = new SpoofDevice(s);

                            File fileJsonDir2 = new File(getFilesDir().getPath() + File.separator + "devices" + File.separator + name);
                            FileOutputStream fos = new FileOutputStream (new File(fileJsonDir2.getAbsolutePath()), false);
// Create buffered writer
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                            writer.write(x);
                            writer.close();

                            Log.d(Common.TAG, "sp: "+sp);
                            Log.d(Common.TAG, "sp2: "+sp);
                            spoofDevices.add(sp);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return spoofDevices;
        }

        @Override
        protected void onPostExecute(List<SpoofDevice> spoofDevices) {
            this.pd.dismiss();

            if(spoofDevices == null) {
                return;
            }

            DeviceSelectFragment_RecyclerViewAdapter d = (DeviceSelectFragment_RecyclerViewAdapter) f.getRecyclerView().getAdapter();
            d.getSpoofDevices().clear();
            d.getSpoofDevices().addAll(spoofDevices);
            d.notifyDataSetChanged();

            f.getRecyclerView().setVisibility(View.VISIBLE);
            f.getNoDeviceFoundTextView().setVisibility(View.GONE);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_device_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                for(View view : mConfigViews) {
                    if (view.hasFocus()) {
                        View focus = getCurrentFocus();
                        if (focus != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                            focus.clearFocus();
                        }
                        return true;
                    }
                }

                if(mFAM.isOpened()) {
                    mFAM.close(true);
                    return true;
                }

                boolean isValid = validateFields();
                if(isValid) {
                    saveSpoofDeviceToSharedPreferences(true);
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("Cannot Continue")
                            .setMessage("Some of the spoof device configuration fields are empty or contain invalid inputs. Please fix them in order to save your configurations.")
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                    return true;
                }
                break;
            case R.id.learn_more:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/reference/android/os/Build.html")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        for(View view : mConfigViews) {
            if (view instanceof EditText && view.hasFocus()) {
                View focus = getCurrentFocus();
                if (focus != null) {
                    focus.clearFocus();
                    return;
                }
            }
        }

        if(mFAM.isOpened()) {
            mFAM.close(true);
            return;
        }

        boolean isValid = validateFields();
        if(isValid) {
            saveSpoofDeviceToSharedPreferences(true);
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Invalids Field Detected")
                .setMessage("Some of the spoof device configuration fields are invalid. Please fix them in order to save your configurations.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            builder.show();
        }
    }

    @Override
    public void onPause() {
        boolean isValid = validateFields();
        if(isValid) {
            saveSpoofDeviceToSharedPreferences(true);
        }
        super.onPause();
    }

    private void displayDeviceSelect(boolean loadFromSharedPreferences, String humanReadableDeviceName) {
        View focus = this.getCurrentFocus();

        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            focus.clearFocus();
        }


        if(loadFromSharedPreferences) {
            loadSpoofDeviceFromSharedPreferences();
            return;
        }

        switch (humanReadableDeviceName) {
            case "Your Actual Device":
                boolean isSelfHooked = mSharedPref.getStringSet(SharedPreferencesKeys.PACKAGES, new HashSet<String>()).contains(getApplicationInfo().packageName);
                if(isSelfHooked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("Actual Device Information cannot be loaded")
                            .setCancelable(false)
                            .setMessage("Due to having spoofing enabled on this application we are unable to retrieve your actual device information as the spoof device information is seen instead." +
                                    " Please remove this application from the 'spoofed applications' list and restart the this config app inorder to use this setting.")

                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    loadSpoofDeviceFromActualDevice();
                }
                break;
            default:
                loadSpoofDeviceFromPreconfigured(humanReadableDeviceName);
                break;
        }
    }

    private void saveSpoofDeviceToSharedPreferences(boolean displayToast) {
        SharedPreferences.Editor sharedPrefEditor = mSharedPref.edit();
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOARD, mEditText__build_board.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_ID, mEditText__build_id.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DISPLAY, mEditText__build_display.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_PRODUCT, mEditText__build_product.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE, mEditText__build_device.getText().toString());

        RadioButton cpuAbi = (RadioButton) findViewById(mRadioGroup__build_cpuAbi.getCheckedRadioButtonId());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI, cpuAbi.getText().toString());
        RadioButton cpuAbi2 = (RadioButton) findViewById(mRadioGroup__build_cpuAbi2.getCheckedRadioButtonId());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI2, cpuAbi2.getText().toString());

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, mEditText__build_manufacturer.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BRAND, mEditText__build_brand.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MODEL, mEditText__build_model.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, mEditText__build_bootloader.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_RADIO, mEditText__build_radio.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, mEditText__build_hardware.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, mEditText__build_serial.getText().toString());

        StringBuilder supportedAbis32bit = new StringBuilder();
        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
            if(checkBox.isChecked()) {
                supportedAbis32bit.append(checkBox.getText()).append(",");
            }
        }
        if(supportedAbis32bit.length() > 0) {
            supportedAbis32bit.deleteCharAt(supportedAbis32bit.length() - 1);
        }

        StringBuilder supportedAbis64bit = new StringBuilder();
        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
            if(checkBox.isChecked()) {
                supportedAbis64bit.append(checkBox.getText()).append(",");
            }
        }
        if(supportedAbis64bit.length() > 0) {
            supportedAbis64bit.deleteCharAt(supportedAbis64bit.length() - 1);
        }

        String supportedAbis;
        if(supportedAbis64bit.toString().equals("") && supportedAbis32bit.toString().equals("")) {
            supportedAbis = "";
        } else if(supportedAbis64bit.toString().equals("") && !supportedAbis32bit.toString().equals("")) {
            supportedAbis = new String(supportedAbis32bit);
        } else if(!supportedAbis64bit.toString().equals("") && supportedAbis32bit.toString().equals("")) {
            supportedAbis = new String(supportedAbis64bit);
        } else if(!supportedAbis64bit.toString().equals("") && !supportedAbis32bit.toString().equals("")) {
            supportedAbis = String.valueOf(supportedAbis64bit) + "," + supportedAbis32bit;
        } else {
            supportedAbis = null;
        }

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_32_BIT, supportedAbis32bit.toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_64_BIT, supportedAbis64bit.toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS, supportedAbis);

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TYPE, mEditText_build_type.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TAGS, mEditText_build_tags.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, mEditText_build_fingerprint.getText().toString());

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_INCREMENTAL, mEditText__version_incremental.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_RELEASE, mEditText__version_release.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK, mEditText__version_sdk.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK_INT, mEditText__version_sdkint.getText().toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, mEditText__version_codename.getText().toString());

        sharedPrefEditor.apply();
        if(displayToast) {
            Toast.makeText(this, "Spoof device information saved.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpoofDeviceFromSharedPreferences() {
        mEditText__build_board.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOARD, Build.UNKNOWN));
        mEditText__build_id.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_ID,  Build.UNKNOWN));
        mEditText__build_display.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DISPLAY,  Build.UNKNOWN));
        mEditText__build_product.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_PRODUCT,  Build.UNKNOWN));
        mEditText__build_device.setText( mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE,  Build.UNKNOWN));

        String cpuAbi = mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI, Build.UNKNOWN);
        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi)) {
                radioButton.setChecked(true);
                break;
            }
        }

        String cpuAbi2 = mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI2, Build.UNKNOWN);
        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi2)) {
                radioButton.setChecked(true);
                break;
            }
        }

        mEditText__build_manufacturer.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, Build.UNKNOWN));
        mEditText__build_brand.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BRAND, Build.UNKNOWN));
        mEditText__build_model.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MODEL, Build.UNKNOWN));
        mEditText__build_bootloader.setText( mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, Build.UNKNOWN));
        mEditText__build_radio.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_RADIO, Build.UNKNOWN));
        mEditText__build_hardware.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, Build.UNKNOWN));
        mEditText__build_serial.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, Build.UNKNOWN));

        //String[] supportedAbis = mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS, "").split(",");
        String[] supportedAbis32bit = mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_32_BIT, "").split(",");
        String[] supportedAbis64bit = mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_64_BIT, "").split(",");

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
            for(String abi : supportedAbis32bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
            for(String abi : supportedAbis64bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }


        mEditText_build_type.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TYPE, Build.UNKNOWN));
        mEditText_build_tags.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TAGS, Build.UNKNOWN));
        mEditText_build_fingerprint.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, Build.UNKNOWN));

        mEditText__version_incremental.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_INCREMENTAL, Build.UNKNOWN));
        mEditText__version_release.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_RELEASE, Build.UNKNOWN));
        mEditText__version_sdk.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK, Build.UNKNOWN));
        mEditText__version_sdkint.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK_INT, Build.UNKNOWN));
        mEditText__version_codename.setText(mSharedPref.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, Build.UNKNOWN));

        Toast.makeText(this, "Loaded Spoof Device data from file", Toast.LENGTH_SHORT).show();
    }



    private void loadSpoofDeviceFromPreconfigured(String humanReadableDeviceName) {
        SpoofDevice spoofDevice = SpoofDevices.getDeviceInfoByHumanDeviceName(humanReadableDeviceName);

        if(spoofDevice == null) {
            return;
        }

        mEditText__build_board.setText(spoofDevice.Build.BOARD);
        mEditText__build_id.setText(spoofDevice.Build.ID);
        mEditText__build_display.setText(spoofDevice.Build.DISPLAY);
        mEditText__build_product.setText(spoofDevice.Build.PRODUCT);
        mEditText__build_device.setText(spoofDevice.Build.DEVICE);

        String cpuAbi = spoofDevice.Build.CPU_ABI;
        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi)) {
                radioButton.setChecked(true);
                break;
            }
        }

        String cpuAbi2 = spoofDevice.Build.CPU_ABI2;
        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi2)) {
                radioButton.setChecked(true);
                break;
            }
        }

        mEditText__build_manufacturer.setText(spoofDevice.Build.BOARD);
        mEditText__build_brand.setText(spoofDevice.Build.BRAND);
        mEditText__build_model.setText(spoofDevice.Build.MODEL);
        mEditText__build_bootloader.setText(spoofDevice.Build.BOOTLOADER);
        mEditText__build_radio.setText(spoofDevice.Build.RADIO);
        mEditText__build_hardware.setText(spoofDevice.Build.HARDWARE);
        mEditText__build_serial.setText(spoofDevice.Build.SERIAL);

        //String[] supportedAbis  = spoofDevice.Build.SUPPORTED_ABIS;
        String[] supportedAbis32bit = spoofDevice.Build.SUPPORTED_32_BIT_ABIS;
        String[] supportedAbis64bit = spoofDevice.Build.SUPPORTED_64_BIT_ABIS;

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
            for(String abi : supportedAbis32bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
            for(String abi : supportedAbis64bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

        mEditText_build_type.setText(spoofDevice.Build.TYPE);
        mEditText_build_tags.setText(spoofDevice.Build.TAGS);
        mEditText_build_fingerprint.setText(spoofDevice.Build.FINGERPRINT);

        mEditText__version_incremental.setText(spoofDevice.VERSION.INCREMENTAL);
        mEditText__version_release.setText(spoofDevice.VERSION.RELEASE);
        mEditText__version_sdk.setText(spoofDevice.VERSION.SDK);
        mEditText__version_sdkint.setText(String.valueOf(spoofDevice.VERSION.SDK_INT));
        mEditText__version_codename.setText(spoofDevice.VERSION.CODENAME);

        Toast.makeText(this, "Loaded Spoof Device data from "+humanReadableDeviceName, Toast.LENGTH_SHORT).show();
    }

    private void loadSpoofDeviceFromPreconfigured(SpoofDevice spoofDevice) {
        //SpoofDevice spoofDevice = SpoofDevices.getDeviceInfoByHumanDeviceName(humanReadableDeviceName);

        if(spoofDevice == null) {
            return;
        }

        mEditText__build_board.setText(spoofDevice.Build.BOARD);
        mEditText__build_id.setText(spoofDevice.Build.ID);
        mEditText__build_display.setText(spoofDevice.Build.DISPLAY);
        mEditText__build_product.setText(spoofDevice.Build.PRODUCT);
        mEditText__build_device.setText(spoofDevice.Build.DEVICE);

        String cpuAbi = spoofDevice.Build.CPU_ABI;
        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi)) {
                radioButton.setChecked(true);
                break;
            }
        }

        String cpuAbi2 = spoofDevice.Build.CPU_ABI2;
        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi2)) {
                radioButton.setChecked(true);
                break;
            }
        }

        mEditText__build_manufacturer.setText(spoofDevice.Build.BOARD);
        mEditText__build_brand.setText(spoofDevice.Build.BRAND);
        mEditText__build_model.setText(spoofDevice.Build.MODEL);
        mEditText__build_bootloader.setText(spoofDevice.Build.BOOTLOADER);
        mEditText__build_radio.setText(spoofDevice.Build.RADIO);
        mEditText__build_hardware.setText(spoofDevice.Build.HARDWARE);
        mEditText__build_serial.setText(spoofDevice.Build.SERIAL);

        //String[] supportedAbis  = spoofDevice.Build.SUPPORTED_ABIS;
        String[] supportedAbis32bit = spoofDevice.Build.SUPPORTED_32_BIT_ABIS;
        String[] supportedAbis64bit = spoofDevice.Build.SUPPORTED_64_BIT_ABIS;

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
            for(String abi : supportedAbis32bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

        for(CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
            for(String abi : supportedAbis64bit) {
                if(checkBox.getText().equals(abi)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

        mEditText_build_type.setText(spoofDevice.Build.TYPE);
        mEditText_build_tags.setText(spoofDevice.Build.TAGS);
        mEditText_build_fingerprint.setText(spoofDevice.Build.FINGERPRINT);

        mEditText__version_incremental.setText(spoofDevice.VERSION.INCREMENTAL);
        mEditText__version_release.setText(spoofDevice.VERSION.RELEASE);
        mEditText__version_sdk.setText(spoofDevice.VERSION.SDK);
        mEditText__version_sdkint.setText(String.valueOf(spoofDevice.VERSION.SDK_INT));
        mEditText__version_codename.setText(spoofDevice.VERSION.CODENAME);

        Toast.makeText(this, "Loaded Spoof Device data from "+spoofDevice.getHumanDeviceName(), Toast.LENGTH_SHORT).show();
    }


    @SuppressWarnings("deprecation")
    private void loadSpoofDeviceFromActualDevice() {
        mEditText__build_board.setText(Build.BOARD);
        mEditText__build_id.setText(Build.ID);
        mEditText__build_display.setText(Build.DISPLAY);
        mEditText__build_product.setText(Build.PRODUCT);
        mEditText__build_device.setText(Build.DEVICE);

        String cpuAbi = Build.CPU_ABI;
        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi)) {
                radioButton.setChecked(true);
                break;
            }
        }

        String cpuAbi2 = Build.CPU_ABI2;
        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            if(radioButton.getText().equals(cpuAbi2)) {
                radioButton.setChecked(true);
                break;
            }
        }

        mEditText__build_manufacturer.setText(Build.BOARD);
        mEditText__build_brand.setText(Build.BRAND);
        mEditText__build_model.setText(Build.MODEL);
        mEditText__build_bootloader.setText(Build.BOOTLOADER);
        mEditText__build_radio.setText(Build.getRadioVersion());
        mEditText__build_hardware.setText(Build.HARDWARE);
        mEditText__build_serial.setText(Build.SERIAL);

        //String[] supportedAbis  = Build.SUPPORTED_ABIS;
        String[] supportedAbis32bit = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            supportedAbis32bit = Build.SUPPORTED_32_BIT_ABIS;
            String[] supportedAbis64bit = Build.SUPPORTED_64_BIT_ABIS;

            for (CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
                for (String abi : supportedAbis32bit) {
                    if (checkBox.getText().equals(abi)) {
                        checkBox.setChecked(true);
                        break;
                    }
                }
            }

            for (CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
                for (String abi : supportedAbis64bit) {
                    if (checkBox.getText().equals(abi)) {
                        checkBox.setChecked(true);
                        break;
                    }
                }
            }
        }

        mEditText_build_type.setText(Build.TYPE);
        mEditText_build_tags.setText(Build.TAGS);
        mEditText_build_fingerprint.setText(Build.FINGERPRINT);

        mEditText__version_incremental.setText(Build.VERSION.INCREMENTAL);
        mEditText__version_release.setText(Build.VERSION.RELEASE);
        mEditText__version_sdk.setText(Build.VERSION.SDK);
        mEditText__version_sdkint.setText(String.valueOf(Build.VERSION.SDK_INT));
        mEditText__version_codename.setText(Build.VERSION.CODENAME);

        Toast.makeText(this, "Loaded Spoof Device data from actual device", Toast.LENGTH_SHORT).show();
    }


    /**
     * Clears all fields and resets a few fields
     */
    private void clearAllSpoofDeviceFields() {
        //final String DEFAULT_CPU_ABI = Build.CPU_ABI;
        //final String DEFAULT_CPU_ABI2 = Build.CPU_ABI2;


        mEditText__build_board.setText("");
        mEditText__build_id.setText("");
        mEditText__build_display.setText("");
        mEditText__build_product.setText("");
        mEditText__build_device.setText("");

        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            //if(radioButton.getText().equals(DEFAULT_CPU_ABI)) {
            //    radioButton.setChecked(true);
            //} else {
                radioButton.setChecked(false);
           // }
        }

        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            //if(radioButton.getText().equals(DEFAULT_CPU_ABI2)) {
            //    radioButton.setChecked(true);
            //} else {
                radioButton.setChecked(false);
            //}
        }

        mEditText__build_manufacturer.setText("");
        mEditText__build_brand.setText("");
        mEditText__build_model.setText("");
        mEditText__build_bootloader.setText("");
        mEditText__build_radio.setText("");
        mEditText__build_hardware.setText("");
        mEditText__build_serial.setText("");

        for (CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
            //if(checkBox.getText().equals(DEFAULT_CPU_ABI)) {
            //    checkBox.setChecked(true);
            //} else {
                checkBox.setChecked(false);
            //}
        }

        for (CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
            //if(checkBox.getText().equals(DEFAULT_CPU_ABI2)) {
            //    checkBox.setChecked(true);
            //} else {
                checkBox.setChecked(false);
            //}
        }

        mEditText_build_type.setText("");
        mEditText_build_tags.setText("");
        mEditText_build_fingerprint.setText("");

        mEditText__version_incremental.setText("");
        mEditText__version_release.setText("");
        mEditText__version_sdk.setText("");
        mEditText__version_sdkint.setText("");
        mEditText__version_codename.setText("");

        Toast.makeText(this, "Cleared all Spoof Device fields.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Validate the Configuration views to an acceptable standard which won't crash spoofed
     * application or the Xposed process due invalid user input. This specifically checks SDK_INT
     * and the supported ABI
     *
     * @return true, if all fields are validated and/or have correct inputs, otherwise return false
     * if there are any inconsistent and/or incorrect inputs.
     */
    private boolean validateFields() {
        boolean isValid = true;
        //List<Integer> optionalFields = new ArrayList<>();
        //optionalFields.add(R.id.type_editText);
        //optionalFields.add(R.id.version_incremental_editText);
        //optionalFields.add(R.id.version_codename_editText);

        for(View view: mConfigViews) {
            /* No longer validate EditText views are being empty is an accepted value.

            if(view instanceof EditText) {
                EditText editText = (EditText) view;

                //Ignore some option fields
                if(optionalFields.contains(editText.getId())) {
                    continue;
                }

                if(editText.getText().length() == 0) {
                    isValid = false;
                    editText.setError("Field cannot be empty.");
                }
            } else*/ if(view instanceof RadioGroup) {
                boolean isAnyRadioButtonChecked = false;
                RadioGroup radioGroup = (RadioGroup) view;

                for(int i =0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if(radioButton.isChecked()) {
                        isAnyRadioButtonChecked = true;
                        break;
                    }
                }

                if(!isAnyRadioButtonChecked) {
                    isValid = false;
                    TextView textView = null;

                    switch(radioGroup.getId()) {
                        case R.id.cpuAbi_radioGroup:
                            textView = mTextView__cpuAbiLabel;
                            break;
                        case R.id.cpuAbi2_radioGroup:
                            textView = mTextView__cpuAbi2Label;
                            break;
                    }

                    if(textView != null){
                        textView.setError("You must select a CPU architecture.");
                    }
                }
            }
        }


        boolean isCpuAbiInSupportedAbis = false;
        for(int i = 0; i < mRadioGroup__build_cpuAbi.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi.getChildAt(i);
            if(radioButton.isChecked()) {
                for (CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
                    if(radioButton.getText().equals(checkBox.getText())) {
                        isCpuAbiInSupportedAbis = true;
                        break;
                    }
                }
                if(isCpuAbiInSupportedAbis) {
                    break;
                }
                for (CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
                    if(radioButton.getText().equals(checkBox.getText())) {
                        isCpuAbiInSupportedAbis = true;
                        break;
                    }
                }
            }
        }
        boolean isCpuAbi2InSupportedAbis = false;
        for(int i = 0; i < mRadioGroup__build_cpuAbi2.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup__build_cpuAbi2.getChildAt(i);
            if(radioButton.isChecked()) {
                for (CheckBox checkBox : mCheckBoxes__build_supportedAbis32bit) {
                    if(radioButton.getText().equals(checkBox.getText())) {
                        isCpuAbi2InSupportedAbis = true;
                        break;
                    }
                }
                if(isCpuAbi2InSupportedAbis) {
                    break;
                }
                for (CheckBox checkBox : mCheckBoxes__build_supportedAbis64bit) {
                    if(radioButton.getText().equals(checkBox.getText())) {
                        isCpuAbi2InSupportedAbis = true;
                        break;
                    }
                }
            }
        }

        /*if(!isCpuAbiInSupportedAbis || !isCpuAbi2InSupportedAbis) {
            isValid = false;
            mTextView__supportedAbisLabel.setError("Your CPU ABI or CPU ABI 2 selection is not supported based on your Supported ABIs selection.");
        }*/

        if(!mEditText__version_sdk.getText().toString().equals(mEditText__version_sdkint.getText().toString())) {
            isValid = false;
            mEditText__version_sdkint.setError("SDK and SDK_INT cannot be different.");
            mEditText__version_sdk.setError("SDK and SDK_INT cannot be different.");
        }

        return isValid;
    }

    private void clearAllErrors() {
        for (View view : mConfigViews) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setError(null);
            }
        }

        mRadioGroup__build_cpuAbi.clearCheck();
        mRadioGroup__build_cpuAbi2.clearCheck();

        mTextView__cpuAbiLabel.setError(null);
        mTextView__cpuAbi2Label.setError(null);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        // Clear errors if existing
        if(radioGroup.getId() == R.id.cpuAbi_radioGroup) {
            mTextView__cpuAbiLabel.setError(null);
        } else if(radioGroup.getId() == R.id.cpuAbi2_radioGroup) {
            mTextView__cpuAbi2Label.setError(null);
        }
    }
}
