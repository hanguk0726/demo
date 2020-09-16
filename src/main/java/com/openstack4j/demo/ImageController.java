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
@RequestMapping("/v2/images")
public class ImageController {


  private final ImageService imageService;
  private final IdentityService identityService;

  @GetMapping
  public ModelAndView indexImage(ModelAndView mv) {
    String message = "TEST";
    mv.addObject("message", message);
    mv.addObject("listImage", imageService.listImage(
        identityService.getToken()));
    mv.setViewName("index");
    return mv;
  }

  @PostMapping("/createImageRequest")
  public RedirectView createImage(@ModelAttribute("dto") ImageCreateRequestDto dto,
      RedirectAttributes redirectAttributes) throws IOException {
    imageService.createImage(identityService.getToken(), dto);
    return new RedirectView("/v2/images");
  }

  @GetMapping("/updateImage/{imageId}")
  public ModelAndView goToUpdateImage(@PathVariable String imageId, ModelAndView mv) {
    String message = "UPDATE";
    mv.addObject("message", message);
    mv.addObject("image", imageService.getImage(
        identityService.getToken(), imageId));
    mv.addObject("imageId",imageId);
    mv.setViewName("updateImage");

    return mv;
  }

  @PostMapping("/updateImageRequest")
  public RedirectView updateImage(@RequestParam String name, @RequestParam String imageId) {
    imageService.updateImage(identityService.getToken(), name, imageId);
    return new RedirectView("/v2/images");
  }

  @PostMapping("/deleteImageRequest")
  public RedirectView deleteImage(@RequestParam String imageId) {
    imageService.deleteImage(identityService.getToken(), imageId);
    return new RedirectView("/v2/images");
  }


}
