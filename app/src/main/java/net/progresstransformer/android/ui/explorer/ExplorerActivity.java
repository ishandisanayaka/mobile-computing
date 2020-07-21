package net.progresstransformer.android.ui.explorer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.progresstransformer.android.R;
import net.progresstransformer.android.ui.ShareActivity;
import net.progresstransformer.android.util.Settings;
import net.progresstransformer.android.viewData.Database.DBHelper;
import net.progresstransformer.android.viewData.VideoLoder.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Explorer for browsing directories
 */
public class ExplorerActivity extends AppCompatActivity  {

    private static final String TAG = "ExplorerActivity";
    private static final int SHARE_REQUEST = 1;

    /**
     * Storage directory (internal, SD card, etc.)
     */
//    private class StorageDirectory {
//
//        private String mName;
//        private String mPath;
//
//        StorageDirectory(String name, String path) {
//            mName = name;
//            mPath = path;
//        }
//
//        String name() {
//            return mName;
//        }
//
//        String path() {
//            return mPath;
//        }
//    }
//
//    private ExplorerFragment mFragment;
//    private boolean mShowHidden = false;
//    private ArrayList<StorageDirectory> mDirectories;
//
//    /**
//     * Create and display a fragment for the specified directory
//     * @param directory path to directory
//     * @param clearStack clear the back stack with this item
//     */
//    private void showDirectory(String directory, boolean clearStack) {
//        mFragment = new ExplorerFragment();
//
//        // If directory is not empty, pass it along to the fragment
//        if (directory != null) {
//            Bundle arguments = new Bundle();
//            arguments.putString(ExplorerFragment.DIRECTORY, directory);
//            arguments.putBoolean(ExplorerFragment.SHOW_HIDDEN, mShowHidden);
//            mFragment.setArguments(arguments);
//        }
//
//        // Begin a transaction to insert the fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        // If the stack isn't being cleared, animate the transaction
//        if (!clearStack) {
//            transaction.setCustomAnimations(
//                    R.anim.enter_from_right,
//                    R.anim.exit_to_left,
//                    R.anim.enter_from_left,
//                    R.anim.exit_to_right
//            );
//        }
//
//        // Insert the new fragment
//        transaction.replace(R.id.directory_container, mFragment);
//
//        // If the stack isn't being cleared, add this one to the back
//        if (!clearStack) {
//            transaction.addToBackStack(null);
//        }
//
//        // Commit the transaction
//        transaction.commit();
//    }


    public int positionOfArray;
    private Uri urlOfVideo;
    private Uri progress_text;
    private DBHelper dbHelper;
    private int lastPosition;
    private String type;
    private String filename;
    ArrayList<HashMap<String, String>> progressAttay1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper=new DBHelper(this);

        ArrayList<Uri> uris= new ArrayList<Uri>();

        Intent intent = getIntent();
        positionOfArray = intent.getIntExtra("uri", 0);
        type=intent.getStringExtra("type");

        Log.d("aa",type);

        if (type.equals("video")){
            urlOfVideo = Uri.fromFile(Constant.allMediaList.get(positionOfArray));
            filename = Constant.allMediaList.get(positionOfArray).getName();
//            String a= String.valueOf(Constant.allMediaList.get(positionOfArray));
//            Log.d("aaaa","jgjgkjkl5556"+a);
//            String[] getdataList=a.split("");
//            Log.d("aaaa", String.valueOf(getdataList));
//            String b=getdataList[getdataList.length-1];
//            Log.d("aaaa","jgjgkjkl5556"+b);

            progressAttay1 = dbHelper.getvideoData(String.valueOf(urlOfVideo));
            if (!progressAttay1.isEmpty()) {
                lastPosition = Integer.parseInt(progressAttay1.get(0).get("progress"));
            } else{
                lastPosition=0;
            }
        }else if (type.equals("audio")){
            urlOfVideo = Uri.fromFile(Constant.allaudioList.get(positionOfArray));
            filename = Constant.allaudioList.get(positionOfArray).getName();
            progressAttay1 = dbHelper.getAudioData(String.valueOf(urlOfVideo));
            if (!progressAttay1.isEmpty()) {
                lastPosition = Integer.parseInt(progressAttay1.get(0).get("progress"));
            } else{
                lastPosition=0;
            }
        }else if (type.equals("pdf")){
            urlOfVideo = Uri.fromFile(Constant.allpdfList.get(positionOfArray));
            filename = Constant.allpdfList.get(positionOfArray).getName();
            progressAttay1 = dbHelper.getPdfData(String.valueOf(urlOfVideo));
            if (!progressAttay1.isEmpty()) {
                lastPosition = Integer.parseInt(progressAttay1.get(0).get("progress"));
            } else{
                lastPosition=0;
            }
        }
        Log.d("aa", String.valueOf(lastPosition));
        Log.d("aa", filename);
        setTheme(new Settings(this).getTheme());
        setContentView(R.layout.activity_explorer);
        dbHelper=new DBHelper(this);

