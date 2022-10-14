package com.example.perceptron.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weight {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<Double> weightsOnNeuron;

  public Weight(Weight weight) {
    this.weightsOnNeuron = new ArrayList<>();
    this.weightsOnNeuron.addAll(weight.getWeightsOnNeuron());
  }
}
