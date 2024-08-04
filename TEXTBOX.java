import processing.core.PApplet;

// base code written by Mitko Nikov
public class TEXTBOX extends PApplet{
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

    public String Text = "";
    public int TextLength = 0;
    public int TextSelectedIndicator = 1;
    public boolean Censor = false;

    private boolean selected = false;


    TEXTBOX(PApplet P) {
        // CREATE OBJECT DEFAULT TEXTBOX
        p = P;
    }

    TEXTBOX(PApplet P, int x, int y, int w, int h) {
        X = x; Y = y; W = w; H = h; p = P;
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

            // DRAW INDICATOR PIPE AT TEXT END
        if(p.frameCount%(40) == 0) TextSelectedIndicator *= -1;
        if(selected && TextSelectedIndicator == 1) {
            if(Censor) {
                String cText = "";
                for(int i = 0; i < TextLength; i++){
                    cText = cText + "*";
                }
                p.text(cText + "|", X + (p.textWidth("a") / 2), Y + TEXTSIZE);
            } else {
                p.text(Text + "|", X + (p.textWidth("a") / 2), Y + TEXTSIZE);
            }
        } else {
            if(Censor) {
                String cText = "";
                for(int i = 0; i < TextLength; i++){
                    cText = cText + "*";
                }
                p.text(cText, X + (p.textWidth("a") / 2), Y + TEXTSIZE);
            } else {
                p.text(Text, X + (p.textWidth("a") / 2), Y + TEXTSIZE);
            }
        }
        p.popStyle();
    }

    // IF THE KEYCODE IS ENTER RETURN 1
    // ELSE RETURN 0
    boolean KEYPRESSED(char KEY, int KEYCODE) {
        if (selected) {
            if (KEYCODE == (int)BACKSPACE) {
                BACKSPACE();
            } else if (KEYCODE == 32) {
                // SPACE
                addText(' ');
            } else if (KEYCODE == (int)ENTER) {
                return true;
            } else {
                // CHECK IF VALID CHARACTER
                boolean isKeyValidCharacter= (KEY >= '!' && KEY <= '~');
                /* CHECK IF THE KEY IS A LETTER OR A NUMBER
                boolean isKeyCapitalLetter = (KEY >= 'A' && KEY <= 'Z');
                boolean isKeySmallLetter = (KEY >= 'a' && KEY <= 'z');
                boolean isKeyNumber = (KEY >= '0' && KEY <= '9');

                if (isKeyCapitalLetter || isKeySmallLetter || isKeyNumber) {
                    addText(KEY);
                }
                 */
                if (isKeyValidCharacter) {
                    addText(KEY);
                }
            }
        }

        return false;
    }

    private void addText(char text) {
        // IF THE TEXT WIDHT IS IN BOUNDARIES OF THE TEXTBOX
        if (p.textWidth(Text + text) < W) {
            Text += text;
            TextLength++;
        }
    }

    private void BACKSPACE() {
        if (TextLength - 1 >= 0) {
            Text = Text.substring(0, TextLength - 1);
            TextLength--;
        }
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
            selected = true;
        } else {
            selected = false;
        }
    }

    void censor(boolean input) {
        Censor = input;
    }
}