        uris.add(urlOfVideo);

        generateNoteOnSD(this, "sample", String.valueOf(lastPosition)+" "+filename+" "+type);
        //generateNoteOnSD( this, "filename", filename);

        //Toast.makeText(this, readFile(), Toast.LENGTH_SHORT).show();

        progress_text=Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Notes/sample"));

        uris.add(progress_text);

        Intent shareIntent = new Intent(this, ShareActivity.class);
        shareIntent.setAction("android.intent.action.SEND_MULTIPLE");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivityForResult(shareIntent, SHARE_REQUEST);

//        if(savedInstanceState == null) {
//            //showDirectory(null, true);
//            Toast.makeText(this, getText(R.string.activity_explorer_hint),
//                    Toast.LENGTH_SHORT).show();
//        }
    }

//    @Override
//    protected void onResume() {
//
//        // (Re)initialize the list of directories
//        mDirectories = new ArrayList<>();
//
//        // Finding the path to the storage directories is very difficult - for
//        // API 19+, use the getExternalFilesDirs() method and extract the path
//        // from the app-specific path returned; older devices cannot access
//        // removably media :(
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            // Enumerate all of the storage directories
//            File files[] = getExternalFilesDirs(null);
//            for (int i = 0; i < files.length; ++i) {
//
//                // If the directory is usable, retrieve its path
//                if (files[i] == null) {
//                    continue;
//                }
//                String path = files[i].getAbsolutePath();
//
//                // The path should contain Android/data and the portion of the
//                // path preceding that is the root (a hack, but it works)
//                int rootIndex = path.indexOf("Android/data");
//                if (rootIndex == -1) {
//                    continue;
//                }
//                path = path.substring(0, rootIndex);
//
//                // Assume that the first directory is for internal storage and
//                // the others are removable (either card slots or USB OTG)
//
//                Log.i(TAG, String.format("found storage directory: \"%s\"", path));
//
//                mDirectories.add(new StorageDirectory(
//                        i == 0 ?
//                                getString(R.string.activity_explorer_internal) :
//                                getString(R.string.activity_explorer_removable, new File(path).getName()),
//                        path
//                ));
//            }
//        }
//
//        // Invoke the parent method
//        super.onResume();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_explorer_options, menu);
//
//        // Create menu options for each of the storage directories
//        if (mDirectories.size() > 1) {
//            for (int i = 0; i < mDirectories.size(); ++i) {
//                menu.add(Menu.NONE, i, Menu.NONE, mDirectories.get(i).name());
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        switch (itemId) {
//            case R.id.option_show_hidden:
//                mShowHidden = !item.isChecked();
//                item.setChecked(mShowHidden);
//                mFragment.showHidden(mShowHidden);
//                return true;
//        }
//        if (itemId < mDirectories.size()) {
//            showDirectory(mDirectories.get(itemId).path(), true);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBrowseDirectory(String directory) {
//        showDirectory(directory, false);
//    }
//
//    @Override
//    public void onSendUris(ArrayList<Uri> uris) {
//        Intent shareIntent = new Intent(this, ShareActivity.class);
//        shareIntent.setAction("android.intent.action.SEND_MULTIPLE");
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        startActivityForResult(shareIntent, SHARE_REQUEST);
//    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateNoteOnSDFileName(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFile() {
        File fileEvents = new File(Environment.getExternalStorageDirectory()+"/Notes/sample");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        return result;
    }
    public void readNote(){
        //Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard,"file.txt");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        Toast.makeText(this, text.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHARE_REQUEST) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
