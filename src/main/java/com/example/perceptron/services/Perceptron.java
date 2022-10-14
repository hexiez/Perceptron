package com.example.perceptron.services;

import com.example.perceptron.data.Image;

import java.util.List;

public interface Perceptron {
  void startLearning();
  int countOfOperation();
  double errorSummary();
  void printTest(List<Image> images);
}
