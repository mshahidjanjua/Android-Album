package com.example.albumsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.storage.StorageManager;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class Album {

    Context context;
    String NoImageError="No image found";
    final ArrayList<File> imgFolderList = new ArrayList<File>();
    private ArrayList<FileData> falData = new ArrayList<FileData>();
    DataObjectListener listener;
    ListPhotoTask flptPhotoGetter;

    Album(Context context){
        this.context=context;
    this.listener=null;}
    void processData(){
         ArrayList<FileData> respFalData;
         FileData fdata =null;
        flptPhotoGetter = new ListPhotoTask();
        flptPhotoGetter.execute(fdata);
    }
    private class ListPhotoTask extends AsyncTask<FileData, FileData, ArrayList<FileData> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected ArrayList<FileData>  doInBackground(FileData... params) {

            if(isCancelled()){
                return null;
            }

            FileData ff = params[0];
            if(ff ==  null){
                try {

                    if (selectImagesStorage(false)) {
                        selectImagesStorage(true);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    listener.onError(e.getLocalizedMessage());
                }

            }
            listener.onDataLoaded(falData);
            return falData;
        }
        @Override
        protected void onProgressUpdate(FileData... progress) {
//			falData.add(progress[0]);

        }
        @Override
        protected void onPostExecute(ArrayList<FileData>  result) {
            super.onPostExecute(result);


        }
        boolean selectImagesStorage(boolean removable)
        {
            boolean response=false;
            String extFilePath = getExternalStoragePath(context,removable); // for internal storage
            imgFolderList.clear();
            if (extFilePath != null){
                searchImageAlbumInStorages(extFilePath);
                response=true;
            }

            return response;
        }
        void searchImageAlbumInStorages(String path)
        {
            String[] response = getAlbums(path);

            if (response.length<0){

                Toast.makeText(context, NoImageError, Toast.LENGTH_SHORT).show();
            }

        }
        String[] getAlbums(String path)
        {
            File file = new File(path);
            String[] directories={};

            final String[] INTERNAL_EXTERNAL_NON_IMAGE_FOLDERS = new String[]{
                    "Android","LOST.DIR","Alarms","Android","Fonts","Movies","Music","Notifications","Playlists","Podcasts","Ringtones","Samsung","recordmaster" // and other formats you need
            };
            try {

                directories = file.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        File albumFolder = new File(current, name);

                        boolean response = false;
                        boolean nonImageFolder = false;
                        if (albumFolder.isDirectory()) {
                            String dotFolder = name.substring(0, 1);
                            for (final String foldername : INTERNAL_EXTERNAL_NON_IMAGE_FOLDERS) {
                                if (name.equalsIgnoreCase(foldername) || dotFolder.equalsIgnoreCase(".")) {
                                    nonImageFolder = true;
                                }
                            }

                            if (!nonImageFolder) {
                                ArrayList<File> imgFiles = getImagesFilesInAlbum(albumFolder.getPath());
                                if (imgFiles.size() > 0) {
                                    response = true;
                                    if (!imgFolderList.contains(albumFolder)) {

                                        FileData fData = new FileData();
                                        String thumbPath= imgFiles.get(0).getAbsolutePath();
                                        int imgCount= imgFiles.size();

                                        fData.setFsName(albumFolder.getName());
                                        fData.setFsLocalFilePath(albumFolder.getAbsolutePath());
                                        fData.setFiImageCount(imgCount);
                                        fData.setFsThumbNailPath(thumbPath);
                                        fData.setFsFiles(imgFiles);

                                        falData.add(fData);

                                        getAlbums(albumFolder.getPath());
                                    }


                                } else {
                                    getAlbums(albumFolder.getPath());
                                }
                            }

                        }
                        return response;
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return directories;

        }
        ArrayList<File> getImagesFilesInAlbum(String path)
        {
            final ArrayList<File> responseFiles= new ArrayList<File>();
            File file = new File(path);
            file.getName();
            file.getAbsoluteFile();
            // array of supported extensions (use a List if you prefer)
            final String[] EXTENSIONS = new String[]{
                    "jpg","jpeg", "png" // and other formats you need
            };
            try {
                int images = file.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        File imageFile = new File(current, name);
                        boolean response = false;
                        if (imageFile.isFile()) {
                            for (final String ext : EXTENSIONS) {
                                if (name.endsWith("." + ext)) {
                                    response = true;
                                    responseFiles.add(imageFile);

                                }
                            }
                        }

                        return response;
                    }
                }).length;
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return responseFiles;
        }
    }
    private static String getExternalStoragePath(Context mContext, boolean is_removable) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removable == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Assign the listener implementing events interface that will receive the events
    public void setDataObjectListener(DataObjectListener listener) {
        this.listener = listener;
    }
    public interface DataObjectListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onError(String error);
        // or when data has been loaded
        public void onDataLoaded(ArrayList<FileData> data);
    }
}

