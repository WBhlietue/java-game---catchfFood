
public class GameManager extends Game {

    public int score = 0;

    public float spawnDelay = 1.5f;
    Text t;
    public static GameManager instance;

    public GameManager() {
        instance = this;
    }

    @Override
    public void Start() {
        t = new Text(1000, 100, 50, 600, 0);
        t.SetText("0");
    }

    @Override
    public void Update() {
        Food food = new Food();
        food.name = "food";
        int num = MyTools.getRandomNum(1, 10);
        food.Init(75, 75, "image/foods/" + num + ".png", MyTools.getRandomNum(400, 800), -100);
        CollisionManager.addCheckObj(food);
        PauseWithSec(spawnDelay);

    }

    public void addScore(int num) {
        score += num;
        t.SetText(score + "");
    }
}
