package edu.plu.heathdj.mocha;

import java.util.LinkedList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

class CGPoint {
    public double x,y;

    public CGPoint(double xx, double yy) {
        x=xx;
        y=yy;
    }
}

abstract class Construct {
    String[] character = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
    boolean isReal=true, isShown=true, showLabel=false;
    double value = -1.0;
    CGPoint coordinates;
    CGPoint slope = new CGPoint(1.0,0.0);
    CGPoint lastSlope = new CGPoint(1.0,0.0);
    LinkedList<Construct> parent = new LinkedList();
    int type = 0;
    int index = -1;
    int POINT = 1, PTonLINE = 2, PTonCIRCLE = 3, MIDPOINT = 4;
    int LINEintLINE = 5, FOLDedPT = 6, INVERTedPT=7;
    int CIRCintCIRC0 = 8,CIRCintCIRC1 = 9, LINEintCIRC0 = 10, LINEintCIRC1 = 11;
    int BiPOINT = 12, THREEptCIRCLEcntr=13, BELOCHpt0 = 14, BELOCHpt1 = 15;
    int BELOCHpt2 = 16, HIDDENthing=17, MOVedPT=18, FIXedPT=19;
    int DISTANCE = 20, ANGLE = 21, TriAREA=22, CircAREA=23;
    int SUM = 24, DIFFERENCE = 25, PRODUCT = 26, RATIO = 27, SINE=28, COSINE=29;
    int CIRCLE = 0;
    int LINE = -1, PERP = -2, PARALLEL = -3, BISECTOR0 = -4, BISECTOR1 = -5, BELOCHline0 = -7;
    int BELOCHline1 = -8, BELOCHline2 = -9, THREEptLINE = -10, SEGMENT = -11, RAY = -12;
    double epsilon = 0.000001;
    String textString="";
    float canvasWidth = 200, canvasHeight=200, strokeWidth=5;

    public Construct(CGPoint point, int number) {
        coordinates = point;
        index = number;
    }
    public Construct(LinkedList<Construct> ancestor, CGPoint point, int number) {
        coordinates = point;
        index = number;
        for (int i=0;i<ancestor.size();i++) {
            parent.add(ancestor.get(i));
        }
    }
    public void update(float width, float height) {
        canvasWidth = width;
        canvasHeight=height;
    }
    public void update(CGPoint point) {
        coordinates=point;
    }
    public void update(CGPoint point, double unitValue) {
    }
    //public void update(LinkedList<Construct> ancestor) {
    //}
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
    }
    public double distance(CGPoint point) {
        return 1024.0;
    }
    public double distance(CGPoint point1, CGPoint point2) {
        return Math.sqrt(Math.pow(point1.x-point2.x,2)+Math.pow(point1.y-point2.y,2));
    }
}

class FixedPoint extends Point {
    public FixedPoint(CGPoint point, int number) {
        super(point, number);
        type = FIXedPT;
    }
    public void update(CGPoint point) {}
}

class Point extends Construct {
    public Point(CGPoint point, int number) {
        super(point, number);
        type = POINT;
    }

    public Point(LinkedList<Construct> ancestor, CGPoint location, int number) {
        super(ancestor, location, number);
        type = POINT;
    }

    public void update(CGPoint point) {
        coordinates = point;
    }

    public double distance(CGPoint point) {
        return distance(point, coordinates);
    }

    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        paint.setStrokeWidth(strokeWidth);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.YELLOW);
        }
        if (type <= PTonCIRCLE) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle((float) (coordinates.x), (float) (coordinates.y), 3 * strokeWidth, paint);
        if (showLabel) {
            String string = character[index % 24] + Integer.toString(index / 24);
            paint.setTextSize(45);
            canvas.drawText(string, (float) (coordinates.x + 36), (float) (coordinates.y + 45), paint);
        }
    }
    public void invalidatePointOffSegment(int i, CGPoint location) {
        if (parent.get(i).parent.get(0).coordinates.x < parent.get(i).parent.get(1).coordinates.x) {
            if (location.x < parent.get(i).parent.get(0).coordinates.x || location.x > parent.get(i).parent.get(1).coordinates.x) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.x > parent.get(i).parent.get(1).coordinates.x) {
            if (location.x > parent.get(i).parent.get(0).coordinates.x || location.x < parent.get(i).parent.get(1).coordinates.x) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.y < parent.get(i).parent.get(1).coordinates.y) {
            if (location.y < parent.get(i).parent.get(0).coordinates.y || location.y > parent.get(i).parent.get(1).coordinates.y) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.y > parent.get(i).parent.get(1).coordinates.y) {
            if (location.y > parent.get(i).parent.get(0).coordinates.y || location.y < parent.get(i).parent.get(1).coordinates.y) {
                isReal = false;
            }
        }
    }
    public void invalidatePointOffRay(int i, CGPoint location) {
        if (parent.get(i).parent.get(0).coordinates.x < parent.get(i).parent.get(1).coordinates.x) {
            if (location.x < parent.get(i).parent.get(0).coordinates.x) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.x > parent.get(i).parent.get(1).coordinates.x) {
            if (location.x > parent.get(i).parent.get(0).coordinates.x) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.y < parent.get(i).parent.get(1).coordinates.y) {
            if (location.y < parent.get(i).parent.get(0).coordinates.y) {
                isReal = false;
            }
        } else if (parent.get(i).parent.get(0).coordinates.y > parent.get(i).parent.get(1).coordinates.y) {
            if (location.y > parent.get(i).parent.get(0).coordinates.y) {
                isReal = false;
            }
        }
    }
}

class PointOnLine extends Point {                                                  // parents: line
    public PointOnLine(LinkedList<Construct> ancestor, CGPoint point, int number) {  //
        super(ancestor, point, number);        // it needs a location
        update(point);
        type = PTonLINE;
        index = number;
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal) {
            isReal = false;
        } else {
            isReal = true;
            double x1 = parent.get(0).parent.get(0).coordinates.x, y1 = parent.get(0).parent.get(0).coordinates.y;
            double x0 = point.x, y0 = point.y, sx = parent.get(0).slope.x, sy = parent.get(0).slope.y;
            coordinates = new CGPoint((sx * sx * x0 + sx * sy * y0 - sx * sy * y1 + sy * sy * x1) / (sx * sx + sy * sy), (sx * sx * y1 + sx * sy * x0 - sx * sy * x1 + sy * sy * y0) / (sx * sx + sy * sy));
            // next we check to see if the point is on the segment/ray, and if not,
            // we put it at the closest point.
            if (parent.get(0).type == SEGMENT) {
                if (parent.get(0).parent.get(0).coordinates.x < parent.get(0).parent.get(1).coordinates.x) {
                    if (coordinates.x < parent.get(0).parent.get(0).coordinates.x) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    } else if (coordinates.x > parent.get(0).parent.get(1).coordinates.x) {
                        coordinates = parent.get(0).parent.get(1).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.x > parent.get(0).parent.get(1).coordinates.x) {
                    if (coordinates.x < parent.get(0).parent.get(1).coordinates.x) {
                        coordinates = parent.get(0).parent.get(1).coordinates;
                    } else if (coordinates.x > parent.get(0).parent.get(0).coordinates.x) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.y < parent.get(0).parent.get(1).coordinates.y) {
                    if (coordinates.y < parent.get(0).parent.get(0).coordinates.y) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    } else if (coordinates.y > parent.get(0).parent.get(1).coordinates.y) {
                        coordinates = parent.get(0).parent.get(1).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.y > parent.get(0).parent.get(1).coordinates.y) {
                    if (coordinates.y < parent.get(0).parent.get(1).coordinates.y) {
                        coordinates = parent.get(0).parent.get(1).coordinates;
                    } else if (coordinates.y > parent.get(0).parent.get(0).coordinates.y) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                }
            }
            if (parent.get(0).type == RAY) {
                if (parent.get(0).parent.get(0).coordinates.x < parent.get(0).parent.get(1).coordinates.x) {
                    if (coordinates.x < parent.get(0).parent.get(0).coordinates.x) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.x > parent.get(0).parent.get(1).coordinates.x) {
                    if (coordinates.x > parent.get(0).parent.get(0).coordinates.x) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.y < parent.get(0).parent.get(1).coordinates.y) {
                    if (coordinates.y < parent.get(0).parent.get(0).coordinates.y) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                } else if (parent.get(0).parent.get(0).coordinates.y > parent.get(0).parent.get(1).coordinates.y) {
                    if (coordinates.y > parent.get(0).parent.get(0).coordinates.y) {
                        coordinates = parent.get(0).parent.get(0).coordinates;
                    }
                }
            }
        }
    }
}


