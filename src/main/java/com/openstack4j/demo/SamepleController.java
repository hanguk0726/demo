package com.openstack4j.demo;

import lombok.RequiredArgsConstructor;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.internal.OSClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class SamepleController {


  private final SampleService sampleService;
  private final IdentityService identityService;

  @GetMapping("/")
  public ModelAndView goIndex() {
    String message = "TEST";
    ModelAndView mv = new ModelAndView();
    mv.addObject("message", message);
    mv.addObject("list_image", sampleService.list_image(
        identityService.getToken()));
    mv.setViewName("index");
    return mv;
  }

  @PostMapping("/upload_image")
  public RedirectView postFile(@RequestParam MultipartFile file,
      @RequestParam String image_name,
      RedirectAttributes redirectAttributes) throws IOException {
    OSClient.OSClientV3 token = identityService.getToken();
    sampleService.upload_image(file, token);
    System.out.printf("Printing image_name %s", image_name);

    return new RedirectView("/");
  }

}
