package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityForm;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityFormRepo extends JpaRepository<ActivityForm, Long>{
    @Nullable Object findActivityFormBySubmitted(boolean b);
    List<ActivityForm> findBySubmitted(Boolean submitted);
    List<ActivityForm> findBySubmittedAndUserID(boolean submitted, Integer userID);
}
