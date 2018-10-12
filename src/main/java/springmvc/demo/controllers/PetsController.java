package springmvc.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springmvc.demo.Repositories.PetsRepository;
import springmvc.demo.models.Pet;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

@RestController
@RequestMapping("api/pets")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class PetsController {

    @Autowired
    private PetsRepository petsRepository;

    public void setPetsRepository(PetsRepository pet) {

        petsRepository = pet;
    }

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    public Resources<Pet> getPetList() {
        System.out.println("get all pets...");
        List<Pet> petList = petsRepository.findAll();

        for(Pet p: petList) {
            p.removeLinks();
            Link selfLink = linkTo(PetsController.class).slash(p.getName()).withSelfRel();
            p.add(selfLink);
        }

        Link link = linkTo(PetsController.class).withSelfRel();
        Resources<Pet> result = new Resources<>(petList, link);
        return result;
    }
}
