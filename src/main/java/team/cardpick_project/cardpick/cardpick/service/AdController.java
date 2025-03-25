package team.cardpick_project.cardpick.cardpick.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.cardpick_project.cardpick.cardpick.cardpickDto.AdResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdRequest;

@RestController
public class AdController {
    public final AdvertiseService advertiseService;

    public AdController(AdvertiseService advertiseService) {
        this.advertiseService = advertiseService;
    }

    @PostMapping("/ad")
    public AdResponse create(@RequestBody CreateAdRequest request){
        return advertiseService.create(request);
    };


}
