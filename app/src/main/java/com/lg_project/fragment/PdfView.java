package com.lg_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.lg_project.R;
import com.lg_project.activity.MainActivity2;
import com.lg_project.fragment.ShowMenu;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class PdfView extends Fragment implements OnPageChangeListener, OnLoadCompleteListener {

    // PDFView pdfView;
    File dir;
    ImageView back;
    PDFView pdfView;
    Integer pageNumber = 0;
    TextView titletxt;
    String path="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pdf_view, container, false);


//        MainActivity2.l1.setVisibility(View.GONE);
         path = getArguments().getString("path",null);
         Log.e("path",path);
         String title = getArguments().getString("title",null);
         Log.e("title",title);



        back=(ImageView)v.findViewById(R.id.back);
        pdfView= (PDFView)v.findViewById(R.id.main_pdf_view);
        titletxt=(TextView)v.findViewById(R.id.login_text);

        titletxt.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

//                ShowMenu menu=new ShowMenu() ;
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.lay_pdf_menu,menu).commit();
                }

        });



      /*  pdfView=(PDFView)v.findViewById(R.id.activity_main_pdf_view);
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.example.avipdf_pro/files/My_PDF_Folder/");
       // Log.d("OOOO",dir+value+".pdf");
        File listFile[]= dir.listFiles();
        Log.d("VAJHG",dir+"/"+value+".pdf");
        pdfView.fromFile(dir+"/"+value+".pdf");
        pdfView.setPadding(10,10,10,10);
        pdfView.setZoomEnabled(true);
        pdfView.show();
*/
//        pdfView.fromUri(Uri.parse(path));

//        pdfView.fromAsset(path).load();
//        pdfView.fromUri(Uri.parse(path)).load();

        init(v);


        return v;
    }
    private void init(View v){

        // position = getIntent().getIntExtra("position",-1);
//        displayFromSdcard();

        new RetrivePDFfromUrl().execute(path);
    }
    private void displayFromSdcard() {
        //get file name
//        pdfFileName = value;
//        dir = new File(Environment.getExternalStorageDirectory()+"/My_PDF_Folder/");
        // Log.d("OOOO",dir+value+".pdf");
//        File listFile[]= dir.listFiles();
//        Log.d("VAJHG", ppath);
        //get complete file
        //pdfView.fromFile(new File(dir + "/" + value + ".pdf"))
        pdfView.fromUri(Uri.parse(path))
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                // .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .load();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        //  printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(path);
                // below is the step where we are
                // creating our connection.

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }

    public void intennt(File outputFile)
    {
        Uri uri = Uri.fromFile(outputFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share"));
    }
}