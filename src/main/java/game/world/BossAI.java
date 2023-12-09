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

    private CreatureFactory factory;

    private int interval;

    private int[] manPrev;
    
    public BossAI(Creature creature, List<String> messages, CreatureFactory factory) {
        super(creature);
        this.messages = messages;
        this.factory = factory;
        interval = 0;
        manPrev = null;
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        creature.tryMove(x, y);
    }

    private int[][] analyse(World world) {
        int width = world.width();
        int height = world.height();
        int[][] dist = new int[width][height];
        boolean[][] vis = new boolean[width][height];
        int[] manPos = world.getManPos();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dist[i][j] = 100000;
                vis[i][j] = false;
            }
        }
        if (manPos == null) {
            return dist;
        }
        Queue<int[]> queue = new ArrayDeque<>();
        for (int mask: manPos) {
            int x = mask >> 10 & 0x3ff, y = mask & 0x3ff;
            dist[x][y] = 0;
            vis[x][y] = true;
            queue.add(new int[]{x, y});
        }
        while (queue.size() > 0) {
            int sz = queue.size();
            for (; sz > 0; sz--) {
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
        }
        return dist;
    }

    @Override
    public void onUpdate() {
        World world = creature.getWorld();
        if (creature.hp() < 1) {
            world.remove(creature);
            task.cancel(true);
            System.out.println("boss died");
            return;
        }        
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
            manPrev = world.getManPos();
        } else if (interval == 5) {
            if (manPrev == null) {
                System.out.println("man null????");
            } else {
                for (int mask: manPrev) {
                    int manX = mask >> 10 & 0x3ff, manY = mask & 0x3ff;
                    for (int i = 0; i < 4; i++) {
                        int dogX = manX + dx[i], dogY = manY + dy[i];
                        factory.newDog(dogX, dogY);
                    }                
                }                
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
            creature.getWorld().remove(another);
            //creature.tryMove(another.x(), another.y());
        }
    }

    @Override
    public void onNotify(String message) {

    }
}
