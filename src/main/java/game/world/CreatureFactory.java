package game.world;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import game.asciiPanel.AsciiPanel;

public class CreatureFactory implements Serializable {

    private World world;

    public static int maxBeans = 10;
    public int totalBeans;

    public transient ScheduledThreadPoolExecutor exector;

    public ScheduledThreadPoolExecutor getExecutor() {
        return exector;
    }

    public boolean emptyExecutor() {
        return exector == null;
    }

    public void setExecutor(ScheduledThreadPoolExecutor executor) {
        this.exector = executor;
    }

    public CreatureFactory(World world) {
        this.world = world;
        totalBeans = 0;
        exector = new ScheduledThreadPoolExecutor(20);
    }

    public Creature newSnake(List<String> messages) {
        System.out.println("create snake");
        Creature snake = new Creature(this.world, (char) 241, AsciiPanel.brightWhite, 50, 20, 5, 9);
        world.addAtEmptyLocation(snake);
        snake.setAttackedGlyph((char) 64);
        SnakeAI snakeAIInstance = new SnakeAI(snake, messages);
        snakeAIInstance.setScheduledFuture(exector.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //System.out.println("snake move");
                snakeAIInstance.onUpdate();
            }
        }, 0, 100, TimeUnit.MILLISECONDS));
        return snake;
    }

    public void setSnakeTask(SnakeAI snake) {
        snake.setScheduledFuture(exector.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //System.out.println("snake move");
                snake.onUpdate();
            }
        }, 0, 100, TimeUnit.MILLISECONDS));    
    }

    public Creature newBoss(List<String> messages, CreatureFactory factory) {
        Creature boss = new Creature(this.world, (char) 2, AsciiPanel.brightBlack, 10, 10, 5, 0);
        boss.modifyHP(boss.maxHP());
        world.addAtEmptyLocation(boss);
        world.setPolluted(boss.x(), boss.y());
        BossAI bossAIInstance = new BossAI(boss, messages, factory);
        bossAIInstance.setScheduledFuture(exector.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //System.out.println("boss move");
                bossAIInstance.onUpdate();
            }
        }, 0, 1, TimeUnit.SECONDS));
        return boss;
    }

    public void setBossTask(BossAI boss) {
        boss.setScheduledFuture(exector.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //System.out.println("boss move");
                boss.onUpdate();
            }
        }, 0, 1, TimeUnit.SECONDS)); 
    }

    public Creature newBean() {
        Creature bean = new Creature(this.world, (char) 139, AsciiPanel.green, 10, -1, 1, 0);
        world.addAtEmptyLocation(bean);
        new BeanAI(bean, this);
        totalBeans++;
        return bean;
    }

    public Creature newBullet(int x, int y, int dx, int dy) {
        Creature bullet = new Creature(this.world, (char) 133, AsciiPanel.white, 1, 1, 1, 0);
        world.addAtLocation(bullet, x + dx, y + dy);
        new BulletAI(bullet, dx, dy);
        return bullet;
    }

    public Creature newDog(int x, int y) {
        Creature dog = new Creature(this.world, (char) 243, AsciiPanel.brightGreen, 1, 1, 1, 0);
        world.addAtLocation(dog, x, y);
        new DogAI(dog);
        return dog;
    }

    public int getTotalBeans() {
        return totalBeans;
    }

    public int decreaseBeans() {
        totalBeans--;
        return totalBeans;
    }
}
