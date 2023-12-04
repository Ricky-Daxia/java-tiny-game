package game.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class WorldBuilder implements Serializable {

    private int width;
    private int height;
    private Tile[][] tiles;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }

    public WorldBuilder makeRectangle() {
        String filePath = "map1.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            for (int i = 0; i < width; i++) {
                line = reader.readLine();
                //System.out.println(line);
                int j = -1;
                for (int k = 0; k < line.length(); k += 2) {
                    j++;
                    char ch = line.charAt(k);
                    //System.out.println(i + " " + j);
                    switch (ch) {
                        case '0': 
                            tiles[i][j] = Tile.FLOOR;
                            break;
                        case '1':
                            tiles[i][j] = Tile.WALL;
                            break;
                        case '2':
                            tiles[i][j] = Tile.WATER;
                            break;
                        case '3':
                            tiles[i][j] = Tile.LeftUp;
                            break;
                        case '4':
                            tiles[i][j] = Tile.RightUp;
                            break;
                        case '5':
                            tiles[i][j] = Tile.LeftDown;
                            break;
                        case '6':
                            tiles[i][j] = Tile.RightDown;
                            break;
                        case '7':
                            tiles[i][j] = Tile.Horizontal;
                            break;
                        case '8':
                            tiles[i][j] = Tile.Vertical;
                            break;
                        case '9':
                            tiles[i][j] = Tile.Polluted;
                            break;
                        default:
                            System.out.println(ch + " " + i + " " + j);
                            break;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return this;
    }

    private WorldBuilder randomizeTiles() {
        Random rand = new Random();
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                switch (rand.nextInt(Tile.values().length - 1)) {
                    case 0:
                        tiles[width][height] = Tile.FLOOR;
                        break;
                    case 1:
                        tiles[width][height] = Tile.WALL;
                        break;
                }
            }
        }
        return this;
    }

    private WorldBuilder smooth(int factor) {
        Tile[][] newtemp = new Tile[width][height];
        if (factor > 1) {
            smooth(factor - 1);
        }
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                // Surrounding walls and floor
                int surrwalls = 0;
                int surrfloor = 0;

                // Check the tiles in a 3x3 area around center tile
                for (int dwidth = -1; dwidth < 2; dwidth++) {
                    for (int dheight = -1; dheight < 2; dheight++) {
                        if (width + dwidth < 0 || width + dwidth >= this.width || height + dheight < 0
                                || height + dheight >= this.height) {
                            continue;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.FLOOR) {
                            surrfloor++;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.WALL) {
                            surrwalls++;
                        }
                    }
                }
                Tile replacement;
                if (surrwalls > surrfloor) {
                    replacement = Tile.WALL;
                } else {
                    replacement = Tile.FLOOR;
                }
                newtemp[width][height] = replacement;
            }
        }
        tiles = newtemp;
        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(19);
    }
}
