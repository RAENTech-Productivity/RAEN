package com.google.devrel.ar.codelab;

import android.os.Environment;

import com.google.devrel.ar.codelab.Day;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FilingStuff {
    private String _completePath;

    //Constructor
    public FilingStuff(){
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard + "/kalendar");
        directory.mkdirs();
        File file =  new File(directory, "Schedule.ser");
        String path = file.getAbsolutePath();
        _completePath = path;
    }

    /**
     * Deserializes an object
     * @return ArrayList<Day> the previously saved entries
     */
    public ArrayList<Day> deserializeObject(){
        ArrayList<Day> list = new ArrayList<>();

        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(_completePath));
            list = (ArrayList<Day>) objectIn.readObject();
            objectIn.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Serializes an object
     * @param objToSerialize - object to be serialized
     */
    public void serializeObject(Object objToSerialize){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(_completePath));
            outputStream.writeObject(objToSerialize);
            outputStream.close();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(bos);
            outputStream.writeObject(objToSerialize);
            outputStream.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
