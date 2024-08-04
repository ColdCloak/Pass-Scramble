import processing.awt.PSurfaceAWT;
import processing.core.*;
import processing.core.PSurface;
import java.awt.event.InputEvent;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.Robot;
import java.awt.AWTException;

public class Sketch extends PApplet {
    public static PApplet p;
    public ArrayList<TEXTBOX> textboxes = new ArrayList<TEXTBOX>();
    public ArrayList<BUTTON> buttons = new ArrayList<BUTTON>();
    public TEXTBOX saltTB;
    public BUTTON saltB;
    public String salt;
    public int saltedNumber;
    public static int rem;
    public float pScale;
    public boolean hoverTEXTBOXES = false;
    public BigInteger workingBase = new BigInteger("0");
    public BigInteger workingKey = new BigInteger("0");
    public BigInteger workingSalt = new BigInteger("0");
    public BigInteger workingMix = new BigInteger("0");
    public String workingString = "";
    public String postString = "";
    public String scrambleOutput = "";
    public String clipboardString = "This text will be copied into clipboard";
    public boolean output = false;
    public Robot robot;
    public boolean firstTime = false;
    public TIMER killSwitch;
    public boolean METADATA = false;


    public void settings(){
        p = this;
        if(p.displayHeight > 1440){
            size(1200, 800);
        }
        else if(p.displayHeight > 900){
            size(900, 600);
        }
        else {
            size(500, 500);
        }
        rem = 96;
        pScale = (float)0.5;
    }
    public void setup() {
        p.getSurface().setTitle("Pass Scramble");
        p.getSurface().setResizable(true);
        PImage icon = loadImage("icon.png");
        icon.resize(256,256);
        p.getSurface().setIcon(icon);
        // USERNAME TEXTBOX
        TEXTBOX inputTB = new TEXTBOX(p,0, 0, 800, (int)(rem/2));
        inputTB.Background = color(20,20,20);
        inputTB.BackgroundSelected = color(40,40,40);
        inputTB.Foreground = color(255,255,255);
        inputTB.Border = color(80,80,80);
        inputTB.BorderEnable = true;

        // PASSWORD TEXTBOX
        // CONFIGURED USING THE CLASS CONSTRACTOR
        TEXTBOX keyTB = new TEXTBOX(p,0, rem, 800, (int)(rem/2));
        keyTB.Background = color(20,20,20);
        keyTB.BackgroundSelected = color(40,40,40);
        keyTB.Foreground = color(255,255,255);
        keyTB.Border = color(80,80,80);
        keyTB.BorderEnable = true;

        saltTB = new TEXTBOX(p,0, rem, 800, (int)(rem/2));
        saltTB.Background = color(20,20,20);
        saltTB.BackgroundSelected = color(40,40,40);
        saltTB.Foreground = color(255,255,255);
        saltTB.Border = color(80,80,80);
        saltTB.BorderEnable = true;

        TEXTBOX crunchTB = new TEXTBOX(p,0, rem, 200, (int)(rem/2));
        crunchTB.Background = color(20,20,20);
        crunchTB.BackgroundSelected = color(40,40,40);
        crunchTB.Foreground = color(255,255,255);
        crunchTB.Border = color(80,80,80);
        crunchTB.BorderEnable = true;

        TEXTBOX allowedTB = new TEXTBOX(p,0, rem, 200, (int)(rem/2));
        allowedTB.Background = color(20,20,20);
        allowedTB.BackgroundSelected = color(40,40,40);
        allowedTB.Foreground = color(255,255,255);
        allowedTB.Border = color(80,80,80);
        allowedTB.BorderEnable = true;

        textboxes.add(inputTB);
        textboxes.add(keyTB);
        textboxes.add(crunchTB);
        textboxes.add(allowedTB);

        // CENSOR BUTTON
        BUTTON censorB = new BUTTON (p, "A","*",0, 0, (int)(rem/2), (int)(rem/2));
        censorB.Background = color(20,20,20);
        censorB.BackgroundSelected = color(20,20,20);
        censorB.Foreground = color(255,255,255);
        censorB.Border = color(80,80,80);
        censorB.selectedBorder = color(80,80,80);
        censorB.SelectedBorderWeight = 1;
        censorB.BorderEnable = true;

        BUTTON censorB2 = new BUTTON (p, "A","*",0, 0, (int)(rem/2), (int)(rem/2));
        censorB2.Background = color(20,20,20);
        censorB2.BackgroundSelected = color(20,20,20);
        censorB2.Foreground = color(255,255,255);
        censorB2.Border = color(80,80,80);
        censorB2.selectedBorder = color(80,80,80);
        censorB2.SelectedBorderWeight = 1;
        censorB2.BorderEnable = true;

        BUTTON censorB3 = new BUTTON (p, "A","*",0, 0, (int)(rem/2), (int)(rem/2));
        censorB3.Background = color(20,20,20);
        censorB3.BackgroundSelected = color(20,20,20);
        censorB3.Foreground = color(255,255,255);
        censorB3.Border = color(80,80,80);
        censorB3.selectedBorder = color(80,80,80);
        censorB3.SelectedBorderWeight = 1;
        censorB3.BorderEnable = true;

        BUTTON ResetB4 = new BUTTON (p, "Reset Salt","Reset Salt",0, 0, (int)(3*textWidth("Reset Salt")), (int)(rem/2));
        ResetB4.Background = color(20,20,20);
        ResetB4.BackgroundSelected = color(20,20,20);
        ResetB4.Foreground = color(255,255,255);
        ResetB4.Border = color(80,80,80);
        ResetB4.selectedBorder = color(80,80,80);
        ResetB4.SelectedBorderWeight = 1;
        ResetB4.BorderEnable = true;

        saltB = new BUTTON (p, "A","*",0, 0, (int)(rem/2), (int)(rem/2));
        saltB.Background = color(20,20,20);
        saltB.BackgroundSelected = color(20,20,20);
        saltB.Foreground = color(255,255,255);
        saltB.Border = color(80,80,80);
        saltB.selectedBorder = color(80,80,80);
        saltB.SelectedBorderWeight = 1;
        saltB.BorderEnable = true;

        buttons.add(censorB);
        buttons.add(censorB2);
        buttons.add(censorB3);
        buttons.add(ResetB4);

        registerMethod("pre", this);

        try {
            robot = new Robot();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }

        killSwitch = new TIMER (60*10);
        killSwitch.stopTimer();

        if(SaltCheck("data/salt.txt")){
            firstTime = false;
            try {
                BufferedReader reader = new BufferedReader(new FileReader("data/salt.txt"));
                String line = null;
                if((line = reader.readLine()) != null && line.length() > 0){
                    salt = line;
                    int lr = 48;
                    char [] st = salt.toCharArray();
                    for (char boost : st) {
                        lr += boost - 48;
                        if(lr > 57 && lr < 65) lr = 65 + (lr - 57);
                        else if (lr > 90 && lr < 97) lr = 97  + (lr - 90);
                        else if (lr > 122) lr = 48 + (lr - 122);
                    }
                    saltedNumber = lr;
                }
            } catch (IOException e) {
                System.out.println("first check was wrong?");
            }
        } else {
            firstTime = true;
        }
    }

