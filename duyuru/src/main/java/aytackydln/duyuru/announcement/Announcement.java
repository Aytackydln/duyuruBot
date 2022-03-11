package aytackydln.duyuru.announcement;

import aytackydln.duyuru.topic.Topic;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Announcement {
    private Topic topic;
    private String title;
    private String link;
    private Date date;

    public String getUrl() {
        return topic.getBaseLink() + link;
    }

    @Override
    public String toString() {
        return "<b>" + topic.toString() + "</b>\n<a href=\"" + getUrl() + "\">" + title + "</a>";
    }
}
