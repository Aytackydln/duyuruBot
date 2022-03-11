package aytackydln.duyuru.topic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Department extends Topic {
    private String classesUri;
    private String classElementSelector;
    private String classAnnSelector;
    private String classAnnTitleSelector;
    private String classAnnLinkSelector;
}
