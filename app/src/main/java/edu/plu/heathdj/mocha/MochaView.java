package edu.plu.heathdj.mocha;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.LinkedList;

public class MochaView extends View {
    Construct potentialClick;
    LinkedList<Construct> clickedList = new LinkedList();
    LinkedList<Integer> clickedIndex = new LinkedList();
    CGPoint firstTouch;
    boolean activeConstruct = false,newPoint=false,newFirstPoint=false,firstMove=true;
    double touchSense = 48.0;
    static boolean unitChosen=false;
    static int unitIndex = -1, numberOfMeasures = 1;
    static double totalScaleFactor=1.0;
    static double lastAngle = 0.0, lastRatio = 1.0;
    static CGPoint lastVector = new CGPoint(0,0);
    final int makePoints=0, makeMidpoint=1, makeIntersections=2, foldPoints=3, invertPoints=4;
    final int makeSegments=5, makeRays=6, makeLines=7, makePerps=8, makeParallels=9;
    final int makeBisectors=10, makeBelochFolds=11, makeCircles=12, make3PTCircle=13;
    final int measureDistance=20, measureAngle=21, measureTriArea=22, measureCircArea=23;
    final int measureSum=24, measureDifference=25, measureProduct=26, measureRatio=27;
    final int measureSine=28, measureCosine=29, hideObject=30, toggleLabel=31, scaleEverything=32;
    final int unitCircle=33;
    final int POINT = 1, PTonLINE = 2, PTonCIRCLE = 3, MIDPOINT = 4;
    final int LINEintLINE = 5, FOLDedPT = 6, INVERTedPT=7;
    final int CIRCintCIRC0 = 8,CIRCintCIRC1 = 9, LINEintCIRC0 = 10, LINEintCIRC1 = 11;
    final int BiPOINT = 12, THREEptCIRCLEcntr=13;
    final int BELOCHpt0 = 14, BELOCHpt1 = 15, BELOCHpt2 = 16, HIDDENthing = 17, MOVedPT = 18;
    final int DISTANCE = 20, ANGLE = 21, TriAREA=22, CircAREA=23;
    final int SUM = 24, DIFFERENCE = 25, PRODUCT = 26, RATIO = 27, SINE=28, COSINE=29;
    final int CIRCLE = 0;
    final int LINE = -1, PERP = -2, PARALLEL = -3, BISECTOR0 = -4, BISECTOR1 = -5, BELOCHline0 = -7;
    final int BELOCHline1 = -8, BELOCHline2 = -9, THREEptLINE = -10, SEGMENT = -11, RAY = -12;
    
