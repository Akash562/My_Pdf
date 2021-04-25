package com.ak.my_pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.BuildCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.itextpdf.text.Element;
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

public class MainActivity extends AppCompatActivity {

    EditText ed1;
    MaterialButton g_pdf;

    public String FOLDER_PDF= Environment.getExternalStorageDirectory() + File.separator+"Jowner";
    public String path;

    PdfWriter writer;

    String[] permissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean isSettings = false;

    private static final int PERMISSION_CALLBACK = 111;
    private static final int PERMISSION_REQUEST = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1=findViewById(R.id.ed1);
        g_pdf=findViewById(R.id.g_pdf);

        loadInt();

        g_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_pdf();
            }
        });

    }

    public void create_pdf(){

        String datawrite=ed1.getText().toString();

        path=FOLDER_PDF+File.separator+"Report.pdf";

        File file =new File(path);
        if (file.exists()){
            file.mkdir();
        }

        //   Document document = new Document(PageSize.A4);
        Document document = new Document(PageSize.A4, 36, 36, 36, 72);
        document.addCreationDate();
        document.addAuthor("Jowner");
        document.addCreator("Bill");

        // Location to save
        try {
            writer =PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.open();

        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(50);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        String theName=datawrite;
        String theAddress="Address of person";

        PdfPCell preName = new PdfPCell(new Phrase(theName));
        PdfPCell preAddress = new PdfPCell(new Phrase(theAddress));
        PdfPCell preDate = new PdfPCell(new Phrase("DATE: "+formattedDate));
        PdfPCell preBill=new PdfPCell(new Phrase("No : 0001"));

        preBill.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preBill.setHorizontalAlignment(Element.ALIGN_RIGHT);

        preDate.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preName.setBorder(Rectangle.NO_BORDER);
        preAddress.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);

        try {
            tableHeading.addCell(preName);
            tableHeading.addCell(preBill);
            tableHeading.addCell(preAddress);
            tableHeading.addCell(preDate);
            document.add(tableHeading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.close();

        open_pdf();

    }

    public void open_pdf(){

        File file = new File(path);
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


    public void loadInt(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            ChkPerm();
        }else{
            afterPermission();
        }
    }

    private void afterPermission() {
        File folderPdf=new File(FOLDER_PDF);
        if (!folderPdf.exists()){
            folderPdf.mkdir();
        }else {
        }
    }

    public void ChkPerm(){
        if(forSelfPermission()){

            if(shouldShow()){
                permissionCallBack();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this,permissionList, PERMISSION_CALLBACK);
            }
        } else {
            //You already have the permission, just go ahead.
            afterPermission();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                afterPermission();
            } else if(shouldShow()){

                permissionCallBack();
            } else {

                permissionSettings();
                Toast.makeText(getBaseContext(),"Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean forSelfPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }

    private boolean resultPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
            } else {
                allgranted = false;
                break;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }


    private boolean shouldShow(){

        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionList[i])) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }
    }

    private void permissionCallBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs Multiple permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(MainActivity.this,permissionList, PERMISSION_CALLBACK);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void permissionSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs permission allow them from settings.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isSettings = true;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, PERMISSION_REQUEST);
                // Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("R","result");
        if (requestCode == PERMISSION_REQUEST) {

            if (resultPermission()){
                Log.d("R","result s");
                afterPermission();
            }else{
                Log.d("R","result c");
                ChkPerm();
            }

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isSettings) {
            Log.d("R","resume");
            if (resultPermission()){
                Log.d("R","resume s");
                afterPermission();
            }else{
                Log.d("R","resume c");
                ChkPerm();
            }

            isSettings=false;
        }
    }

}