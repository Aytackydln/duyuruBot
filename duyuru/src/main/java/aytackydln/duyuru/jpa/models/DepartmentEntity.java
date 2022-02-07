package aytackydln.duyuru.jpa.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

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