class PointOnCircle extends Point {                                                // parents: line
    public PointOnCircle(LinkedList<Construct> ancestor, CGPoint point, int number) {  //
        super(ancestor, point, number);      // it needs a location
        update(point);
        type = PTonCIRCLE;
        index = number;
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal || parent.get(0).parent.get(0).distance(point) < epsilon) {
            isReal = false;
        } else {
            isReal = true;
            double xx = point.x - parent.get(0).coordinates.x, yy = point.y - parent.get(0).coordinates.y;
            double ss = Math.sqrt(xx * xx + yy * yy);
            double rr = Math.sqrt(Math.pow(parent.get(0).parent.get(0).coordinates.x - parent.get(0).parent.get(1).coordinates.x, 2) + Math.pow(parent.get(0).parent.get(0).coordinates.y - parent.get(0).parent.get(1).coordinates.y, 2));
            coordinates = new CGPoint(xx * rr / ss + parent.get(0).coordinates.x, yy * rr / ss + parent.get(0).coordinates.y);
        }
    }
}
class MidPoint extends Point {                                                     // parents: point, point
    public MidPoint(LinkedList<Construct> ancestor, CGPoint point, int number) {  // this is usually
        super(ancestor, point, number);       // invisible
        update(point);
        type = MIDPOINT;
        index = number;
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            double x0 = parent.get(0).coordinates.x, y0 = parent.get(0).coordinates.y;
            double x1 = parent.get(1).coordinates.x, y1 = parent.get(1).coordinates.y;
            coordinates = new CGPoint((x0 + x1) / 2, (y0 + y1) / 2);
        }
    }
}
class LineIntLine extends Point {                                                  // parents: line, line
    public LineIntLine(LinkedList<Construct> ancestor, CGPoint point, int number) {  // this is the intersection
        super(ancestor, point, number);        // point of two lines
        type = LINEintLINE;
        update(point);
        index = number;
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            Line p0 = (Line) parent.get(0);
            {
                Line p1 = (Line) parent.get(1);
                {
                    double x0 = p0.parent.get(0).coordinates.x, y0 = p0.parent.get(0).coordinates.y;
                    double x1 = p1.parent.get(0).coordinates.x, y1 = p1.parent.get(0).coordinates.y;
                    double sx0 = p0.slope.x, sy0 = p0.slope.y, sx1 = p1.slope.x, sy1 = p1.slope.y;
                    if (Math.abs(sx0 * sy1 - sx1 * sy0) < epsilon) {
                        isReal = false;
                    } else {
                        coordinates = new CGPoint((sx0 * sx1 * y0 - sx0 * sx1 * y1 + sx0 * sy1 * x1 - sx1 * sy0 * x0) / (sx0 * sy1 - sx1 * sy0), (sx0 * sy1 * y0 - sx1 * sy0 * y1 - sy0 * sy1 * x0 + sy0 * sy1 * x1) / (sx0 * sy1 - sx1 * sy0));
                    }
                    // now if either parent is a segment or ray, we need to check whether the point of intersection is on it, and if not, set isReal=false
                    if (parent.get(0).type == SEGMENT) {
                        invalidatePointOffSegment(0,coordinates);
                    }
                    if (parent.get(1).type == SEGMENT) {
                        invalidatePointOffSegment(1,coordinates);
                    }
                    if (parent.get(0).type == RAY) {
                        invalidatePointOffRay(0,coordinates);
                    }
                    if (parent.get(1).type == RAY) {
                        invalidatePointOffRay(1,coordinates);
                    }
                }
            }
        }
    }
//    public double distance(CGPoint point) {
//        if (isReal) {
//            return Math.sqrt(Math.pow(coordinates.x - point.x, 2) + Math.pow(coordinates.y - point.y, 2));
//        } else {
//            return 1024;
//        }
//    }
}

class CircIntCirc0 extends Point {                                 // parent: circle, circle
    CGPoint alternateCoordinates = new CGPoint(0, 0),                 // the slope will be used as the lastPoint
            alternateSlope = new CGPoint(1, 0);                      // and alternates...will be used by CircIntCirc1

    public CircIntCirc0(LinkedList<Construct> ancestor, CGPoint point, int number) {  // this is the intersection
        super(ancestor, point, number);        // point of two lines
        update(point);
        slope = coordinates;
        alternateSlope = alternateCoordinates;
        type = CIRCintCIRC0;
        index = number;
    }

    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            double x0 = parent.get(0).coordinates.x, y0 = parent.get(0).coordinates.y;  // coordinates of center of circle0
            double sx0 = parent.get(0).slope.x, sy0 = parent.get(0).slope.y;            // coordinates of point on circle0
            double x1 = parent.get(1).coordinates.x, y1 = parent.get(1).coordinates.y;  // coordinates of center of circle1
            double sx1 = parent.get(1).slope.x, sy1 = parent.get(1).slope.y;            // coordinates of point on circle1
            double r0 = Math.pow(x0 - sx0, 2) + Math.pow(y0 - sy0, 2), r1 = Math.pow(x1 - sx1, 2) + Math.pow(y1 - sy1, 2);
            double discriminant = -(x1 * x1 * x1 * x1 - 4 * x0 * x1 * x1 * x1 + (6 * x0 * x0 + 2 * y0 * y0 - 4 * y0 * y1 + 2 * y1 * y1 - 2 * r0 - 2 * r1) * x1 * x1 + 4 * x0 * (-x0 * x0 - y0 * y0 + 2 * y0 * y1 - y1 * y1 + r0 + r1) * x1 + x0 * x0 * x0 * x0 + (2 * y0 * y0 - 4 * y0 * y1 + 2 * y1 * y1 - 2 * r0 - 2 * r1) * x0 * x0 + r0 * r0 + (-2 * y0 * y0 + 4 * y0 * y1 - 2 * y1 * y1 - 2 * r1) * r0 + (r1 - (y0 - y1) * (y0 - y1)) * (r1 - (y0 - y1) * (y0 - y1))) * (y0 - y1) * (y0 - y1);
            if (discriminant >= 0) {
                isReal = true;
                double xx = (Math.sqrt(discriminant) + x0 * x0 * x0 - x0 * x0 * x1 + (-x1 * x1 + y0 * y0 - 2 * y0 * y1 + y1 * y1 - r0 + r1) * x0 + x1 * x1 * x1 + (y0 * y0 - 2 * y0 * y1 + y1 * y1 + r0 - r1) * x1) / (2 * x0 * x0 - 4 * x0 * x1 + 2 * x1 * x1 + 2 * (y0 - y1) * (y0 - y1));
                double yy = y0 + Math.sqrt(-xx * xx + 2 * xx * x0 - x0 * x0 + r0);
                if (parent.get(1).distance(new CGPoint(xx, yy)) > epsilon || parent.get(0).distance(new CGPoint(xx, yy)) > epsilon) {
                    yy = y0 - Math.sqrt(-xx * xx + 2 * xx * x0 - x0 * x0 + r0);
                }
                coordinates = new CGPoint(xx, yy);
                double xxx = (-Math.sqrt(discriminant) + x0 * x0 * x0 - x0 * x0 * x1 + (-x1 * x1 + y0 * y0 - 2 * y0 * y1 + y1 * y1 - r0 + r1) * x0 + x1 * x1 * x1 + (y0 * y0 - 2 * y0 * y1 + y1 * y1 + r0 - r1) * x1) / (2 * x0 * x0 - 4 * x0 * x1 + 2 * x1 * x1 + 2 * (y0 - y1) * (y0 - y1));
                double yyy = y0 - Math.sqrt(-xxx * xxx + 2 * xxx * x0 - x0 * x0 + r0);
                if (parent.get(1).distance(new CGPoint(xxx, yyy)) > epsilon || parent.get(0).distance(new CGPoint(xxx, yyy)) > epsilon) {
                    yyy = y0 + Math.sqrt(-xxx * xxx + 2 * xxx * x0 - x0 * x0 + r0);
                }
                alternateCoordinates = new CGPoint(xxx, yyy);
            } else {
                isReal = false;
            }
        } else {
            isReal = false;
        }
        if (distance(coordinates, slope) + distance(alternateCoordinates, alternateSlope) > distance(coordinates, alternateSlope) + distance(alternateCoordinates, slope)) {
            CGPoint temp = alternateCoordinates;
            alternateCoordinates = coordinates;
            coordinates = temp;
        }
        slope = coordinates;
        alternateSlope = alternateCoordinates;
    }
}

