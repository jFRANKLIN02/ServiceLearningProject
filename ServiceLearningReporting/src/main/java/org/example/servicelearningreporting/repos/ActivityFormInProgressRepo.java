package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityFormInProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityFormInProgressRepo extends JpaRepository<ActivityFormInProgress, Long> {
}
