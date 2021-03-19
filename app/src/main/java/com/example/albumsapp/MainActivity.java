package com.example.albumsapp;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    Album album;
    private ArrayList<FileData> falData = new ArrayList<>();
    TextView albums;
    TextView images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        albums= findViewById(R.id.albums);
        images= findViewById(R.id.images);
        Button loadData= findViewById(R.id.loaddata);

        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                falData.clear();
                album= new Album(MainActivity.this);
                album.setDataObjectListener(new Album.DataObjectListener() {
                    @Override
                    public void onError(String error) {
                        // Code to handle object ready
                    }

                    @Override
                    public void onDataLoaded(final ArrayList<FileData> data) {
                        // Code to handle data loaded from network
                        // Use the data here!
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                processImages(data);
                            }
                        });
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        permissionReadExternalStorage();
                    } else if (checkSelfPermission(READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        album.processData();
                    }
                }
                else {
                        album.processData();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region permissionReadExternalStorage()
    private boolean permissionReadExternalStorage()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String []{READ_EXTERNAL_STORAGE},6);
                return false;
            }
        }
        return true;
    }

    //endregion

    void processImages(ArrayList<FileData> fdata){
        try {

            if (fdata != null) {
               int inImgCount=0;

                albums.setText(fdata.size()+"");

                for (final FileData album : fdata) {

                    inImgCount += album.getFiImageCount();
                }
                images.setText(inImgCount+"");


            } else {
                Toast.makeText(MainActivity.this, "No Image error", Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
