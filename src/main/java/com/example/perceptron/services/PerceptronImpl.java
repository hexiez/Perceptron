package com.example.perceptron.services;

import com.example.perceptron.data.Image;
import com.example.perceptron.data.Infrastructure;
import com.example.perceptron.data.Weight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class PerceptronImpl implements Perceptron{
  public static final int biasNeuron = 1;
  public static final double paceOfLearning = 0.5;
  public static final double errorBound = 5.0e-15;
  public static final int maxIterationCount = 100;

  private final Infrastructure infrastructure;
  private final List<Image> imageList;
  private final Function<Double, Double> sigmoid;
  private final InfrastructureService infrastructureService;

  private int countOfOperation = 0;
  private double errorSummary = 0;

  @Autowired
  public PerceptronImpl(Infrastructure infrastructure, @Qualifier("educationalImageList") List<Image> imageList, Function<Double, Double> sigmoid, InfrastructureService service) {
    this.infrastructure = infrastructure;
    this.imageList = imageList;
    this.sigmoid = sigmoid;
    this.infrastructureService = service;
  }

  @Override
  public void startLearning() {
    do {
      errorSummary = 0.0;
      for (Image image : imageList) {
        infrastructure.setInputs(image.getInputs());
        for (int i = 0; i < image.outputsCount(); i++) {
          double accum = biasNeuron * infrastructure.getWeights().get(0).getWeightsOnNeuron().get(i);
          for (int j = 1; j < image.inputsCount() + 1; j++) {
            accum += infrastructure.getInputs().get(j - 1) * infrastructure.getWeights().get(j).getWeightsOnNeuron().get(i);
          }
          infrastructure.getOutputs().set(i, sigmoid.apply(accum));
          double delta = image.getOutputs().get(i) - infrastructure.getOutputs().get(i);
          errorSummary += 0.5 * Math.pow(delta, 2);
          rosenblattDeltaLearning(infrastructure.getWeights(), i, delta, image);
        }
      }
      countOfOperation++;
    } while (countOfOperation <= maxIterationCount && errorSummary > errorBound);
    infrastructureService.save(new Infrastructure(infrastructure));
  }

  private void rosenblattDeltaLearning(List<Weight> allWeights, int outputIndex, double delta, Image image) {
    List<Double> weight = allWeights.get(0).getWeightsOnNeuron();
    weight.set(outputIndex, weight.get(outputIndex) + paceOfLearning * biasNeuron * delta);
    for (int i = 1; i < image.inputsCount() + 1; i++) {
      weight = allWeights.get(i).getWeightsOnNeuron();
      weight.set(outputIndex, weight.get(outputIndex) + paceOfLearning * image.getInputs().get(i - 1) * delta);
    }
  }

  @Override
  public void printTest(List<Image> images) {
    StringBuilder resultText = new StringBuilder();
    resultText.append("\nTests[input -> actual : expected]:\n");
    for (Image image : images) {
      infrastructure.setInputs(image.getInputs());
      for (int i = 0; i < image.outputsCount(); i++) {
        double accum = biasNeuron * infrastructure.getWeights().get(0).getWeightsOnNeuron().get(i);
        for (int j = 1; j < image.inputsCount() + 1; j++) {
          accum += infrastructure.getInputs().get(j - 1) * infrastructure.getWeights().get(j).getWeightsOnNeuron().get(i);
        }
        infrastructure.getOutputs().set(i, sigmoid.apply(accum));
      }
      for (int j = 0; j < image.inputsCount(); j++) {
        resultText.append(infrastructure.getInputs().get(j) >= 0.5 ? 1 : 0).append(" ");
      }
      resultText.append("-> ");
      for (int j = 0; j < image.outputsCount(); j++) {
        resultText.append(infrastructure.getOutputs().get(j) >= 0.5 ? 1 : 0).append(" ");
      }
      resultText.append(": ");
      for (int j = 0; j < image.outputsCount(); j++) {
        resultText.append(image.getOutputs().get(j) >= 0.5 ? 1 : 0).append(" ");
      }
      resultText.append("\n");
    }
    System.out.println(resultText);
  }

  @Override
  public int countOfOperation() {
    return countOfOperation;
  }

  @Override
  public double errorSummary() {
    return errorSummary;
  }
}