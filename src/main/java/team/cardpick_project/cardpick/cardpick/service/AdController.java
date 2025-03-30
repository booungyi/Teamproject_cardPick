package team.cardpick_project.cardpick.cardpick.service;

import org.springframework.web.bind.annotation.*;
import team.cardpick_project.cardpick.cardpick.cardpickDto.ActiveResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.AdResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdTermRequest;

import java.util.List;

@RestController
public class AdController {
    public final AdvertiseService advertiseService;

    public AdController(AdvertiseService advertiseService) {
        this.advertiseService = advertiseService;
    }

    @PostMapping("/ad")
    public void createAd(@RequestBody CreateAdRequest request) {
        advertiseService.create(request);
    }

    @GetMapping("/ad")
    public List<ActiveResponse> findAd() {
        return advertiseService.findAllAD();
    }

    @PutMapping("/ad/{adCardId}")
    public void updateAd(
            @PathVariable Long adCardId,
            @RequestBody CreateAdTermRequest request) {
        advertiseService.termUpdate(adCardId, request);
    }

    @DeleteMapping("ad/{adCardId}")
    public void deleteAd(@PathVariable Long adCardId) {
        advertiseService.deleteAd(adCardId);
    }
}
