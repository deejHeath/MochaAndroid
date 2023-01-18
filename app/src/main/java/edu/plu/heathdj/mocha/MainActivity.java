package edu.plu.heathdj.mocha;

import static edu.plu.heathdj.mocha.MochaView.getUnitIndex;
import static edu.plu.heathdj.mocha.MochaView.setUnitIndex;
import static edu.plu.heathdj.mocha.MochaView.resetScaleFactor;
import static edu.plu.heathdj.mocha.MochaView.resetUnitChosen;
import static edu.plu.heathdj.mocha.MochaView.resetNumberOfMeasures;
import static edu.plu.heathdj.mocha.MochaView.decrementNumberOfMeasures;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Path;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button measureButton;
    Button createButton;
    Button shareButton;
    Button clearLastButton;
    Button clearAllButton;
    Button infoButton;
    TextView infoLabel;
    TextView infoXLabel;
    String[] actionText={"Create or move POINTS", "Swipe between POINTS to create midpoint","Select 2 OBJECTS to create their intersection","Swipe from LINE to POINT to reflect","Swipe from CIRCLE to POINT to invert", "Swipe between POINTS to create segment", "Swipe between POINTS to create ray","Swipe between POINTS to create line","Swipe from LINE to POINT to create ⊥ line","Swipe from LINE to POINT to create || line","Select 2 LINES to create bisector","Select 2 POINTS, 2 LINES to create Beloch fold","Swipe between POINTS to create circle","Select 3 POINTs to create circle"};
    String[] measureText={"Select 2 POINTS to measure distance","Select 3 POINTS to measure angle","Select 3 POINTS to measure area of triangle","Measure area of CIRCLE", "Measure sum of 2 MEASURES","Measure difference of 2 MEASURES","Measure product of 2 MEASURES","Measure ratio of 2 MEASURES","FIND sine of MEASURE","Find cosine of MEASURE.","Hide OBJECT","Show/hide label of OBJECT","Double swipe or pinch to move/scale","Create or move POINTS"};
    static int whatToDo = 7;
    public static LinkedList<Construct> linkedList = new LinkedList();
    final int makePoints=0, makeMidpoint=1, makeIntersections=2, foldPoints=3, invertPoints=4;
    final int makeSegments=5, makeRays=6, makeLines=7, makePerps=8, makeParallels=9;
    final int makeBisectors=10, useOrigamiSix=11, makeCircles=12, make3PTCircle=13;
    final int measureDistance=20, measureAngle=21, measureTriArea=22, measureCircArea=23;
    final int measureSum=24, measureDifference=25, measureProduct=26, measureRatio=27;
    final int measureSine=28, measureCosine=29, hideObject=30, toggleLabel=31, scaleEverything=32;
    final int unitCircle=33;
    final int POINT = 1, PTonLINE = 2, PTonCIRCLE = 3, MIDPOINT = 4;
    final int LINEintLINE = 5, FOLDedPT = 6, INVERTedPT=7;
    final int CIRCintCIRC0 = 8,CIRCintCIRC1 = 9, LINEintCIRC0 = 10, LINEintCIRC1 = 11;
    final int BiPOINT = 12, THREEptCIRCLEcntr=13, BELOCHpt0 = 14, BELOCHpt1 = 15;
    final int BELOCHpt2 = 16, HIDDENthing = 17, MOVedPT = 18, FIXedPT = 19;
    final int DISTANCE = 20, ANGLE = 21, TriAREA=22, CircAREA=23;
    final int SUM = 24, DIFFERENCE = 25, PRODUCT = 26, RATIO = 27, SINE=28, COSINE=29;
    final int CIRCLE = 0;
    final int LINE = -1, PERP = -2, PARALLEL = -3, BISECTOR0 = -4, BISECTOR1 = -5, BELOCHline0 = -7;
    final int BELOCHline1 = -8, BELOCHline2 = -9, THREEptLINE = -10, SEGMENT = -11, RAY = -12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        measureButton = findViewById(R.id.measureBtn);
        createButton = findViewById(R.id.createBtn);
        shareButton = findViewById(R.id.shareBtn);
        clearLastButton = findViewById(R.id.clearLastBtn);
        clearAllButton = findViewById(R.id.clearAllBtn);
        infoButton = findViewById(R.id.infoBtn);
        infoLabel = findViewById(R.id.infoLbl);
        infoXLabel = findViewById(R.id.infoXLbl);
        measureButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        clearLastButton.setOnClickListener(this);
        clearAllButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);
        infoLabel.setBackgroundColor(Color.WHITE);
        infoXLabel.setBackgroundColor(Color.WHITE);
        android.content.Intent intent = getIntent();
        whatToDo=intent.getIntExtra(CreateActivity.createNumber,7);
        whatToDo=intent.getIntExtra(MeasureActivity.measureNumber,7);
        if (whatToDo < 14) {
            infoLabel.setText(actionText[whatToDo]);
            infoXLabel.setText(actionText[whatToDo]);
        } else {
            infoLabel.setText(measureText[whatToDo-20]);
            infoXLabel.setText(measureText[whatToDo-20]);
        }
    }
    public static int getWhatToDo() {
        return whatToDo;
    }
    @Override
    public void onClick(View view) {
        View MochaView = (View) findViewById(R.id.rect);
        switch (view.getId()) {
            case R.id.createBtn:
                openCreateActivity();
                break;
            case R.id.measureBtn:
                openMeasureActivity();
                break;
            case R.id.shareBtn:
                // View to BitMap
                Bitmap b  = getScreenShot(getWindow().getDecorView().findViewById(R.id.rect));

                //BitMap to Parsable Uri (needs write permissions)
                String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), b,"title", null);
                Uri bmpUri = Uri.parse(pathofBmp);

                //Share the image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));
                break;
            case R.id.clearLastBtn:
                doClearLastStuff();
                MochaView.invalidate();
                break;
            case R.id.clearAllBtn:
                resetNumberOfMeasures();
                resetUnitChosen();
                resetScaleFactor();
                linkedList.clear();
                resetWhatToDo();
                MochaView.invalidate();
                break;
            case R.id.infoBtn:
                openInfoActivity();
                break;
            default:
                break;
        }
    }

    public void openCreateActivity() {
        android.content.Intent intent = new android.content.Intent(this,CreateActivity.class);
        startActivity(intent);
    }

    public void openMeasureActivity() {
        android.content.Intent intent = new android.content.Intent(this,MeasureActivity.class);
        startActivity(intent);
    }

    public void openInfoActivity() {
        //android.util.Log.d("made it here","yeah, here");
        android.content.Intent intent = new android.content.Intent(this,InfoActivity.class);
        startActivity(intent);
    }

    public void doClearLastStuff() {
        if (linkedList.size()>0) {
            if (linkedList.size() - 1 == getUnitIndex()) {
                resetUnitChosen();
                setUnitIndex(0);
            }
            if (linkedList.getLast().type >= DISTANCE) {
                decrementNumberOfMeasures();
            }
            if (linkedList.getLast().type==HIDDENthing) {
                //linkedList.getLast().parent.get(0).isShown=true;
                for(int i=0;i<linkedList.getLast().parent.size();i++) {
                    linkedList.getLast().parent.get(i).isShown=true;
                }
            }
//            if (linkedList.getLast().type==MOVedPT) {
//                MovedPoint temp = (MovedPoint) linkedList.getLast();
//                linkedList.getLast().parent.get(0).update(temp.lastCoordinates);
//                for (int i=0;i<linkedList.size();i++) {
//                    linkedList.get(i).update(linkedList.get(i).coordinates);
//                }
//
//            }
            if (linkedList.size() > 1) {
                if (linkedList.get(linkedList.size() - 2).type == THREEptCIRCLEcntr || linkedList.getLast().type == BISECTOR1) {
                    linkedList.removeLast();
                    linkedList.removeLast();         // since there were three created at once
                }
                if (linkedList.getLast().type == CIRCintCIRC1 || linkedList.getLast().type == LINEintCIRC1) {
                    linkedList.removeLast();         // since there were two created at once
                }
                if (linkedList.getLast().type == BELOCHline2) {
                    linkedList.removeLast();
                    linkedList.removeLast();         // since there were six created at once
                    linkedList.removeLast();
                    linkedList.removeLast();
                    linkedList.removeLast();
                }
            }
            linkedList.removeLast();
        }
        if (linkedList.size()<2) {
            resetWhatToDo();
        }
        if (linkedList.size()==0) {
            resetScaleFactor();
        }
    }
    public void resetWhatToDo() {
        if (whatToDo != makePoints && whatToDo != makeLines && whatToDo != makeSegments && whatToDo != makeRays && whatToDo != makeCircles && whatToDo != makeMidpoint) {
            whatToDo = makeLines;
        }
        infoLabel.setText(actionText[whatToDo]);
        infoXLabel.setText(actionText[whatToDo]);
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
