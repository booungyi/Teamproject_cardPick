package team.cardpick_project.cardpick.cardAdverise;

import org.springframework.web.bind.annotation.*;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.AdResponse;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdTermRequest;
import team.cardpick_project.cardpick.cardPick.cardDto.ActiveResponse;

import java.util.List;

@RestController
public class AdController {
    public final AdvertiseService advertiseService;

    public AdController(AdvertiseService advertiseService) {
        this.advertiseService = advertiseService;
    }

    @PostMapping("/adCard")
    public void createAd(@RequestBody CreateAdRequest request) {
        System.out.println("📌 받은 요청 데이터: " + request);
        advertiseService.create(request);
    }

    @GetMapping("/ad")
    public List<ActiveResponse> findAd() {
        return advertiseService.findAllAD();
    }
    //광고 목록 조회 test용 추가
    @GetMapping("/adList")
    public List<AdResponse> findAdList() {
        return advertiseService.findAdList();
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
