package com.github.deadspark.wouldyourather;

import com.github.deadspark.wouldyourather.commands.SendVoteToOne;
import com.github.deadspark.wouldyourather.commands.SendVoteToTwo;
import com.github.deadspark.wouldyourather.commands.WYRCommand;
import com.github.deadspark.wouldyourather.utils.OptionVotes;
import com.github.deadspark.wouldyourather.utils.QuestionsDataFile;
import com.github.deadspark.wouldyourather.utils.RandomWouldYouRather;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public final class WouldYouRather extends JavaPlugin {

    static int wouldYouRatherTask;
    static int questionNumber = 0;
    boolean haveHoverEvent = true;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        // ---------------------------Plugin startup logic-----------------------------
        printPluginEnabled(true);
        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Vault dependency not found economic rewards will not work without vault");
        }

        // ---------------------------Loading config files-----------------------------
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        QuestionsDataFile.setup();

        // ---------------------------Registering commands-----------------------------
        getCommand("voteOptionOne").setExecutor(new SendVoteToOne());
        getCommand("voteOptionTwo").setExecutor(new SendVoteToTwo());
        getCommand("wyr").setExecutor(new WYRCommand());

        // --------------------------Scheduling main task------------------------------
        BukkitScheduler scheduler = getServer().getScheduler();

        int questioningInterval = getConfig().getInt("Interval");

        wouldYouRatherTask = scheduler.scheduleSyncRepeatingTask(this, () -> {

            ArrayList<String> randomQuestion = RandomWouldYouRather.getRandomQuestions();

            TextComponent optionOne = new TextComponent(randomQuestion.get(0));
            TextComponent optionTwo = new TextComponent(randomQuestion.get(1));

            optionOne.setItalic(true);
            optionOne.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            optionOne.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/voteOptionOne " + questionNumber));

            optionTwo.setItalic(true);
            optionTwo.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            optionTwo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/voteOptionTwo " + questionNumber));

            if (haveHoverEvent) {
                try {

                    optionOne.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to vote").create()));
                    optionTwo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to vote").create()));

                }catch (Exception e) {

                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error trying to set hover text for votes");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Disabled the hover text reload to enable it");
                    haveHoverEvent = false;

                }
            }

            for (Player player : getServer().getOnlinePlayers()) {

                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "\nWould you rather....");
                player.sendMessage(" ");
                player.spigot().sendMessage(optionOne);
                player.sendMessage(ChatColor.GRAY + "or");
                player.spigot().sendMessage(optionTwo);
                player.sendMessage(" ");
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "???");
                player.sendMessage(" ");
                player.sendMessage(ChatColor.GREEN + "Click on the options to vote. You can only vote one option and cannot revert your vote.\n ");

            }

            scheduler.runTaskLater(this, () -> {

                int optionOneVoteSize = OptionVotes.getOptionOneVotes().size();
                int optionTwoVoteSize = OptionVotes.getOptionTwoVotes().size();

                if (optionOneVoteSize > optionTwoVoteSize) {

                    for (Player player : OptionVotes.getOptionOneVotes()) {

                        for (String command : getConfig().getStringList("WinnerCustomCommands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<winner>", player.getName()));
                        }
                        player.sendMessage(ChatColor.GREEN + "Option one received the most votes and you are one of the winner");
                        int winAmount = getConfig().getInt("Amount");

                        if (setupEconomy()) {

                            if (!(winAmount <= 0)) {
                                EconomyResponse winReward = econ.depositPlayer(player, winAmount);

                                if (!winReward.transactionSuccess()) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error giving currency to winners please make sure you have vault installed");
                                }
                            }

                        }

                    }

                    for (Player player : OptionVotes.getOptionTwoVotes()) {

                        for (String command : getConfig().getStringList("LoserCustomCommands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<loser>", player.getName()));
                        }
                        player.sendMessage(ChatColor.RED + "Option one received the most votes and you had vote for option two....");
                        player.sendMessage(ChatColor.RED + "You lose....");

                    }

                }else if (optionTwoVoteSize > optionOneVoteSize) {

                    for (Player player : OptionVotes.getOptionTwoVotes()) {

                        for (String command : getConfig().getStringList("WinnerCustomCommands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<winner>", player.getName()));
                        }
                        player.sendMessage(ChatColor.GREEN + "Option two received the most votes and you are one of the winner");
                        int winAmount = getConfig().getInt("Amount");

                        if (setupEconomy()) {

                            if (!(winAmount <= 0)) {
                                EconomyResponse winReward = econ.depositPlayer(player, winAmount);

                                if (!winReward.transactionSuccess()) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an error giving currency to winners please make sure you have vault installed");
                                }
                            }

                        }

                    }

                    for (Player player : OptionVotes.getOptionOneVotes()) {

                        for (String command : getConfig().getStringList("LoserCustomCommands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<loser>", player.getName()));
                        }
                        player.sendMessage(ChatColor.RED + "Option two received the most votes and you had vote for option one....");
                        player.sendMessage(ChatColor.RED + "You lose....");

                    }

                }else {

                    for (Player player : OptionVotes.getVotedPlayers()) {

                        player.sendMessage(ChatColor.DARK_GREEN + "Both options got same number of votes and no one won");

                    }

                }

                for (String command : getConfig().getStringList("GlobalCustomCommands")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }

                OptionVotes.getVotedPlayers().clear();
                OptionVotes.getOptionTwoVotes().clear();
                OptionVotes.getOptionOneVotes().clear();

                questionNumber++;
                SendVoteToOne.nextQuestion();
                SendVoteToTwo.nextQuestion();

            }, questioningInterval - 100);

        }, 0L, questioningInterval);

    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
      printPluginEnabled(false);

    }

    public void printPluginEnabled(boolean pluginActivated) {

        if (pluginActivated) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +ChatColor.AQUA + "==============================");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +" ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +"Would you rather plugin version (ALPHA-1.0) " + ChatColor.GREEN + "enabled");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +" ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +ChatColor.AQUA + "==============================");
        }else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +ChatColor.AQUA + "==============================");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +" ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +"Would you rather plugin version (ALPHA-1.0) " + ChatColor.RED + "disabled");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +" ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "( Would You Rather ) " + ChatColor.RESET +ChatColor.AQUA + "==============================");
        }

    }

    public static int getWouldYouRatherTask() {
        return wouldYouRatherTask;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}

