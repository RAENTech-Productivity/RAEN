/*
Copyright 2018 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.google.devrel.ar.codelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gamesparks.sdk.GSEventConsumer;
import com.gamesparks.sdk.android.GSAndroidPlatform;
import com.gamesparks.sdk.api.GSData;
import com.gamesparks.sdk.api.autogen.GSResponseBuilder;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ArMainActivity extends AppCompatActivity {

    private ArFragment fragment;
    private PointerDrawable pointer = new PointerDrawable();
    private boolean isTracking;
    private boolean isHitting;

    private float lat;
    private float lon; //maybe need to be global... we'll see
    float [] closestNotes = new float[10];
    String [] closestNotesMessages = new String[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            GSAndroidPlatform.gs().getRequestBuilder().createLogEventRequest()
                    .setEventKey("LOAD_MESSAGE")
                    .send(new GSEventConsumer<GSResponseBuilder.LogEventResponse>() {
                        @Override
                        public void onEvent(GSResponseBuilder.LogEventResponse logEventResponse) {
                            if (!logEventResponse.hasErrors()) {
                                //DO something
                                Log.i("GOTHERE", "the AR connection worked");
                                GSData scriptData = logEventResponse.getScriptData();
                                Map data = scriptData.getBaseData();
                                Log.i("GOTHERE", "Map" + data);

                                String notes = data.toString();
                                String note_lon = "\"messLon\"";
                                String note_lat = "\"messLat\"";
                                String note_mess = "\"messText\"";
                                float latitude;
                                float longitude;
                                String renderable_message;

                                for (int i = 0; i < 5; i++) {
                                    int second_colon = notes.indexOf(note_lon) + note_lon.length();
                                    int first_quote = second_colon + 1;
                                    int second_quote = notes.indexOf("\"", first_quote + 1);

                                    if (notes.substring(first_quote + 1, second_quote) != null && !notes.substring(first_quote + 1, second_quote).equals("")) {
                                        longitude = Float.valueOf(notes.substring(first_quote + 1, second_quote));
                                    } else {
                                        longitude = 33333;
                                    }


                                    second_colon = notes.indexOf(note_lat) + note_lat.length();
                                    first_quote = second_colon + 1;
                                    second_quote = notes.indexOf("\"", first_quote + 1);

                                    if (notes.substring(first_quote + 1, second_quote) != null && !notes.substring(first_quote + 1, second_quote).equals("")) {
                                        latitude = Float.valueOf(notes.substring(first_quote + 1, second_quote));
                                    } else {
                                        latitude = 33333;
                                    }

                                    if (latitude != 3333 && longitude != 3333) {
                                        closestNotes[i] = latitude;
                                        closestNotes[i + 1] = longitude;
                                    }


                                    second_colon = notes.indexOf(note_mess) + note_mess.length();
                                    first_quote = second_colon + 1;
                                    second_quote = notes.indexOf("\"", first_quote + 1);

                                    if (notes.substring(first_quote + 1, second_quote) != null && !notes.substring(first_quote + 1, second_quote).equals("")) {
                                        renderable_message = notes.substring(first_quote + 1, second_quote);
                                    } else {
                                        renderable_message = "RAENTECH DEFAULT MESSAGE";
                                    }

                                    closestNotesMessages[i] = renderable_message;
                                    Log.i("GOTHERE MESSAGES", "" + closestNotesMessages[i]);

//                               PLEASE KEEP THESE PRINT STATEMENTS FOR FUTURE TESTING!!!

//                               Log.i("GOTHERE", "ARRAY" + closestNotes[0]);
//                                Log.i("GOTHERE", "2nd colon lat" + second_colon );
//                                Log.i("GOTHERE", "1st quote lat" + first_quote );
//                                Log.i("GOTHERE", "2nd quote lat" + second_quote );
//                                Log.i("GOTHERE", "float long" + longitude);
//                               Log.i("GOTHERE", "float lat" + latitude);
//                               Log.i("GOTHERE", "ar message" + renderable_message);

                                    Log.i("GOTHERE", "ar message" + renderable_message);

                                    notes = notes.substring(second_colon);
                                    notes = notes.substring(notes.indexOf("messLon") - 1);
                                }


                                Log.i("GOTHEREDATA", "AR completed event");
                            }

                        }
                    });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { takePhoto(); }
        });
        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        fragment.getArSceneView().getScene().setOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });

//        closestNotesMessages[0] = "hello";
//   where initializeGallery was
        initializeGallery();
        Log.i("GOTHERE MESSAGE", "" + closestNotesMessages);
//        initializeGallery();
        Log.i("GOTHERE", "the end of AR onCreate");
    }

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = fragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(ArMainActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(ArMainActivity.this,
                            ArMainActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(ArMainActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private void onUpdate(){
        boolean trackingChanged = updateTracking();
        View contentView = findViewById(android.R.id.content);
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer);
            } else {
                contentView.getOverlay().remove(pointer);
            }
            contentView.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
                contentView.invalidate();
            }
        }
    }
    private boolean updateTracking(){
        Frame frame = fragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }
    private boolean updateHitTest(){
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if ((trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose()))) {
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private android.graphics.Point getScreenCenter(){
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        //Once we get the message to write to a PNG:
        //createPNG(closestNotesMessages[0]);
        //int noteMessage = R.drawable.message_thumb; //need message to be placed in drawable with name "message_thumb.png"

        //int noteMessage = R.drawable.sticky_thumb;
        ImageView andy = new ImageView(this);
        andy.setImageResource(R.drawable.droid_thumb);
        andy.setContentDescription("andy");
        andy.setOnClickListener(view ->{addObject( Uri.parse("andy.sfb"));});
        gallery.addView(andy);

        ImageView sticky = new ImageView(this);
        sticky.setImageResource(R.drawable.sticky_thumb);
//        sticky.setImageResource(noteMessage);
        sticky.setContentDescription("sticky note");
        sticky.setOnClickListener(view ->{addObject(Uri.parse("model.sfb"));});
        gallery.addView(sticky);


//        ImageView user_note = new ImageView(this);
//        user_note.setImageResource(noteMessage);
//        user_note.setContentDescription("sticky note with message");
//        user_note.setOnClickListener(view ->{addObject(Uri.parse("model.sfb"));});
//        gallery.addView(user_note);

    }
    private void addObject(Uri model) {
        Frame frame = fragment.getArSceneView().getArFrame();
        Point pt = getScreenCenter();

        if (closestNotes != null) {       //this one brings up the stored lat and lon
            List<HitResult> hits;
            if (frame != null) {
                for (int i = 0; i >= 10; i = i + 2) {
                    hits = frame.hitTest(closestNotes[i], closestNotes[i + 1]);
                    Log.i("GOTHEREDATA", "Closest Note Lat: "+ closestNotes[i]);
                    Log.i("GOTHEREDATA", "Closest Note Lon: "+ closestNotes[i+1]);
                    for (HitResult hit : hits) {
                        Trackable trackable = hit.getTrackable();
                        if ((trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose()))) {
                            placeObject(fragment, hit.createAnchor(), model);
                            break;
                        }
                    }
                }
            }
        }


            List<HitResult> hits;

            if
            (frame != null) {
                hits = frame.hitTest(pt.x, pt.y);

                for (HitResult hit : hits) {

                    Trackable trackable = hit.getTrackable();

                    if
                    ((trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose()))) {

                        placeObject(fragment, hit.createAnchor(), model);

                        break;

                    }

                }

            }
//        }
        }

    private void placeObject(ArFragment fragment, Anchor anchor, Uri model) {
        CompletableFuture<Void> renderableFuture =
                ModelRenderable.builder()
                        .setSource(fragment.getContext(), model)
                        .build()
                        .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                        .exceptionally((throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage())
                                    .setTitle("Error!");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return null;
                        }));
    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }


    private void createPNG(String text) {
//        String text = "This \nis \nmultiline";
//        Log.i("GOTHERE MESSAGE", text);

        final Rect bounds = new Rect();
        TextPaint textPaint = new TextPaint() {
            {
                setColor(Color.WHITE);
                setTextAlign(Paint.Align.LEFT);
                setTextSize(20f);
                setAntiAlias(true);
            }
        };
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int maxWidth = -1;
        for (int i = 0; i < mTextLayout.getLineCount(); i++) {
            if (maxWidth < mTextLayout.getLineWidth(i)) {
                maxWidth = (int) mTextLayout.getLineWidth(i);
            }
        }
        final Bitmap bmp = Bitmap.createBitmap(maxWidth, mTextLayout.getHeight(),
                Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.BLACK); //just adding black background
        final Canvas canvas = new Canvas(bmp);
        mTextLayout.draw(canvas);

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "message_thumb.png");


        try {
            if( !file.exists() ){
                file.createNewFile();
            }

        } catch (IOException e) {

        } finally {

           try {
               FileOutputStream stream = new FileOutputStream(file); //create your FileOutputStream here
               bmp.compress(Bitmap.CompressFormat.PNG, 85, stream);
               bmp.recycle();
               try {
                   stream.close();
               } catch (IOException e) {
                   //
               }
           }
           catch (FileNotFoundException e1) {
               // put print statement
           }

        }


    }


}


