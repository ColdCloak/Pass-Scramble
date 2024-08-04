import processing.core.PApplet;

//Create a Timer based on frames updated
public class TIMER extends PApplet {
    //fields for its attributes
    public int frames;
    public int startingframes;
    public boolean active = true;
    //constructor
    public TIMER(int frames){
        this.frames = frames;
        startingframes = frames;
    }
    //methods
    public void startTimer(){
        active = true;
    }
    public void stopTimer(){
        active = false;
    }
    public void setTimer(int frames){
        this.frames = frames;
        startingframes = frames;
    }
    public void rewind(){
        frames = startingframes;
    }
    public void finish(){
        frames = 0;
    }
    public void update(){
        if (active && frames > 0) frames -= 1;
    }
    public boolean checkFinished(){
        if (frames <= 0) return true;
        else return false;
    }
}