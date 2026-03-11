package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityForm;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityFormRepo extends JpaRepository<ActivityForm, Long>{
    @Nullable Object findActivityFormBySubmitted(boolean b);
    List<ActivityForm> findBySubmitted(boolean submitted);
    List<ActivityForm> findBySubmittedOrderByDateAsc(boolean submitted);
    List<ActivityForm> findBySubmittedOrderByDateDesc(boolean submitted);
}
