/**
 * Testing for AC_Interval class
 */
public class Test_AC_Interval {

    public static void run() {

        Tester t = new Tester();
        System.out.println("Testing: AC_Interval - interface");
        Boolean check = false;

        AC_Interval interval = new AC_Interval(3, 2);

        t.is(interval.getRatioTop(), 3, "ratio top value is as set in constructor");
        t.is(interval.getRatioBottom(),2, "ratio bottom value is as set in constructor");
        t.is(interval.getRatio_double(), 3.0/2.0, "ratio double value is correct");
        t.is(t.round(interval.getCents(),2), 701.96, "cents calculated correctly");
        t.is(interval.getRatio_string(), "3/2", "ratio to string works");

        t.is(interval.raiseByOctave(1).getRatio_double(),
                interval.getRatio_double()*2.0, "raise by octave doubles ratio");
        t.is(interval.raiseByOctave(2).getRatio_double(),
                interval.getRatio_double()*4.0, "raise by 2 octaves quadruples ratio");

        t.results();
    }


    public static void main(String[] args) {
        run();
    }

}