class CircIntCirc1 extends Point {                                       // parent: circ, circ, cic0
    public CircIntCirc1(LinkedList<Construct> ancestor, CGPoint point, int number) {  // this is the intersection
        super(ancestor, point, number);
        CGPoint point0 = ancestor.get(0).coordinates;
        update(point0);
        type = CIRCintCIRC1;
        index = number;
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal && parent.get(2).isReal) {
            CircIntCirc0 temp = (CircIntCirc0) parent.get(2);
            isReal = temp.isReal;
            coordinates = temp.alternateCoordinates;
        } else {
            isReal = false;
        }
    }
}

class LineIntCirc0 extends Point {                                 // parent: circle, circle
    CGPoint alternateCoordinates = new CGPoint(0, 0),   // the slope will be used as the lastPoint
            alternateSlope = new CGPoint(1, 0);                      // and alternates...will be used by LineIntCirc1
    public LineIntCirc0(LinkedList<Construct> ancestor, CGPoint point, int number) {  //
        super(ancestor, ancestor.get(0).coordinates, number);
        update(ancestor.get(0).coordinates);
        slope = coordinates;
        alternateSlope = alternateCoordinates;
        type = LINEintCIRC0;
        index = number;
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            double x0 = parent.get(0).coordinates.x, y0 = parent.get(0).coordinates.y;  // coordinates of point on line
            double sx0 = parent.get(0).slope.x, sy0 = parent.get(0).slope.y;            // slope of line
            double x1 = parent.get(1).coordinates.x, y1 = parent.get(1).coordinates.y;  // coordinates of center of circle
            double sx1 = parent.get(1).slope.x, sy1 = parent.get(1).slope.y;            // coordinates of point on circle
            double r1 = Math.pow(x1 - sx1, 2) + Math.pow(y1 - sy1, 2);
            double discriminant = (-y0 * y0 + 2 * y0 * y1 - y1 * y1 + r1) * sx0 * sx0 + 2 * sy0 * (y0 - y1) * (x0 - x1) * sx0 + sy0 * sy0 * (-x0 * x0 + 2 * x0 * x1 - x1 * x1 + r1);
            if (discriminant >= 0) {
                isReal = true;
                double xx = (sx0 * sx0 * x1 + (-y0 + y1) * sy0 * sx0 + sy0 * sy0 * x0 + sx0 * Math.sqrt(discriminant)) / (sx0 * sx0 + sy0 * sy0),
                       yy = y1 - Math.sqrt(-xx * xx + 2 * xx * x1 - x1 * x1 + r1);
                if (parent.get(1).distance(new CGPoint(xx, yy)) > epsilon || parent.get(0).distance(new CGPoint(xx, yy)) > epsilon) {
                    yy = y1 + Math.sqrt(-xx * xx + 2 * xx * x1 - x1 * x1 + r1);
                }
                coordinates = new CGPoint(xx, yy);
                double xxx = (sx0 * sx0 * x1 + (-y0 + y1) * sy0 * sx0 + sy0 * sy0 * x0 - sx0 * Math.sqrt(discriminant)) / (sx0 * sx0 + sy0 * sy0),
                        yyy = y1 + Math.sqrt(-xxx * xxx + 2 * xxx * x1 - x1 * x1 + r1);
                if (parent.get(1).distance(new CGPoint(xxx, yyy)) > epsilon || parent.get(0).distance(new CGPoint(xxx, yyy)) > epsilon) {
                    yyy = y1 - Math.sqrt(-xxx * xxx + 2 * xxx * x1 - x1 * x1 + r1);
                }
                alternateCoordinates = new CGPoint(xxx, yyy);
            } else {
                isReal = false;
            }
        } else {
            isReal = false;
        }
        if (distance(coordinates, slope) + distance(alternateCoordinates, alternateSlope) > distance(coordinates, alternateSlope) + distance(alternateCoordinates, slope)) {
            CGPoint temp = alternateCoordinates;
            alternateCoordinates = coordinates;
            coordinates = temp;
        }
        slope = coordinates;
        alternateSlope = alternateCoordinates;
        // now we need to check if parent.get(0) is a segment or ray, and if so, whether the point of intersection is on it.  If not, set isReal=false. (And pass second root to LIC1)
        if (parent.get(0).type == SEGMENT) {
            invalidatePointOffSegment(0, coordinates);
        }
        if (parent.get(0).type == RAY) {
            invalidatePointOffRay(0, coordinates);
        }
    }
}

class LineIntCirc1 extends Point {                                       // parent: line, circ, lic0
    public LineIntCirc1(LinkedList<Construct> ancestor, CGPoint point, int number) {  //
        super(ancestor, ancestor.get(0).coordinates, number);
        update(ancestor.get(0).coordinates);
        type = LINEintCIRC1;
        index = number;
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            LineIntCirc0 temp = (LineIntCirc0) parent.get(2);
                double x0 = parent.get(0).coordinates.x, y0 = parent.get(0).coordinates.y; // coordinates of point on line
                double sx0 = parent.get(0).slope.x, sy0 = parent.get(0).slope.y;           // slope of line
                double x1 = parent.get(1).coordinates.x, y1 = parent.get(1).coordinates.y; // coordinates of center of circle
                double sx1 = parent.get(1).slope.x, sy1 = parent.get(1).slope.y;           // coordinates of point on circle
                double r1 = Math.pow(x1 - sx1, 2) + Math.pow(y1 - sy1, 2);
                double discriminant = (-y0 * y0 + 2 * y0 * y1 - y1 * y1 + r1) * sx0 * sx0 + 2 * sy0 * (y0 - y1) * (x0 - x1) * sx0 + sy0 * sy0 * (-x0 * x0 + 2 * x0 * x1 - x1 * x1 + r1);
                isReal = (discriminant >= 0);
                coordinates = temp.alternateCoordinates;
                if (parent.get(0).type == SEGMENT) {
                    invalidatePointOffSegment(0, coordinates);
                }
                if (parent.get(0).type == RAY) {
                    invalidatePointOffRay(0, coordinates);
                }
        } else {
            isReal = false;
        }
    }
}

class FoldedPoint extends Point {                                                  // parents: point, line
    public FoldedPoint(LinkedList<Construct> ancestor, CGPoint point, int number) {  // fold point with crease
        super(ancestor, ancestor.get(0).coordinates, number);
        update(point);
        type = FOLDedPT;
        index = number;
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            CGPoint point0 = parent.get(0).coordinates;
            CGPoint point1 = parent.get(1).parent.get(0).coordinates;
            double mx = parent.get(1).slope.x, my = parent.get(1).slope.y;
            double C = (mx * mx - my * my) / (mx * mx + my * my), S = 2 * mx * my / (mx * mx + my * my);
            double x0 = point0.x, y0 = point0.y, x1 = point1.x, y1 = point1.y;
            coordinates = new CGPoint(x0 * C + y0 * S + x1 - x1 * C - y1 * S, x0 * S - y0 * C + y1 + y1 * C - x1 * S);
        }
    }
}

