package aytackydln.duyuru.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity(name = "Department")
@PrimaryKeyJoinColumn(name = "topic_id")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DepartmentEntity extends TopicEntity {
    private String classesUri;
    private String classElementSelector;
    private String classAnnSelector;
    private String classAnnTitleSelector;
    private String classAnnLinkSelector;
}
