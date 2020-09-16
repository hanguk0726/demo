package com.openstack4j.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class ImageCreateRequestDto {

  @NonNull
  private String name;

  @NonNull
  private String diskFormat;

  @NonNull
  private MultipartFile file;



}
