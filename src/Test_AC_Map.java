/**
 * Testing for AC_Map class
 */
public class Test_AC_Map {

    public static void run() {

        Tester t = new Tester();
        Boolean check = false;

        // sawtooth wave  approximation
        double[] amp = {1.0, 1.0/2.0, 1.0/3.0, 1.0/4.0, 1.0/5.0, 1.0/6.0};
        AC_Spectrum spec = new AC_Spectrum(amp);
        AC_Note note1 = new AC_Note(spec, 256, 0.8);
        AC_Scale scale = new AC_Scale(9);
        AC_Instrument instr = new AC_Instrument(note1, scale);

        AC_Map map = new AC_Map(instr);

        // quick tests on basic formula
        t.is(map.calcRoughness_pair(256, 1, 256, 1),0.0,
                "unison, single partial gives 0 roughness") ;
        t.is(t.round(map.calcRoughness_pair(256, 1, 256* 1.059463, 1),6) ,0.084831,
                "min 2nd, single partial, gives correct roughness") ;
        t.is(map.calcRoughness_pair(256, 1, 256* 1.059463, 0), 0.0,
                "min 2nd, single partial, 0 amplitude on note 2: 0 roughness") ;

        t.is(map.getNoteRatio(0),1.0, "first note is ratio 1");
        t.is(map.getNoteRatio(1),1.125, "2nd note ratio is correct");
        t.is(map.getNoteRatio(10),1.6, "10th note ratio is correct");
        t.is(map.getNoteRatio(21),4.0, "last note is at 4.5");


        // Get correct results for Vasillakis test - Equal Temperament scale on root 256hz,
        // with sawtooth spectrum (partial n amplitude - 1/n).
        AC_Note note2 = note1;
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.007764,
            "unison correct value");
        note2 = note1.getTransposition(1.059463);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.389706,
            "m2 correct value");
        note2 = note1.getTransposition(1.122462);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.262114,
            "M2 correct value");
        note2 = note1.getTransposition(1.189207);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.190209,
            "m3 correct value");
        note2 = note1.getTransposition(1.259921);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.160353,
            "M3 correct value");
        note2 = note1.getTransposition(1.33484);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.098744,
            "4 correct value");
        note2 = note1.getTransposition(1.414214);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.121525,
            "Tt correct value");
        note2 = note1.getTransposition(1.498307);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.039254,
            "5 correct value");
        note2 = note1.getTransposition(1.587401);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.094857,
            "A5 correct value");
        note2 = note1.getTransposition(1.681793);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.052731,
            "6 correct value");
        note2 = note1.getTransposition(1.781797);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.054106,
            "m7 correct value");
        note2 = note1.getTransposition(1.887749);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.062231,
            "M7 correct value");
        note2 = note1.getTransposition(2.0);
        t.is(t.round(map.calcRoughness_complex(note1, note2),6),0.002111,
            "octave correct value");

        // now look at roughnesses converted to consonances for 9-limit, just
        // intonation scale generated on construction
        t.is(t.round(map.getConsonance(0,0),6),0.973669, "high consonance on unison");

        t.is(t.round(map.getConsonance(0,4),6),0.633692, "correct consonance on 0 with 4");
        t.is(t.round(map.getConsonance(4,0),6),0.633692, "identical consonance on 4 with 0");
        t.is(t.round(map.getConsonance(0,7),6),0.774069, "correct consonance on 0 with 7");


        // refactored to support 3 voices - test
        // set up major triad
        note2 = note1.getTransposition(1.259921);   // M3
        AC_Note note3 = note1.getTransposition(1.498307);   //5

        t.is(t.round(map.calcRoughness_complex(note1, note2, note3),6),2.147074,
                "correct value for major triad");




        t.results();
    }

    public static void main(String[] args) {
        run();
    }

}
