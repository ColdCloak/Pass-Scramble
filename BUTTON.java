import processing.core.PApplet;

public class BUTTON extends PApplet{
    public int X = 0, Y = 0, H = 35, W = 200;
    private PApplet p;
    public int TEXTSIZE = (int)Sketch.rem/3;

    // COLORS
    public int Background = color(140, 140, 140);
    public int Foreground = color(0, 0, 0);
    public int BackgroundSelected = color(160, 160, 160);
    public int selectedBorder = color(37, 122, 253);
    public int Border = color(30, 30, 30);

    public boolean BorderEnable = false;
    public int BorderWeight = 1;
    public int SelectedBorderWeight = 3;

    public String Text = "Button";
    public String selectedText = "Button";
    public int TextSelectedIndicator = 1;

    private boolean selected = false;


    BUTTON(PApplet P) {
        // CREATE OBJECT DEFAULT BUTTON
        p = P;
    }

    BUTTON(PApplet P, String title, int x, int y, int w, int h) {
        Text = title; selectedText = title; X = x; Y = y; W = w; H = h; p = P;
    }

    BUTTON(PApplet P, String title, String title2, int x, int y, int w, int h) {
        Text = title; selectedText = title2; X = x; Y = y; W = w; H = h; p = P;
    }

    void DRAW() {
        p.pushStyle();
        // SET DEFAULTS
        p.fill(Background);
        p.noStroke();
        // SET CONFIGS
        if (BorderEnable) {
            p.strokeWeight(BorderWeight);
            p.stroke(Border);
        }
        if(selected){
            p.strokeWeight(SelectedBorderWeight);
            p.stroke(selectedBorder);
            p.fill(BackgroundSelected);
        }

        p.rect(X, Y, W, H);

        // DRAWING THE TEXT ITSELF
        p.fill(Foreground);
        p.textSize(TEXTSIZE);
        if(selected) {
            p.text(selectedText, X + (W/2) - (p.textWidth(Text) / 2), Y + TEXTSIZE);
        } else {
            p.text(Text, X + (W/2) - (p.textWidth(Text) / 2), Y + TEXTSIZE);
        }
        p.popStyle();
    }

    // FUNCTION FOR TESTING IS THE POINT
    // OVER THE TEXTBOX
    public boolean overBox(int x, int y, double scaling) {
        if (x >= X*scaling && x <= X*scaling + W*scaling) {
            if (y >= Y*scaling && y <= Y*scaling + H*scaling) {
                return true;
            }
        }

        return false;
    }

    void PRESSED(int x, int y, float scaling) {
        if (overBox(x, y, scaling)) {
            if(selected) selected = false;
            else selected = true;
        }
    }

    public boolean getState() {
        return selected;
    }

    public void setState(boolean input) {
        selected = input;
    }
}
