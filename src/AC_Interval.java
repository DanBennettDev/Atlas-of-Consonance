/**
 *  Interval class for Atlas of Consonance
 *  stores interval as a ratio of integers and in cents
 *  returns in various utility formats
 */
public class AC_Interval {

    private double cents;
    private int ratio_top;
    private int ratio_bottom;
    private boolean isJust;

    AC_Interval(int ratio_top, int ratio_bottom){
        if(ratio_top<ratio_bottom){
            throw new IllegalArgumentException("Top of ratio must be > bottom.");
            }
        if(ratio_top<1 || ratio_bottom<1){
            throw new IllegalArgumentException("Neither part of ratio may be <1" +
                        "values: " + ratio_top + " " + ratio_bottom);
        }
        this.ratio_bottom = ratio_bottom;
        this.ratio_top = ratio_top;
        this.isJust = true;
        cents = ratioToCents(ratio_top, ratio_bottom);
    }

    AC_Interval(double cents){
        if(cents<0){
            throw new IllegalArgumentException("interval in cents must be positive");
        }
        this.ratio_bottom = 0;
        this.ratio_top = 0;
        this.isJust = false;
        cents = cents;
    }

    private double ratioToCents(int ratio_top, int ratio_bottom){
        return 1200.0 * AC_log2((double)this.ratio_top / (double)this.ratio_bottom);
    }

    private double AC_log2(double num)
    {
        return (Math.log(num)/Math.log(2));
    }

    public String getRatio_string(){
        return "" + this.ratio_top + "/" + this.ratio_bottom;
    }

    public double getRatio_double(){
        return (double)this.ratio_top / (double)this.ratio_bottom;
    }

    public double getCents(){
        return this.cents;
    }
    public int getRatioTop(){ return this.ratio_top;}
    public int getRatioBottom(){ return this.ratio_bottom;}

    // TODO - reduce to lowest form after raising (eg 9/5 not 18/10)
    public AC_Interval raiseByOctave(int octaves){
        if(octaves<1){
            throw new IllegalArgumentException("must raise by at least 1 octave");
        }
        return new AC_Interval((int)(this.ratio_top*Math.pow(2,octaves)),
                                    this.ratio_bottom);
    }
}
