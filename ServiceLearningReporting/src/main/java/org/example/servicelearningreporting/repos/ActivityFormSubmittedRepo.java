package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityFormSubmitted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityFormSubmittedRepo extends JpaRepository<ActivityFormSubmitted, Long> {
}
