package com.example.mikatsuki.myamazingtasks;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ImageSwitcher sw;
    int imageIds[] = {R.drawable.cheersa, R.drawable.sagirihappy, R.drawable.concernsa, R.drawable.sagirihopefulstat, R.drawable.angrys1, R.drawable.doorclosed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // ADD HERE
        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        sw = findViewById(R.id.IS);

        sw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                //myView.setScaleType(ImageView.ScaleType.FIT_XY);
                myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myView.setAdjustViewBounds(true);
                myView.setClickable(false);


                myView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT));
                myView.requestFocus();
                return myView;

            }
        });


        moodChanger();

    }


    public void onAddItem(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            //noinspection ConstantConditions
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        EditText etNewItem = findViewById(R.id.etNewItem);
        etNewItem.bringToFront();
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        moodChanger1();

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
                sw.setImageResource(imageIds[3]);
                dialog(pos);

            }
        });


    }

    @SuppressWarnings("unchecked")
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


                cheers();


                //Changes image back to default
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

        @SuppressWarnings("ConstantConditions") WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();


        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setAttributes(wmlp);
        dialog.show();

    }


    public void moodChanger() {

        //Default Image
        float count = lvItems.getCount();
            if (count == 0) {
                sw.setImageResource(imageIds[0]);
            } else if (count <= 3.0) {
                sw.setImageResource(imageIds[1]);
            } else if (count <= 4.0) {
                sw.setImageResource(imageIds[2]);
            } else if (count <= 6.0) {
                sw.setImageResource(imageIds[4]);
            } else if (count == 7.0) {
                sw.setImageResource(imageIds[5]);
            } else if (count >= 8.0) {

                sw.setImageResource(imageIds[5]);
            }
        }

    //Avoids Angry gif when deleting tasks
    public void moodChanger1() {

        //Default Image
        float count = lvItems.getCount();

        if (count == 0) {
            sw.setImageResource(imageIds[0]);
        } else if (count <= 3.0) {
            sw.setImageResource(imageIds[1]);
        } else if (count <= 5.0) {
            sw.setImageResource(imageIds[2]);
        } else if (count <= 6.0) {
            sw.setImageResource(imageIds[4]);
        } else if (count == 8.0) {
            disappointed();
            sw.setImageResource(imageIds[5]);
        } else if (count >= 8.0) {

            sw.setImageResource(imageIds[5]);
        }
    }

    public void cheers() {
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        final GifImageView img = findViewById(R.id.cheeringView);
        img.setBackgroundResource(R.drawable.cheer);
        img.setVisibility(View.VISIBLE);

        // Get the background, which has been compiled to an GifDrawable object.
        final GifDrawable newGif = (GifDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        newGif.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                newGif.stop();
                img.setVisibility(View.INVISIBLE);


            }
        }, 800);

    }

    public void disappointed() {
        //Angry Message
        Context context = getApplicationContext();
        CharSequence text = "Too many tasks...BAKA";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.

        final GifImageView img1 = findViewById(R.id.madView);
        img1.setBackgroundResource(R.drawable.angrys);
        img1.setVisibility(View.VISIBLE);

        // Get the background, which has been compiled to an GifDrawable object.
        final GifDrawable newGif = (GifDrawable) img1.getBackground();

        // Start the animation (looped playback by default).
        newGif.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                newGif.stop();
                img1.setVisibility(View.INVISIBLE);


            }
        }, 3000);


    }


}









