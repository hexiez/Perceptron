package com.example.perceptron;

import com.example.perceptron.data.Image;
import com.example.perceptron.services.Perceptron;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    Perceptron perceptron = context.getBean(Perceptron.class);

    System.out.println("Start learning...");
    perceptron.startLearning();
    System.out.println("End of learning!\n");
    System.out.println("Error summary: " + perceptron.errorSummary());
    System.out.println("Count of operation: " + perceptron.countOfOperation());
    List<Image> testImages = context.getBean("educationalImageListTest", List.class);
    perceptron.printTest(testImages);
  }
}
