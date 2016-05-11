
// http://www.mitpressjournals.org/doi/abs/10.1162/LMJ_a_00102?journalCode=lmj#.Vuv5z9KLSUk
// http://quod.lib.umich.edu/i/icmc/bbp2372.1995.169?rgn=main;view=fulltext
//https://kb.osu.edu/dspace/bitstream/handle/1811/24077/EMR000007a-mashinter.pdf?sequence=1

/*
 * Note class for Atlas of Consonance app
 * holds "normalled" spectrum of the note (rootFreq &1hz), its rootFreq frequency
 * and its rootAmp
 */



public class AC_Note {

    private static final double MINFREQ = 20;
    private static final double MAXFREQ = 10000;

    private AC_Spectrum spectrum;
    private double rootFreq;
    private double rootAmp;

    AC_Note(AC_Spectrum spectrum, double rootFreq, double amplitude){
        this.spectrum = spectrum;
        setRootFrequency(rootFreq);
        setRootAmp(amplitude);
    }

    // index bounds checked in AC_Spectrum class
    public double getSpecFrequency(int i){
        return spectrum.getFrequency(i) * rootFreq;
    }

    // index bounds checked in AC_Spectrum class
    public double getSpecAmplitude(int i){
        return spectrum.getAmplitude(i) * rootAmp;
    }

    public double getRootFrequency(){
        return rootFreq;
    }

    private void setRootFrequency(double rootFreq){
        if(rootFreq<MINFREQ || rootFreq>MAXFREQ){
            throw new IllegalArgumentException("rootFreq frequency ("+rootFreq+") must be between "+ MINFREQ + " and "+MAXFREQ);
        }
        this.rootFreq = rootFreq;
    }

    public double getNoteAmplitude(){
        return rootAmp;
    }

    public double getFrequency(int i){
        return this.spectrum.getFrequency(i) * this.rootFreq;
    }


    public double getAmplitude(int i){
        return this.spectrum.getAmplitude(i) * this.rootAmp;
    }

    public void setRootAmp(double rootAmp){
        if(rootAmp <0.0){ rootAmp =0.0; }
        if(rootAmp >1.0){ rootAmp =1.0; }

        this.rootAmp = rootAmp;
    }

    public AC_Note getTransposition(double ratio){
        AC_Note newNote = new AC_Note(this.spectrum,
                                      this.rootFreq * ratio,
                                      this.rootAmp);
        return newNote;
    }

    public int size() {
        return this.spectrum.size();
    }



}