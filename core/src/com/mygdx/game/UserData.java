package com.mygdx.game;

import com.badlogic.gdx.maps.MapProperties;

public class UserData {
    public String name = "";
    public MapProperties map = new MapProperties();

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", map=" + map +
                '}';
    }

    public static class Builder {
        private String name = "";
        private MapProperties map = new MapProperties();

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setMap(MapProperties map) {
            this.map = map;
            return this;
        }

        public Builder append(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public UserData build() {
            UserData userData = new UserData();
            userData.name = this.name;
            userData.map = this.map;
            return userData;
        }
    }
}
