package com.bragitoff.crystalviewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    WebView webview;
    EditText h;
    EditText k;
    EditText l;
    Button latticePlaneButton;
    String filePath="Silicon.cif";
    String semiPath;
    String html;
    Spinner spinner1;
    Spinner spinner2;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title=(TextView)findViewById(R.id.titleText);

        spinner1 = (Spinner) findViewById(R.id.spinner_search1);
        spinner2 = (Spinner) findViewById(R.id.spinner_search2);
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.crystals, android.R.layout.simple_spinner_item);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        h=(EditText)findViewById(R.id.h);
        k=(EditText)findViewById(R.id.k);
        l=(EditText)findViewById(R.id.l);

        latticePlaneButton=(Button)findViewById(R.id.latticePlane);

        webview=(WebView)findViewById(R.id.web);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:///android_asset/jsmolt.html");
        //webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
        //webview.loadUrl("http://rruff.geo.arizona.edu/AMS/viewJmol.php?id=10548");
        webview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void readFile(View view){
        if (isStoragePermissionGranted()){
            /*
            Intent intent = new Intent();
            intent.setType("text/csv");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent,"Select CSV "), 33);
            */
            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.cif$")) // Filtering files and directories by file name using regexp
                    //.withFilterDirectories(true) // Set directories filterable (false by default)
                    //.withHiddenFiles(true) // Show hidden files and folders
                    .withRootPath(Environment.getExternalStorageDirectory().toString())
                    .start();

        }
        else if(Build.VERSION.SDK_INT < 23){
            Toast.makeText(getBaseContext(),"Please enable storage permission from settings",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            //read
            /*html="<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<title>A simple Jsmol example</title>\n" +
                    "<head>\n" +
                    "<script type=\"text/javascript\" src=\"JSmol.min.js\"></script>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<script type=\"text/javascript\">\n" +
                    "var Info = {\n" +
                    "  width: 300,\n" +
                    "  height: 300,\n" +
                    "  serverURL: \"jsmol.php \",\n" +
                    "  use: \"HTML5\",\n" +
                    "  j2sPath: \"j2s\",\n" +
                    "  console: \"jmolApplet0_infodiv\"\n" +
                    "}\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "  jmolApplet0 = Jmol.getApplet(\"jmolApplet0\", Info);\n" +
                    "  Jmol.script(jmolApplet0,\"load "+filePath+" packed\")\n" +
                    "</script>\n" +
                    "<br>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spin on')\">spin</a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spin off')\">off</a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill')\">spacefill</a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill 23%;wireframe 0.15\n')\">balls-sticks</a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill off;wireframe 0.15\n')\">wireframe</a>\n" +
                    "</body>";*/
            webview.loadDataWithBaseURL("file:///android_asset/", html(filePath), "text/html", "utf-8", "");
            //filepath is your file's path
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void latticePlane(View view) {
        if (h.getText().toString() == null || h.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(h);
        } else if (k.getText().toString() == null || k.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(k);
        } else if (l.getText().toString() == null || l.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(l);
        } else {
            String h1=h.getText().toString();
            String k1=k.getText().toString();
            String l1=l.getText().toString();
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<title>A simple Jsmol example</title>\n" +
                    "<head>\n" +
                    "<script type=\"text/javascript\" src=\"JSmol.min.js\"></script>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<script type=\"text/javascript\">\n" +
                    "var Info = {\n" +
                    "  width: 270,\n" +
                    "  height: 270,\n" +
                    "  serverURL: \"jsmol.php \",\n" +
                    "  use: \"HTML5\",\n" +
                    "  j2sPath: \"j2s\",\n" +
                    "  console: \"jmolApplet0_infodiv\"\n" +
                    "}\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "  jmolApplet0 = Jmol.getApplet(\"jmolApplet0\", Info);\n" +
                    "  Jmol.script(jmolApplet0,\"load " + filePath + " packed; axes 2; axes scale 2.5\")\n" +
                    "  Jmol.script(jmolApplet0,\"isosurface hkl {" +h1+ " " + k1 + " " + l1 +"} color red translucent\")\n" +

                    "</script>\n" +
                    "<br>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spin on')\"><button>spin</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spin off')\"><button>off</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill')\"><button>spacefill</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill 23%;wireframe 0.15\\n')\"><button>balls-sticks</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill off;wireframe 0.15\\n')\"><button>wireframe</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'select all; selectionHalos ON; ')\"><button>select all</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'select none; selectionHalos ON; ')\"><button>select none</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'select none; selectionHalos ON; set picking SELECT')\"><button>select manual</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'label on')\"><button>label on</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'label off')\"><button>label off</button></a>\n" +
                    "<!--<a href=\"javascript:Jmol.script(jmolApplet0,'x={selected};prompt x')\"><button>print selected</button></a>-->\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'if ({selected}.length==2){ prompt \\'Distance = \\'+distance({selected}[0] {selected}[1] )+\\'Angstroms\\'} else {prompt \\'select 2 atoms\\';}')\"><button>distance</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'unitcell \\'primitive\\'')\"><button>primitive cell</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'unitcell \\'conv\\'')\"><button>conventional cell</button></a>\n" +
                    "<!--<a href=\"javascript:Jmol.script(jmolApplet0,'set echo top right;font echo 20 serif bolditalic;color echo green;echo \\'Distance = \\'+distance({selected}[0] {selected}[1] )')\"><button>echo</button></a>-->\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'draw pointgroup')\"><button>draw pointgroup on</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'draw off')\"><button>pointgroup off</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'selectedAtoms={selected};n=selectedAtoms.length;totalMass=0;comX=0;comY=0;comZ=0;for(i=1;i<=n;i++){mass=selectedAtoms[i].mass;\ttotalMass=totalMass+mass;\tcomX=comX+selectedAtoms[i].atomX*mass;\tcomY=comY+selectedAtoms[i].atomY*mass;\tcomZ=comZ+selectedAtoms[i].atomZ*mass;}comX=comX/totalMass;comY=comY/totalMass;comZ=comZ/totalMass;prompt \\'COM X-coord: \\'+comX+\\'COM Y-coord: \\'+comY+\\'COM Z-coord: \\'+comZ; draw sphere {@comX @comY @comZ} radius 0.3 color magenta')\"><button>COM of selected</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS x')\"><button>along a</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS y')\"><button>along b</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS z')\"><button>along c</button></a>\n" +
                    "<a href=\"javascript:Jmol.script(jmolApplet0,'load " + filePath + " packed; axes 2; axes scale 2.5;')\"><button>reset</button></a>\n" +
                    "</body>";
            webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
            //filepath is your file's path
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        if(parent.getId()!=spinner1.getId()){
            if(!selectedItem.equals("Select")){
                filePath=semiPath+"/"+selectedItem;
                title.setText(selectedItem);
                webview.loadDataWithBaseURL("file:///android_asset/", html(filePath), "text/html", "utf-8", "");
            }
        }
        else {
            ArrayAdapter<CharSequence> adapter;
            switch (selectedItem) {
                case "Select":
                    break;
                case "elements":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.elements, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "antimonides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.antimonides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "arsenides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.arsenides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "carbides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.carbides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "carbonates":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.carbonates, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "clays":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.clays, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "halides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.halides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "hydrides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.hydrides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "hydroxides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.hydroxides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "ice":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.ice, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "intermetallics":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.intermetallics, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "nitrides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.clays, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "other":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.clays, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "oxides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.clays, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "phosphides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.phosphides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "selenides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.selenides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "silicates":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.silicates, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "sulfides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.sulfides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "sulfates":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.sulfates, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "telurides":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.telurides, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "titanates":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.titanates, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;
                case "zeolites":
                    // Creating ArrayAdapter using the string array and default spinner layout
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.zeolites, android.R.layout.simple_spinner_item);
                    // Specify layout to be used when list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Applying the adapter to our spinner
                    spinner2.setAdapter(adapter);
                    spinner2.setOnItemSelectedListener(this);
                    semiPath = "crystals/"+selectedItem;
                    break;

            }
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public String html(String filePath){
        String html="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<title>A simple Jsmol example</title>\n" +
                "<head>\n" +
                "<script type=\"text/javascript\" src=\"JSmol.min.js\"></script>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "var Info = {\n" +
                "  width: 270,\n" +
                "  height: 270,\n" +
                "  serverURL: \"jsmol.php \",\n" +
                "  use: \"HTML5\",\n" +
                "  j2sPath: \"j2s\",\n" +
                "  console: \"jmolApplet0_infodiv\"\n" +
                "}\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<script type=\"text/javascript\">\n" +
                "  jmolApplet0 = Jmol.getApplet(\"jmolApplet0\", Info);\n" +
                "  Jmol.script(jmolApplet0,\"load "+filePath+" packed; axes 2; axes scale 2.5\")\n" +
                "</script>\n" +
                "<br>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'spin on')\"><button>spin</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'spin off')\"><button>off</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill')\"><button>spacefill</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill 23%;wireframe 0.15\\n')\"><button>balls-sticks</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'spacefill off;wireframe 0.15\\n')\"><button>wireframe</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'select all; selectionHalos ON; ')\"><button>select all</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'select none; selectionHalos ON; ')\"><button>select none</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'select none; selectionHalos ON; set picking SELECT')\"><button>select manual</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'label on')\"><button>label on</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'label off')\"><button>label off</button></a>\n" +
                "<!--<a href=\"javascript:Jmol.script(jmolApplet0,'x={selected};prompt x')\"><button>print selected</button></a>-->\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'if ({selected}.length==2){ prompt \\'Distance = \\'+distance({selected}[0] {selected}[1] )+\\'Angstroms\\'} else {prompt \\'select 2 atoms\\';}')\"><button>distance</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'unitcell \\'primitive\\'')\"><button>primitive cell</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'unitcell \\'conv\\'')\"><button>conventional cell</button></a>\n" +
                "<!--<a href=\"javascript:Jmol.script(jmolApplet0,'set echo top right;font echo 20 serif bolditalic;color echo green;echo \\'Distance = \\'+distance({selected}[0] {selected}[1] )')\"><button>echo</button></a>-->\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'draw pointgroup')\"><button>draw pointgroup on</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'draw off')\"><button>pointgroup off</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'selectedAtoms={selected};n=selectedAtoms.length;totalMass=0;comX=0;comY=0;comZ=0;for(i=1;i<=n;i++){mass=selectedAtoms[i].mass;\ttotalMass=totalMass+mass;\tcomX=comX+selectedAtoms[i].atomX*mass;\tcomY=comY+selectedAtoms[i].atomY*mass;\tcomZ=comZ+selectedAtoms[i].atomZ*mass;}comX=comX/totalMass;comY=comY/totalMass;comZ=comZ/totalMass;prompt \\'COM X-coord: \\'+comX+\\'COM Y-coord: \\'+comY+\\'COM Z-coord: \\'+comZ; draw sphere {@comX @comY @comZ} radius 0.3 color magenta')\"><button>COM of selected</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS x')\"><button>along a</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS y')\"><button>along b</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,' moveto 2 AXIS z')\"><button>along c</button></a>\n" +
                "<a href=\"javascript:Jmol.script(jmolApplet0,'load "+ filePath +" packed; axes 2; axes scale 2.5')\"><button>reset</button></a>"+
                "</body>";
        return html;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        String appPackageName=getApplicationContext().getPackageName();// getPackageName() from Context or Activity
        switch (item.getItemId()) {
            case R.id.action_about :
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_rate:
                Uri uri_playstore=Uri.parse("market://details?id="+appPackageName);
                Uri uri_browser=Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
                try {
                    Intent rateintent = new Intent(Intent.ACTION_VIEW, uri_playstore);
                    startActivity(rateintent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent rateintent = new Intent(Intent.ACTION_VIEW, uri_browser);
                    startActivity(rateintent);
                }
                return true;
            case R.id.action_shareapp:
                String uri = "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent share_app_intent = new Intent(Intent.ACTION_SEND);
                share_app_intent.putExtra(Intent.EXTRA_TEXT, "Loving this app. Download it from: "+uri);
                share_app_intent.setType("text/plain");
                startActivity(Intent.createChooser(share_app_intent, "Share app"));
                //startActivity(share_app_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