    public void pre() {
        if(p.width < 600 ) {
            p.getSurface().setSize(600, p.height);
            p.getSurface().getNative();
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            int x = ( (PSurfaceAWT.SmoothCanvas) (surface).getNative()).getFrame().getX();
            int y = ( (PSurfaceAWT.SmoothCanvas) (surface).getNative()).getFrame().getY();
            //robot.mouseMove(x+600, y+p.mouseY);
        }
        if(p.height < 400 ) {
            p.getSurface().setSize(p.width, 400);
            p.getSurface().getNative();
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            int x = ( (PSurfaceAWT.SmoothCanvas) (surface).getNative()).getFrame().getX();
            int y = ( (PSurfaceAWT.SmoothCanvas) (surface).getNative()).getFrame().getY();
            //robot.mouseMove(x+p.mouseX, y+400);
        }
        if(p.width/(float)p.height < 1.0) {
            p.getSurface().setSize(p.height, p.height);
        }

    }

    public void draw() {
        p.background(20, 20, 20);
        // LABELS
        p.pushStyle();
        p.fill(250, 250, 250);
        if(p.height > 300) {
            pScale = p.map(p.height, 300, 1080, (float)1/3, 1);
        }
        p.scale(pScale);
        p.textSize(rem/2);
        p.text("Pass Scramble", (width/pScale - textWidth("Pass Scramble")) / 2, (int)(rem*0.6));
        p.textSize(rem/4);
        p.text("By Steven Chan", rem / 2, (height / pScale) - rem / 2);
        p.text("Turn Off Clipboard History", (width / pScale - textWidth("Turn Off Clipboard History")) / 2, (int) ((height / pScale) - rem / 2));

        if (firstTime) {
            p.textSize(rem/3);
            p.text("Press Enter with text box selected to Submit Salt", (width / pScale - textWidth("Press Enter with text box selected to Submit Salt")) / 2, (int) (rem * 1.2));
            p.textSize(24);

            BUTTON b3 = saltB;

            hoverTEXTBOXES = false;
            TEXTBOX t3 = saltTB;
            t3.X = (int) (width / pScale - t3.W) / 2;
            t3.Y = (int) (rem * 3.5);
            t3.censor(!saltB.getState());
            t3.DRAW();
            if (t3.overBox(mouseX, mouseY, pScale)) {
                hoverTEXTBOXES = true;
            }

            BUTTON b4 = saltB;
            b4.X = (int) ((width / pScale + t3.W) / 2);
            b4.Y = (int) (rem * 3.5);
            b4.DRAW();

            if (hoverTEXTBOXES) {
                p.cursor(TEXT);
            } else {
                p.cursor(ARROW);
            }
            p.popStyle();

        } else {
            killSwitch.update();
            if(killSwitch.checkFinished())
            {
                p.println("Removed");
                intoClipboard("PWNED");
                killSwitch.rewind();
                killSwitch.stopTimer();
            }
            p.textSize(rem/3);
            p.text("Press Enter with text box selected to scrabble", (width / pScale - textWidth("Press Enter with text box selected to scrabble")) / 2, (int) (rem * 1.2));
            p.textSize(24);

            BUTTON b1 = buttons.get(0);
            BUTTON b2 = buttons.get(1);

            // DRAW THE TEXTBOXES
            hoverTEXTBOXES = false;
            TEXTBOX t1 = textboxes.get(0);
            t1.X = (int) (width / pScale - t1.W) / 2;
            t1.Y = (int) (rem * 2);
            t1.censor(!b1.getState());
            t1.DRAW();
            if (t1.overBox(mouseX, mouseY, pScale)) {
                hoverTEXTBOXES = true;
            }

            b1.X = (int) (width / pScale + t1.W) / 2;
            b1.Y = (int) (rem * 2);
            b1.DRAW();

            TEXTBOX t2 = textboxes.get(1);
            t2.X = (int) (width / pScale - t2.W) / 2;
            t2.Y = (int) (rem * 3.5);
            t2.censor(!b2.getState());
            t2.DRAW();
            if (t2.overBox(mouseX, mouseY, pScale)) {
                hoverTEXTBOXES = true;
            }

            b2.X = (int) (width / pScale + t2.W) / 2;
            b2.Y = (int) (rem * 3.5);
            b2.DRAW();

            BUTTON b3 = buttons.get(2);
            b3.X = (int) (width / pScale - t2.W - rem) / 2;
            b3.Y = (int) ((height / pScale) - (rem * 1.5));
            b3.DRAW();

            TEXTBOX t3 = textboxes.get(2);
            t3.X = (int) ((width / pScale - t3.W*2) / 2 + t1.W/2);
            t3.Y = (int) (rem * 5);
            t3.DRAW();

            if (t3.overBox(mouseX, mouseY, pScale)) {
                hoverTEXTBOXES = true;
            }
            TEXTBOX t4 = textboxes.get(3);
            t4.X = (int) ((width / pScale - t4.W*2) / 2 + t1.W/2);
            t4.Y = (int) (rem * 6.5);
            t4.DRAW();

            if (t4.overBox(mouseX, mouseY, pScale)) {
                hoverTEXTBOXES = true;
            }
            if (hoverTEXTBOXES) {
                p.cursor(TEXT);
            } else {
                p.cursor(ARROW);
            }

            BUTTON b4 = buttons.get(3);
            b4.X = (int) ((width / pScale)-((textWidth(b4.Text))*2));
            b4.Y = (int) ((height / pScale) - (rem*2/3));
            b4.DRAW();

            p.pushMatrix();
            p.translate(((width / pScale) / 2), 0);
            p.text("Password: ", -400, (int) (rem * 1.9));
            p.text("Key: ", -400, (int) (rem * 3.4));
            p.text("Set Output Length: ", 400 - textWidth("Set Output Length: "), (int) (rem * 4.9));
            p.text("Allow Specific Symbols: ", 400 - textWidth("Allow Specific Symbols: "), (int) (rem * 6.4));
            p.textSize(rem / 3);
            if(!b3.getState()) {
                String cText = "";
                for (int i = 0; i < scrambleOutput.length(); i++) {
                    cText = cText + "*";
                }
                p.text(cText, -400, (int) ((height / pScale) - rem * 1.2));
            } else {
                p.text(scrambleOutput, -400, (int) ((height / pScale) - rem * 1.2));
            }
            p.stroke(250, 250, 250);
            p.line(-400, (height / pScale) - rem, 400, (height / pScale) - rem);

            p.fill(80, 80, 80);
            p.textSize(rem / 4);
            p.text("Recommended to be more than 8 characters", -(int) (textWidth("Recommended to be more than 8 characters") / 2), (int) (rem * 2.8));
            p.text("The Key will be converted from String to Int.", -(int) (textWidth("The Key will be converted from String to Int.") / 2), (int) (rem * 4.3));
            p.text("[\"OFF\"] or [Number]", 300-(int) (textWidth("[\"OFF\"] or [Number]") / 2), (int) (rem * 5.8));
            p.text("[Blank for No Restrictions]\n[A/a/0 for only Legal Characters]\n[Symbols for Specific Allowed]", 300-(int) (textWidth("[Blank for No Restrictions]") / 2), (int) (rem * 7.3));

            // JUST FOR DEMO (DO NOT EVER DO THAT!)
            if (output && METADATA) {
                p.text("Base to Int: " + workingBase.toString(), -400, (int) (rem * 4.6));
                p.text("Key to Int: " + workingKey.toString(), -400, (int) (rem * 4.9));
                p.text("Base*Key*Salt: " + workingMix.toString(), -400, (int) (rem * 5.2));
                p.text("Char Split: " + workingString, -400, (int) (rem * 5.5));
                p.text("Char legal: " + postString, -400, (int) (rem * 5.8));
            }
            p.popMatrix();
            p.popStyle();
        }
    }

