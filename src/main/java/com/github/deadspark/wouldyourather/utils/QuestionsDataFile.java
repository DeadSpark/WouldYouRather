package com.github.deadspark.wouldyourather.utils;

import com.github.deadspark.wouldyourather.WouldYouRather;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QuestionsDataFile {

    static WouldYouRather plugin = JavaPlugin.getPlugin(WouldYouRather.class);

    static File questionsFile;

    public static void setup() {

        questionsFile = new File(plugin.getDataFolder(), "questions.txt");

        if (!questionsFile.exists()) {

            try {
                questionsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR CREATING THE QUESTIONS FILE");
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(questionsFile));
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR WRITING THE QUESTIONS FILE");
            }

            String[] questionsArray = new String[] {
                    "go into past and meet your ancestors :or: go into future and meet your grandchildren",
                    "have more time :or: have more money",
                    "have a rewind button on your life :or: have a pause button on your life",
                    "be able to talk with animals :or: speak all foreign languages",
                    "win the lottery :or: live twice as long",
                    "feel worse if no ones showed up in your wedding :or: feel worse if no one showed up on your funeral",
                    "be without internet for a week :or: be without your phone for a week",
                    "lose your vision :or: lose your hearing",
                    "work more hours per day, but fewer days :or: work fewer hours per day, but more days",
                    "listen to music from 70's :or: listen to music from today"

            };

            int i = 0;

            while (i < questionsArray.length) {
                try {
                    writer.write(questionsArray[i]);
                    writer.newLine();
                }catch (IOException e) {
                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR WRITING THE QUESTIONS FILE");
                }
                i++;
            }

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR WRITING THE QUESTIONS FILE");
            }

        }

    }

    public static File get() {
        return questionsFile;
    }

}
