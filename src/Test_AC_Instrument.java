/**
 * Testing for AC_Instrument class
 */
public class Test_AC_Instrument {

    public static void run() {

        Tester t = new Tester();
        System.out.println("Testing: AC_Instrument - interface");
        Boolean check = false;

        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0};
        AC_Spectrum spec = new AC_Spectrum(amp);
        AC_Note note = new AC_Note(spec, 100, 0.8);
        AC_Scale scale = new AC_Scale(9);

        AC_Instrument instr = new AC_Instrument(note, scale);

        t.is(instr.getRootFreq(0), 100.0, "note 0 freq is correct");
        t.is(t.round(scale.getInterval(2).getRatio_double(),4),1.1429, "scale saved correctly");
        t.is(t.round(instr.getRootFreq(2),2), 114.29, "note 2 freq is correct");
        t.is(t.round(instr.getRootFreq(16),2), 233.33, "note 16 freq is correct");
        t.is(t.round(instr.getRootFreq(21),2), 400.00, "last note freq is correct");




        t.results();
    }


    public static void main(String[] args) {
        run();
    }

}