    public void mousePressed() {
        if(firstTime){
            saltB.PRESSED(mouseX, mouseY, pScale);
            saltTB.PRESSED(mouseX, mouseY, pScale);
        } else {
            for (TEXTBOX t : textboxes) {
                t.PRESSED(mouseX, mouseY, pScale);
            }
            for (BUTTON b : buttons) {
                b.PRESSED(mouseX, mouseY, pScale);
            }
            if(buttons.get(3).getState()){
                try {
                    SaltReset("data/salt.txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                firstTime = true;
                buttons.get(3).setState(false);
            }
        }
    }

    public void Submit() {
        if (textboxes.get(0).Text.length() > 0) {
            if (textboxes.get(1).Text.length() > 0) {
                output = true;
                killSwitch.startTimer();
                Scramble(textboxes.get(0).Text, textboxes.get(1).Text);
            } else {
                scrambleOutput = "Missing Key";
                output = false;
            }
        } else {
            scrambleOutput = "Missing Base";
            output = false;
        }
    }
    public boolean SaltCheck(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            if((line = reader.readLine()) != null && line.length() > 0){
                return true;
            }
            else return false;
        } catch (IOException e) {
            return false;
        }
    }
    public void SaltSubmit(String file) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(saltTB.Text);
            writer.close();
            salt = saltTB.Text;
            firstTime = false;
            int lr = 48;
            char [] st = salt.toCharArray();
            for (char boost : st) {
                lr += boost - 48;
                if(lr > 57 && lr < 65) lr = 65 + (lr - 57);
                else if (lr > 90 && lr < 97) lr = 97  + (lr - 90);
                else if (lr > 122) lr = 48 + (lr - 122);
            }
            saltedNumber = lr;
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }

