package com.openstack4j.demo;

import com.openstack4j.demo.dto.ImageCreateRequestDto;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.internal.OSClientSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import static org.openstack4j.api.OSClient.OSClientV3;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.servlet.view.RedirectView;

@Service
public class ImageService {

  @Transactional
  public List<? extends Image> listImage(Token token) {
    OSClientV3 clientV3 = OSFactory.clientFromToken(token);
    return clientV3.imagesV2().list();

  }

  @Transactional
  public Image getImage(Token token, String iamgeId) {
    OSClientV3 clientV3 = OSFactory.clientFromToken(token);
    return clientV3.imagesV2().get(iamgeId);
  }

  @Transactional
  public void createImage(Token token, ImageCreateRequestDto dto) throws IOException {
    OSClientV3 clientV3 = OSFactory.clientFromToken(token);
    Image imageSetUp = clientV3.imagesV2().create(
        Builders.imageV2()
            .name(dto.getName())
            .containerFormat(ContainerFormat.BARE)
            .visibility(Image.ImageVisibility.PUBLIC)
            .diskFormat(DiskFormat.value(dto.getDiskFormat()))
            .build()
    );
    InputStream imageFileData = dto.getFile().getInputStream();
    Payload<InputStream> payload = Payloads.create(imageFileData);
    Image imageInstance = clientV3.imagesV2().get(imageSetUp.getId());
    clientV3.imagesV2().upload(imageInstance.getId(), payload, imageInstance);
  }

  @Transactional
  public void updateImage(Token token, String name, String imageId) {
    OSClientV3 clientV3 = OSFactory.clientFromToken(token);
    clientV3.imagesV2().update(clientV3.imagesV2().get(imageId)
        .toBuilder().containerFormat(ContainerFormat.BARE).name(name).build());
  }

  @Transactional
  public void deleteImage(Token token, String imageId) {
    OSClientV3 clientV3 = OSFactory.clientFromToken(token);
    clientV3.imagesV2().delete(imageId);

  }
}
