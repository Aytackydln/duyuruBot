package aytackydln.duyuru.adapter;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.announcement.port.AnnouncementReaderPort;
import aytackydln.duyuru.topic.Topic;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnouncementReader implements AnnouncementReaderPort {

    @Override
    @SneakyThrows
    public List<Announcement> readAnnouncements(Topic topic) {
        List<Announcement> announcements = new ArrayList<>();
        final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
        final Elements links = doc.select(topic.getAnnSelector());
        for (Element link : links) {
            announcements.add(buildAnnouncement(topic, link));
        }
        return announcements;
    }

    private Announcement buildAnnouncement(Topic topic, Element htmlElement) {
        var announcement = new Announcement();
        if (ObjectUtils.isEmpty(topic.getAnnTitleSelector())) {
            announcement.setTitle(htmlElement.html());
        } else {
            announcement.setTitle(htmlElement.selectFirst(topic.getAnnTitleSelector()).html());
        }
        announcement.setTopic(topic);
        if (ObjectUtils.isEmpty(topic.getAnnLinkSelector())) {
            announcement.setLink(htmlElement.attr("href"));
        } else {
            announcement.setLink(htmlElement.selectFirst(topic.getAnnLinkSelector()).attr("href"));
        }
        return announcement;
    }
}
