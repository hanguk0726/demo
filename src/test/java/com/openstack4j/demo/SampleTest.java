package com.openstack4j.demo;

import org.junit.jupiter.api.Test;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.builder.BlockDeviceMappingBuilder;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.*;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.openstack.OSFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    Identifier domainIdentifier = Identifier.byName("Default");

    OSClient.OSClientV3 os = OSFactory.builderV3()
            .endpoint("http://192.168.18.129:5000/v3")
            .credentials("admin", "test123", domainIdentifier)
            .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
            .authenticate();

    @Test
    void 빈_이미지만들기() {
        Image image1 = os.imagesV2().create(
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
    }

    @Test
    void 네트워크_만들기() {

        Identifier domainIdentifier = Identifier.byName("Default");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();

        Network network1 = os.networking().network()
                .create(Builders.network()
                        .adminStateUp(true)
                        .physicalNetwork("providerPhysicalNetwork")
                        .networkType(NetworkType.FLAT)
                        .isRouterExternal(true)
                        .name("ext_net")
                        .build());

        Subnet subnet1 = os.networking().subnet().create(Builders.subnet()
                .name("ext_subnet")
                .networkId(network1.getId())
                .addPool("211.183.3.100", "211.183.3.200")
                .ipVersion(IPVersionType.V4)
                .cidr("211.183.3.0/24")
                .build());

        Port port1 = os.networking().port().create(Builders.port()
                .name("ext-port1")
                .networkId(network1.getId())
                .fixedIp("211.183.3.101", subnet1.getId())
                .build());

    }

    @Test
    void 키페어_만들기() {
        Identifier domainIdentifier = Identifier.byName("Default");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();

        Keypair kp = os.compute().keypairs().create("First-keypair", null);
    }

    @Test
    void 보안그룹_만들기() {
        Identifier domainIdentifier = Identifier.byName("Default");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();

        SecGroupExtension scg = os.compute().securityGroups().create("First Group", "Permits ICMP and SSH");
        // Permit Port 80 against an existing Group for anyone
        SecGroupExtension.Rule rule = os.compute().securityGroups()
                .createRule(Builders.secGroupRule()
                        .parentGroupId(scg.getId())
                        .protocol(IPProtocol.TCP)
                        .cidr("0.0.0.0/0")
                        .range(80, 80)
                        .build());

    }

    @Test
    void 볼륨_맵핑() {
        Identifier domainIdentifier = Identifier.byName("Default");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();

        Volume volume1 = os.blockStorage().volumes()
                .create(Builders.volume()
                        .name("First Volume")
                        .description("Simple volume to store backups on")
                        .size(20)
                        .bootable(true)
                        .build()
                );

//        BlockDeviceMappingBuilder blockDeviceMappingBuilder = Builders.blockDeviceMapping()
//                .uuid(volume1.getId())
//                .deviceName("/dev/vda")
//                .bootIndex(1);
    }

    @Test
    void 인스턴스_만들기() throws MalformedURLException {
        Identifier domainIdentifier = Identifier.byName("Default");


        OSClient.OSClientV3 osClientV3 = OSFactory.builderV3()
                .endpoint("http://192.168.18.129:5000/v3")
                .credentials("admin", "test123", domainIdentifier)
                .scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
                .authenticate();


        BlockDeviceMappingBuilder blockDeviceMappingBuilder = Builders.blockDeviceMapping()
                .uuid("5fa98799-e229-4fdd-a66c-b801f0c1405e")
                .deviceName("/dev/vdb")
                .bootIndex(0);

        ServerCreate sc = Builders.server()
                .name("cirros-inst1")
                .flavor("1")
                .image("98c150d7-cbf8-4d73-b990-8159fe4c3cd4")
                .blockDevice(blockDeviceMappingBuilder.build())
                .addNetworkPort("ff33557b-b0ad-413b-8ef5-5c8aeb2a0624")
                .addSecurityGroup("default").keypairName("my-keypair").build();

        Server server = osClientV3.compute().servers().boot(sc);
    }


}