    public MochaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onDraw(Canvas canvas){
        int x=canvas.getWidth(),y=canvas.getHeight();
        super.onDraw(canvas);
        Paint paint = new Paint();
        if (MainActivity.whatToDo==33) {
            MainActivity.whatToDo=0;
            numberOfMeasures = 1;
            unitChosen = false;
            totalScaleFactor = 1.0;
            MainActivity.linkedList.clear();
            CGPoint location = new CGPoint(7.0 * getWidth() / 12.0, getHeight() - Math.min(getWidth(), getHeight()) / 2.0);
            MainActivity.linkedList.add(new FixedPoint(location, 0));
            location = new CGPoint(7.0 * getWidth() / 12.0 + Math.min(getWidth(), getHeight()) / 3.0, getHeight() - Math.min(getWidth(), getHeight()) / 2.0);
            MainActivity.linkedList.add(new FixedPoint(location, 1));
            LinkedList<Construct> tempList = new LinkedList();
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(1));
            MainActivity.linkedList.add(new Line(tempList, location, 2));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(2));
            MainActivity.linkedList.add(new PerpLine(tempList, location, 3));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(1));
            MainActivity.linkedList.add(new Circle(tempList, location, 4));
            location = new CGPoint(7.0 * getWidth() / 12.0 + Math.min(getWidth(), getHeight()) / 3.0 * 0.8, getHeight() / 2.0 - Math.min(getWidth(), getHeight()) / 3.0 * 0.6);
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(4));
            MainActivity.linkedList.add(new PointOnCircle(tempList, location, 5));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(1));
            MainActivity.linkedList.add(new Distance(tempList, new CGPoint(12, 20 * numberOfMeasures), 6));
            MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
            numberOfMeasures += 1;
            unitChosen=true;
            unitIndex=6;
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(1));
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(5));
            MainActivity.linkedList.add(new Angle(tempList, location, 7));
            MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
            numberOfMeasures += 1;
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(5));
            tempList.add(MainActivity.linkedList.get(2));
            MainActivity.linkedList.add(new PerpLine(tempList, location, 8));
            MainActivity.linkedList.get(8).isShown = false;
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(2));
            tempList.add(MainActivity.linkedList.get(8));
            MainActivity.linkedList.add(new LineIntLine(tempList, location, 9));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(5));
            tempList.add(MainActivity.linkedList.get(3));
            MainActivity.linkedList.add(new PerpLine(tempList, location, 10));
            MainActivity.linkedList.get(10).isShown = false;
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(3));
            tempList.add(MainActivity.linkedList.get(10));
            MainActivity.linkedList.add(new LineIntLine(tempList, location, 11));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(8));
            tempList.add(MainActivity.linkedList.get(10));
            MainActivity.linkedList.add(new HiddenThing(tempList, location, 12));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(0));
            tempList.add(MainActivity.linkedList.get(5));
            MainActivity.linkedList.add(new Segment(tempList, location, 13));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(5));
            tempList.add(MainActivity.linkedList.get(9));
            MainActivity.linkedList.add(new Segment(tempList, location, 14));
            tempList.clear();
            tempList.add(MainActivity.linkedList.get(5));
            tempList.add(MainActivity.linkedList.get(11));
            MainActivity.linkedList.add(new Segment(tempList, location, 15));
            invalidate();
        }

        for (int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).type<=0 && MainActivity.linkedList.get(i).isReal && MainActivity.linkedList.get(i).isShown) {
                if (clickedIndex.contains(MainActivity.linkedList.get(i).index)) {
                    MainActivity.linkedList.get(i).draw(canvas, paint, true);
                } else {
                    MainActivity.linkedList.get(i).draw(canvas, paint, false);
                }
            }
        }
        for (int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).type>0 && MainActivity.linkedList.get(i).isReal && MainActivity.linkedList.get(i).isShown) {
                if (clickedIndex.contains(MainActivity.linkedList.get(i).index)) {
                    MainActivity.linkedList.get(i).draw(canvas, paint, true);
                } else {
                    MainActivity.linkedList.get(i).draw(canvas, paint, false);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int whatToDo=MainActivity.getWhatToDo();
        CGPoint location = new CGPoint(event.getX(),event.getY());
        if (location.y<0) location.y=0;
        if (location.y>getHeight()) location.y=getHeight();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstTouch=location;
                activeConstruct=false;
                newPoint=false;
                newFirstPoint=false;
                firstMove=true;
                switch(whatToDo){
                    case makePoints:
                        getPointOrMeasure(location);
                        if (!activeConstruct) {
                            newPoint = true;
                            getLineOrCircle(location);
                        }
                    if (!activeConstruct) {
                        MainActivity.linkedList.add(new Point(location, MainActivity.linkedList.size()));
                        setActiveConstruct(MainActivity.linkedList.size()-1);
                    } else {
                        if (clickedList.get(0).type<0) {
                            MainActivity.linkedList.add(new PointOnLine(clickedList, location, MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size()-1);
                        } else if (clickedList.get(0).type==0) {
                            MainActivity.linkedList.add(new PointOnCircle(clickedList, location, MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size()-1);
                        } else {
                            //MainActivity.linkedList.add(new MovedPoint(clickedList, clickedList.get(0).coordinates, MainActivity.linkedList.size()));
                        }
                    }
                    break;
                    case makeLines:
                    case makeSegments:
                    case makeRays:
                    case makeMidpoint:
                    case makeCircles:
                        getPointOrLineOrCircle(location);
                        if (activeConstruct) {
                            if (clickedList.get(0).type<0) {
                                newFirstPoint = true;
                                MainActivity.linkedList.add(new PointOnLine(clickedList,
                                location, MainActivity.linkedList.size()));
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                                clickedList.removeFirst();
                                clickedIndex.removeFirst();
                        } else if (clickedList.get(0).type==0) {
                            newFirstPoint=true;
                            MainActivity.linkedList.add(new PointOnCircle(clickedList, location, MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size()-1);
                            clickedList.removeFirst();
                            clickedIndex.removeFirst();
                            }
                        } else {
                            newFirstPoint = true;
                            MainActivity.linkedList.add(new Point(new LinkedList<Construct>(),location,MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size()-1);
                        }
                        break;
                    case makeIntersections:
                        getLineOrCircle(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case foldPoints:
                    case makePerps:
                    case makeParallels:
                    case makeBisectors:
                        getLine(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case invertPoints:
                    case measureCircArea:
                        getCircle(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case makeBelochFolds:
                        if (clickedList.size()==0 || clickedList.size()==1) {
                            getPoint(location);
                        } else if (clickedList.size()==2 || clickedList.size()==3) {
                            getLine(location);
                        }
                        break;
                    case measureDistance:
                    case make3PTCircle:
                    case measureAngle:
                    case measureTriArea:
                        getPoint(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case measureRatio:
                    case measureSum:
                    case measureProduct:
                    case measureDifference:
                    case measureSine:
                    case measureCosine:
                        getMeasure(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case hideObject:
                    case toggleLabel:
                        getPointOrLineOrCircle(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                        }
                        break;
                    case scaleEverything:
                        if (event.getPointerCount() > 1) {
                            CGPoint location1 = new CGPoint(event.getX(1),event.getY(1));
                            lastAngle=Math.atan2(location1.y-location.y,location1.x-location.x);
                            lastRatio=Math.sqrt(Math.pow(location1.x-location.x,2+Math.pow(location1.y-location.y,2)));
                            lastVector = new CGPoint((location1.x+location.x)/2,(location1.y+location.y)/2);
                            //android.util.Log.d("Multitouch down event0", "("+location.x+","+location.y+")");
                            //android.util.Log.d("Multitouch down event1", "("+location1.x+","+location1.y+")");
                        } else {
                            lastAngle = 0.0;
                            lastRatio = 1.0;
                            lastVector = new CGPoint(0,0);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch(whatToDo) {
                    case makePoints:
                        if (!newPoint) {
                            clickedList.get(0).update(location);
                        } else { // otherwise the point is new, and we can do what we like with it.
                            clearAllPotentials();
                            getLineOrCircle(location);
                            if (activeConstruct) {
                            if (clickedList.get(0).type<0) {
                                    MainActivity.linkedList.removeLast();
                                    MainActivity.linkedList.add(new PointOnLine(clickedList, location,
                                    MainActivity.linkedList.size()));
                                    setActiveConstruct(MainActivity.linkedList.size() - 1);
                            } else {
                                MainActivity.linkedList.removeLast();
                                MainActivity.linkedList.add(new PointOnCircle(clickedList, location, MainActivity.linkedList.size()));
                                setActiveConstruct(MainActivity.linkedList.size()-1);
                                }
                            } else {
                                MainActivity.linkedList.removeLast();
                                MainActivity.linkedList.add(new Point(location, MainActivity.linkedList.size()));
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            }
                        }
                        for (int i=0;i<MainActivity.linkedList.size();i++) {
                            if (MainActivity.linkedList.get(i).type <= 0 || MainActivity.linkedList.get(i).type > PTonCIRCLE && MainActivity.linkedList.get(i).type < DISTANCE) {
                                MainActivity.linkedList.get(i).update(location);
                            }
                        }
                        break;
                    case makeLines:
                    case makeSegments:
                    case makeRays:
                    case makeMidpoint:
                    case makeCircles:
                        if (firstMove) {
                            firstMove = false;
                        } else {
                            MainActivity.linkedList.removeLast(); // remove temporary segment
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                            if (newPoint) {
                                MainActivity.linkedList.removeLast(); // remove temporary point
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                                newPoint = false;
                            }
                        }
                        activeConstruct=false;
                        while (clickedList.size()>1) {
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                        }
                        getPointOrLineOrCircle(location);
                        if (activeConstruct) {
                            LinkedList<Construct> temp = new LinkedList();
                            temp.add(clickedList.getLast());
                            if (clickedList.getLast().type > 0) {
                                newPoint = false;
                            } else if (clickedList.getLast().type < 0) {
                                newPoint = true;
                                MainActivity.linkedList.add(new PointOnLine(temp, location, MainActivity.linkedList.size()));
                                clickedList.remove(1);
                                clickedIndex.remove(1);
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            } else {
                                newPoint = true;
                                MainActivity.linkedList.add(new PointOnCircle(temp, location, MainActivity.linkedList.size()));
                                clickedList.remove(1);
                                clickedIndex.remove(1);
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            }
                        } else {
                            newPoint = true;
                            MainActivity.linkedList.add(new Point(new LinkedList<Construct>(),
                            location, MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size() - 1);
                        }
                        switch (whatToDo) {
                            case makeLines:
                                MainActivity.linkedList.add(new Line(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(),getHeight());
                                break;
                            case makeCircles:
                                MainActivity.linkedList.add(new Circle(clickedList, location, MainActivity.linkedList.size()));
                                break;
                            case makeSegments:
                                MainActivity.linkedList.add(new Segment(clickedList, location, MainActivity.linkedList.size()));
                                break;
                            case makeRays:
                                MainActivity.linkedList.add(new Ray(clickedList, location, MainActivity.linkedList.size()));
                                break;
                            case makeMidpoint:
                                MainActivity.linkedList.add(new MidPoint(clickedList, location, MainActivity.linkedList.size()));
                                break;
                        }
                        setActiveConstruct(MainActivity.linkedList.size()-1);
                        break;
                    case makeIntersections:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            getLineOrCircle(location);
                        }
                        break;
                    case makeBisectors:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            getLine(location);
                        }
                        break;
                    case foldPoints:
                    case makePerps:
                    case makeParallels:
                    case invertPoints:
                        if (firstMove) {
                            firstMove = false;
                        } else if (clickedList.size()>0) {
                            MainActivity.linkedList.removeLast(); // remove temporary segment
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                            if (newPoint) {
                                MainActivity.linkedList.removeLast(); // remove temporary point
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                                newPoint = false;
                            }
                        }
                        activeConstruct=false;
                        while (clickedList.size()>1) {
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                        }
                        if (clickedList.size()==1) {
                            getPointOrLineOrCircleAllowingRepeatConstructions(location);
                            if (activeConstruct) {
                                if (clickedList.getLast().type > 0) {
                                    newPoint = false;
                                } else if (clickedList.getLast().type < 0) {
                                    newPoint = true;
                                    LinkedList<Construct> temp = new LinkedList();
                                    temp.add(clickedList.getLast());
                                    MainActivity.linkedList.add(new PointOnLine(temp,
                                    location, MainActivity.linkedList.size()));
                                    clickedList.remove(1);
                                    clickedIndex.remove(1);
                                    setActiveConstruct(MainActivity.linkedList.size() - 1);
                                } else {
                                    newPoint = true;
                                    LinkedList<Construct> temp = new LinkedList();
                                    temp.add(clickedList.getLast());
                                    MainActivity.linkedList.add(new PointOnCircle(temp,location, MainActivity.linkedList.size()));
                                    clickedList.remove(1);
                                    clickedIndex.remove(1);
                                    setActiveConstruct(MainActivity.linkedList.size() - 1);
                                }
                            } else {
                                newPoint = true;
                                LinkedList<Construct> temp = new LinkedList();
                                MainActivity.linkedList.add(new Point(temp,location, MainActivity.linkedList.size()));
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            }
                            LinkedList<Construct> newList = new LinkedList();
                            newList.add(clickedList.get(1));
                            newList.add(clickedList.get(0));
                            switch (whatToDo) {
                                case makePerps:
                                    MainActivity.linkedList.add(new PerpLine(newList, location, MainActivity.linkedList.size()));
                                    break;
                                case makeParallels:
                                    MainActivity.linkedList.add(new ParallelLine(newList, location, MainActivity.linkedList.size()));
                                    break;
                                case foldPoints:
                                    MainActivity.linkedList.add(new FoldedPoint(newList, location, MainActivity.linkedList.size()));
                                    break;
                                case invertPoints:
                                    MainActivity.linkedList.add(new InvertedPoint(newList, location, MainActivity.linkedList.size()));
                                    break;
                            }
                            setActiveConstruct(MainActivity.linkedList.size() - 1);
                        }
                        break;
                    case makeBelochFolds:
                        getRidOfActivesThatAreTooFar(location);
                        if (clickedList.size()==0 || clickedList.size()==1) {
                            getPoint(location);
                        } else if (clickedList.size()==2 || clickedList.size()==3) {
                            getLine(location);
                        }
                        break;
                    case measureDistance:
                    case make3PTCircle:
                    case measureAngle:
                    case measureTriArea:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            getPoint(location);
                        }
                        break;
                    case measureRatio:
                    case measureSum:
                    case measureDifference:
                    case measureProduct:
                    case measureSine:
                    case measureCosine:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            getMeasure(location);
                        }
                        break;
                    case measureCircArea:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            getCircle(location);
                        }
                        break;
                    case hideObject:
                    case toggleLabel:
                        getRidOfActivesThatAreTooFar(location);
                        getPointOrLineOrCircle(location);
                        break;
                    case scaleEverything:
                        if (event.getPointerCount() > 1) {
                            CGPoint location1 = new CGPoint(event.getX(1),event.getY(1));
                            CGPoint vector = new CGPoint((location1.x+location.x)/2- lastVector.x,(location1.y+location.y)/2- lastVector.y);
                            if (lastVector.x==0.0 && lastVector.y==0.0 || Math.sqrt(Math.pow(vector.x,2)+Math.pow(vector.y,2))>64.0) {
                                vector = new CGPoint(0.0,0.0);
                            }
                            double angle = Math.atan2(location1.y-location.y,location1.x-location.x)-lastAngle;
                            if (Math.abs(angle)>.3) {angle=0.0;}
                            double ratio = Math.sqrt(Math.pow(location1.x - location.x, 2) + Math.pow(location1.y - location.y, 2)) / lastRatio;
                            if (ratio>1.2) {ratio=1.0;}
                            lastAngle=Math.atan2(location1.y-location.y,location1.x-location.x);
                            lastRatio=Math.sqrt(Math.pow(location1.x-location.x,2)+Math.pow(location1.y-location.y,2));
                            lastVector = new CGPoint((location1.x+location.x)/2,(location1.y+location.y)/2);
                            if ((totalScaleFactor<32.0 && ratio>1.0) || (totalScaleFactor>0.03125 && ratio<1.0)) {
                                totalScaleFactor*=ratio;
                                for (int i=0;i<MainActivity.linkedList.size();i++) {
                                    if (MainActivity.linkedList.get(i).type < DISTANCE) {
                                        MainActivity.linkedList.get(i).update(new CGPoint(ratio * (MainActivity.linkedList.get(i).coordinates.x - getWidth() / 2.0) + getWidth() / 2.0, ratio * (MainActivity.linkedList.get(i).coordinates.y - getHeight() / 2.0) + getHeight() / 2.0));
                                    }
                                }
                            }
                            double C=Math.cos(angle), S=Math.sin(angle);
                            for (int i=0;i<MainActivity.linkedList.size();i++) {
                                if (MainActivity.linkedList.get(i).type<DISTANCE) {
                                    double X=MainActivity.linkedList.get(i).coordinates.x-getWidth()/2.0;
                                    double Y=MainActivity.linkedList.get(i).coordinates.y-getHeight()/2.0;
                                    MainActivity.linkedList.get(i).update(new CGPoint(C*X-S*Y+getWidth()/2.0 ,S*X+C*Y+getHeight()/2.0));
                                }
                            }
                            for (int i=0;i<MainActivity.linkedList.size();i++) {
                                if (MainActivity.linkedList.get(i).type < DISTANCE) {
                                    MainActivity.linkedList.get(i).update(new CGPoint(MainActivity.linkedList.get(i).coordinates.x+vector.x,MainActivity.linkedList.get(i).coordinates.y+vector.y));
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch(whatToDo) {
                    case makePoints:
                        if (activeConstruct) {
                            if (clickedList.get(0).type > 0) {
                                clickedList.get(0).update(location);
                            } else if (clickedList.get(0).type < 0) {
                                MainActivity.linkedList.removeLast();
                                MainActivity.linkedList.add(new PointOnLine(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(location);
                            } else if (clickedList.get(0).type == 0) {
                                MainActivity.linkedList.removeLast();
                                MainActivity.linkedList.add(new PointOnCircle(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(location);
                            }
                        } else {
                            MainActivity.linkedList.add(new Point(location, MainActivity.linkedList.size()));
                        }
                        clearAllPotentials();
                        for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                            if (MainActivity.linkedList.get(i).type <= 0 || MainActivity.linkedList.get(i).type > PTonCIRCLE && MainActivity.linkedList.get(i).type < DISTANCE) {
                                MainActivity.linkedList.get(i).update(location);
                            }
                        }
                        break;
                    case makeLines:
                    case makeSegments:
                    case makeRays:
                    case makeMidpoint:
                    case makeCircles:
                        if (!firstMove) {
                            if (MainActivity.linkedList.getLast().type<=0) {
                                MainActivity.linkedList.removeLast(); // remove temporary segment/ray etc.
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                            }
                            if (newPoint) {
                                MainActivity.linkedList.removeLast(); // remove temporary point
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                                newPoint = false;
                            }
                        }
                        activeConstruct = false;
                        while (clickedList.size() > 1) {
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                        }
                        getPointOrLineOrCircle(location);
                        if (activeConstruct) {
                            LinkedList<Construct> temp = new LinkedList();
                            temp.add(clickedList.getLast());
                            if (clickedList.getLast().type > 0) {
                                newPoint = false;
                            } else if (clickedList.getLast().type < 0) {
                                newPoint = true;
                                MainActivity.linkedList.add(new PointOnLine(temp, location, MainActivity.linkedList.size()));
                                clickedList.remove(1);
                                clickedIndex.remove(1);
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            } else {
                                newPoint = true;
                                MainActivity.linkedList.add(new PointOnCircle(temp, location, MainActivity.linkedList.size()));
                                clickedList.remove(1);
                                clickedIndex.remove(1);
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            }
                        } else {
                            newPoint = true;
                            MainActivity.linkedList.add(new Point(new LinkedList<Construct>(), location, MainActivity.linkedList.size()));
                            setActiveConstruct(MainActivity.linkedList.size() - 1);
                        }
                        if (whatToDo != makeRays && whatToDo != makeCircles) {
                            arrangeClickedObjectsByIndex();
                        }
                        if (clickedList.get(0).distance(location) > 0.01) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if ((MainActivity.linkedList.get(i).type == LINE && whatToDo == makeLines) || (MainActivity.linkedList.get(i).type == SEGMENT && whatToDo == makeSegments) || (MainActivity.linkedList.get(i).type == RAY && whatToDo == makeRays) || (MainActivity.linkedList.get(i).type == CIRCLE && whatToDo == makeCircles) || (MainActivity.linkedList.get(i).type == MIDPOINT && whatToDo == makeMidpoint)) {
                                    if (MainActivity.linkedList.get(i).parent.get(0).index == clickedList.get(0).index && MainActivity.linkedList.get(i).parent.get(1).index == clickedList.get(1).index) {
                                        alreadyExists = true;
                                        MainActivity.linkedList.get(i).isShown = true;
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                switch (whatToDo) {
                                    case makeSegments:
                                        MainActivity.linkedList.add(new Segment(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                    case makeLines:
                                        MainActivity.linkedList.add(new Line(clickedList, location, MainActivity.linkedList.size()));
                                        MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                        break;
                                    case makeRays:
                                        MainActivity.linkedList.add(new Ray(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                    case makeMidpoint:
                                        MainActivity.linkedList.add(new MidPoint(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                    case makeCircles:
                                        MainActivity.linkedList.add(new Circle(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                }
                            }
                        } else if (newPoint) {
                            MainActivity.linkedList.removeLast();
                            if (newFirstPoint) {
                                MainActivity.linkedList.removeLast();
                            }
                        }
                        clearAllPotentials();
                        clearActives();
                        break;
                    case makeIntersections:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size() == 2) {       // need to construct InterPt0 & InterPt1.
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == LINEintLINE) {
                                    if (!alreadyExists) {
                                        if ((MainActivity.linkedList.get(i).parent.get(0).index == clickedList.get(0).index && MainActivity.linkedList.get(i).parent.get(1).index == clickedList.get(1).index) || (MainActivity.linkedList.get(i).parent.get(0).index == clickedList.get(1).index && MainActivity.linkedList.get(i).parent.get(1).index == clickedList.get(0).index)) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                if (clickedList.get(0).type < 0 && clickedList.get(1).type < 0) {             // both lines
                                    MainActivity.linkedList.add(new LineIntLine(clickedList, location, MainActivity.linkedList.size()));
                                    //MainActivity.linkedList.getLast().update(location);
                                    clearAllPotentials();
                                } else if (clickedList.get(0).type == 0 && clickedList.get(1).type == 0) {    // both circles
                                    MainActivity.linkedList.add(new CircIntCirc0(clickedList, location, MainActivity.linkedList.size()));
                                    clickedList.add(MainActivity.linkedList.get(MainActivity.linkedList.size() - 1));
                                    // used this for getting CIC0 in there to pass information to CIC1
                                    MainActivity.linkedList.add(new CircIntCirc1(clickedList, location, MainActivity.linkedList.size()));
                                    clearAllPotentials();
                                } else {                                                        // one line, one circle
                                    if (clickedList.get(0).type == 0) {                 // make sure line is [0], circle is [1]
                                        clickedList.add(clickedList.get(0));
                                        clickedList.removeFirst();
                                    }
                                    MainActivity.linkedList.add(new LineIntCirc0(clickedList, location, MainActivity.linkedList.size()));
                                    clickedList.add(MainActivity.linkedList.get(MainActivity.linkedList.size() - 1));
                                    // used this for getting LIC0 in there to pass information to LIC1
                                    MainActivity.linkedList.add(new LineIntCirc1(clickedList, location, MainActivity.linkedList.size()));
                                    clearAllPotentials();
                                }
                            }
                        }
                        break;
                    case makeBisectors:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size() == 2) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == BiPOINT) {
                                    Construct temp = MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i + 1).isShown = true;
                                            MainActivity.linkedList.get(i + 2).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new BisectorPoint(clickedList, location, MainActivity.linkedList.size()));
                                int i=MainActivity.linkedList.getLast().index;
                                MainActivity.linkedList.getLast().isShown = false;
                                clickedList.add(0, MainActivity.linkedList.getLast());
                                MainActivity.linkedList.add(new Bisector0(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                MainActivity.linkedList.add(new Bisector1(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                clickedList.clear();
                                clickedList.add(MainActivity.linkedList.get(i));
                                MainActivity.linkedList.add(new HiddenThing(clickedList,location,MainActivity.linkedList.size()));
                                clearAllPotentials();
                            }
                        }
                        break;
                    case makePerps:
                    case makeParallels:
                    case foldPoints:
                    case invertPoints:
                        if (firstMove) {
                            firstMove = false;
                            if (MainActivity.linkedList.getLast().type<=0) {
                                MainActivity.linkedList.removeLast(); // remove temporary segment
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                            }
                            if (newPoint) {
                                MainActivity.linkedList.removeLast(); // remove temporary point
                                clickedList.removeLast();
                                clickedIndex.removeLast();
                                newPoint = false;
                            }
                        }
                        activeConstruct = false;
                        while (clickedList.size() > 1) {
                            clickedList.removeLast();
                            clickedIndex.removeLast();
                        }
                        if (clickedList.size() == 1) {
                            getPointOrLineOrCircleAllowingRepeatConstructions(location);
                            if (activeConstruct) {
                                if (clickedList.getLast().type > 0) {
                                    newPoint = false;
                                } else if (clickedList.getLast().type < 0) {
                                    newPoint = true;
                                    LinkedList<Construct> temp = new LinkedList();
                                    temp.add(clickedList.getLast());
                                    MainActivity.linkedList.add(new PointOnLine(temp, location, MainActivity.linkedList.size()));
                                    clickedList.remove(1);
                                    clickedIndex.remove(1);
                                    setActiveConstruct(MainActivity.linkedList.size() - 1);
                                } else {
                                    newPoint = true;
                                    LinkedList<Construct> temp = new LinkedList();
                                    temp.add(clickedList.getLast());
                                    MainActivity.linkedList.add(new PointOnCircle(temp, location, MainActivity.linkedList.size()));
                                    clickedList.remove(1);
                                    clickedIndex.remove(1);
                                    setActiveConstruct(MainActivity.linkedList.size() - 1);
                                }
                            } else {
                                newPoint = true;
                                LinkedList<Construct> temp = new LinkedList();
                                MainActivity.linkedList.add(new Point(temp, location, MainActivity.linkedList.size()));
                                setActiveConstruct(MainActivity.linkedList.size() - 1);
                            }
                            boolean alreadyExists = false;
                            if (clickedList.size() == 2) {
                                if (whatToDo == makeParallels && clickedList.get(1).type == PTonLINE) {
                                    if (clickedList.get(1).parent.get(0).index == clickedList.get(0).index) {
                                        alreadyExists = true;
                                    }
                                }
                                for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                    if (!alreadyExists && ((whatToDo == makePerps && MainActivity.linkedList.get(i).type == PERP) || (whatToDo == makeParallels && MainActivity.linkedList.get(i).type == PARALLEL) || (whatToDo == foldPoints && MainActivity.linkedList.get(i).type == FOLDedPT))) {
                                        if (MainActivity.linkedList.get(i).parent.get(0).index == clickedList.get(1).index && MainActivity.linkedList.get(i).parent.get(1).index == clickedList.get(0).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                        }
                                    }
                                }
                            }
                            LinkedList<Construct> newList = new LinkedList();
                            newList.add(clickedList.get(1));
                            newList.add(clickedList.get(0));
                            if (!alreadyExists) {
                                switch (whatToDo) {
                                    case makePerps:
                                        MainActivity.linkedList.add(new PerpLine(newList, location, MainActivity.linkedList.size()));
                                        MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                        break;
                                    case makeParallels:
                                        MainActivity.linkedList.add(new ParallelLine(newList, location, MainActivity.linkedList.size()));
                                        MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                        break;
                                    case invertPoints:
                                        MainActivity.linkedList.add(new InvertedPoint(newList, location, MainActivity.linkedList.size()));
                                        break;
                                    default:
                                        MainActivity.linkedList.add(new FoldedPoint(newList, location, MainActivity.linkedList.size()));
                                        break;
                                }
                            }
                            if (newPoint && alreadyExists) {
                                MainActivity.linkedList.removeLast();
                            }
                        }
                        clearAllPotentials();
                        clearActives();
                        break;
                    case makeBelochFolds:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                            activeConstruct = false;
                        }
                        if (clickedList.size() == 4) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == BELOCHpt0) {
                                    BelochPoint0 temp = (BelochPoint0) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index && temp.parent.get(2).index == clickedList.get(2).index && temp.parent.get(3).index == clickedList.get(3).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i + 1).isShown = true;
                                            MainActivity.linkedList.get(i + 3).isShown = true;
                                            MainActivity.linkedList.get(i + 5).isShown = true;

                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new BelochPoint0(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().isShown = false;
                                clickedList.add(MainActivity.linkedList.getLast());
                                MainActivity.linkedList.add(new BelochLine0(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                MainActivity.linkedList.add(new BelochPoint1(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().isShown = false;
                                clickedList.add(0, MainActivity.linkedList.getLast());
                                MainActivity.linkedList.add(new BelochLine1(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                clickedList.remove(0);
                                MainActivity.linkedList.add(new BelochPoint2(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().isShown = false;
                                clickedList.add(0, MainActivity.linkedList.getLast());
                                MainActivity.linkedList.add(new BelochLine2(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                            }
                            clearAllPotentials();
                        }
                        break;
                    case make3PTCircle:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==3) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type < 0 && !alreadyExists) {
                                    if (MainActivity.linkedList.get(i).type == THREEptLINE) {
                                        ThreePointLine temp = (ThreePointLine) MainActivity.linkedList.get(i);
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index && temp.parent.get(2).index == clickedList.get(2).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            MainActivity.linkedList.get(i + 1).isShown = true;
                                            MainActivity.linkedList.get(i + 2).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new ThreePointLine(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(getWidth(), getHeight());
                                clickedList.add(MainActivity.linkedList.getLast());
                                MainActivity.linkedList.add(new ThreePointCircleCntr(clickedList, location, MainActivity.linkedList.size()));
                                int i = MainActivity.linkedList.getLast().index;
                                MainActivity.linkedList.getLast().isShown=false;
                                Point temp = (Point) clickedList.get(0);
                                clearAllPotentials();
                                clickedList.add(MainActivity.linkedList.getLast());
                                clickedList.add(temp);
                                MainActivity.linkedList.add(new Circle(clickedList, location, MainActivity.linkedList.size()));
                                clickedList.clear();
                                clickedList.add(MainActivity.linkedList.get(i));
                                MainActivity.linkedList.add(new HiddenThing(clickedList,location,MainActivity.linkedList.size()));
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureDistance:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==2) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == DISTANCE) {
                                    Distance temp = (Distance) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                if (unitChosen) {
                                    clickedList.add(MainActivity.linkedList.get(unitIndex));
                                } else {
                                    unitChosen = true;
                                    unitIndex = MainActivity.linkedList.size();
                                }
                                MainActivity.linkedList.add(new Distance(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureAngle:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==3) {                               // 
//                if clickedList.get(0).index>clickedList.get(2).index {    // this code would be used
//                    clickedList.insert(clickedList.get(0), at: 2)     // if we measured angles
//                    clickedList.insert(clickedList.get(3), at: 1)     // without handedness, i.e.
//                    clickedList.removeFirst()                     // only positive measures.
//                    clickedList.removeLast()                      //
//                }                                                 //
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == ANGLE) {
                                    Angle temp = (Angle) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(1).index == clickedList.get(1).index && temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(2).index == clickedList.get(2).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new Angle(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureSum:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==2) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == SUM) {
                                    Sum temp = (Sum) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new Sum(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureProduct:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==2) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == PRODUCT) {
                                    Product temp = (Product) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new Product(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureDifference:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==2) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == DIFFERENCE) {
                                    Difference temp = (Difference) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new Difference(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureRatio:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==2) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == RATIO) {
                                    Ratio temp = (Ratio) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                MainActivity.linkedList.add(new Ratio(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureSine:
                    case measureCosine:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==1) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type >= DISTANCE) {
                                    Measure temp = (Measure) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.type == whatToDo) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                switch (whatToDo) {
                                    case measureSine:
                                        MainActivity.linkedList.add(new Sine(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                    case measureCosine:
                                        MainActivity.linkedList.add(new Cosine(clickedList, location, MainActivity.linkedList.size()));
                                        break;
                                    default:
                                        break;
                                }
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                numberOfMeasures += 1;
                                clearAllPotentials();
                            }
                        }
                        break;
                    case measureTriArea:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==3) {
                            arrangeClickedObjectsByIndex();
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == TriAREA) {
                                    Triangle temp = (Triangle) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index && temp.parent.get(1).index == clickedList.get(1).index && temp.parent.get(2).index == clickedList.get(2).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                if (!unitChosen) { // if no unit length, create one
                                    unitChosen = true;
                                    unitIndex = MainActivity.linkedList.size();
                                    LinkedList<Construct> tempList = new LinkedList();
                                    tempList.add(clickedList.get(0));
                                    tempList.add(clickedList.get(1));
                                    MainActivity.linkedList.add(new Distance(tempList, location, MainActivity.linkedList.size()));
                                    MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                    numberOfMeasures += 1;
                                }
                                clickedList.add(MainActivity.linkedList.get(unitIndex));
                                MainActivity.linkedList.add(new Triangle(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                clearAllPotentials();
                                numberOfMeasures += 1;
                            }
                        }
                        break;
                    case measureCircArea:
                        getRidOfActivesThatAreTooFar(location);
                        clearActives();
                        getRidOfDuplicates();
                        if (clickedList.size()==1) {
                            boolean alreadyExists = false;
                            for (int i = 0; i < MainActivity.linkedList.size(); i++) {
                                if (MainActivity.linkedList.get(i).type == CircAREA) {
                                    CircleArea temp = (CircleArea) MainActivity.linkedList.get(i);
                                    if (!alreadyExists) {
                                        if (temp.parent.get(0).index == clickedList.get(0).index) {
                                            alreadyExists = true;
                                            MainActivity.linkedList.get(i).isShown = true;
                                            clearAllPotentials();
                                        }
                                    }
                                }
                            }
                            if (!alreadyExists) {
                                if (!unitChosen) { // if no unit length, create one
                                    unitChosen = true;
                                    unitIndex = MainActivity.linkedList.size();
                                    if (clickedList.get(0).parent.get(0).index < clickedList.get(0).parent.get(1).index) {
                                        LinkedList<Construct> tempList = new LinkedList();
                                        tempList.add(clickedList.get(0).parent.get(0));
                                        tempList.add(clickedList.get(0).parent.get(1));
                                        MainActivity.linkedList.add(new Distance(tempList, location, MainActivity.linkedList.size()));
                                    } else {
                                        LinkedList<Construct> tempList = new LinkedList();
                                        tempList.add(clickedList.get(0).parent.get(1));
                                        tempList.add(clickedList.get(0).parent.get(0));
                                        MainActivity.linkedList.add(new Distance(tempList, location, MainActivity.linkedList.size()));
                                    }
                                    clickedList.get(0).parent.get(0).isShown = true;
                                    clickedList.get(0).parent.get(1).isShown = true;
                                    MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                    numberOfMeasures += 1;
                                }
                                clickedList.add(MainActivity.linkedList.get(unitIndex));
                                MainActivity.linkedList.add(new CircleArea(clickedList, location, MainActivity.linkedList.size()));
                                MainActivity.linkedList.getLast().update(new CGPoint(24, 55 * numberOfMeasures));
                                clearAllPotentials();
                                numberOfMeasures += 1;
                            }
                        }
                        break;
                    case hideObject:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                            activeConstruct = false;
                        }
                        if (clickedList.size()==1) {
                            MainActivity.linkedList.get(clickedIndex.get(0)).isShown = false;
                            if (MainActivity.linkedList.getLast().type==HIDDENthing) {
                                MainActivity.linkedList.getLast().parent.add(clickedList.get(0));
                            } else {
                                MainActivity.linkedList.add(new HiddenThing(clickedList, location, MainActivity.linkedList.size()));
                            }
                            clearAllPotentials();
                        }
                        break;
                    case toggleLabel:
                        getRidOfActivesThatAreTooFar(location);
                        if (!activeConstruct) {
                            potentialClick = null;
                            activeConstruct = false;
                        }
                        if (clickedList.size()==1) {
                            MainActivity.linkedList.get(clickedIndex.get(0)).showLabel = !MainActivity.linkedList.get(clickedIndex.get(0)).showLabel;
                            clearAllPotentials();
                        }
                        break;
//                    case scaleEverything:
//                        if (event.getPointerCount() > 1) {
//                            lastRatio = 1.0;
//                            lastAngle = 0.0;
//                        }
//                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        for (int i=0;i<MainActivity.linkedList.size();i++) {
            MainActivity.linkedList.get(i).update(MainActivity.linkedList.get(i).coordinates);
        }
        invalidate();
        return true;
    }

    public void arrangeClickedObjectsByIndex() {
        if (clickedIndex.get(0)>clickedIndex.get(1)) {
            clickedList.add(2,clickedList.get(0));
            clickedList.removeFirst();
            clickedIndex.add(2,clickedIndex.get(0));
            clickedIndex.removeFirst();
        }
        if (clickedList.size()>=3) {
            if (clickedIndex.get(0)>clickedIndex.get(2)) {
                clickedList.add(2,clickedList.get(0));
                clickedList.add(1,clickedList.get(3));
                clickedList.removeFirst();
                clickedList.removeLast();
                clickedIndex.add(2,clickedIndex.get(0));
                clickedIndex.add(1,clickedIndex.get(3));
                clickedIndex.removeFirst();
                clickedIndex.removeLast();
            }
            if (clickedIndex.get(1)>clickedIndex.get(2)) {
                clickedList.add(clickedList.get(1));
                clickedList.remove(1);
                clickedIndex.add(clickedIndex.get(1));
                clickedIndex.remove(1);
            }
        }
    }
    public void getRidOfActivesThatAreTooFar(CGPoint location) {
        if (activeConstruct) {
            if (potentialClick.distance(location)>touchSense) {
                clearLastPotential();
            }
        }
    }
    public void getRidOfDuplicates() {
        if (clickedList.size()>1) {
            if (clickedIndex.get(0)==clickedIndex.get(1)) {
                clearLastPotential();
            }
            if (clickedList.size()==3) {
                if (clickedIndex.get(0)==clickedIndex.get(2) || clickedIndex.get(1)==clickedIndex.get(2)) {
                    clearLastPotential();
                }
            }
        }
    }
    public void getMeasure(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location)<touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if (MainActivity.linkedList.get(i).type>=DISTANCE) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void getPoint(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location)<touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if (MainActivity.linkedList.get(i).type>0 && MainActivity.linkedList.get(i).type<DISTANCE) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void getPointOrMeasure(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location)<touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if ((MainActivity.linkedList.get(i).type>0 && MainActivity.linkedList.get(i).type<MIDPOINT) || MainActivity.linkedList.get(i).type>=DISTANCE) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void getPointOrLineOrCircle(CGPoint location) {
        getPoint(location);
        if (!activeConstruct) {
            getLineOrCircle(location);
        }
    }
    public void getPointOrLineOrCircleAllowingRepeatConstructions(CGPoint location) {
        getPoint(location);
        if (!activeConstruct) {
            for (int i=0;i<MainActivity.linkedList.size();i++) {
                if (MainActivity.linkedList.get(i).distance(location)<touchSense && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                    if (MainActivity.linkedList.get(i).type <= 0) {
                        setActiveConstruct(i);
                    }
                }
            }
        }
    }
    public void getLineOrCircle(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location) < touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if (MainActivity.linkedList.get(i).type <= 0) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void getLine(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location) < touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if (MainActivity.linkedList.get(i).type < 0) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void getCircle(CGPoint location) {
        for(int i=0;i<MainActivity.linkedList.size();i++) {
            if (MainActivity.linkedList.get(i).distance(location) < touchSense && !clickedIndex.contains(i) && !activeConstruct && MainActivity.linkedList.get(i).isShown && MainActivity.linkedList.get(i).isReal) {
                if (MainActivity.linkedList.get(i).type == 0) {
                    setActiveConstruct(i);
                }
            }
        }
    }
    public void setActiveConstruct(int i) {
        activeConstruct=true;
        potentialClick=MainActivity.linkedList.get(i);
        clickedList.add(MainActivity.linkedList.get(i));
        clickedIndex.add(i);
    }
    public void clearActives() {
        if (activeConstruct) {
            potentialClick=null;
            activeConstruct=false;
        }
    }
    public void clearLastPotential() {
        clearActives();
        clickedList.removeLast();
        clickedIndex.removeLast();
    }
    public void clearAllPotentials() {
        clearActives();
        clickedIndex.clear();
        clickedList.clear();
    }
    public static int getUnitIndex() {
        return unitIndex;
    }
    public static void setUnitIndex(int index) {
        unitIndex=index;
    }
    public static void resetScaleFactor() {
        totalScaleFactor=1.0;
    }
    public static void resetUnitChosen() {
        unitChosen=false;
    }
    public static void decrementNumberOfMeasures() {
        numberOfMeasures-=1;
    }
    public static void resetNumberOfMeasures() { numberOfMeasures=1; }
}