class InvertedPoint extends Point {                                // parent: point, circle
    public InvertedPoint(LinkedList<Construct> ancestor, CGPoint point, int number) {  // invert point in circle
        super(ancestor, ancestor.get(0).coordinates, number);
        update(point);
        type = INVERTedPT;
        index = number;
    }

    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            CGPoint point0 = parent.get(0).coordinates;                  // point to invert
            CGPoint point1 = parent.get(1).parent.get(0).coordinates;    // center of circle
            double radius = parent.get(1).parent.get(0).distance(parent.get(1).slope); // radius of circle
            double distance = parent.get(1).parent.get(0).distance(point0);
            if (distance < epsilon) {
                isReal = false;
            } else {
                isReal = true;
                coordinates = new CGPoint(Math.pow(radius, 2) * (point0.x - point1.x) / Math.pow(distance, 2) + point1.x, Math.pow(radius, 2) * (point0.y - point1.y) / Math.pow(distance, 2) + point1.y);
            }
        }
    }
}

class BisectorPoint extends Point {                                                // parents: line, line
    public BisectorPoint(LinkedList<Construct> ancestor, CGPoint point, int number) {  // two cases: If the lines
        super(ancestor, point, number);     // intersect, then this is an
        update(point);      // IntPT. If not, then this pt needsto be any midpoint between the lines.
        type = BiPOINT;     // We find it by using the PT0 on L0, find the PerpLine using it & L1,
        index = number;     // intersect PL with L1, then the midpoint of PT0 and temp1.
    }
    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            Line p0 = (Line) parent.get(0);
            Line p1 = (Line) parent.get(1);
            double x0 = p0.parent.get(0).coordinates.x, y0 = p0.parent.get(0).coordinates.y;
            double x1 = p1.parent.get(0).coordinates.x, y1 = p1.parent.get(0).coordinates.y;
            double sx0 = p0.slope.x, sy0 = p0.slope.y, sx1 = p1.slope.x, sy1 = p1.slope.y;
            if (Math.abs(sx0 * sy1 - sx1 * sy0) < epsilon) {           // if the two lines are parallel
                LinkedList<Construct> temp = new LinkedList();
                temp.add(p0.parent.get(0));
                temp.add(p1);
                Construct temp0 = new PerpLine(temp, p1.coordinates, 0);
                temp.clear();
                temp.add(temp0);
                temp.add(p1);
                Construct temp1 = new LineIntLine(temp, p1.coordinates, 1);
                temp.clear();
                temp.add(p0.parent.get(0));
                temp.add(temp1);
                Construct temp2 = new MidPoint(temp, temp1.coordinates, 2);
                coordinates = temp2.coordinates;
            } else {                                    // otherwise they intersect
                coordinates = new CGPoint(
                        (sx0 * sx1 * y0 - sx0 * sx1 * y1 + sx0 * sy1 * x1 - sx1 * sy0 * x0) / (sx0 * sy1 - sx1 * sy0),
                        (sx0 * sy1 * y0 - sx1 * sy0 * y1 - sy0 * sy1 * x0 + sy0 * sy1 * x1) / (sx0 * sy1 - sx1 * sy0));
            }
        }
    }
}
class ThreePointCircleCntr extends Point { // parent: point, point, point, ThreePointLine
    public ThreePointCircleCntr(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = THREEptCIRCLEcntr;
        index = number;
    }
    public void update(CGPoint point) {
        isReal = (!parent.get(3).isReal && parent.get(0).isReal && parent.get(1).isReal && parent.get(2).isReal);
        if (isReal) {
            coordinates = parent.get(3).coordinates;
        }
    }
}

class Line extends Construct {                                                  // parents: point, point
    public Line(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type=LINE;
    }
    public void normalizeSlope() {
        double ds=Math.sqrt(Math.pow(slope.x,2)+Math.pow(slope.y,2));
        if (ds < epsilon) {
            isReal=false;
        } else {
            isReal=true;
            if (slope.x<0) {
                slope = new CGPoint(-slope.x/ds,-slope.y/ds);
            } else {
                slope = new CGPoint(slope.x/ds, slope.y/ds);
            }
        }
    }
    public double distance(CGPoint point) {
        if (isReal) {
            double x1 = parent.get(0).coordinates.x, y1 = parent.get(0).coordinates.y;
            double sx = slope.x, sy = slope.y;
            double x0 = point.x, y0 = point.y;
            if (sx * sx + sy * sy < epsilon) {
                isReal = false;
                return 1024;
            } else {
                isReal = true;
                return Math.sqrt((sx * y0 - sx * y1 - sy * x0 + sy * x1) * (sx * y0 - sx * y1 - sy * x0 + sy * x1) / (sx * sx + sy * sy));
            }
        } else {
            return 1024;
        }
    }
    public void update(CGPoint location) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            coordinates = parent.get(0).coordinates;
            slope = new CGPoint(coordinates.x - parent.get(1).coordinates.x,
            coordinates.y - parent.get(1).coordinates.y);
            normalizeSlope();
        }
    }
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            if (type == BISECTOR0 || type == BISECTOR1) {
                paint.setColor(Color.BLUE);
            } else if (type == BELOCHline0 || type == BELOCHline1 || type == BELOCHline2) {
                paint.setColor(Color.GREEN);
            } else {
                paint.setColor(Color.LTGRAY);
            }
        }
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float) (coordinates.x + 65536 * slope.x), (float) (coordinates.y + 65536 * slope.y), (float) (coordinates.x - 65536 * slope.x), (float) (coordinates.y - 65536 * slope.y), paint);

        if (showLabel) {
            String string = character[index % 24] + Integer.toString(index / 24);
            double xx = 10.0, yy = 10.0;
            if (Math.abs(slope.x) > epsilon) {
                if (coordinates.y + slope.y / slope.x * (20 - coordinates.x) > 60 && coordinates.y + slope.y / slope.x * (20 - coordinates.x) < canvasHeight-60) {
                    xx = 20 - slope.y * 20;
                    yy = coordinates.y + slope.y / slope.x * (20 - coordinates.x) + slope.x * 20;
                } else if (coordinates.y + slope.y / slope.x * (canvasWidth - 40 - coordinates.x) > 60 && coordinates.y + slope.y / slope.x * (canvasWidth - 40 - coordinates.x) < canvasHeight - 60) {
                    xx = canvasWidth - 100 - slope.y * 20;
                    yy = coordinates.y + slope.y / slope.x * (canvasWidth - 100 - coordinates.x) + slope.x * 20;
                } else if (Math.abs(slope.y) > epsilon) {
                    xx = coordinates.x - slope.x / slope.y * (coordinates.y - 45) - slope.y * 20;
                    yy = 60 + slope.x * 20;
                }
                if (type == BISECTOR0 || type == BISECTOR1) {
                    paint.setColor(Color.argb(255,32,64,255));
                }
                //paint.setColor(Color.WHITE);
                paint.setTextSize(45);
                canvas.drawText(string, (float) (xx), (float) (yy), paint);
            }
        }
    }
}
class Segment extends Line {                                                  // parents: point, point
    public Segment(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type = SEGMENT;
    }

    public double distance(CGPoint point) {
        LinkedList<Construct> tempList = new LinkedList();
        tempList.add(this);
        Construct temp = new PointOnLine(tempList, point, 0);
        temp.update(point);
        return Math.sqrt(Math.pow(temp.coordinates.x - point.x, 2) + Math.pow(temp.coordinates.y - point.y, 2));
    }

    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.LTGRAY);
        }
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float) (parent.get(0).coordinates.x), (float) (parent.get(0).coordinates.y), (float) (parent.get(1).coordinates.x), (float) (parent.get(1).coordinates.y), paint);

        if (showLabel) {
            String string = character[index % 24] + Integer.toString(index / 24);
            double xx = (coordinates.x + (parent.get(1).coordinates.x - coordinates.x) / 3) + slope.y * 20, yy = (coordinates.y + (parent.get(1).coordinates.y - coordinates.y) / 3) - slope.x * 20;
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            canvas.drawText(string, (float)xx, (float)yy, paint);
        }
    }
}

