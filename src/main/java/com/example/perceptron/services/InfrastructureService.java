package com.example.perceptron.services;

import com.example.perceptron.data.Infrastructure;
import com.example.perceptron.data.InfrastructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfrastructureService {
  private final InfrastructureRepository repository;

  @Autowired
  public InfrastructureService(InfrastructureRepository repository) {
    this.repository = repository;
  }

  public void save(Infrastructure infrastructure) {
    repository.save(infrastructure);
  }

  public Infrastructure get(int id) {
    return repository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Infrastructure with id: " + id + " not found!"));
  }

  public Infrastructure getLast() {
    List<Infrastructure> all = repository.findAll();
    if (all.isEmpty()) {
      return null;
    } else {
      return all.get(all.size() - 1);
    }
  }
}
