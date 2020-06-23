package net.progresstransformer.android.viewData.ViewPdf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import net.progresstransformer.android.R;
import net.progresstransformer.android.viewData.Database.DBHelper;
import net.progresstransformer.android.viewData.VideoLoder.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPdf extends AppCompatActivity {
    private DBHelper dbHelper;
    private Uri urlOfPdf;
    private PDFView pdfView;
    private int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        dbHelper = new DBHelper(this);


        Intent intent = getIntent();
        int positionOfArray = intent.getIntExtra("uri", 0);
        urlOfPdf = Uri.fromFile(Constant.allpdfList.get(positionOfArray));
        pdfView = (PDFView) findViewById(R.id.pdfView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start the pdf at the begining?");
        builder.setCancelable(false);

        ArrayList<HashMap<String, String>> progressAttay2 = dbHelper.getPdfData(String.valueOf(urlOfPdf));

        if (progressAttay2.isEmpty()) {
            lastPage = pdfView.getCurrentPage();
            dbHelper.insertPdfData(Constant.allMediaList.get(positionOfArray).getName(), String.valueOf(urlOfPdf), String.valueOf(lastPage));
            //Constant.allpdfSendToDB.add(String.valueOf(urlOfPdf));
        } else {
//            ArrayList<HashMap<String, String>> progressAttay=dbHelper.getPdfData(String.valueOf(urlOfPdf));
//            lastPage=Integer.parseInt(progressAttay.get(0).get("progress"));
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lastPage = pdfView.getCurrentPage();
                    dbHelper.updatePdfPageNumber(String.valueOf(pdfView.getCurrentPage()), String.valueOf(urlOfPdf));
                    viewPdf();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<HashMap<String, String>> progressAttay = dbHelper.getPdfData(String.valueOf(urlOfPdf));
                    lastPage = Integer.parseInt(progressAttay.get(0).get("progress"));
                    viewPdf();
                    dialog.cancel();
                }
            });
            builder.show();
        }
//        viewPdf();
//        pdfView.fromUri(urlOfPdf)
//                .defaultPage(lastPage)
//                .enableSwipe(true)
//        .swipeHorizontal(false)
//        .onDraw(new OnDrawListener() {
//            @Override
//            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//
//            }
//        }).onPageChange(new OnPageChangeListener() {
//            @Override
//            public void onPageChanged(int page, int pageCount) {
//
//                dbHelper.updatePdfPageNumber(String.valueOf(pdfView.getCurrentPage()), String.valueOf(urlOfPdf));
//
//            }
//        }).enableAnnotationRendering(true)
//        .load();
    }

    private void viewPdf() {
        pdfView.fromUri(urlOfPdf)
                .defaultPage(lastPage)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                }).onPageChange(new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {

                dbHelper.updatePdfPageNumber(String.valueOf(pdfView.getCurrentPage()), String.valueOf(urlOfPdf));

            }
        }).enableAnnotationRendering(true)
                .load();
    }
}
