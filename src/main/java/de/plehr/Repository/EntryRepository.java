package de.plehr.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.plehr.Model.DataEntry;
 
public interface EntryRepository extends JpaRepository<DataEntry, Integer> {

    List<DataEntry> findByTopic(String name);

    List<DataEntry> findBySource(String name);

 
}