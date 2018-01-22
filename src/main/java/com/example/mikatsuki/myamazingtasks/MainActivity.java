package com.example.mikatsuki.myamazingtasks;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ImageSwitcher sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ADD HERE
        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();


        sw = findViewById(R.id.IS);
        sw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.WRAP_CONTENT,
                        ImageSwitcher.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });


        moodChanger();

    }

    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        moodChanger();

    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View item,
                                    int pos,
                                    long id) {
                //Change image to Curious
                sw.setImageResource(R.drawable.curious);
                dialog(pos);


                // Remove the item within array at position
                //items.remove(pos);
                // Refresh the adapter

            }
        });


    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dialog(final int pos) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message);


        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Remove Item
                items.remove(pos);

                //Notifies of change
                itemsAdapter.notifyDataSetChanged();

                // Return true consumes the long click event (marks it handled)
                writeItems(); // <---- Add this line

                //Happy Message
                Context context = getApplicationContext();
                CharSequence text = "Yay!!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //sw.setImageResource(R.drawable.cheers);
                cheers();


                //Changes image back to default
                //sw.setImageResource(R.drawable.happy);
                moodChanger();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                //Changes image back to default
                //sw.setImageResource(R.drawable.happy);
                moodChanger();
            }
        });
        // Set other dialog properties
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

                //sw.setImageResource(R.drawable.happy);
                moodChanger();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();


        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        //wmlp.x = 100;   //x position
        // wmlp.y = 100;   //y position
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setAttributes(wmlp);
        dialog.show();

    }


    public void moodChanger() {

        //Default Image
        //sw.setImageResource(R.drawable.happy);


        float count = lvItems.getCount();
        float halfCount = count / 2;


        if (count <= 2) {
            sw.setImageResource(R.drawable.happy);
        } else if (count <= 4.9) {
            sw.setImageResource(R.drawable.concerned);
        } else if (count >= 5.0) {
            sw.setImageResource(R.drawable.angry);
        } else return;

        Log.d("MyTagGoesHere", "count is" + count);
        Log.d("MyTagGoesHere", "halfCount is" + halfCount);

    }

    public void cheers() {


        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        final ImageView img = findViewById(R.id.cheering);
        img.setBackgroundResource(R.drawable.animation);
        img.setVisibility(View.VISIBLE);
        // Get the background, which has been compiled to an AnimationDrawable object.
        final AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        img.bringToFront();

        // Start the animation (looped playback by default).

        frameAnimation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                frameAnimation.stop();
                img.setVisibility(View.INVISIBLE);



            }
        }, 1800);







           /* sw.postDelayed(new Runnable()
            {
                int i = 0;
                public void run() {
                    sw.setImageResource(
                            i++ % 2 == 1 ?
                                    R.drawable.cheers:
                                    R.drawable.curious);


                    sw.postDelayed(this, 1000);
                }
            }, 1000);*/


    }
}




            /*sw.setImageResource(R.drawable.cheers);
            sw.setImageResource(R.drawable.curious);
            sw.setImageResource(R.drawable.cheers);
            sw.setImageResource(R.drawable.curious);
            Log.d("MyTagGoesHere", "cheers is working?");*/




