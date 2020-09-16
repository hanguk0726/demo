package com.openstack4j.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImageCreateRequestDto {

  @NonNull
  private String imageName;

  private String imageDesc;

  @NonNull
  private String imageDiskFormat;

  @NonNull
  private MultipartFile imageFile;



}
