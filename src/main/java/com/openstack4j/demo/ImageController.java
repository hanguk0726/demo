package com.openstack4j.demo;

import com.openstack4j.demo.dto.ImageCreateRequestDto;
import com.openstack4j.demo.keystone.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ImageController {


  private final ImageService imageService;
  private final IdentityService identityService;

  @GetMapping("/")
  public ModelAndView goToIndex(ModelAndView mv) {
    String message = "TEST";
    mv.addObject("message", message);
    mv.addObject("listImage", imageService.listImage(
        identityService.getToken()));
    mv.setViewName("index");
    return mv;
  }

  @PostMapping("/createImage")
  public RedirectView createImage(@ModelAttribute("dto") ImageCreateRequestDto dto,
      RedirectAttributes redirectAttributes) throws IOException {
    System.out.printf("dto = %s", dto);
    imageService.createImage(dto, identityService.getToken());
    return new RedirectView("/");
  }

  @GetMapping("/updateImage/{imageId}")
  public ModelAndView updateImage(ModelAndView mv) {
    String message = "UPDATE";
    mv.addObject("message", message);
    mv.addObject("imageInstance", imageService.listImage(
        identityService.getToken()));
    mv.setViewName("updateImage");
    return mv;
  }



}
