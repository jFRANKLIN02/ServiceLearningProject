package org.example.servicelearningreporting.repos;

import org.example.servicelearningreporting.models.ActivityForm;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ActivityFormRepo extends JpaRepository<ActivityForm, Long>{
    @Nullable Object findActivityFormBySubmitted(boolean b);
    List<ActivityForm> findBySubmitted(Boolean submitted);
    Page<ActivityForm> findBySubmittedAndUserID(boolean submitted, Integer userID, Pageable pageable);
    Page<ActivityForm> findBySubmittedAndUserIDAndCampusContainingIgnoreCaseAndProgramContainingIgnoreCase(
            boolean submitted,
            Integer userID,
            String campus,
            String program,
            Pageable pageable);

    @Query("SELECT a FROM ActivityForm a WHERE a.submitted = true AND a.userID = :userID " +
            "AND (:campus IS NULL OR :campus = '' OR LOWER(a.campus) LIKE LOWER(CONCAT('%', :campus, '%'))) " +
            "AND (:program IS NULL OR :program = '' OR LOWER(a.program) LIKE LOWER(CONCAT('%', :program, '%'))) " +
            "AND (:search IS NULL OR :search = '' OR " +
            "LOWER(a.organizationName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.activityDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.campus) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.program) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:startDate IS NULL OR a.date >= :startDate) " +
            "AND (:endDate IS NULL OR a.date <= :endDate)")
    Page<ActivityForm> findBySubmittedAndUserIDWithFiltersAndSearchAndDateRange(
            @Param("userID") Integer userID,
            @Param("campus") String campus,
            @Param("program") String program,
            @Param("search") String search,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    // For in-progress forms with date range
    @Query("SELECT a FROM ActivityForm a WHERE a.submitted = false AND a.userID = :userID " +
            "AND (:campus IS NULL OR :campus = '' OR LOWER(a.campus) LIKE LOWER(CONCAT('%', :campus, '%'))) " +
            "AND (:program IS NULL OR :program = '' OR LOWER(a.program) LIKE LOWER(CONCAT('%', :program, '%'))) " +
            "AND (:search IS NULL OR :search = '' OR " +
            "LOWER(a.organizationName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.activityDescription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.campus) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.program) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:startDate IS NULL OR a.date >= :startDate) " +
            "AND (:endDate IS NULL OR a.date <= :endDate)")
    Page<ActivityForm> findBySubmittedFalseAndUserIDWithFiltersAndSearchAndDateRange(
            @Param("userID") Integer userID,
            @Param("campus") String campus,
            @Param("program") String program,
            @Param("search") String search,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    // For submitted forms date range statistics
    @Query("SELECT MIN(a.date) as minDate, MAX(a.date) as maxDate FROM ActivityForm a WHERE a.submitted = true AND a.userID = :userID")
    Object[] getSubmittedDateRangeStats(@Param("userID") Integer userID);

    // For in-progress forms date range statistics
    @Query("SELECT MIN(a.date) as minDate, MAX(a.date) as maxDate FROM ActivityForm a WHERE a.submitted = false AND a.userID = :userID")
    Object[] getInProgressDateRangeStats(@Param("userID") Integer userID);
}
