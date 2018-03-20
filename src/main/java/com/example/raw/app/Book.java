package com.example.raw.app;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Book implements Serializable{

    private String name;
    private String filePath;
    private String size;
    private float totalRead;
    private long lastActivity;
    private Extensions extension;

    Book(String name, String filePath, String size, long lastActivity, Extensions extension){
        this.name = name;
        this.filePath = filePath;
        this.size = size;
        this.lastActivity = lastActivity;
        this.totalRead = 0;
        this.extension = extension;
    }

    private String lastActivityProcessing(long lastActivity){
        SimpleDateFormat sdf;

        if(lastActivity+86400000 > (new Date().getTime())) //60*60*24*1000 one day
            sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        else
            sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

        return sdf.format(lastActivity);
    }

    Bitmap coverTreatment(){
        Bitmap bitmap = null;

        try{
            PdfRenderer rend = new PdfRenderer(ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
            PdfRenderer.Page page = rend.openPage(0);
            bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                    Bitmap.Config.ARGB_4444);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            rend.close();
            page.close();
        }
        catch(Exception e){}

        return bitmap;
    }

    public void setLastActivity(long lastActivity){
        this.lastActivity = lastActivity;
    }

    public void setTotalRead(float totalRead){
        this.totalRead = totalRead;
    }

    public String getName(){
        return name;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getSize(){
        return size;
    }

    public float getTotalRead(){
        return totalRead;
    }

    public String getLastActivity(){
        return lastActivityProcessing(lastActivity);
    }

    public Extensions getExtension() { return extension; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (filePath != null ? !filePath.equals(book.filePath) : book.filePath != null)
            return false;
        if (size != null ? !size.equals(book.size) : book.size != null) return false;
        return extension == book.extension;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        return result;
    }
}
