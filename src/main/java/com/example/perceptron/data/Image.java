package com.example.perceptron.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Image {
  private final List<Double> inputs;
  private final List<Double> outputs;

  public Image(List<Double> inputs, Function<List<Double>, List<Double>> educationalRule) {
    this.inputs = inputs;
    this.outputs = educationalRule.apply(inputs);
  }

  public List<Double> getInputs() {
    return new ArrayList<>(inputs);
  }

  public List<Double> getOutputs() {
    return new ArrayList<>(outputs);
  }

  public int inputsCount() {
    return inputs.size();
  }

  public int outputsCount() {
    return outputs.size();
  }
}
