package SpriteSheet;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheetMaster {

    private static Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();

    public static void addSpriteSheetToMap(String indicatorName, SpriteSheet spriteSheet) {
        spriteSheetMap.put(indicatorName, spriteSheet);
    }

    public static Map<String, SpriteSheet> getSpriteSheetMap() {
        return spriteSheetMap;
    }

    public static SpriteSheet getSpriteSheetFromMap(String indicatorName) {
        return spriteSheetMap.get(indicatorName);
    }
}
