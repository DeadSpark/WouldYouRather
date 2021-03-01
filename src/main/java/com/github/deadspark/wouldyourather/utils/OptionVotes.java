package com.github.deadspark.wouldyourather.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class OptionVotes {

    static ArrayList<Player> votedPlayers = new ArrayList<>();
    static ArrayList<Player> optionOneVotes = new ArrayList<>();
    static ArrayList<Player> optionTwoVotes = new ArrayList<>();

    public static ArrayList<Player> getVotedPlayers() {
        return votedPlayers;
    }

    public static ArrayList<Player> getOptionOneVotes() {
        return optionOneVotes;
    }

    public static ArrayList<Player> getOptionTwoVotes() {
        return optionTwoVotes;
    }

}
