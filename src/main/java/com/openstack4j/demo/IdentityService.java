package com.openstack4j.demo;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@PropertySource("appAuth.properties")
public class IdentityService {

  @Autowired
  Environment env;

  @Transactional
  public OSClient.OSClientV3 getToken() {

    return OSFactory.builderV3()
        .endpoint(env.getProperty("AUTH_ENDPOINT"))
        .credentials("admin", env.getProperty("ADMIN_PASSWORD"),
            Identifier.byName(env.getProperty("DOMAIN_NAME")))
        .scopeToProject(Identifier.byName("admin"),
            Identifier.byName(env.getProperty("DOMAIN_NAME")))
        .authenticate();
  }
}
