package com.example.perceptron.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Infrastructure {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Double> inputs;

  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Double> outputs;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Weight> weights;

  public Infrastructure(Infrastructure infrastructure) {
    this.weights = new ArrayList<>();
    for (int i = 0; i < infrastructure.weights.size(); i++) {
      this.weights.add(new Weight(infrastructure.weights.get(i)));
    }

    this.inputs = new ArrayList<>();
    this.inputs.addAll(infrastructure.inputs);

    this.outputs = new ArrayList<>();
    this.outputs.addAll(infrastructure.outputs);
  }

  public Infrastructure(List<Weight> weights) {
    this.weights = weights;
    this.inputs = new ArrayList<>();
    this.outputs = new ArrayList<>(Stream.generate(() -> 0d).limit(weights.get(0).getWeightsOnNeuron().size()).toList());
  }
}
