package game.world;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class BossAI extends CreatureAI {

    private int[] dirX = {1, 1, 0, -1, -1, -1, 0, 1};
    private int[] dirY = {0, 1, 1, 1, 0, -1, -1, -1};
    private int[] dx = {-1, 0, 1, 0};
    private int[] dy = {0, 1, 0, -1};

    private List<String> messages;

    private boolean setByTimer;

    private CreatureFactory factory;

    private int interval;

    private int manPrevX;
    private int manPrevY;
    
    public BossAI(Creature creature, List<String> messages, CreatureFactory factory) {
        super(creature);
        this.messages = messages;
        setByTimer = false;
        this.factory = factory;
        interval = 0;
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        creature.tryMove(x, y);
    }

    public void changeSetting() {
        setByTimer = !setByTimer;
    }

    private int[][] analyse(World world) {
        int width = world.width();
        int height = world.height();
        int[][] dist = new int[width][height];
        boolean[][] vis = new boolean[width][height];
        int manX = world.getManPosX();
        int manY = world.getManPosY();
        System.out.println("man in" + manX + " " + manY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dist[i][j] = 100000;
                vis[i][j] = false;
            }
        }
        dist[manX][manY] = 0;
        vis[manX][manY] = true;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{manX, manY});
        while (queue.size() > 0) {
            int[] front = queue.poll();
            int x = front[0], y = front[1];
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i], newY = y + dy[i];
                if (world.tile(newX, newY).isGround() && dist[newX][newY] > dist[x][y] + 1) {
                    queue.add(new int[]{newX, newY});
                    vis[newX][newY] = true;
                    dist[newX][newY] = dist[x][y] + 1;
                }
            }
        }
        return dist;
    }

    @Override
    public void onUpdate() {
        if (!setByTimer) {
            return;
        }
        if (creature.hp() < 1) {
            task.cancel(true);
            System.out.println("boss died");
            return;
        }
        World world = creature.getWorld();
        // if (world.creature(world.getManPosX(), world.getManPosY()) == null || world.creature(world.getManPosX(), world.getManPosY()).hp() < 0) {
        //     System.out.println("man has died");
        //     return;
        // }
        int x = creature.x();
        int y = creature.y();
        int[][] dist = analyse(world);
        int newX = x, newY = y, minDist = dist[x][y];
        for (int i = 0; i < 8; i++) {
            int nx = x + dirX[i], ny = y + dirY[i];
            if (world.tile(nx, ny).isGround() && dist[nx][ny] < minDist) {
                newX = nx;
                newY = ny;
                minDist = dist[nx][ny];
            }
        }
        if (newX != x || newY != y) {
            creature.tryMove(newX, newY);
        }
        world.setPolluted(creature.x(), creature.y());

        interval++;
        if (interval == 1) {
            manPrevX = world.getManPosX();
            manPrevY = world.getManPosY();
        } else if (interval == 5) {
            for (int i = 0; i < 4; i++) {
                int dogX = manPrevX + dx[i], dogY = manPrevY + dy[i];
                factory.newDog(dogX, dogY);
            }
            interval = 0;
        }
    }

    @Override
    public void attack(Creature another) {
        another.modifyHP(-creature.attackValue());
        if (another.getAI() instanceof DogAI) {
            ;
        } else {
            creature.modifyHP(-another.attackValue());
        }
        if (another.hp() < 1) {
            this.onEnter(another.x(), another.y(), another.tile(another.x(), another.y()));
        }
    }

    @Override
    public void onNotify(String message) {

    }
}
