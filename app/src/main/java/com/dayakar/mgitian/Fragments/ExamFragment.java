package com.dayakar.mgitian.Fragments;


import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayakar.mgitian.Adapters.File_Adapter;
import com.dayakar.mgitian.Interfaces.OnDownloadListener;
import com.dayakar.mgitian.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExamFragment extends Fragment implements OnDownloadListener {
    private   String fragment_name,file_share_path,file_share_name;
    private TextView networkstatus;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ImageView networkImage;
    private ArrayList<String> file_names=new ArrayList<>();
    private ArrayList<String> file_links=new ArrayList<>();
    private boolean isItemSelected=false;
    private static final String TAG = "Academic.class";
    private String download_link;
    private File_Adapter adapter;
    private ImageView books;


    public ExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_exam_online, container, false);
        fragment_name=this.getArguments().getString("fragment_title");
          setUpUI(v);
          setHasOptionsMenu(true);
              if(fragment_name.equalsIgnoreCase("Online files")){
                  if (checkingConnectionStatus()) {
                      mProgressBar.setVisibility(View.VISIBLE);
                      load_Online_Ebook_files();
                  }

              }else{
                  if (checkingConnectionStatus()) {

                      mProgressBar.setVisibility(View.VISIBLE);
                      load_Online_Exam_files();
                  }



     }
        return v;
    }
    private boolean checkingConnectionStatus(){

        ConnectivityManager
                cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {

            return true;
        }else {
            networkstatus.setText("No internet Connection...");
            networkImage.setVisibility(View.VISIBLE);
            return false;


        }
    }
    private void setUpUI(View v){
        networkstatus=v.findViewById(R.id.networkTextexam);
        mProgressBar=v.findViewById(R.id.exam_progress);
        mRecyclerView=v.findViewById(R.id.online_exam_recyclerview);
        books=v.findViewById(R.id.empty_books);
        networkImage=v.findViewById(R.id.networkImage_exam);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }
    private void setUpAdapter(){

         adapter=new File_Adapter(this,getContext(),file_names,file_links,fragment_name);
        mProgressBar.setVisibility(View.INVISIBLE);
        if(adapter.getItemCount()==0){
            books.setVisibility(View.VISIBLE);
            networkstatus.setText("No files found at this moment.");
        }else{
            mRecyclerView.setAdapter(adapter);

        }

    }
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.share_delete_menu,menu);
        MenuItem item=menu.findItem(R.id.item_share);
        if(isItemSelected){
            item.setVisible(true);
        }else item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void load_Online_Exam_files() {


        //colleges/MGIT/exams
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/exams");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                file_names.clear();
                file_links.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    file_names.add(postSnapshot.getKey());
                    file_links.add(postSnapshot.getValue().toString());
                }
                setUpAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void load_Online_Ebook_files() {
        //colleges/MGIT/exams
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("colleges/MGIT/ebooks");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                file_names.clear();
                file_links.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        file_names.add(postSnapshot.getKey());
                        file_links.add(postSnapshot.getValue().toString());
                    }
                    setUpAdapter();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onDownload(String file,String name) {
        download_link = file;
        if(isFile_Exist(name)) {
            Toast.makeText(getContext(), "File alredy exist", Toast.LENGTH_SHORT).show();
        }else {
                startDownloadfile(file);
            }

    }

    @Override
    public void onFileOpen(String path, String name) {
        open_file(path);


    }

    @Override
    public void onItemSelected(String filePath, String name) {
        file_share_path=filePath;
        file_share_name=name;
        isItemSelected=true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            if (file_share_path != null) {
                shareItem(file_share_path);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDownloadfile(String file_link) {

        Toast.makeText(getContext(), "Download started", Toast.LENGTH_SHORT).show();
        // holder.download_progress.setVisibility(View.VISIBLE);
        String nameofFile = URLUtil.guessFileName(file_link,
                null, MimeTypeMap.getFileExtensionFromUrl(file_link));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file_link));

        request.setTitle(nameofFile);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                   /* request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, nameofFile);

                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    dm.enqueue(request); */

        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/MGIT");

            if (!Dir.exists()) {

                Dir.mkdir();
            }

            File inDir = new File(Dir.getAbsolutePath() + "/Documents");
            if (!inDir.exists()) {

                inDir.mkdir();
            } else {

            }


        } else {


            Toast.makeText(getContext(), "SD card not found", Toast.LENGTH_SHORT).show();
        }
        //  request.setDestinationInExternalFilesDir(context,"/",nameofFile);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){

            request.setDestinationInExternalFilesDir(getContext(),"Documents",nameofFile);
        }else{
            request.setDestinationInExternalPublicDir("MGIT/Documents", nameofFile);

        }
        Log.d("Exam fragment", file_link);
        DownloadManager dms = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadID = dms.enqueue(request);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == reference) {
                    // Do something with downloaded file.
                    Toast.makeText(context, "download completed click open file", Toast.LENGTH_SHORT).show();
                    if(adapter.getItemCount()!=0){
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
        getContext().registerReceiver(receiver, filter);

    }

    private boolean isFile_Exist(String file_name){
        boolean found=false;
        String new_file=file_name+".pdf";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            String root=getContext().getExternalFilesDir(null).getAbsolutePath();
            File dir=new File(root+"/Documents");
            if(dir.exists()){
                for(File single:dir.listFiles()) {
                    if (single.getName().equals(new_file)) {
                        found = true;

                    }
                }}


        }else{
            File root = Environment.getExternalStorageDirectory();
            File path = new File(root.getAbsolutePath() + "/MGIT/Documents");
            if(path.exists()){
                for(File single:path.listFiles()){
                    if(single.getName().equals(new_file)){
                        found=true;

                    }
                }

            }}


        return found;
    }

    private void open_file(String filePath) {

        File file = new File(filePath);
        try {
        Uri pdfuri = FileProvider.getUriForFile(getContext(), "com.dayakar.mgitian.provider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);

        target.setDataAndType(pdfuri, "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");


            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No application found to open this file", Toast.LENGTH_SHORT).show();
        }
    }
    private void shareItem(String path){
        File outputFile = new File(path);
        Uri uri = FileProvider.getUriForFile(getContext(), "com.dayakar.classroom.provider",outputFile);
        Intent share = new Intent();
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        getContext().startActivity(share);
    }




}
