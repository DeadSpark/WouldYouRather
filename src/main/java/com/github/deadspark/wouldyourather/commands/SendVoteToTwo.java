package com.github.deadspark.wouldyourather.commands;

import com.github.deadspark.wouldyourather.utils.OptionVotes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SendVoteToTwo implements CommandExecutor {

    static int questionNumber = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 1) {

                if (args[0].equals(String.valueOf(questionNumber))) {

                    ArrayList<Player> votedPlayers = OptionVotes.getVotedPlayers();
                    ArrayList<Player> optionTwoVotes = OptionVotes.getOptionTwoVotes();

                    if (votedPlayers.contains(player)) {

                        player.sendMessage(ChatColor.RED + "Looks like you have already voted");

                    }else {

                        optionTwoVotes.add(player);
                        player.sendMessage(ChatColor.GREEN + "Successfully voted for option two");

                    }

                    votedPlayers.add(player);

                }else {

                    player.sendMessage("Seems like the question you are trying to vote for has been expired");

                }


            }else {

                player.sendMessage(ChatColor.RED + "Invalid arguments!");

            }

        }else {

            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Only players can execute this command and vote for an option!");

        }

        return false;
    }

    public static void nextQuestion() {
        questionNumber++;
    }

}
