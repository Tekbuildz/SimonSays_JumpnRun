package SpriteSheet;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheetMaster {

    private static Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();

    /**
     *
     * adds a sprite sheet to a map
     *
     * @param indicatorName - the key of the map (String)
     * @param spriteSheet - the value of the map (SpriteSheet)
     */
    public static void addSpriteSheetToMap(String indicatorName, SpriteSheet spriteSheet) {
        spriteSheetMap.put(indicatorName, spriteSheet);
    }

    /**
     *
     * @return the map containing all the sprite sheets
     */
    public static Map<String, SpriteSheet> getSpriteSheetMap() {
        return spriteSheetMap;
    }

    /**
     *
     * gets the sprite sheet from the map using the indicatorName as a key
     *
     * @param indicatorName - key for the map to find the associated
     *                      sprite sheet
     * @return the sprite sheet associated with the key indicatorName
     */
    public static SpriteSheet getSpriteSheetFromMap(String indicatorName) {
        return spriteSheetMap.get(indicatorName);
    }
}