class Ray extends Line {                                                  // parents: point, point
    public Ray(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type = RAY;
    }

    public double distance(CGPoint point) {
        LinkedList<Construct> tempList = new LinkedList();
        tempList.add(this);
        Construct temp = new PointOnLine(tempList, point, 0);
        temp.update(point);
        return Math.sqrt(Math.pow(temp.coordinates.x - point.x, 2) + Math.pow(temp.coordinates.y - point.y, 2));
    }

    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.LTGRAY);
        }
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float) (parent.get(0).coordinates.x), (float) (parent.get(0).coordinates.y), (float) (coordinates.x + 256 * (parent.get(1).coordinates.x - coordinates.x)), (float) (coordinates.y + 256 * (parent.get(1).coordinates.y - coordinates.y)), paint);

        if (showLabel) {
            String string = character[index % 24] + Integer.toString(index / 24);
            double xx = xx = (coordinates.x + 1.4 * (parent.get(1).coordinates.x - coordinates.x)) + slope.y * 20, yy = (coordinates.y + 1.4 * (parent.get(1).coordinates.y - coordinates.y)) - slope.x * 20;
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            canvas.drawText(string, (float) xx, (float) yy, paint);
        }
    }
}

class PerpLine extends Line {                   // parents: point, line
    public PerpLine(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type = PERP;
        index = number;
    }

    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            coordinates = parent.get(0).coordinates;
            slope = new CGPoint(parent.get(1).slope.y, -parent.get(1).slope.x);
            normalizeSlope();
        }
    }
}

class ParallelLine extends Line {                   // parents: point, line
    public ParallelLine(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type = PARALLEL;
        index = number;
    }

    public void update(CGPoint point) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            coordinates = parent.get(0).coordinates;
            slope = parent.get(1).slope;
            normalizeSlope();
        }
    }
}

class Bisector0 extends Line {                            // parents: point, line, line
    public Bisector0(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, ancestor.get(0).coordinates, number);
        CGPoint point0 = parent.get(0).coordinates;
        update(point0);
        type = BISECTOR0;
        index = number;
    }
    public void update(CGPoint location) {
        coordinates = parent.get(0).coordinates;
        if (!parent.get(0).isReal || !parent.get(1).isReal || !parent.get(2).isReal) {
            isReal = false;
        } else {
            isReal = true;
            double c0 = parent.get(1).slope.x, c1 = parent.get(2).slope.x;
            double s0 = parent.get(1).slope.y, s1 = parent.get(2).slope.y;
            if (Math.abs(s1 * c0 - s0 * c1) < epsilon) {
                slope = parent.get(1).slope;
            } else if (Math.abs(s0 + s1) < epsilon) {
                slope = new CGPoint(1.0, 0.0);
            } else{
                isReal = true;
                CGPoint slope1 = new CGPoint(Math.sqrt((1 + c0 * c1 - s0 * s1) / 2), Math.sqrt((1 - c0 * c1 + s0 * s1) / 2) * Math.abs(s0 + s1) / (s0 + s1));
                CGPoint slope2 = new CGPoint(Math.sqrt((1 - c0 * c1 + s0 * s1) / 2), -Math.sqrt((1 + c0 * c1 - s0 * s1) / 2) * Math.abs(s0 + s1) / (s0 + s1));
                CGPoint point1 = new CGPoint(Math.cos(2 * Math.atan2(slope1.x, slope1.y)), Math.sin(2 * Math.atan2(slope1.x, slope1.y)));
                CGPoint point2 = new CGPoint(Math.cos(2 * Math.atan2(slope2.x, slope2.y)), Math.sin(2 * Math.atan2(slope2.x, slope2.y)));
                CGPoint point = new CGPoint(Math.cos(2 * Math.atan2(lastSlope.x, lastSlope.y)), Math.sin(2 * Math.atan2(lastSlope.x, lastSlope.y)));
                if (Math.sqrt(Math.pow(point1.x - point.x, 2) + Math.pow(point1.y - point.y, 2)) < Math.sqrt(Math.pow(point2.x - point.x, 2) + Math.pow(point2.y - point.y, 2))) {
                    slope = slope1;
                } else {
                    slope = slope2;
                }
            }
            lastSlope = slope;
            normalizeSlope();
        }
    }
}

