package agh.oop.project;


import java.util.Random;

public enum MapDirection{
    NORTH,
    NORTHWEST,
    NORTHEAST,
    SOUTH,
    SOUTHWEST,
    SOUTHEAST,
    WEST,
    EAST,
    ;

    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
            case EAST -> "Wschód";
            case NORTHEAST -> "Północny-Wschód";
            case SOUTHEAST -> "Południowy-Wschód";
            case NORTHWEST -> "Północny-Zachód";
            case SOUTHWEST -> "Południowy-Zachód";

        };
    }

    public MapDirection next(){
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST ;
            case NORTHWEST -> NORTH;

        };

    }
    public MapDirection previous(){
        return switch (this) {
            case NORTH -> NORTHWEST ;
            case NORTHWEST-> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;

        };
    }
    public Vector2d toUnitVector(){
        return switch (this) {
            case WEST -> new  Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST-> new Vector2d(-1, 1);
        };

    }
    public static MapDirection drawMapDirection(){
        Random random = new Random();
        int i = random.nextInt(8);
        return switch (i){
            case 0 -> NORTHEAST;
            case 1 -> EAST;
            case 2 -> SOUTHEAST;
            case 3 -> SOUTH;
            case 4 -> SOUTHWEST;
            case 5 -> WEST;
            case 6 -> NORTHWEST ;
            case 7 -> NORTH;
            default -> null;
        };
    }

}
