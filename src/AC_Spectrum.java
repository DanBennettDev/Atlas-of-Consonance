
/*
 *  Class to handle frequency spectra for Atlas of Consonance program
 *  Spectrums are "normal" (freq 0 @ 1hz, others multiples thereof)
 *  Transposition handled in Note class
 *  Defaults to harmonic spectra if no frequencies entered 
 *  (frequencies = integer series) 
 *  Amplitudes must be between 0.0 and 1.0
 *  Freq n+1 must be > freq n.
 *  Partials are assumed to be in phase.
 */


import java.util.ArrayList;
import java.util.List;

public class AC_Spectrum {

    private List<Double> frequencies;
    private List<Double> amplitudes;    
    private int partialCount;
    
    AC_Spectrum(double[] frequencies, double[] amplitudes){
        if(frequencies==null){
            frequencies = makeHarmonicSeries(amplitudes.length);
        }
        if(frequencies.length!=amplitudes.length){
            throw new IllegalArgumentException("freq list diff length to amp list");
        }
        if(frequencies[0]!=1.0){
            throw new IllegalArgumentException("first freq must equal 1.0");
        }
        double prevF = 0.0, amp;
        this.frequencies = new ArrayList<>();
        this.amplitudes = new ArrayList<>();

        for(int i=0; i<frequencies.length; i++){
            if(frequencies[i]<prevF){
                throw new IllegalArgumentException("freq "+i+" < freq"+(i-1));
            }
            amp = putAmpInRange(amplitudes[i]);
            this.frequencies.add(frequencies[i]);
            this.amplitudes.add(amp);
        }
        this.partialCount = frequencies.length;
    }
    
    // defaults to natural integer series frequencies
    AC_Spectrum(double[] amplitudes){
        this(null, amplitudes);
    }

    double[] makeHarmonicSeries(int size){
        double[] freq = new double[size];
        for(int i=0; i< size; i++){
            freq[i] = (double)(i+1);
        }
        return freq;
    }




    // returns frequency for the 
    public double getFrequency(int i){
        if(i>=partialCount){
            throw new IndexOutOfBoundsException();
        }
        return this.frequencies.get(i);
    }

    public double getAmplitude(int i){
        if(i>=partialCount){
            throw new IndexOutOfBoundsException();
        }
        return this.amplitudes.get(i);
    }

    public void setFrequency(int i, double freq){
        if(i==0 && freq!=1.0){
            throw new IllegalArgumentException("first freq must equal 1.0");            
        }
        if(i>0 && freq<getFrequency(i-1)){
            throw new IllegalArgumentException("freq "+i+" < freq"+(i-1));
        }
        if(i>=partialCount){
            throw new IndexOutOfBoundsException();
        }
        this.frequencies.set(i, freq);
    }

    public void setAmplitude(int i, double amp){
        if(i>=partialCount){
            throw new IndexOutOfBoundsException();
        }
        amp = putAmpInRange(amp);
        this.amplitudes.set(i, amp);
    }

    // enforced that size of amplitudes=size of frequencies
    public int size(){
        return this.amplitudes.size();
    }

    private double putAmpInRange(double amp){
        if(amp<0.0){
            System.out.println("amp < 0.0. Setting amp to 0.0");
            amp=0.0;
        }
        if(amp>1.0){
            System.out.println("amp > 1.0. Setting amp to 1.0");
            amp=1.0;
        };
        return amp;
    }

}