class Bisector1 extends Line {                            // parents: point, line, line
    public Bisector1(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, ancestor.get(0).coordinates, number);
        CGPoint point0 = parent.get(0).coordinates;
        lastSlope = new CGPoint(0.0,1.0);
        update(point0);
        type = BISECTOR1;
        index = number;
    }
    public void update(CGPoint location) {
        coordinates = parent.get(0).coordinates;
        double c0 = parent.get(1).slope.x, c1 = parent.get(2).slope.x;
        double s0 = parent.get(1).slope.y, s1 = parent.get(2).slope.y;
        if (!parent.get(0).isReal || !parent.get(1).isReal || !parent.get(2).isReal || Math.abs(s0 * c1 - c0 * s1) < epsilon) {
            isReal = false;
        } else {
            isReal = true;
            if (Math.abs(s0 + s1) < epsilon) {
                slope = new CGPoint(1.0, 0.0);
            } else {
                CGPoint slope1 = new CGPoint(Math.sqrt((1 + c0 * c1 - s0 * s1) / 2), Math.sqrt((1 - c0 * c1 + s0 * s1) / 2) * Math.abs(s0 + s1) / (s0 + s1));
                CGPoint slope2 = new CGPoint(Math.sqrt((1 - c0 * c1 + s0 * s1) / 2), -Math.sqrt((1 + c0 * c1 - s0 * s1) / 2) * Math.abs(s0 + s1) / (s0 + s1));
                CGPoint point1 = new CGPoint(Math.cos(2 * Math.atan2(slope1.x, slope1.y)), Math.sin(2 * Math.atan2(slope1.x, slope1.y)));
                CGPoint point2 = new CGPoint(Math.cos(2 * Math.atan2(slope2.x, slope2.y)), Math.sin(2 * Math.atan2(slope2.x, slope2.y)));
                CGPoint point = new CGPoint(Math.cos(2 * Math.atan2(lastSlope.x, lastSlope.y)), Math.sin(2 * Math.atan2(lastSlope.x, lastSlope.y)));
                if (Math.sqrt(Math.pow(point1.x - point.x, 2) + Math.pow(point1.y - point.y, 2)) < Math.sqrt(Math.pow(point2.x - point.x, 2) + Math.pow(point2.y - point.y, 2))) {
                    slope = slope1;
                } else {
                    slope = slope2;
                }
            }
            lastSlope = slope;
            normalizeSlope();
        }
    }
}
class ThreePointLine extends Line {     // parent: point, point, point.
    public ThreePointLine(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = THREEptLINE;
        index = number;
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal && parent.get(2).isReal) {
            LinkedList<Construct> tempList = new LinkedList();
            tempList.add(parent.get(0));
            tempList.add(parent.get(1));
            Line temp0 = new Line(tempList, parent.get(0).coordinates, 0);
            if (temp0.distance(parent.get(2).coordinates) < epsilon) {
                isReal = true;
                slope = temp0.slope;
                coordinates = parent.get(0).coordinates;
            } else {
                isReal = false;
                MidPoint temp1 = new MidPoint(tempList, point, 1);
                Line temp2 = new Line(tempList, point, 3);
                tempList.removeLast();
                tempList.add(parent.get(2));
                MidPoint temp3 = new MidPoint(tempList, point, 2);
                Line temp4 = new Line(tempList, point, 4);
                tempList.clear();
                tempList.add(temp1);
                tempList.add(temp2);
                PerpLine temp5 = new PerpLine(tempList, point, 5);
                tempList.clear();
                tempList.add(temp3);
                tempList.add(temp4);
                PerpLine temp6 = new PerpLine(tempList, point, 6);
                tempList.clear();
                tempList.add(temp5);
                tempList.add(temp6);
                LineIntLine temp7 = new LineIntLine(tempList, point, 7);
                coordinates = temp7.coordinates;
            }
        } else {
            isReal = false;
        }
    }
}
class BelochPoint0 extends Point {                   // parents: point, point, line, line
    CGPoint[] points = {new CGPoint(0, 0), new CGPoint(0, 0), new CGPoint(0, 0)};
    CGPoint[] lastPoints = {new CGPoint(0, 0), new CGPoint(0, 0), new CGPoint(0, 0)};
    CGPoint[] slopes = {new CGPoint(0, 0), new CGPoint(0, 0), new CGPoint(0, 0)};
    CGPoint[] lastSlopes = {new CGPoint(0, 0), new CGPoint(0, 0), new CGPoint(0, 0)};
    boolean[] reals = {true, true, true};
    public BelochPoint0(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, ancestor.get(0).coordinates, number);
        update(ancestor.get(0).coordinates);
        type = BELOCHpt0;
        index = number;
    }
    public void update(CGPoint location) {
        if (parent.get(0).isReal && parent.get(1).isReal && parent.get(2).isReal && parent.get(3).isReal) {
            CGPoint point0 = parent.get(0).coordinates, point1 = parent.get(1).coordinates;
            CGPoint point2 = parent.get(2).parent.get(0).coordinates, line2 = parent.get(2).slope;
            CGPoint point3 = parent.get(3).parent.get(0).coordinates, line3 = parent.get(3).slope;
            double x0 = point0.x, y0 = point0.y, x1 = point1.x, y1 = point1.y;
            double x2 = point2.x, y2 = point2.y, x3 = point3.x, y3 = point3.y;
            double mx = 1.0;                                                        // and x4 = 0
            double sx2 = line2.x, sy2 = line2.y, sx3 = line3.x, sy3 = line3.y;
            double d = mx * mx * mx * sx3 * ((y0 + y2) * sx2 + sy2 * (x0 - x2)) - mx * mx * mx * sx2 * ((y1 + y3) * sx3 + sy3 * (x1 - x3));
            double c = -2 * mx * mx * sx3 * (sx2 * x0 - sy2 * y0) + sy3 * ((y0 + y2) * sx2 + sy2 * (x0 - x2)) * mx * mx + 2 * mx * mx * sx2 * (sx3 * x1 - sy3 * y1) - sy2 * ((y1 + y3) * sx3 + sy3 * (x1 - x3)) * mx * mx;
            double b = mx * sx3 * (-(y0 - y2) * sx2 - sy2 * (x0 + x2)) - 2 * sy3 * (sx2 * x0 - sy2 * y0) * mx - mx * sx2 * (-(y1 - y3) * sx3 - sy3 * (x1 + x3)) + 2 * sy2 * (sx3 * x1 - sy3 * y1) * mx;
            double a = sy3 * (-(y0 - y2) * sx2 - sy2 * (x0 + x2)) - sy2 * (-(y1 - y3) * sx3 - sy3 * (x1 + x3));
            LinkedList<Complex> mySolutions = Complex.cubicSolve(a, b, c, d);
            for (int i=0;i<3;i++) {
                if (i<mySolutions.size()) {
                    reals[i]=true;
                    double imaginaryPart=mySolutions.get(i).imag;
                    if (Math.abs(imaginaryPart)>0.0) {
                        reals[i]=false;
                    } else {
                        double my=mySolutions.get(i).real;
                        if (Math.abs((((y0+y2)*sx2+sy2*(x0-x2))*mx*mx-2*my*(sx2*x0-sy2*y0)*mx-my*my*((y0-y2)*sx2+sy2*(x0+x2)))/(2*mx*(mx*sx2+my*sy2))) < 4096.0) {
                            points[i]=new CGPoint(0.0,(((y0+y2)*sx2+sy2*(x0-x2))*mx*mx-2*my*(sx2*x0-sy2*y0)*mx-my*my*((y0-y2)*sx2+sy2*(x0+x2)))/(2*mx*(mx*sx2+my*sy2)));
                        } else {
                            points[i]=new CGPoint((((y0-y2)*sx2+sy2*(x0+x2))*my*my+2*mx*(sx2*x0-sy2*y0)*my-((y0+y2)*sx2+sy2*(x0-x2))*mx*mx)/(2*my*(mx*sx2+my*sy2)),0.0);
                        }
                        slopes[i]=new CGPoint(1.0, my);
                    }
                } else {
                    reals[i]=false;
                }
            }
            coordinates = points[0];
        }
        // Next we need to check whether we should permute the points/slopes based on their proximity to lastSlopes.

        CGPoint[] s = {new CGPoint(0, 0),new CGPoint(0, 0),new CGPoint(0, 0)};
        CGPoint[] ls = {new CGPoint(0, 0),new CGPoint(0, 0),new CGPoint(0, 0)};
        for (int i=0;i<2;i++) {
            for (int j=i+1;j<3;j++) {
                s[i]=new CGPoint(Math.cos(2*Math.atan2(slopes[i].x,slopes[i].y)),Math.sin(2*Math.atan2(slopes[i].x,slopes[i].y)));
                ls[i]=new CGPoint(Math.cos(2*Math.atan2(lastSlopes[i].x,lastSlopes[i].y)),Math.sin(2*Math.atan2(lastSlopes[i].x,lastSlopes[i].y)));
                s[j]=new CGPoint(Math.cos(2*Math.atan2(slopes[j].x,slopes[j].y)),Math.sin(2*Math.atan2(slopes[j].x,slopes[j].y)));
                ls[j]=new CGPoint(Math.cos(2*Math.atan2(lastSlopes[j].x,lastSlopes[j].y)),Math.sin(2*Math.atan2(lastSlopes[j].x,lastSlopes[j].y)));
                if ((Math.pow(s[i].x-ls[i].x,2)+Math.pow(s[i].y-ls[i].y,2)+Math.pow(s[j].x-ls[j].x,2)+Math.pow(s[j].y-ls[j].y,2)) >
                        (Math.pow(s[i].x-ls[j].x,2)+Math.pow(s[i].y-ls[j].y,2)+Math.pow(s[j].x-ls[i].x,2)+Math.pow(s[j].y-ls[i].y,2))) {
                    CGPoint temp0=points[i], temp1=slopes[i];
                    boolean temp2=reals[i];
                    points[i]=points[j];
                    slopes[i]=slopes[j];
                    reals[i]=reals[j];
                    points[j]=temp0;
                    slopes[j]=temp1;
                    reals[j]=temp2;
                }
            }
        }
        isReal = reals[0];
        coordinates = points[0];
        for (int i = 0; i < 3; i++) {
            if (reals[i]) {
                lastSlopes[i] = slopes[i];
                lastPoints[i] = points[i];
            }
        }
    }
}

