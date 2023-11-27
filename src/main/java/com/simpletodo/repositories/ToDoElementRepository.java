package com.simpletodo.repositories;

import com.simpletodo.entities.ToDoElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoElementRepository extends JpaRepository<ToDoElement, Long> {
    ToDoElement save(ToDoElement toDoElement);
    Optional<ToDoElement> findById(Long id);
    void delete(ToDoElement toDoElement);
}
