package aytackydln.duyuru.adapter;

import aytackydln.duyuru.topic.Department;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.topic.port.TopicReaderPort;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class TopicReaderAdapter implements TopicReaderPort {
    @Override
    public List<Topic> readTopics(Department department) {
        try {
            final Document doc = Jsoup.connect(department.getBaseLink() + department.getClassesUri()).get();
            final Elements courseLinks = doc.select(department.getClassElementSelector());

            return courseLinks.stream().map(l -> createTopic(department, l)).toList();
        } catch (IOException e) {
            LOGGER.info(department.getId()+" classes could not be loaded ("+e.getClass().getSimpleName()+")");
            LOGGER.trace("", e);
            throw new IllegalStateException(e);
        }
    }

    private Topic createTopic(Department department, Element l) {
        final String courseAppend = l.attr("href");
        final String courseId = l.html();

        Topic topic = new Topic();
        topic.setId(courseId);
        topic.setDepartmentId(department.getId());
        topic.setBoardAppend(courseAppend);
        topic.setAnnSelector(department.getClassAnnSelector());
        topic.setAnnTitleSelector(department.getClassAnnTitleSelector());
        topic.setAnnLinkSelector(department.getClassAnnLinkSelector());
        topic.setBaseLink(department.getBaseLink());
        return topic;
    }
}