class BelochLine0 extends Line {                                  // parents: point (T6P0)
    public BelochLine0(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        BelochPoint0 temp = (BelochPoint0) parent.get(0);
        update(point);
        type = BELOCHline0;
        index = number;
    }
    public void update(CGPoint point) {
        BelochPoint0 temp = (BelochPoint0) parent.get(0);
        coordinates = temp.points[0];
        slope = temp.slopes[0];
        isReal = temp.reals[0];
        normalizeSlope();
        isReal = (isReal && parent.get(0).isReal);
    }
}
class BelochPoint1 extends Point {                                  // parents: point (T6P0)
    public BelochPoint1(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = BELOCHpt1;
        index = number;
    }
    public void update(CGPoint point) {
        BelochPoint0 temp = (BelochPoint0) parent.get(0);
        isReal = temp.reals[1];
        coordinates = temp.points[1];
        slope = temp.slopes[1];
    }
}
class BelochLine1 extends Line {                                  // parents: T6P1, point T6P0
    public BelochLine1(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = BELOCHline1;
        index = number;
    }
    public void update(CGPoint point) {
        BelochPoint1 temp = (BelochPoint1) parent.get(0);
        isReal = temp.isReal;
        coordinates = temp.coordinates;
        slope = temp.slope;
        normalizeSlope();
        isReal = (isReal && parent.get(0).isReal);
    }
}
class BelochPoint2 extends Point {                                  // parents: point (T6P0)
    public BelochPoint2(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = BELOCHpt2;
        index = number;
    }
    public void update(CGPoint point) {
        BelochPoint0 temp = (BelochPoint0) parent.get(0);
        isReal = temp.reals[2];
        coordinates = temp.points[2];
        slope = temp.slopes[2];
    }
}
class BelochLine2 extends Line {                                  // parents: T6P2, point T6P0
    public BelochLine2(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        update(point);
        type = BELOCHline2;
        index = number;
    }
    public void update(CGPoint point) {
        BelochPoint2 temp = (BelochPoint2) parent.get(0);
        isReal = temp.isReal;
        coordinates = temp.coordinates;
        slope = temp.slope;
        normalizeSlope();
        isReal = (isReal && parent.get(0).isReal);
    }
}

class Circle extends Construct {                        // parent: point, point
    public Circle(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
        update(point);
        type = CIRCLE;
    }
    public void update(CGPoint location) {
        if (!parent.get(0).isReal || !parent.get(1).isReal) {
            isReal = false;
        } else {
            isReal = true;
            coordinates = parent.get(0).coordinates;
            slope = parent.get(1).coordinates;        // We use slope for coordinates
        }                                             // of second point
    }
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.argb(255,0,0,255));
        }
        paint.setStrokeWidth(strokeWidth);
        double radius = Math.sqrt(Math.pow(coordinates.x - slope.x, 2) + Math.pow(coordinates.y - slope.y, 2));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) (coordinates.x), (float) (coordinates.y), (float) (radius), paint);
        if (showLabel) {
            String string = character[index % 24] + Integer.toString(index / 24);
            paint.setColor(Color.argb(255,32,64,255));
            paint.setTextSize(45);
            double xx=slope.x-coordinates.x, yy=slope.y-coordinates.y, dd=Math.sqrt(xx*xx+yy*yy);
            canvas.drawText(string, (float) (yy/dd*(dd+45)+coordinates.x), (float) (-xx*(dd+45)/dd+coordinates.y), paint);
        }
    }
    public double distance(CGPoint point) {
        if (!isReal) {
            return 1024;
        } else {
            return Math.abs(Math.sqrt(Math.pow(parent.get(1).coordinates.x - parent.get(0).coordinates.x, 2) + Math.pow(parent.get(1).coordinates.y - parent.get(0).coordinates.y, 2)) - Math.sqrt(Math.pow(point.x - parent.get(0).coordinates.x, 2) + Math.pow(point.y - parent.get(0).coordinates.y, 2)));
        }
    }
}

class Measure extends Point {
    public Measure(LinkedList<Construct> ancestor, CGPoint point, int number) {  // (usually)             // the first must be a
        super(ancestor, ancestor.get(0).coordinates, number);       // point
    }
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        paint.setStyle(Paint.Style.STROKE);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.WHITE);
        }
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle((float) (coordinates.x), (float) (coordinates.y), 3 * strokeWidth, paint);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.LTGRAY);
        }
        String string = textString + " ≈ "+Double.toString(Math.round(10000*(value)+0.3)/10000.0);
        paint.setTextSize(42);
        canvas.drawText(string,(float)(coordinates.x + 30), (float)(coordinates.y+15),paint);
    }
    public int toRed(double x) {
        double temp = x-Math.floor(x);
        if (temp<1.0/3.0) {
            return (int)(765*(1.0/3.0-temp));
        } else if (temp>2.0/3.0) {
            return (int)(765*(temp-2.0/3.0));
        } else return 0;
    }
    public int toGreen(double x) {
        double temp = x-Math.floor(x);
        if (temp<1.0/3.0) {
            return (int)(765*(temp));
        } else if (temp>=1.0/3.0 && temp<2.0/3.0) {
            return (int)(765*(2.0/3.0-temp));
        } else return 0;
    }
    public int toBlue(double x) {
        double temp = x-Math.floor(x);
        if (temp>1.0/3.0 && temp<=2.0/3.0) {
            return (int)(765*(temp-1.0/3.0));
        } else if (temp>2.0/3.0) {
            return (int)(765*(1-temp));
        } else return 0;
    }
}
class Distance extends Measure {       // parents: point, point (for unit distance), or
    double measuredValue=1.0;      // parents: point, point, (unit) distance.
    public Distance(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates=parent.get(0).coordinates;
        type = DISTANCE;
        index=number;
        update(coordinates);
        parent.get(0).showLabel=true;
        parent.get(1).showLabel=true;
        showLabel=false;
        textString = character[index%24] + Integer.toString(index/24) + " : d(" + character[parent.get(0).index%24] + Integer.toString(parent.get(0).index/24) + "," + character[parent.get(1).index%24] + Integer.toString(parent.get(1).index/24) + ")";
    }
    public void update(CGPoint point) {
        boolean parentsAllReal = true;
        for (int i = 0; i < parent.size(); i++) {
            if (!parent.get(i).isReal) {
                parentsAllReal = false;
            }
        }
        if (parentsAllReal) {
            isReal = true;
            coordinates = point;
            measuredValue = Math.sqrt(Math.pow(parent.get(0).coordinates.x - parent.get(1).coordinates.x, 2) + Math.pow(parent.get(0).coordinates.y - parent.get(1).coordinates.y, 2));
            if (parent.size() == 3) {
                Distance temp = (Distance) parent.get(2);

                if (temp.measuredValue > epsilon) {
                    value = measuredValue / temp.measuredValue;
                } else {
                    isReal = false;
                }
            } else {
                value = 1.0;
            }
        } else {
            isReal = false;
        }
    }
}
class Angle extends Measure {
    public Angle(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        type = ANGLE;
        index = number;
        update(point);
        parent.get(0).showLabel = true;
        parent.get(1).showLabel = true;
        parent.get(2).showLabel = true;
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : ∠(" + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + "," + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24) + "," + character[parent.get(2).index % 24] + Integer.toString(parent.get(2).index / 24) + ")";
    }

    public void update(CGPoint point) {
        boolean parentsAllReal = true;
        for (int i = 0; i < parent.size(); i++) {
            if (!parent.get(i).isReal) {
                parentsAllReal = false;
            }
        }
        if (parentsAllReal) {
            isReal = true;
            coordinates = point;
            CGPoint p0 = parent.get(0).coordinates, p1 = parent.get(1).coordinates, p2 = parent.get(2).coordinates;
            double uDotV = (p0.x - p1.x) * (p2.x - p1.x) + (p0.y - p1.y) * (p2.y - p1.y);
            double normU = Math.sqrt(Math.pow(p0.x - p1.x, 2) + Math.pow(p0.y - p1.y, 2));
            double normV = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
            value = Math.acos(uDotV / (normU * normV)) * 180 / 3.141592653589793;
            value = value * Math.signum((p0.y - p1.y) * (p2.x - p1.x) - (p0.x - p1.x) * (p2.y - p1.y));
            // this last line makes left- and right-handed angles
        } else {
            isReal = false;
        }
    }
}
class Ratio extends Distance {
    public Ratio(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = RATIO;
        index = number;
        if (Math.abs(parent.get(1).value) > epsilon) {
            value = parent.get(0).value / parent.get(1).value;
        } else {
            isReal = false;
        }
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : " + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + " / " + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24);
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal && Math.abs(parent.get(1).value) > epsilon) {
            isReal = true;
            coordinates = point;
            value = parent.get(0).value / parent.get(1).value;
        } else {
            isReal = false;
        }
    }
}
class Product extends Distance {
    public Product(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = PRODUCT;
        index = number;
        value = parent.get(0).value * parent.get(1).value;
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : " + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + " ⋅ " + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24);
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            isReal = true;
            coordinates = point;
            value = parent.get(0).value * parent.get(1).value;
        } else {
            isReal = false;
        }
    }
}
class Sum extends Distance {
    public Sum(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = SUM;
        index = number;
        value = parent.get(0).value + parent.get(1).value;
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : " + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + " + " + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24);
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            isReal = true;
            coordinates = point;
            value = parent.get(0).value + parent.get(1).value;
        } else {
            isReal = false;
        }
    }
}
class Difference extends Distance {
    public Difference(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = DIFFERENCE;
        index = number;
        value = parent.get(0).value - parent.get(1).value;
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : " + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + " - " + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24);
    }
    public void update(CGPoint point) {
        if (parent.get(0).isReal && parent.get(1).isReal) {
            isReal = true;
            coordinates = point;
            value = parent.get(0).value - parent.get(1).value;
        } else {
            isReal = false;
        }
    }
}
class Sine extends Measure {
    public Sine(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = SINE;
        index = number;
        value = Math.sin(parent.get(0).value);
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : sin(" + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + ")";
    }

