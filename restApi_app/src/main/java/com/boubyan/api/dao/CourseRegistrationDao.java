package com.boubyan.api.dao;

import com.boubyan.api.model.CourseRegistration;
import com.boubyan.api.model.CourseRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRegistrationDao extends JpaRepository<CourseRegistration, CourseRegistrationId> {

}

