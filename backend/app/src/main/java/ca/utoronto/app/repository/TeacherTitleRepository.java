package ca.utoronto.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.utoronto.app.model.TeacherTitle;

@Repository
public interface TeacherTitleRepository extends JpaRepository<TeacherTitle, Long> {}
