package aytackydln.duyuru.topic;

import aytackydln.duyuru.subscriber.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.List;

@Getter
@Setter
public class Topic {
    private String id;
    private String departmentId;
    private String baseLink;
    private String boardAppend;
    private String annSelector;
    private String annTitleSelector;
    private String annLinkSelector;
    private List<Subscription> subscriptions;

    @Override
    public String toString() {
        return id;
    }

    @Transient
    public final String getAnnouncementLink() {
        return getBaseLink() + getBoardAppend();
    }
}
