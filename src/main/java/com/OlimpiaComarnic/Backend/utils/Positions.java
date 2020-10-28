package com.OlimpiaComarnic.Backend.utils;

import java.util.HashMap;
import java.util.Map;

public enum Positions {
    GK("GK", "Goalkeeper"),
    SW("SW", "Sweeper"),
    CB("CB", "Center back"),
    LCB("LCB", "Left center back"),
    RCB("RCB", "Right center back"),
    LB("LB", "Left back"),
    RB("RB", "Right back"),
    LWB("LWB", "Left wing back"),
    RWB("RWB","Right wing back"),
    CDM("CDM", "Central Defensive Midfielder"),
    LDM("LDM", "Left Defensive Midfielder"),
    RDM("RDM", "Right Defensive Midfielder"),
    CM("CM", "Center Central Midfielder"),
    LCM("LCM", "Left Central Midfielder"),
    RCM("RCM", "Right Central Midfielder"),
    LM("LM", "Left Midfielder"),
    RM("RM", "Right Midfielder"),
    CAM("CAM", "Central Attacking Midfielder"),
    LAM("LAM","Left Attacking Midfielder"),
    RAM("RAM", "Right Attacking Midfielder"),
    LW("LW", "Left Winger"),
    RW("RW", "Right Winger"),
    CF("CF", "Central Forward"),
    LCF("LCF","Left Central Forward"),
    RCF("RCF", "Right Central Forward");
    ST("ST","Striker");

    private final String _key;
    private final String _value;

    private static Map<String, Positions> value;

    Positions(String key, String value) {
        _key = key;
        _value = value;
    }

    public static Positions getPlayerPosition(String i) {
        if(value == null) {
            initMapping();
        }
        return value.get(i);
    }

    private static void initMapping() {
        value = new HashMap<>();
        for(Positions pos: values())
            value.put(pos._value, pos);
    }

    public String getShortPos() {
        return _key;
    }

    public String getLongPos() {
        return _value;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Position: { key: ").append(_key).append(", value: ").append(_value).append('}');
        return stringBuilder.toString();
    }
}