    public void SaltReset(String file) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("");
            writer.close();
            salt = null;
            saltTB.Text = "";
            saltTB.TextLength = 0;
            saltTB.TextSelectedIndicator = 1;
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }


    public void keyPressed() {
        if(firstTime){
            if (saltTB.KEYPRESSED(key, (int)keyCode)) {
                try {
                    SaltSubmit("data/salt.txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            for (TEXTBOX t : textboxes) {
                if (t.KEYPRESSED(key, (int)keyCode)) {
                    Submit();
                }
            }
        }
    }

    public void Scramble(String input, String uniqueKey){
        char [] ch = input.toCharArray();
        char [] ky = uniqueKey.toCharArray();
        char [] st = salt.toCharArray();
        char [] ol = textboxes.get(2).Text.toCharArray();
        char [] sl = textboxes.get(3).Text.toCharArray();
        char [] cuttingBoard;
        int outputLength = 0;

        if(ol.length > 0){
            for (char OutLength : ol) {
                if( OutLength - 48 < 0 || OutLength - 48 > 9) {
                    scrambleOutput = "Output Length is not a number";
                    output = false;
                    return;
                }
                else {
                    outputLength *= 10;
                    outputLength += OutLength - 48;
                }
            }
        }
        workingBase = new BigInteger("0");
        workingKey = new BigInteger("0");
        workingSalt = new BigInteger("0");
        for(char c : ch){
            workingBase = new BigInteger(workingBase.toString() + String.valueOf((int)c));
        }
        for(char k : ky){
            workingKey = new BigInteger(workingKey.toString() + String.valueOf((int)k));
        }
        for(char s : st){
            workingSalt = new BigInteger(workingKey.toString() + String.valueOf((int)s));
        }
        workingMix = new BigInteger(workingBase.toString()).multiply(workingKey).multiply(workingSalt);
        cuttingBoard = workingMix.toString().toCharArray();
        scrambleOutput = "";
        workingString = "";
        int replaceNumber = 33;
        int lr = saltedNumber;
        for(int i = 0; i < cuttingBoard.length && (scrambleOutput.length() < outputLength || outputLength == 0); i++){
            int numericValue = cuttingBoard[i] - 48;
            if(i+1 == cuttingBoard.length){
                numericValue *= 10;
                workingString+=Integer.toString(numericValue) + " ";
                if (numericValue < 33) {
                    scrambleOutput += (char) replaceNumber;
                    replaceNumber++;
                    if(replaceNumber > 126) replaceNumber = 33;
                } else {
                    scrambleOutput += (char) numericValue;
                }
            }
            else {
                if (numericValue == 1 && cuttingBoard.length - 1 - i > 1) {
                    numericValue *= 10;
                    numericValue += cuttingBoard[i + 1] - 48;
                    numericValue *= 10;
                    numericValue += cuttingBoard[i + 2] - 48;
                    workingString+=Integer.toString(numericValue) + " ";
                    if (numericValue > 126) {
                        scrambleOutput += (char) replaceNumber;
                        replaceNumber++;
                        if(replaceNumber > 126) replaceNumber = 33;
                    } else {
                        scrambleOutput += (char) numericValue;
                    }
                    i += 2;
                } else {
                    numericValue *= 10;
                    numericValue += cuttingBoard[i + 1] - 48;
                    workingString+=Integer.toString(numericValue) + " ";
                    if (numericValue < 33) {
                        scrambleOutput += (char) replaceNumber;
                        replaceNumber++;
                        if(replaceNumber > 126) replaceNumber = 33;
                    } else {
                        scrambleOutput += (char) numericValue;
                    }
                    i += 1;
                }
            }
            if(sl.length > 0){
                char lastChar = scrambleOutput.charAt(scrambleOutput.length()-1);
                boolean isLegal = false;
                for (char KEY : sl) {
                    if(lastChar == KEY){
                        isLegal = true;
                    }
                }
                boolean isKeyCapitalLetter = (lastChar >= 'A' && lastChar <= 'Z');
                boolean isKeySmallLetter = (lastChar >= 'a' && lastChar <= 'z');
                boolean isKeyNumber = (lastChar >= '0' && lastChar <= '9');
                if (!(isKeyCapitalLetter || isKeySmallLetter || isKeyNumber || isLegal)){
                    scrambleOutput = removeLastChar(scrambleOutput);
                    scrambleOutput += (char) lr;
                    lr+=1;
                    if(lr > 57 && lr < 65) lr = 65 + (lr - 57);
                    else if (lr > 90 && lr < 97) lr = 97  + (lr - 90);
                    else if (lr > 122) lr = 48 + (lr - 122);
                }
            }
        }
        p.println(scrambleOutput);
        clipboardString = scrambleOutput;
        StringSelection stringSelection = new StringSelection(clipboardString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        postString = "";
        cuttingBoard = scrambleOutput.toCharArray();
        for(char sep : cuttingBoard){
            postString+= (int)sep+" ";
        }
    }

    void intoClipboard(String input) {
        clipboardString = input;
        StringSelection stringSelection = new StringSelection(clipboardString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        postString = "";
        char [] cuttingBoard = scrambleOutput.toCharArray();
        for(char sep : cuttingBoard){
            postString+= (int)sep+" ";
        }
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }
}