package com.github.deadspark.wouldyourather.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SplittableRandom;

public class RandomWouldYouRather {

    public static ArrayList<String> getRandomQuestions() {

        ArrayList<String> randomQuestion = new ArrayList<>();

        try {

            Scanner scanner = new Scanner(QuestionsDataFile.get());
            ArrayList<String> allQuestions = new ArrayList<>();

            while (scanner.hasNextLine()) {
                allQuestions.add(scanner.nextLine());
            }

            int randomInt = new SplittableRandom().nextInt(0, allQuestions.size());

            String[] options = allQuestions.get(randomInt).split(" :or: ");

            randomQuestion.add(options[0]);
            randomQuestion.add(options[1]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return randomQuestion;

    }

}
