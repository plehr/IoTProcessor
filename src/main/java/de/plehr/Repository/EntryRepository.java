package de.plehr.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.plehr.Model.DataEntry;
 
public interface EntryRepository extends JpaRepository<DataEntry, Integer> {
 
}