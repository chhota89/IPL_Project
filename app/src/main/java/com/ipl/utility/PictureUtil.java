package com.ipl.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by bridgeit007 on 2/6/16.
 */

public class PictureUtil {

    private static final String IPL_FOLDER = "IPL";
    public static String TAG = "PictureUtil";

    public static File getSavePath(String folderName, String filename) {
        File filePath;
        if (hasSDCard()) {
            filePath = new File(getSDCardPath() + "/" + IPL_FOLDER + "/" + folderName + "/" + filename);
        } else {
            filePath = Environment.getDataDirectory();
        }
        return filePath;
    }

    public static Bitmap loadFileFromLocalStorage(String folderName, String filename) throws FileNotFoundException {
        File filePath = getSavePath(folderName, filename);
        return loadFromFile(filePath.getAbsolutePath());
    }

    private static Bitmap loadFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);

        if (!file.exists())
            throw new FileNotFoundException();

        Bitmap btm = BitmapFactory.decodeFile(filename);
        return btm;
    }

    private static String getSDCardPath() {
        File filePath = Environment.getExternalStorageDirectory();
        return filePath.getAbsolutePath();
    }

    //--method to check if there is External SD card is available or not
    private static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    //store image in to the memory
    public static void storeImageInFile(Bitmap bitmap, String folderName, String ImageName) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        try {
            File directory = new File(root + IPL_FOLDER + "/" + folderName);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            OutputStream fOut = null;

            File myPath = new File(directory, ImageName);
            if (myPath.exists())
                myPath.delete();
            myPath.createNewFile();
            fOut = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Loading image in Async task
    public static class LoadImageAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return loadFileFromLocalStorage(params[0], params[1]);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "doInBackground: Image is not found");
                return null;
            }
        }
    }
}
