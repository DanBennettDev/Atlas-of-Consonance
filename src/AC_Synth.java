

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.PauseTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.OscillatorBank;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Audio Synthesis for Atlas of Consonance
 *
 */
public class AC_Synth extends AC_SoundPresenter {



    private AudioContext ac;
    private OscillatorBank voice1;
    private OscillatorBank voice2;
    private OscillatorBank voice0;
    private Envelope amplitudeEnvelope;
    private Gain amplifier;


    public AC_Synth(AC_Map model){
        super(model);
        this.ac = new AudioContext();
        this.voice1 = new OscillatorBank(ac, Buffer.SINE, noteX.length);
        this.voice2 = new OscillatorBank(ac, Buffer.SINE, noteY.length);
        this.voice0 = new OscillatorBank(ac, Buffer.SINE, noteY.length);
        this.amplitudeEnvelope = new Envelope(ac, 0);
        envelopeSetup(this.envelope);
        this.amplifier =  new Gain(ac, 1, this.amplitudeEnvelope);
        amplifier.addInput(this.voice1);
        amplifier.addInput(this.voice2);
        amplifier.addInput(this.voice0);
        ac.out.addInput(amplifier);
        update();
        pause(true);
        ac.start();
    }

    public void close(){
        this.amplifier.kill();
        this.amplitudeEnvelope.kill();
        this.voice1.kill();
        this.voice2.kill();
        this.ac.stop();
    }

    private void update(){
        this.voice1.setFrequenciesAndGains(noteX);
        this.voice2.setFrequenciesAndGains(noteY);
        this.voice0.setFrequenciesAndGains(note0);
        envelopeSetup(this.envelope);
    }

    @Override
    public void play(){
        update();
        pause(false);
    }

    public void pause(boolean isPaused){
        this.amplifier.pause(isPaused);
        this.amplitudeEnvelope.pause(isPaused);
        this.voice1.pause(isPaused);
        this.voice2.pause(isPaused);
        this.voice0.pause(isPaused);

    }

    // set up timed events for audio envelope and pausing sound objects
    private void envelopeSetup(float[] env){
        if(env.length!=5){
            throw new IllegalArgumentException(
                    "envelope needs attack, decay, sustain, sustain level, " +
                            "release, in that order");
        }
        // stop the envelope
        float stop = this.amplitudeEnvelope.getCurrentValue();
        this.amplitudeEnvelope.setValue(stop);

        this.amplitudeEnvelope.addSegment(this.gain, env[0]);
        this.amplitudeEnvelope.addSegment(env[3]*this.gain, env[1]);
        this.amplitudeEnvelope.addSegment(env[3]*this.gain, env[2]);
        this.amplitudeEnvelope.addSegment(0.0f, env[4], new PauseTrigger(this.voice1));
        this.amplitudeEnvelope.addSegment(0,1,new PauseTrigger(this.voice2));
        this.amplitudeEnvelope.addSegment(0,1,new PauseTrigger(this.amplitudeEnvelope));
        this.amplitudeEnvelope.addSegment(0,1,new PauseTrigger(this.amplifier));
    }


}
