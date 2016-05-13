package com.example.bitjini.multiselectlistexample;

/**
 * Created by bitjini on 29/4/16.
 */
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.FragmentManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.bitjini.multiselectlistexample.R.id.contactimage;
import static com.example.bitjini.multiselectlistexample.R.id.image;

public class ContactsAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<ContactObject> mainDataList = null;
    private ArrayList<ContactObject> arraylist;
    public ContactsAdapter(Context context, List<ContactObject> mainDataList) {

        mContext = context;
        this.mainDataList = mainDataList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ContactObject>();
        this.arraylist.addAll(mainDataList);
    }

    static class ViewHolder {
        protected TextView name;
        protected TextView number;
        protected ImageView image;
    }

    @Override
    public int getCount() {
        return mainDataList.size();
    }

    @Override
    public ContactObject getItem(int position) {
        return mainDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_row, null);
            holder.name = (TextView) view.findViewById(R.id.contactname);
            holder.number = (TextView) view.findViewById(R.id.contactno);
            holder.image = (ImageView) view.findViewById(contactimage);
            view.setTag(holder);
            view.setTag(R.id.contactname, holder.name);
            view.setTag(R.id.contactno, holder.number);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(mContext, Popup.class);
//                mContext.startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap b1=phoneImage(mainDataList.get(position).getImage());

                Intent intent = new Intent(mContext, AnotherActivity.class);
                System.out.println("Name ======"+mainDataList.get(position).getName());
                intent.putExtra("name",mainDataList.get(position).getName().toString());

                Bundle extras=new Bundle();
                extras.putParcelable("photo",b1);
                intent.putExtras(extras);

                intent.putExtra("number",mainDataList.get(position).getNumber());
                mContext.startActivity(intent);
            }
        });
        holder.name.setText(mainDataList.get(position).getName());
        holder.number.setText(mainDataList.get(position).getNumber());

        if(getByteContactPhoto(mainDataList.get(position).getImage())==null){
            String text=mainDataList.get(position).getName();
            char t1=text.charAt(0);
            CharacterDrawable drawable=new CharacterDrawable(t1);
            holder.image.setImageDrawable(drawable);
        }else{
            holder.image.setImageBitmap(getByteContactPhoto(mainDataList.get(position).getImage()));
        }
        return view;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mainDataList.clear();
        if (charText.length() == 0) {
            mainDataList.addAll(arraylist);
        } else {
            for (ContactObject wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mainDataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    public Bitmap getByteContactPhoto(String contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri, new String[] {Contacts.Photo.DATA15}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                Bitmap image= BitmapFactory.decodeStream( new ByteArrayInputStream(data));
                //   return image;
                return getRoundedCornerBitmap(image);
            }
        } finally {
            cursor.close();
        }
        return null;
    }
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,96,96);
        System.out.println("getWidth : "+bitmap.getWidth());
        System.out.println("getheight : "+bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 50;

        paint.setAntiAlias(true);
        //     canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.CYAN);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //    System.out.println("Bitmap"+bitmap+"rect"+rect+"rect"+rect+"paint"+paint);

        return output;
    }
    public Bitmap phoneImage(String contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri, new String[] {Contacts.Photo.DATA15}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                Bitmap image= BitmapFactory.decodeStream( new ByteArrayInputStream(data));
                image=BITMAP_RESIZER(image,270,200);
                return (image);
            }
        } finally {
            cursor.close();
        }
        return null;
    }
    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight /(float) bitmap.getHeight();
        float middleX = newWidth/ 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() /2, middleY - bitmap.getHeight()/2 , new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }
}
class CharacterDrawable extends ColorDrawable {

    private final char character;
    private final Paint textPaint;
    private final Paint borderPaint;

    public CharacterDrawable(char character) {
        //super(color);
        this.character = character;
        this.textPaint = new Paint();
        this.borderPaint = new Paint();
        // text paint settings

        textPaint.setAntiAlias(true);
        //  textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        borderPaint.setStrokeWidth(5);
        String string="#4FC3F7";
        int color=Color.parseColor(string);
        borderPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final Rect rect = new Rect(10,10, 86, 86);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, 50, 50, borderPaint);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);

        int width = canvas.getWidth();
        System.out.println("New width"+width);
        int height = canvas.getHeight();
        System.out.println("New height"+height);
        textPaint.setTextSize(height / 2);

        canvas.drawText(String.valueOf(character).toUpperCase(), width/2, height/2 - ((textPaint.descent() + textPaint.ascent()) / 2) , textPaint);
        // System.out.println("String"+String.valueOf(character).toUpperCase()+"width"+width/2+"Height"+(height/2 - ((textPaint.descent() + textPaint.ascent()) / 2))+"textPaint"+textPaint);
    }
}