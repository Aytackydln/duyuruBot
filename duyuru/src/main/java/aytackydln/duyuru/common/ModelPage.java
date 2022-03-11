package aytackydln.duyuru.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
public class ModelPage<T> {
    List<T> content;
    PageDetails pageDetails;
}
