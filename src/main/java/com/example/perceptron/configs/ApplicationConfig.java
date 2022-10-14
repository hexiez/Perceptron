package com.example.perceptron.configs;

import com.example.perceptron.data.Image;
import com.example.perceptron.data.Infrastructure;
import com.example.perceptron.data.Weight;
import com.example.perceptron.services.InfrastructureService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Configuration
public class ApplicationConfig {
  public static int inputsCount = 10;
  public static int outputsCount = 1;
  public static int imagesCount = 200;
  public static int testImagesCount = 15;
  public static int initWeight = 5;

  @Bean
  public List<Weight> weights() {
    List<Weight> values = new ArrayList<>();
    for (int i = 0; i < inputsCount + 1; i++) {
      values.add(new Weight());
      values.get(i).setWeightsOnNeuron(new ArrayList<>());
      for (int j = 0; j < outputsCount; j++) {
        values.get(i).getWeightsOnNeuron().add(ThreadLocalRandom.current().nextDouble(0, initWeight));
      }
    }
    return values;
  }

  @Bean
  @Scope("prototype")
  public List<Double> inputs() {
    List<Double> randomInputs = new ArrayList<>();
    for (int i = 0; i < inputsCount; i++) {
      randomInputs.add((double) ThreadLocalRandom.current().nextInt(0, 2));
    }
    return randomInputs;
  }

  //a or !b or c or !d or e or !f
  @Bean
  public Function<List<Double>, List<Double>> perceptronRule() {
    return inputs -> {
      List<Boolean> booleanInputs = inputs.stream()
        .map(input -> Boolean.parseBoolean(String.valueOf(input)))
        .toList();

      List<Double> result = new ArrayList<>();
      for (int i = 0; i < outputsCount; i++) {
        result.add((double) (booleanInputs.get(0) || !booleanInputs.get(1) || booleanInputs.get(2)
          || !booleanInputs.get(3) || booleanInputs.get(4) || !booleanInputs.get(5) ? 1 : 0)
        );
      }
      return result;
    };
  }

  @Bean
  public List<Image> educationalImageList() {
    List<Image> images = new ArrayList<>();
    for (int i = 0; i < imagesCount; i++) {
      images.add(new Image(inputs(), perceptronRule()));
    }
    return images;
  }

  @Bean
  public List<Image> educationalImageListTest() {
    List<Image> images = new ArrayList<>();
    for (int i = 0; i < testImagesCount; i++) {
      images.add(new Image(inputs(), perceptronRule()));
    }
    return images;
  }

  @Bean
  public Function<Double, Double> sigmoid() {
    return x -> 1 / (1 + Math.exp(-x));
  }

  @Bean
  public Infrastructure infrastructure(InfrastructureService service) {
    Infrastructure last = service.getLast();
    if (last != null) {
      return last;
    }
    return new Infrastructure(weights());
  }
}
