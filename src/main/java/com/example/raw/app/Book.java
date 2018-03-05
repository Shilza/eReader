package com.example.raw.app;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Book implements Serializable{

    private String name;
    private String filePath;
    private String size;
    private String totalRead;
    private String lastActivity;
    private Bitmap bitmap;

    Book(String name, String filePath, String size, long lastActivity){
        this.name = name;
        this.filePath = filePath;
        this.size = size;
        this.lastActivity = lastActivityTreatment(lastActivity);
        this.totalRead = "0%";
        //coverTreatment();
    }

    String lastActivityTreatment(long lastActivity){
        SimpleDateFormat sdf;

        if(lastActivity+86400000 > (new Date().getTime())) //60*60*24*1000 one day
            sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        else
            sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

        return sdf.format(lastActivity);
    }

    private void coverTreatment(){
        try {
            PdfRenderer rend = new PdfRenderer(ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
            PdfRenderer.Page page = rend.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                    Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            this.bitmap = bitmap;

            rend.close();
            page.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void setLastActivity(long lastActivity){
        this.lastActivity = lastActivityTreatment(lastActivity);
    }

    String getName(){
        return name;
    }

    String getFilePath(){
        return filePath;
    }

    String getSize(){
        return size;
    }

    String getTotalRead(){
        return totalRead;
    }

    String getLastActivity(){
        return lastActivity;
    }

    Bitmap getCover() { return bitmap; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (filePath != null ? !filePath.equals(book.filePath) : book.filePath != null)
            return false;
        if (size != null ? !size.equals(book.size) : book.size != null) return false;
        if (totalRead != null ? !totalRead.equals(book.totalRead) : book.totalRead != null)
            return false;
        if (lastActivity != null ? !lastActivity.equals(book.lastActivity) : book.lastActivity != null)
            return false;
        return bitmap != null ? bitmap.equals(book.bitmap) : book.bitmap == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (totalRead != null ? totalRead.hashCode() : 0);
        result = 31 * result + (lastActivity != null ? lastActivity.hashCode() : 0);
        result = 31 * result + (bitmap != null ? bitmap.hashCode() : 0);
        return result;
    }
}
