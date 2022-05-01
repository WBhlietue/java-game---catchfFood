import java.lang.Thread;
import java.util.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends Thread {

    long delay = 0;
    long pauseSec = 0;
    int pauseFrame = 0;

    long secCount = pauseSec;
    int frameCount = pauseFrame;

    public boolean enabled = true;

    static GameWindow gameWindow = null;

    public boolean customFrame = false;
    public int targetFrame = 1;

    JLabel thisObj;

    Vector position = new Vector(0, 0);

    public String name = "null";
    public int collDistance = 0;

    public boolean isCollided = false;
    public Game coll = null;

    public void run() {
        Start();
        while (true) {
            try {
                Thread.sleep(delay);
                if (enabled) {
                    if (frameCount < pauseFrame) {
                        frameCount++;
                    }
                    if (secCount < pauseSec) {
                        secCount += delay;
                    }
                    if (frameCount >= pauseFrame && secCount >= pauseSec) {
                        Update();
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public Game() {

        if (gameWindow == null) {
            gameWindow = new GameWindow();
        }
        if (!customFrame) {
            if (delay == 0) {
                delay = 1000 / MyTools.frame;
            }
        } else {
            delay = 1000 / targetFrame;
        }
        Awake();
        start();
    }

    public Vector getPostion() {
        return position;
    }

    public void setPosition(Vector target) {
        position = target;
        thisObj.setLocation(position.x, position.y);
    }

    /////
    ///// Game's some method
    /////

    public GameWindow getWindow() {
        return gameWindow;
    }

    public void setCustomFrame(boolean custom, int frame) {
        targetFrame = (frame <= 0 ? 1 : frame);
        customFrame = custom;
    }

    public void updateCustomFrame() {
        delay = 1000 / targetFrame;
    }

    public void Init(int width, int height, String imagePath, int x, int y) {
        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource(imagePath));
            Image i = icon.getImage();
            icon.setImage(i.getScaledInstance(width, height, 0));
        } catch (Exception e) {
            icon = new ImageIcon();
            MyTools.Print("file not found");
        }

        // thisObj = new JButton(icon);
        thisObj = new JLabel(icon);
        gameWindow.add(thisObj);
        thisObj.setSize(width, height);
        thisObj.setLocation(x, y);
        position.x = thisObj.getLocation().x;
        position.y = thisObj.getLocation().y;
    }

    boolean active = true;
    Vector pos;

    public void setAcive(boolean status) {
        if (!status) {
            if (active) {
                pos = getPostion();
            }
            setPosition(Vector.one.Mult(-100000));
        } else {
            if (!active) {
                setPosition(pos);
            }
        }
        active = status;
    }

    /////
    ///// Child's some method
    /////

    public void Awake() {

    }

    public void Start() {

    }

    public void Update() {

    }

    public void collideStart(Game other) {

    }

    public void Collide(Game other) {

    }

    public void collideEnd(Game other) {

    }

    // pause

    public void PauseWithFrame(int frameNum) {
        secCount = 0;
        pauseSec = 0;
        frameCount = 0;
        pauseFrame = frameNum;
    }

    public void PauseWithSec(float sec) {
        pauseFrame = 0;
        frameCount = 0;
        pauseSec = (long) (sec * 1000);
        secCount = 0;
    }

    // enabled

    public void SetEnable(boolean _enabled) {
        if (!enabled && _enabled) {
            OnEnabled();
        }

        if (enabled && !_enabled) {
            OnDisabled();
        }

        enabled = _enabled;
    }

    public void OnEnabled() {

    }

    public void OnDisabled() {

    }

}

class MyTools {
    public static int frame = 60;
    static Random r;

    public static void Print(Object obj) {
        System.out.println(obj);
    }

    public static int getRandomNum(int min, int max) {
        if (r == null) {
            r = new Random();
        }
        return r.nextInt(max - min) + min;
    }

}

class GameWindow extends JFrame {

    public GameWindow() {
        new CollisionManager();
        setLayout(null);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JPanel(null));
        setVisible(true);
    }

}

class Key extends KeyAdapter {
    public Key() {
        Game.gameWindow.addKeyListener(this);
    }

}

class Vector {

    public static final Vector up = new Vector(0, -1);
    public static final Vector down = new Vector(0, 1);
    public static final Vector right = new Vector(1, 0);
    public static final Vector left = new Vector(-1, 0);
    public static final Vector zero = new Vector(0, 0);
    public static final Vector one = new Vector(1, 1);

    public int x;
    public int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector Add(Vector another) {
        return new Vector(x + another.x, y + another.y);
    }

    public Vector Less(Vector another) {
        return new Vector(x - another.x, y - another.y);
    }

    public Vector Mult(int a) {
        return new Vector(x * a, y * a);
    }

    public Vector Div(int a) {
        return new Vector(x / a, y / a);
    }

    public static int Distance(Vector a, Vector b) {
        return (int) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public int Lenght() {
        return (int) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class CollisionManager extends Thread {
    List<Game> targetObjects = new ArrayList<Game>();
    List<Game> checkObject = new ArrayList<Game>();
    long delay;
    public static CollisionManager instance;

    public void run() {
        while (true) {
            try {
                Thread.sleep(delay);
                Check();

            } catch (Exception e) {

            }
        }
    }

    public CollisionManager() {
        instance = this;
        delay = 1000 / 1000;
        start();
    }

    public static void addCheckObj(Game game) {
        CollisionManager.instance.addCheck(game);
    }

    void addCheck(Game game) {
        checkObject.add(game);
    }

    public static void addtargetObj(Game game) {
        CollisionManager.instance.addTarget(game);
    }

    void addTarget(Game game) {
        targetObjects.add(game);
    }

    public void Check() {
        for (Game i : targetObjects) {
            for (Game j : checkObject) {
                if (i != j) {
                    if (i.thisObj.getBounds().intersects(j.thisObj.getBounds())) {
                        if (!i.isCollided) {
                            i.isCollided = true;
                            i.collideStart(j);
                            i.coll = j;
                        } else {
                            i.Collide(j);
                        }
                    } else if (i.isCollided && j == i.coll) {
                        i.isCollided = false;
                        i.coll = null;
                        i.collideEnd(j);
                    }
                }
            }
        }
    }

}

class Text {

    JLabel area;

    public Text(int width, int height, int fontSize, int x, int y) {
        area = new JLabel("123123123");
        area.setSize(width, height);
        area.setLocation(x, y);
        area.setFont(new Font("Arial", Font.ITALIC, fontSize));
        Game.gameWindow.add(area);
    }

    public void SetText(String txt) {
        area.setText(txt);
    }
}
