package br.edu.ufcg.les142.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.parse.*;

import java.io.ByteArrayOutputStream;

/**
 * Created by lucasmc on 26/01/15.
 */
@ParseClassName("Comentario")
public class Comentario extends ParseObject{


    public void setImage(Bitmap value) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        value.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        // get byte array here
        byte[] bytearray = stream.toByteArray();
        if (bytearray != null) {
            //TODO
            ParseFile file = new ParseFile("IMAGEMDECOMENTARIO".toString()+ "jpg", bytearray);
            try {
                file.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            put("image", file);
        }
    }

    public byte[] getImage() {
        byte[] data = null;
        ParseFile fileObject = getParseFile("image");
        try {
            data = fileObject.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getText() {
        try {
            return fetchIfNeeded().getString("text");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "erro";
    }

    public void setText(String value) {
        put("text", value);
    }

    public String getRelatoID() {
        try {
            return fetchIfNeeded().getString("relatoID");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "erro";
    }

    public void setRelatoID(String value) {
        put("relatoID", value);
    }


    public Boolean hasPhoto() {
        try {
            return fetchIfNeeded().getBoolean("hasPhoto");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setHasPhoto(Boolean hasPhoto) {
        put("hasPhoto", hasPhoto);
    }

    public void setUser(ParseUser value) {
        if (value != null) {
            put("user", value);
        }
    }


    public static ParseQuery<Comentario> getQuery() {
        return ParseQuery.getQuery(Comentario.class);
    }



}
