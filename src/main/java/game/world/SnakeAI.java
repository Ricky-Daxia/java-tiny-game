package game.world;

import java.util.List;

public class SnakeAI extends CreatureAI {


    private List<String> messages;

    public SnakeAI(Creature creature, List<String> messages) {
        super(creature);
        this.messages = messages;
        
        //the creature is virtually the first piece of the snake
    }


    @Override
    public void onEnter(int x, int y, Tile tile) {
        // int xPrev = pieces.get(0).x();
        // int yPrev = pieces.get(0).y();

        // if (tile.isGround()) {
        //     pieces.get(0).setX(x);
        //     pieces.get(0).setY(y);

        //     creature.setX(x);
        //     creature.setY(y);
        // } else if (tile.isDiggable()) {
        //     creature.dig(x, y);
        // } else {
        //     return;
        // }

        // if (pieces.size() > 1) {
        //     for (int i = 1; i < pieces.size(); i++) {
        //         int tempX = pieces.get(i).x();
        //         int tempY = pieces.get(i).y();
        //         pieces.get(i).setX(xPrev);
        //         pieces.get(i).setY(yPrev);
        //         xPrev = tempX;
        //         yPrev = tempY;
        //     }
        // }
        // System.out.println("enter : " + world.tile(x, y));
        //creature.tryMove(x, y);
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            //System.out.println("man set in " + x + "  " + y);
        } else {
            return;
        }
    }

    @Override
    public void attack(Creature another) {
        another.modifyHP(-creature.attackValue());
        if (another.attackValue() > 0) {
            // gonna be hurt
            if (!creature.getAttacked()) {
                creature.changeAttacked();
            }
        }
        creature.modifyHP(-another.attackValue());
        if (another.hp() < 1) {
            this.onEnter(another.x(), another.y(), another.tile(another.x(), another.y()));
        }
    }

    @Override
    public void onUpdate() {
        World world = creature.getWorld();
        world.setManPos(creature.x(), creature.y());
        if (creature.getAttacked()) {
            creature.changeAttacked();
        } 
        if (world.tile(creature.x(), creature.y()).isPolluted()) {
            creature.modifyHP(-1);
            if (!creature.getAttacked()) {
                creature.changeAttacked();
            }
        }
        //System.out.println(creature.x() + " " + creature.y());
        if (creature.hp() < 0) {
            System.out.println("snake died");
            task.cancel(true);
        }
    }

    @Override
    public void onNotify(String message) {
        this.messages.add(message);
    }

    // @Override
    // public void printGlyph(AsciiPanel terminal, int offsetX, int offsetY) {
    //     for (SnakePiece p : this.getPieces()) {
    //         terminal.write(creature.glyph(), p.x() - offsetX, p.y() - offsetY, creature.color());
    //     }
    // }
}
