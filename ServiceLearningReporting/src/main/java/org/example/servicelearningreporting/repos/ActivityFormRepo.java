package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityFormRepo extends JpaRepository<ActivityForm,Integer> {
}
