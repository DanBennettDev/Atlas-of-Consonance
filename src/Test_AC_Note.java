/**
 * Created by dan on 22/03/16.
 */
public class Test_AC_Note {

    public static void run() {

        Tester t = new Tester();
        System.out.println("Testing: AC_Note - interface");
        Boolean check = false;

        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0};
        AC_Spectrum spec = new AC_Spectrum(amp);

        AC_Note note = new AC_Note(spec, 440.0, 0.8);

        t.is(note.getNoteAmplitude(), 0.8, "constructor sets note amplitude");
        t.is(note.getRootFrequency(), 440.0, "constructor sets note amplitude");
        t.is(note.getSpecFrequency(4), 5.0*440, "freq of partials scaled by root Freq 4");
        t.is(note.getSpecFrequency(0), 1.0*440, "freq of partials scaled by root Freq 0");
        t.is(note.getSpecAmplitude(4), (1.0/5.0)*0.8, "amp of partials scaled by base amplitude");


        // construct invalid note

        note.setRootAmp(0.9);
        t.is(note.getNoteAmplitude(), 0.9, "set note amplitude");
        t.is(note.getSpecAmplitude(4), (1.0/5.0)*0.9, "amp of partials scaled by base amplitude after re set");


        note.setRootAmp(10);
        t.is(note.getNoteAmplitude(), 1.0, "set note amplitude too high defaults to 1.0");
        note.setRootAmp(-1);
        t.is(note.getNoteAmplitude(), 0.0, "set note amplitude too low defaults to 0.0");

        t.is(note.getTransposition(2.0).getRootFrequency() ,880.0, "transposition up works");
        t.is(note.getTransposition(0.5).getRootFrequency() ,220.0, "transposition down works");

        t.results();
    }


    public static void main(String[] args) {
        run();
    }

}
