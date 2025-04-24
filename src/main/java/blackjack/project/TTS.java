package blackjack.project;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS implements Runnable {

    private final String line;

    //TTS constructor
    public TTS(String line) {
        this.line = line;
    }

    //Implement the run method of the interface Runnable
    @Override
    public void run() {
        VoiceManager manager = VoiceManager.getInstance();
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = manager.getVoice("kevin");
        voice.allocate();
        voice.speak("you " + this.line);
        voice.deallocate();
    }
}
