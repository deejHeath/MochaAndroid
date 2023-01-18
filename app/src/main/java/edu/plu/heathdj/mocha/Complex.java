package edu.plu.heathdj.mocha;

import java.util.LinkedList;

class Complex {
    double real;
    double imag;
    public Complex(double r, double i) {
        real = r;
        imag = i;
    }
    public Complex(double r) {
        real = r;
        imag = 0;
    }
    public boolean isReal() {
        return (imag == 0);
    }
    public static Complex zero() {
        return new Complex(0.0, 0.0);
    }
    public static double realPart(Complex z) {
        return z.real;
    }
    public double angle() {return round(Math.atan2(this.imag,this.real));}
    public double modulus() {return round(Math.pow(this.real*this.real+this.imag*this.imag,.5));}
    public Complex toThe(Complex z) {
        double a=this.real, b=this.imag, c=z.real, d=z.imag;
        if (a==0 && b==0) return new Complex(0,0);
        else return new Complex(Math.cos(d/2*Math.log(a*a+b*b)+c*Math.atan2(b,a))*
                Math.exp(c/2*Math.log(a*a+b*b)-d*Math.atan2(b,a)),
                Math.sin(d/2*Math.log(a*a+b*b)+c*Math.atan2(b,a))*
                        Math.exp(c/2*Math.log(a*a+b*b)-d*Math.atan2(b,a)));
    }
    public Complex toThe(int n) {
        double a=this.real, b=this.imag;
        if (a==0 && b==0) return new Complex(0,0);
        else return new Complex(Math.cos(n*Math.atan2(b,a))*Math.pow((a*a+b*b),n/2.),
                Math.sin(n*Math.atan2(b,a))*Math.pow((a*a+b*b),n/2.));
    }
    public Complex root(int n, int k) {
        if (n==0) return new Complex(1,0);
        double a=this.real, b=this.imag, c=1./((double)(n));
        if (a==0 && b==0) return new Complex(0,0);
        else return new Complex(Math.cos(c*Math.atan2(b,a)+2*Math.PI*k*c)*Math.pow(a*a+b*b,c/2),
                Math.sin(c*Math.atan2(b,a)+2*Math.PI*k*c)*Math.pow(a*a+b*b,c/2));
    }
    public Complex root(int n) {
        return this.root(n,0);
    }
    public Complex times(Complex z) {
        return new Complex(this.real*z.real-this.imag*z.imag,this.real*z.imag+this.imag*z.real);
    }
    public Complex times(double x) {
        return new Complex(this.real*x,this.imag*x);
    }
    public Complex over(Complex z) {
        if (z.isEqual(new Complex(0,0))) return new Complex(0,0);
        else return new Complex((this.real*z.real+this.imag*z.imag)/(z.real*z.real+z.imag*z.imag),
                (z.real*this.imag-this.real*z.imag)/(z.real*z.real+z.imag*z.imag));
    }
    public Complex over(double x) {
        if (x==0) return new Complex(0,0);
        else return new Complex(this.real/x,this.imag/x);
    }
    public Complex plus(Complex z) {return new Complex(this.real+z.real,this.imag+z.imag);}
    public Complex minus(Complex z) {return new Complex(this.real-z.real,this.imag-z.imag);}
    public void setEqual(Complex z) {this.real=z.real;this.imag=z.imag;}
    public boolean isEqual(Complex z) {
        if (this.real==z.real && this.imag==z.real) return true;
        else return false;
    }
    public boolean isWithin(Complex z,double x) {
        if (((this.real-z.real)*(this.real-z.real)+(this.imag-z.imag)*(this.imag-z.imag))<x*x) return true;
        else return false;
    }
    public boolean isZero() {
        if (this.real==0 && this.imag==0) return true;
        else return false;
    }
    public double getReal() {return this.real;}
    public double getImag() {return this.imag;}
    public void setReal(double x) {this.real=x;}
    public void setImag(double y) {this.imag=y;}
    public String toString() {
        if (this.imag>=0) return ""+round(this.real)+"+"+round(this.imag)+"i";
        else return ""+round(this.real)+""+round(this.imag)+"i";
    }
    private double round(double x) {return Math.rint(100*x)/100.;}
    private double sign(double x) {if (x>0) return 1; else if (x<0) return -1; else return 0;}
    public static LinkedList<Complex> linearSolve(double a, double b) {
        LinkedList<Complex> solution = new LinkedList();
        if (a == 0) {
            return solution;
        } else {
            solution.add(new Complex(-b/a));
            return solution;
        }
    }public static LinkedList<Complex>  quadraticSolve(double a, double b, double c) {
        double threshold = 0.0001;
        if (a == 0) {
            return linearSolve(b, c);
        } else {
            LinkedList<Complex> roots = new LinkedList();
            double d = Math.pow(b, 2) - 4 * a * c; // discriminant
            // Check if discriminate is within the 0 threshold
            if (-threshold < d && d < threshold) {
                d = 0;
            }
            if (d > 0) {
                roots.add(new Complex((-b + Math.sqrt(d)) / (2 * a)));
                roots.add(new Complex((-b - Math.sqrt(d)) / (2 * a)));
            } else if (d == 0) {
                roots.add(new Complex(-b / (2 * a)));
                roots.add(new Complex(-b / (2 * a)));
            } else if (d< 0) {
                roots.add(new Complex(-b / (2 * a), Math.sqrt(-d) / (2 * a)));
                roots.add(new Complex(-b / (2 * a), -Math.sqrt(-d) / (2 * a)));
            }
            return roots;
        }
    }
    public static LinkedList<Complex> cubicSolve(double a, double b, double c, double d) {
        if (a == 0) {
            return quadraticSolve(b, c, d);
        }
        Complex[] r = {new Complex(0,0),new Complex(0,0),new Complex(0,0)};
        LinkedList<Complex> roots = new LinkedList();

        Complex bb=new Complex(1,0);
        Complex cc=new Complex(b/a,0);
        Complex dd=new Complex(c/a,0);
        Complex ee=new Complex(d/a,0);

        Complex w=cc.times(dd).times(36).minus(ee.times(108)).minus(cc.times(cc).times(cc).times(8));
        Complex v=dd.times(dd).times(dd).times(12).minus(dd.times(dd).times(3).times(cc).times(cc)).minus(dd.times(54).times(cc).times(ee)).plus(ee.times(ee).times(81)).plus(ee.times(12).times(cc).times(cc).times(cc));
        Complex x=v.root(2);
        Complex y=x.times(12);
        if (w.plus(y).modulus()<w.minus(y).modulus()) y=x.times(-12);
        Complex z=(w.plus(y)).root(3);
        r[0]=(z.times(z).minus(dd.times(12)).plus(cc.times(cc).times(4)).minus(z.times(cc).times(2))).over(z.times(6));
        r[1]=(z.times(z).times(-1).plus(dd.times(12)).minus(cc.times(cc).times(4)).minus(cc.times(4).times(z)).plus(new Complex(0,Math.sqrt(3)).times(z).times(z)).plus(new Complex(0,12*Math.sqrt(3)).times(dd)).minus(new Complex(0,4*Math.sqrt(3)).times(cc).times(cc))).over(z.times(12));
        r[2]=(z.times(z).minus(dd.times(12)).plus(cc.times(cc).times(4)).plus(cc.times(4).times(z)).plus(new Complex(0,Math.sqrt(3.)).times(z.times(z).plus(dd.times(12)).minus(cc.times(cc).times(4))))).over(z.times(-12));
        for (int i=0;i<3;i++) {
            if (Math.abs(r[i].imag)<0.000001) {
                r[i].imag=0.0;
            }
        }
        roots.add(r[0]);
        roots.add(r[1]);
        roots.add(r[2]);
        return roots;
        // This is pasted together from some code I got on the internet that was incorrect,
        // together with the correct solutions to the general cubic that I wrote in 2005.
    }
    public static LinkedList<Complex> quarticSolve(double a, double b, double c, double d, double e) {
        if (a == 0) {
            return cubicSolve(b, c, d, e);
        } else {  // Roots of quartic
            Complex bb = new Complex(b / a, 0), cc = new Complex(c / a, 0);
            Complex dd = new Complex(d / a, 0), ee = new Complex(e / a, 0);

            Complex u = (bb.times(cc).times(dd).times(-36)).minus(cc.times(ee).times(288)).plus(dd.times(dd).times(108)).plus(bb.times(bb).times(ee).times(108)).plus(cc.times(cc).times(cc).times(8));
            Complex v = ((dd.times(dd).times(bb).times(bb).times(18)).plus(dd.times(bb).times(ee).times(ee).times(576)).plus(ee.times(bb).times(bb).times(cc).times(cc).times(cc).times(12)).minus(dd.times(dd).times(cc).times(ee).times(432)).minus(bb.times(bb).times(cc).times(ee).times(ee).times(432)).minus(bb.times(dd).times(dd).times(dd).times(cc).times(54)).minus(bb.times(bb).times(cc).times(cc).times(dd).times(dd).times(3)).plus(bb.times(cc).times(cc).times(dd).times(ee).times(240)).minus(bb.times(bb).times(bb).times(cc).times(dd).times(ee).times(54)).plus(cc.times(cc).times(ee).times(ee).times(384)).minus(ee.times(cc).times(cc).times(cc).times(cc).times(48)).plus(dd.times(dd).times(cc).times(cc).times(cc).times(12)).plus(ee.times(ee).times(bb).times(bb).times(bb).times(bb).times(81)).minus(ee.times(ee).times(ee).times(768)).plus(bb.times(bb).times(bb).times(dd).times(dd).times(dd).times(12)).plus(dd.times(dd).times(dd).times(dd).times(81))).root(2).times(12);
            Complex x = new Complex(0, 0);
            Complex w = new Complex(0, 0);
            Complex y;
            for (int m = 0; m < 6; m++)
                if (x.isWithin(new Complex(0, 0), 1)) {
                    if (u.plus(v).modulus() > u.minus(v).modulus()) w = (u.plus(v)).root(3, m % 3);
                    else w = (u.minus(v)).root(3, m % 3);
                    y = (((bb.times(bb).times(w).times(3)).minus(cc.times(w).times(8)).plus(w.times(w).times(2)).minus(bb.times(dd).times(24)).plus(ee.times(96)).plus(cc.times(cc).times(8))).over(w)).root(2, m % 2);
                    if (y.modulus() > x.modulus()) x = y;
                }
            y = bb.over(-4).plus(x.times(Math.sqrt(3) / 12));
            u = ((bb.times(bb).times(w).times(x).times(18).minus(cc.times(w).times(x).times(48)).minus(x.times(w).times(w).times(6)).plus(bb.times(dd).times(x).times(72)).minus(ee.times(x).times(288)).minus(x.times(cc).times(cc).times(24)).plus(bb.times(cc).times(w).times(72 * Math.sqrt(3))).minus(w.times(bb).times(bb).times(bb).times(18 * Math.sqrt(3))).minus(w.times(dd).times(144 * Math.sqrt(3)))).over(w).over(x)).root(2).over(12);

            Complex[] r = {new Complex(0, 0), new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)};
            LinkedList<Complex> roots = new LinkedList();
            r[0] = y.plus(u);
            r[1] = y.minus(u);

            y = bb.over(-4).minus(x.times(Math.sqrt(3) / 12));
            u = ((x.times(w).times(bb).times(bb).times(3).minus(x.times(w).times(cc).times(8)).minus(x.times(w).times(w)).plus(bb.times(dd).times(x).times(12)).minus(x.times(ee).times(48)).minus(x.times(cc).times(cc).times(4)).minus(bb.times(cc).times(w).times(12 * Math.sqrt(3))).plus(w.times(bb).times(bb).times(bb).times(3 * Math.sqrt(3))).plus(w.times(dd).times(24 * Math.sqrt(3)))).over(w).over(x)).root(2).times(Math.sqrt(6) / 12);

            r[2] = y.plus(u);
            r[3] = y.minus(u);

            for (int i=0;i<3;i++) {
                if (Math.abs(r[i].imag)<0.000001) {
                    r[i].imag=0.0;
                }
            }
            roots.add(r[0]);
            roots.add(r[1]);
            roots.add(r[2]);
            roots.add(r[3]);
            return roots;
        }
    }  // This is from the same 2005 code, and is left here not for this program,
       // but for posterity.  There is no "quinticSolve" even possible.
}

