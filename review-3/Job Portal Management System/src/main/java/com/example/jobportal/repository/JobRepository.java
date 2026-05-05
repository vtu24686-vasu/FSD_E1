package com.example.jobportal.repository;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);
    List<Job> findByCategoryContainingIgnoreCaseOrLocationContainingIgnoreCaseOrTitleContainingIgnoreCase(String category, String location, String title);

    @Query("""
        select j from Job j
        where j.active = true
          and (:keyword is null or :keyword = '' or
               lower(j.title) like lower(concat('%', :keyword, '%')) or
               lower(j.category) like lower(concat('%', :keyword, '%')) or
               lower(j.location) like lower(concat('%', :keyword, '%')) or
               lower(j.description) like lower(concat('%', :keyword, '%')))
          and (:location is null or :location = '' or lower(j.location) like lower(concat('%', :location, '%')))
          and (:category is null or :category = '' or lower(j.category) = lower(:category))
          and (:experience is null or :experience = '' or lower(j.experienceLevel) like lower(concat('%', :experience, '%')))
          and (:company is null or :company = '' or lower(j.employer.companyName) like lower(concat('%', :company, '%')) or lower(j.employer.name) like lower(concat('%', :company, '%')))
          and (:jobType is null or :jobType = '' or lower(j.jobType) = lower(:jobType))
          and (:minSalary is null or j.maxSalary is null or j.maxSalary >= :minSalary)
          and (:maxSalary is null or j.minSalary is null or j.minSalary <= :maxSalary)
          and (j.deadline is null or j.deadline >= current_date)
        order by j.createdAt desc
        """)
    List<Job> searchActive(@Param("keyword") String keyword,
                           @Param("location") String location,
                           @Param("category") String category,
                           @Param("experience") String experience,
                           @Param("company") String company,
                           @Param("jobType") String jobType,
                           @Param("minSalary") Integer minSalary,
                           @Param("maxSalary") Integer maxSalary);

    @Query("""
        select j from Job j
        where j.active = true
          and (:keyword is null or :keyword = '' or
               lower(j.title) like lower(concat('%', :keyword, '%')) or
               lower(j.category) like lower(concat('%', :keyword, '%')) or
               lower(j.location) like lower(concat('%', :keyword, '%')) or
               lower(j.description) like lower(concat('%', :keyword, '%')))
          and (:location is null or :location = '' or lower(j.location) like lower(concat('%', :location, '%')))
          and (:category is null or :category = '' or lower(j.category) = lower(:category))
          and (:experience is null or :experience = '' or lower(j.experienceLevel) like lower(concat('%', :experience, '%')))
          and (:company is null or :company = '' or lower(j.employer.companyName) like lower(concat('%', :company, '%')) or lower(j.employer.name) like lower(concat('%', :company, '%')))
          and (:jobType is null or :jobType = '' or lower(j.jobType) = lower(:jobType))
          and (:minSalary is null or j.maxSalary is null or j.maxSalary >= :minSalary)
          and (:maxSalary is null or j.minSalary is null or j.minSalary <= :maxSalary)
          and (j.deadline is null or j.deadline >= current_date)
        """)
    Page<Job> searchActivePage(@Param("keyword") String keyword,
                               @Param("location") String location,
                               @Param("category") String category,
                               @Param("experience") String experience,
                               @Param("company") String company,
                               @Param("jobType") String jobType,
                               @Param("minSalary") Integer minSalary,
                               @Param("maxSalary") Integer maxSalary,
                               Pageable pageable);

    @Query("select j.title from Job j where j.active = true and lower(j.title) like lower(concat('%', :term, '%')) order by j.title")
    List<String> suggestTitles(@Param("term") String term, Pageable pageable);
}
