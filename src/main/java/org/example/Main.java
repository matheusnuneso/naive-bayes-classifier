package org.example;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static List<Integer> linesWrongCategory = new ArrayList<>();

    public static void main(String[] args) {
        BayesClassifier<String, String> bayes = new BayesClassifier<>();

        String pathPositiveReviews = "src\\main\\resources\\positiveReviews.txt";
        String pathNegativeReviews = "src\\main\\resources\\negativeReviews.txt";
        String pathUnknownReviews = "src\\main\\resources\\unknownReviews.txt";

        List<String> listPositiveReviews = getTexts(pathPositiveReviews);
        List<String> listNegativeReviews = getTexts(pathNegativeReviews);
        List<String> listUnknownReviews = getTexts(pathUnknownReviews);

        for (String text : listPositiveReviews) {
            bayes.learn("positive", Arrays.asList(text.split("\\s")));
        }

        for (String text : listNegativeReviews) {
            bayes.learn("negative", Arrays.asList(text.split("\\s")));
        }

        for (int i = 0; i < listUnknownReviews.size(); i++) {
            String category = bayes.classify(Arrays.asList(listUnknownReviews.get(i).split("\\s"))).getCategory();

            if (i%2==0){
                System.out.println("expected: negative");

                if (!category.equals("negative")) {
                    linesWrongCategory.add(i+1);
                }

            } else {
                System.out.println("expected: positive");

                if (!category.equals("positive")) {
                    linesWrongCategory.add(i+1);
                }
            }

            System.out.println("was: " + category);
            System.out.println("-------------------");
        }

        System.out.print("Linhas que o classificador errou: ");
        for (Integer line : linesWrongCategory) {
            System.out.print(line + ", ");
        }
    }

    public static List<String> getTexts(String path){
        List<String> listTexts = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line = bf.readLine();

            while (line != null) {

                line = Normalizer.normalize(line, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "")
                        .replaceAll("[^a-zA-Z0-9\\s+]", "")
                        .toLowerCase();

                listTexts.add(line);
                line = bf.readLine();
            }

        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        return listTexts;
    }
}