    public void update(CGPoint point) {
        if (parent.get(0).isReal) {
            isReal = true;
            coordinates = point;
            value = Math.sin(Math.PI*parent.get(0).value/180.0);
        } else {
            isReal = false;
        }
    }
}
class Cosine extends Measure {
    public Cosine(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        coordinates = parent.get(0).coordinates;
        type = COSINE;
        index = number;
        value = Math.cos(parent.get(0).value);
        showLabel = false;
        textString = character[index % 24] + Integer.toString(index / 24) + " : cos(" + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + ")";
    }

    public void update(CGPoint point) {
        if (parent.get(0).isReal) {
            isReal = true;
            coordinates = point;
            value = Math.cos(Math.PI*parent.get(0).value/180.0);
        } else {
            isReal = false;
        }
    }
}

class Triangle extends Measure { // parent: point, point, point, (unit) distance
    public Triangle(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        type = TriAREA;
        textString = character[index % 24] + Integer.toString(index / 24) + " : Δ(" + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + "," + character[parent.get(1).index % 24] + Integer.toString(parent.get(1).index / 24) + "," + character[parent.get(2).index % 24] + Integer.toString(parent.get(2).index / 24) + ")";
        for (int i = 0; i < 3; i++) {
            parent.get(i).showLabel = true;
        }
        showLabel = false;
        coordinates = point;
    }
    public void update(CGPoint point) {
        boolean[] labels = {parent.get(0).showLabel, parent.get(1).showLabel, parent.get(2).showLabel};
        boolean parentsAllReal = true;
        for (int i = 0; i < parent.size(); i++) {
            if (!parent.get(i).isReal) {
                parentsAllReal = false;
            }
        }
        if (parentsAllReal) {
            isReal = true;
            coordinates = point;
            LinkedList<Construct> tempList = new LinkedList();
            tempList.add(parent.get(0));
            tempList.add(parent.get(1));
            Line temp0 = new Line(tempList, point, 0);
            tempList.clear();
            tempList.add(parent.get(2));
            tempList.add(temp0);
            PerpLine temp1 = new PerpLine(tempList, point, 1);
            tempList.clear();
            tempList.add(temp0);
            tempList.add(temp1);
            LineIntLine temp2 = new LineIntLine(tempList, point, 2);
            tempList.clear();
            tempList.add(parent.get(0));
            tempList.add(parent.get(1));
            tempList.add(parent.get(3));
            Distance temp3 = new Distance(tempList, point, 3);
            tempList.clear();
            tempList.add(temp2);
            tempList.add(parent.get(2));
            tempList.add(parent.get(3));
            Distance temp4 = new Distance(tempList, point, 4);
            value = temp4.value * temp3.value / 2;
        } else {
            isReal = false;
        }
        for (int i = 0; i < 3; i++) {
            parent.get(i).showLabel = labels[i];
        }
    }
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        double x=(3*(double)index)/22.0;
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.argb(128, toRed(x), toGreen(x), toBlue(x)));
        }
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo((float) parent.get(0).coordinates.x, (float) parent.get(0).coordinates.y);
        path.lineTo((float) parent.get(1).coordinates.x, (float) parent.get(1).coordinates.y);
        path.lineTo((float) parent.get(2).coordinates.x, (float) parent.get(2).coordinates.y);
        path.lineTo((float) parent.get(0).coordinates.x, (float) parent.get(0).coordinates.y);
        path.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.argb(255,toRed(x),toGreen(x),toBlue(x)));
        }
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle((float) (coordinates.x), (float) (coordinates.y), 3 * strokeWidth, paint);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.LTGRAY);
        }
        String string = textString + " ≈ " + Double.toString(Math.round(10000 * (value) + 0.3) / 10000.0);
        paint.setTextSize(42);
        canvas.drawText(string, (float) (coordinates.x + 30), (float) (coordinates.y + 15), paint);
    }
}

class CircleArea extends Measure { // parent: circle, (unit) distance
    public CircleArea(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        type = CircAREA;
        parent.get(0).showLabel = true;
        textString = character[index % 24] + Integer.toString(index / 24) + " : ⦿(" + character[parent.get(0).index % 24] + Integer.toString(parent.get(0).index / 24) + ")";
        index=number;
    }
    public void update(CGPoint point) {
        Boolean[] labels = {parent.get(0).parent.get(0).showLabel, parent.get(0).parent.get(1).showLabel};
        boolean parentsAllReal = true;
        for (int i = 0; i < parent.size(); i++) {
            if (!parent.get(i).isReal) {
                parentsAllReal = false;
            }
        }
        if (parentsAllReal) {
            isReal = true;
            LinkedList<Construct> tempList = new LinkedList();
            tempList.add(parent.get(0).parent.get(0));
            tempList.add(parent.get(0).parent.get(1));
            tempList.add(parent.get(1));
            Distance temp = new Distance(tempList, point, 0);
            value = 3.141592653589793 * temp.value * temp.value;
            coordinates=point;
        } else {
            isReal = false;
        }
        parent.get(0).parent.get(0).showLabel = labels[0];
        parent.get(0).parent.get(1).showLabel = labels[1];
    }
    public void draw(Canvas canvas, Paint paint, boolean isNew) {
        double x=(3*(double)index)/22.0;
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.argb(128,toRed(x),toGreen(x),toBlue(x)));
        }
        paint.setStyle(Paint.Style.FILL);
        double radius = Math.sqrt(Math.pow(parent.get(0).coordinates.x - parent.get(0).slope.x, 2) + Math.pow(parent.get(0).coordinates.y - parent.get(0).slope.y, 2));
        canvas.drawCircle((float) (parent.get(0).coordinates.x), (float) (parent.get(0).coordinates.y), (float) (radius), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.argb(255,toRed(x),toGreen(x),toBlue(x)));
        canvas.drawCircle((float) (coordinates.x), (float) (coordinates.y), 3 * strokeWidth, paint);
        if (isNew) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.LTGRAY);
        }
        String string = textString + " ≈ " + Double.toString(Math.round(10000 * (value) + 0.3) / 10000.0);
        paint.setTextSize(42);
        canvas.drawText(string, (float) (coordinates.x + 30), (float) (coordinates.y + 15), paint);
    }
}

class HiddenThing extends Construct { // parent: any single
    public HiddenThing(LinkedList<Construct> ancestor, CGPoint point, int number) {
        super(ancestor, point, number);
        type = HIDDENthing;
        index = number;
    }
}

//class MovedPoint extends Construct { // parent: any single
//    CGPoint lastCoordinates=new CGPoint(0.0,0.0);
//    public MovedPoint(LinkedList<Construct> ancestor, CGPoint point, int number) {
//        super(ancestor, point, number);
//        type = MOVedPT;
//        index = number;
//        lastCoordinates=point;
//    }
//}



