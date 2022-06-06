package ca.utoronto.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamPeriod extends BaseEntity<Long> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate registrationStartDate;

    @Column(nullable = false)
    private LocalDate registrationEndDate;

    @Column(nullable = false)
    private LocalDate examStartDate;

    @Column(nullable = false)
    private LocalDate examEndDate;

    @ManyToOne(optional = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "examPeriod")
    private Set<ExamTerm> examTerms = new HashSet<>();
}
