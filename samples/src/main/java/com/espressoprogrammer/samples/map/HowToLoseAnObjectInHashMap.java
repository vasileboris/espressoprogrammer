package com.espressoprogrammer.samples.map;

import java.util.HashMap;
import java.util.Map;

public class HowToLoseAnObjectInHashMap {

    public static void main(String... args) {
        Map<Key, String> map = new HashMap<>();

        Key key = new Key("key1");
        map.put(key, "value1");
        printMapAndValue(map, new Key("key1"));

        key.setValue("key2");
        printMapAndValue(map, new Key("key1"));
        printMapAndValue(map, new Key("key2"));

        key.setValue("key1");
        printMapAndValue(map, new Key("key1"));
    }

    static void printMapAndValue(Map<Key, String> map, Key key) {
        System.out.println("-------------------------------------");
        System.out.println("map: " + map);
        System.out.println("map.get(" + key + "): " + map.get(key));
    }


    static class Key {
        private String value;

        public Key(String value) {
            this.value = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Key other = (Key) obj;

            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
