package com.openstack4j.demo;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SampleService {

    @Transactional
    public List<? extends Image> list_image(OSClient.OSClientV3 token){
        /*List<? extends Image> list = token.imagesV2().list();
        for (Image image: list
        ) {
            image.getName

        }*/
        return token.imagesV2().list();

    }

    @Transactional
    public void upload_image(MultipartFile mfile, OSClient.OSClientV3 token) throws IOException {


        Image image1 = token.imagesV2().create(
                Builders.imageV2()
                        .name("Ubuntu")
                        .osVersion("ubuntu")
                        .containerFormat(ContainerFormat.BARE)
                        .visibility(Image.ImageVisibility.PUBLIC)
                        .diskFormat(DiskFormat.QCOW2)
                        .minDisk(0l)
                        .minRam(0l)
                        .build()
        );

        InputStream fileData = mfile.getInputStream();
        Payload<InputStream> payload = Payloads.create(fileData);

        Image image = token.imagesV2().get(image1.getId());

        token.imagesV2().upload(image.getId(),payload,image);
    }

    @Transactional
    void create_network() {
        Identifier domainIdentifier = Identifier.byName("Default");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();

        Network network = os.networking().network()
                .create(Builders.network()
                        .adminStateUp(true)
                        .physicalNetwork("providerPhysicalNetwork")
                        .networkType(NetworkType.FLAT)     //전제조건이 provider-network
                        .name("ext_net")
                        .build());
    }


}
