import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 22/03/16.
 */
public class Test_AC_Spectrum {

    public static void run() {

        Tester t = new Tester();
        System.out.println("Testing: AC_Spectrum interface");
        Boolean check = false;

        int len = 5;
        double[] freq = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0};
        AC_Spectrum spec = new AC_Spectrum(freq, amp);

        t.is(spec.getFrequency(0), 1.0, "initialise with spectrum & get freq 0");
        t.is(spec.getFrequency(3), 4.0, "initialise with spectrum & get fre1 3");
        t.is(spec.getAmplitude(0), 1.0, "initialise with spectrum & get amp 0");
        t.is(spec.getAmplitude(3), 1.0/4.0, "initialise with spectrum & get amp 0");

        spec.setAmplitude(0,0.5);
        t.is(spec.getAmplitude(0), 0.5, "set amplitude to valid value");
        spec.setAmplitude(0,-1);
        t.is(spec.getAmplitude(0), 0.0, "set amplitude <0 defaults to 0");
        spec.setAmplitude(0,10);
        t.is(spec.getAmplitude(0), 1.0, "set amplitude >0 defaults to 1");

        spec.setFrequency(1, 440.0);
        t.is(spec.getFrequency(1), 440.0, "set freq to valid value");

        // set invalid freq
        check = false;
        try {
            spec.setFrequency(1, -440.0);
        } catch( IllegalArgumentException e ) {
            check = true;
        }
        t.is(check, true, "set freq 1<freq0 throws exception");

        // construct invalid
        double[] freq1 = {1.5, 2.0, 3.0, 4.0, 5.0};
        check = false;
        try {
            spec = new AC_Spectrum(freq1, amp);
        } catch( IllegalArgumentException e ) {
            check = true;
        }
        t.is(check, true, "first freq != 1 throws exception");

        // initialise with amps only
        AC_Spectrum spec1 = new AC_Spectrum (amp);
        t.is(spec1.getFrequency(0), 1.0, "no spectrum defaults to harmonic spectrum 0");
        t.is(spec1.getFrequency(1), 2.0, "no spectrum defaults to harmonic spectrum 1");
        t.is(spec1.getFrequency(3), 4.0, "no spectrum defaults to harmonic spectrum 3");

        t.results();
    }


    public static void main(String[] args) {
        run();
    }

}
