package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityForm;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ActivityFormRepo extends JpaRepository<ActivityForm, Long>{
    @Nullable Object findActivityFormBySubmitted(boolean b);
    List<ActivityForm> findBySubmitted(boolean submitted);

    //sort
    List<ActivityForm> findBySubmittedOrderByDateAsc(boolean submitted);
    List<ActivityForm> findBySubmittedOrderByDateDesc(boolean submitted);

    //search submitted forms
    @Query("""
SELECT af FROM ActivityForm af
WHERE af.submitted = true AND (
LOWER(af.campus) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.program) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.organizationName) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.activityDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.city) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.email) LIKE LOWER(CONCAT('%', :search, '%'))
)
""")
    List<ActivityForm> searchSubmittedForms(@Param("search") String search);

    //Search in progress
    @Query("""
SELECT af FROM ActivityForm af
WHERE af.submitted = false AND (
LOWER(af.campus) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.program) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.organizationName) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.activityDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.city) LIKE LOWER(CONCAT('%', :search, '%')) OR
LOWER(af.email) LIKE LOWER(CONCAT('%', :search, '%'))
)
""")
    List<ActivityForm> searchInProgressForms(@Param("search") String search);
}
