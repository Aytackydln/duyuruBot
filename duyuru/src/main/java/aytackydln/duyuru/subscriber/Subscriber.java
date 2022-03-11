package aytackydln.duyuru.subscriber;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Subscriber implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String language;
    private String status;
    private List<Subscription> subscriptions;

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
