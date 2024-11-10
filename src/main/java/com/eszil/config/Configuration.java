package com.eszil.config;

import com.eszil.enums.ChatModeType;
import com.eszil.enums.DFNodes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "dfutils")
public class Configuration implements ConfigData {

    @ConfigEntry.Category("automation")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public AutoChatModeSection autoChatMode = new AutoChatModeSection();

    public static class AutoChatModeSection {
        public boolean enabled = true;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ChatModeType mode = ChatModeType.Local;
    }

    @ConfigEntry.Category("automation")
    @ConfigEntry.Gui.Tooltip
    public boolean autoLagslayer = true;

    @ConfigEntry.Category("automation")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public AutoJoinDF autoJoinDF = new AutoJoinDF();

    public static class AutoJoinDF {
        public boolean enabled = false;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public DFNodes autoJoinNode = DFNodes.None;

        public boolean autoJoinPlot = false;

        public int autoJoinPlotID = 0;
    }

    @ConfigEntry.Category("keybinds")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public FlightSpeedValuesSection flightSpeedValues = new FlightSpeedValuesSection();

    public static class FlightSpeedValuesSection {
        public int normal = 100;
        public int medium = 300;
        public int high = 1000;
    }

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean relocateLagslayer = true;

    @ConfigEntry.Category("screen")
    @ConfigEntry.Gui.Tooltip
    public boolean codeGUI = false;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip
    public boolean clearInvReset = true;
}
