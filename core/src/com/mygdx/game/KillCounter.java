package com.mygdx.game;

import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.actor.tank.TankActor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KillCounter {

    private Map<String, Integer> kills = new HashMap<>();

    public void killOne(TankActor actor) {
        Integer count = kills.computeIfAbsent(actor.getType(), s -> 0);
        count++;
        kills.put(actor.getType(), count);
    }

    public void clear() {
        kills.clear();
    }

    public String getKillMessage() {

        Iterator<Map.Entry<String, Integer>> iterator = kills.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            Integer value = next.getValue();
            sb.append(key).append("-------").append(value).append("\n");
        }
        return sb.toString();
    }
}
