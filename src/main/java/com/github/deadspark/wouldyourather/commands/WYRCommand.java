package com.github.deadspark.wouldyourather.commands;

import com.github.deadspark.wouldyourather.WouldYouRather;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WYRCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("stoptask")) {

                    if (player.hasPermission("wouldyourather.stoptask")) {

                        Bukkit.getServer().getScheduler().cancelTask(WouldYouRather.getWouldYouRatherTask());

                    }else {

                        player.sendMessage(ChatColor.RED + "You don't have permission to execute this command!");

                    }

                }else if (args[0].equalsIgnoreCase("help")) {

                    player.sendMessage(ChatColor.GREEN + "The commands available are:");
                    player.sendMessage(ChatColor.BLUE + "/wyr stoptask" + ChatColor.RESET + " - " + ChatColor.AQUA + "stops the main task which causes the questioning to be discontinued (reload the plugin to restart the task)");
                    player.sendMessage(ChatColor.BLUE + "/wyr help" + ChatColor.RESET + " - " + ChatColor.AQUA + "show the available list of commands");

                }

            }else {

                player.sendMessage(ChatColor.RED + "please provide parameters (/wyr help) for more info");

            }

        }else {

            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("stoptask")) {

                    Bukkit.getServer().getScheduler().cancelTask(WouldYouRather.getWouldYouRatherTask());
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You don't have permission to execute this command!");


                }else if (args[0].equalsIgnoreCase("help")) {

                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "The commands available are:");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "/wyr stoptask" + ChatColor.RESET + " - " + ChatColor.AQUA + "stops the main task which causes the questioning to be discontinued (reload the plugin to restart the task)");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "/wyr help" + ChatColor.RESET + " - " + ChatColor.AQUA + "show the available list of commands");

                }

            }else {

                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "please provide parameters (/wyr help) for more info");

            }

        }

        return false;
    }

}
