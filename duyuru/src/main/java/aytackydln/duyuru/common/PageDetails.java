package aytackydln.duyuru.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "of")
public class PageDetails {
    int page;
    int size;
    int totalElements;
}
