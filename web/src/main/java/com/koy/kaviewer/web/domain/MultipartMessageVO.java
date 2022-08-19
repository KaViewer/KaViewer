package com.koy.kaviewer.web.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class MultipartMessageVO {

    private String topic;
    private int partition;
    private List<HeaderVO> headers = List.of();
    private MultipartFile key;
    private MultipartFile value;
}
