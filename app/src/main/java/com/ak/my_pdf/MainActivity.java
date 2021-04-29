package com.ak.my_pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.BuildCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText ed1;
    MaterialButton g_pdf;

    Document document;
    PdfWriter writer;
    String getpath;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        ed1=findViewById(R.id.ed1);
        g_pdf=findViewById(R.id.g_pdf);

        g_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_pdf();
            }
        });

    }

    public void create_pdf(){

        getpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file2= new File(getpath,"akash.pdf");

    try {

        //   Document document = new Document(PageSize.A4);
        document = new Document(PageSize.A4, 36, 36, 36, 72);
        document.addCreationDate();
        document.addAuthor("Jowner");
        document.addCreator("Bill");

        // Location to save
        writer =PdfWriter.getInstance(document, new FileOutputStream(file2));
        document.open();

        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(50);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        String theName="GURIMA";
        String theAddress=" Ph #  7743004497";
        PdfPCell preName = new PdfPCell(new Phrase(theName));
        PdfPCell preAddress = new PdfPCell(new Phrase(theAddress));
        PdfPCell preDate = new PdfPCell(new Phrase("DATE   : "+formattedDate));
        PdfPCell preBill=new PdfPCell(new Phrase("Bill.No. : 0001"));
        preName.setBorder(Rectangle.NO_BORDER);
        preAddress.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);
        tableHeading.addCell(preName);
        tableHeading.addCell(preBill);
        tableHeading.addCell(preAddress);
        tableHeading.addCell(preDate);
        document.add(tableHeading);

        PdfPTable tableHeading2 = new PdfPTable(5);
        tableHeading2.setSpacingBefore(30);
        tableHeading2.setWidths(new float[] {3,1.5f,2,1.5f,2});
        PdfPCell headDate = new PdfPCell(new Phrase("Description",boldFont));
        PdfPCell headName = new PdfPCell(new Phrase("Net Wt.",boldFont));
        PdfPCell headDis = new PdfPCell(new Phrase("Tunch+lbr",boldFont));
        PdfPCell headCr = new PdfPCell(new Phrase("Fine",boldFont));
        PdfPCell headDe = new PdfPCell(new Phrase("Amount",boldFont));
        headDate.setPadding(5);
        headDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        headName.setPadding(5);
        headName.setHorizontalAlignment(Element.ALIGN_CENTER);
        headDis.setPadding(5);
        headDis.setHorizontalAlignment(Element.ALIGN_CENTER);
        headCr.setPadding(5);
        headCr.setHorizontalAlignment(Element.ALIGN_CENTER);
        headDe.setPadding(5);
        headDe.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeading2.addCell(headDate);
        tableHeading2.addCell(headName);
        tableHeading2.addCell(headDis);
        tableHeading2.addCell(headCr);
        tableHeading2.addCell(headDe);
        document.add(tableHeading2);

        PdfPTable tableHeading3 = new PdfPTable(6);
        tableHeading3.setSpacingBefore(0);
        tableHeading3.setWidths(new float[] {.4f,2.6f,1.5f,2,1.5f,2});
        PdfPCell S = new PdfPCell(new Phrase("S"));
        PdfPCell Desc = new PdfPCell(new Phrase("PURE GOLD"));
        PdfPCell nwt = new PdfPCell(new Phrase(" -10.000"));
        PdfPCell tlbr = new PdfPCell(new Phrase("55.50+6.00"));
        PdfPCell fine = new PdfPCell(new Phrase("-6.150"));
        PdfPCell amt = new PdfPCell(new Phrase("-"));
        S.setBorder(Rectangle.NO_BORDER);
        Desc.setBorder(Rectangle.NO_BORDER);
        nwt.setBorder(Rectangle.NO_BORDER);
        tlbr.setBorder(Rectangle.NO_BORDER);
        fine.setBorder(Rectangle.NO_BORDER);
        amt.setBorder(Rectangle.NO_BORDER);
        S.setHorizontalAlignment(Element.ALIGN_LEFT);
        S.setBorder(Rectangle.LEFT|Rectangle.RIGHT);
        Desc.setHorizontalAlignment(Element.ALIGN_LEFT);
        Desc.setBorder(Rectangle.RIGHT);
        nwt.setHorizontalAlignment(Element.ALIGN_RIGHT);
        nwt.setBorder(Rectangle.RIGHT);
        tlbr.setHorizontalAlignment(Element.ALIGN_LEFT);
        tlbr.setBorder(Rectangle.RIGHT);
        fine.setHorizontalAlignment(Element.ALIGN_RIGHT);
        fine.setBorder(Rectangle.RIGHT);
        amt.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amt.setBorder(Rectangle.RIGHT);
        tableHeading3.addCell(S);
        tableHeading3.addCell(Desc);
        tableHeading3.addCell(nwt);
        tableHeading3.addCell(tlbr);
        tableHeading3.addCell(fine);
        tableHeading3.addCell(amt);
        document.add(tableHeading3);

        PdfPTable tableHeading4 = new PdfPTable(6);
        tableHeading4.setSpacingBefore(0);
        tableHeading4.setWidths(new float[] {1,2,1.5f,2,1.5f,2});
        PdfPCell t = new PdfPCell(new Phrase("Total",boldFont));
        PdfPCell Desc1 = new PdfPCell(new Phrase("G:-2.000",boldFont));
        PdfPCell nwt1 = new PdfPCell(new Phrase("S:17.800",boldFont));
        PdfPCell tlbr1 = new PdfPCell(new Phrase("21.360",boldFont));
        PdfPCell fine1 = new PdfPCell(new Phrase("-1.270",boldFont));
        PdfPCell amt1 = new PdfPCell(new Phrase("3560",boldFont));

        t.setBorder(Rectangle.NO_BORDER);
        Desc1.setBorder(Rectangle.NO_BORDER);
        nwt1.setBorder(Rectangle.NO_BORDER);
        tlbr1.setBorder(Rectangle.NO_BORDER);

        t.setHorizontalAlignment(Element.ALIGN_LEFT);
        t.setBorder(Rectangle.LEFT|Rectangle.TOP|Rectangle.BOTTOM);

        Desc1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Desc1.setBorder(Rectangle.TOP|Rectangle.BOTTOM);

        nwt1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        nwt1.setBorder(Rectangle.TOP|Rectangle.BOTTOM);

        tlbr1.setHorizontalAlignment(Element.ALIGN_LEFT);
        tlbr1.setBorder(Rectangle.TOP|Rectangle.BOTTOM);

        fine1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amt1.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tableHeading4.addCell(t);
        tableHeading4.addCell(Desc1);
        tableHeading4.addCell(nwt1);
        tableHeading4.addCell(tlbr1);
        tableHeading4.addCell(fine1);
        tableHeading4.addCell(amt1);
        document.add(tableHeading4);

        PdfPTable tableHeading5 = new PdfPTable(4);
        tableHeading5.setSpacingBefore(0);
        tableHeading5.setWidths(new float[] {3,3.5f,1.5f,2});

        PdfPCell Desc2 = new PdfPCell(new Phrase("MR PURE GOLD"));
        PdfPCell nwt2 = new PdfPCell(new Phrase("-20.000 100.00"));
        PdfPCell fine2 = new PdfPCell(new Phrase("-20.000"));
        PdfPCell amt2 = new PdfPCell(new Phrase("-"));

        Desc2.setBorder(Rectangle.NO_BORDER);
        nwt2.setBorder(Rectangle.NO_BORDER);
        fine2.setBorder(Rectangle.NO_BORDER);
        amt2.setBorder(Rectangle.NO_BORDER);

        Desc2.setHorizontalAlignment(Element.ALIGN_LEFT);
        Desc2.setBorder(Rectangle.LEFT|Rectangle.RIGHT);

        nwt2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        nwt2.setBorder(Rectangle.RIGHT);

        fine2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        fine2.setBorder(Rectangle.RIGHT);

        amt2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amt2.setBorder(Rectangle.RIGHT);

        tableHeading5.addCell(Desc2);
        tableHeading5.addCell(nwt2);
        tableHeading5.addCell(fine2);
        tableHeading5.addCell(amt2);
        document.add(tableHeading5);


        PdfPTable tableHeading6 = new PdfPTable(4);
        tableHeading6.setSpacingBefore(0);
        tableHeading6.setWidths(new float[] {3,3.5f,1.5f,2});

        PdfPCell cbal = new PdfPCell(new Phrase("Cl. Balance",boldFont));
        PdfPCell gr = new PdfPCell(new Phrase("20.060",boldFont));
        PdfPCell fin = new PdfPCell(new Phrase("-18.270",boldFont));
        PdfPCell tamt = new PdfPCell(new Phrase("8460",boldFont));

        cbal.setBorder(Rectangle.NO_BORDER);
        gr.setBorder(Rectangle.NO_BORDER);

        cbal.setHorizontalAlignment(Element.ALIGN_LEFT);
        cbal.setBorder(Rectangle.LEFT|Rectangle.TOP|Rectangle.BOTTOM);

        gr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        gr.setBorder(Rectangle.TOP|Rectangle.BOTTOM);

        fin.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tamt.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tableHeading6.addCell(cbal);
        tableHeading6.addCell(gr);
        tableHeading6.addCell(fin);
        tableHeading6.addCell(tamt);
        document.add(tableHeading6);

        PdfPTable tableHeading7 = new PdfPTable(3);
        tableHeading7.setSpacingBefore(0);
        tableHeading7.setWidths(new float[] {6.5f,1.5f,2});

        PdfPCell dr = new PdfPCell(new Phrase("Dr.",boldFont));
        PdfPCell cr = new PdfPCell(new Phrase("Cr.",boldFont));
        PdfPCell dr2 = new PdfPCell(new Phrase("Dr.",boldFont));

        dr.setBorder(Rectangle.NO_BORDER);
        cr.setBorder(Rectangle.NO_BORDER);
        dr2.setBorder(Rectangle.NO_BORDER);

        dr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        dr2.setHorizontalAlignment(Element.ALIGN_RIGHT);

        tableHeading7.addCell(dr);
        tableHeading7.addCell(cr);
        tableHeading7.addCell(dr2);
        document.add(tableHeading7);


        PdfPTable tableHeading8 = new PdfPTable(1);
        tableHeading8.setSpacingBefore(40);
        tableHeading8.setWidths(new float[] {10});
        PdfPCell sign = new PdfPCell(new Phrase("Authorised Signatory",boldFont));
        sign.setBorder(Rectangle.NO_BORDER);
        sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableHeading8.addCell(sign);
        document.add(tableHeading8);


        document.close();

        try{
            open_pdf();
        }catch (Exception e){
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        }

        }
        catch(Exception e){
            Log.d("tag","-------------------->"+e);
        }


    }

    public void open_pdf(){

        File file = new File(getpath,"akash.pdf");
        Uri pdfURI= FileProvider.getUriForFile(MainActivity.this, "com.ak.my_pdf" + ".provider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(pdfURI,"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.d("Tag","----------------->"+e);
        }
    }

    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storage2= ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storage2 != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}

