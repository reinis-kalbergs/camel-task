package com.example.cameltask.repository;

import com.example.cameltask.model.database.RegionReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionReportRepository extends JpaRepository<RegionReportEntity, Long> {
}
