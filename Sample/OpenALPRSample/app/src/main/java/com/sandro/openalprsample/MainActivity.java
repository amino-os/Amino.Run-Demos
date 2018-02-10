package com.sandro.openalprsample;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.openalpr.OpenALPR;
import org.openalpr.SapphireAccess;
import org.openalpr.model.Result;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 100;
    private static final int STORAGE=1;
    private String ANDROID_DATA_DIR;
    private static File destination;
    private TextView resultTextView;
    private ImageView imageView;
    private static final int MAX_NUM_OF_PLATES = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        resultTextView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        resultTextView.setText("Press the button below to start a request.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
//                Uri fileUri = Uri.fromFile(destination);
//                String fileName = destination.getName();
//                long fileSize = destination.length();

//                final ProgressDialog progressFileInfo
//                        = ProgressDialog.show(this, "FileSize", "Name = " + fileName + " Size = " + fileSize, true);

            final ProgressDialog progress
                    = ProgressDialog.show(this, "Loading", "Parsing result...", true);
            final String openAlprConfFile
                    = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            // Picasso requires permission.WRITE_EXTERNAL_STORAGE
            Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(imageView);
            resultTextView.setMovementMethod(new ScrollingMovementMethod());
            resultTextView.setText("Processing");


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    String result = null;

                    try {
                        String filePath = destination.getAbsolutePath();
                        result = new OpenAlprSapphire(MainActivity.this, ANDROID_DATA_DIR, "us", "", filePath, openAlprConfFile, MAX_NUM_OF_PLATES).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("OPEN ALPR", result);

                    try {
                        final Results results = new Gson().fromJson(result, Results.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                                    Toast.makeText(MainActivity.this, "It was not possible to detect the licence plate.", Toast.LENGTH_LONG).show();
                                    resultTextView.setText("It was not possible to detect the licence plate.");
                                } else {
                                    String textToShow = " Processing time: " + String.format("%.2f", ((results.getProcessingTimeMs() / 1000.0) % 60)) + " seconds\n";
                                    textToShow += "Number of plates found: " + results.getResults().size() +"\n";

                                    for (Result result : results.getResults()) {
                                        textToShow += "Plate: " + result.getPlate()
                                                + " Confidence: " + String.format("%.2f", result.getConfidence()) + "%\n";
                                    }

                                    resultTextView.setText(textToShow);
                                }
                            }
                        });

                    } catch (JsonSyntaxException exception) {
                        final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTextView.setText(resultsError.getMsg());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    progressFileInfo.dismiss();
                    progress.dismiss();
                }
            });
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class OpenAlprSapphire extends AsyncTask<Void, Void, String> {
        private Context context;
        private String ANDROID_DATA_DIR;
        private String countryCode;
        private String secondParam;
        private String absolutePath;
        private String openAlprConfFile;
        private int MAX_NUM_OF_PLATES;

        private OpenAlprSapphire(Context context, String ANDROID_DATA_DIR, String countryCode, String secondParam, String absolutePath, String openAlprConfFile, int MAX_NUM_OF_PLATES) {
            this.context = context;
            this.ANDROID_DATA_DIR = ANDROID_DATA_DIR;
            this.countryCode = countryCode;
            this.secondParam = secondParam;
            this.absolutePath = absolutePath;
            this.openAlprConfFile = openAlprConfFile;
            this.MAX_NUM_OF_PLATES = MAX_NUM_OF_PLATES;
        }

        @Override
        protected String doInBackground(Void... params) {
            SapphireAccess.context = MainActivity.this;
            SapphireAccess.ANDROID_DATA_DIR = ANDROID_DATA_DIR;
            String result = SapphireAccess.getResult(this.countryCode, this.secondParam, this.absolutePath, this.openAlprConfFile, this.MAX_NUM_OF_PLATES);
            return result;
        }
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, STORAGE);
        } else { // We already have permissions, so handle as normal
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE:{
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // permission was granted, yay!
                    takePicture();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Storage permission is needed to analyse the picture.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public void takePicture() {
        try {
            createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (destination != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (destination != null) {// Picasso does not seem to have an issue with a null value, but to be safe
            Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(imageView);
        }
    }

    private void createImageFile() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");
    }

}
