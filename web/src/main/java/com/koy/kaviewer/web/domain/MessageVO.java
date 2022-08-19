package com.koy.kaviewer.web.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;

// org.apache.kafka.clients.producer.ProducerRecord
@Data
@ToString
@NoArgsConstructor
public class MessageVO {

    //    @NotEmpty(message = "topic can not be empty.")
    private String topic;
    private int partition;
    private List<HeaderVO> headers = List.of();
    private String key;
    private String value;

    public boolean inValid() {
        return StringUtils.isEmpty(topic);
    }
}
