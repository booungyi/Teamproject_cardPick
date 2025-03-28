package team.cardpick_project.cardpick.ad;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import static team.cardpick_project.cardpick.ad.QAdvertising.advertising;

@RestController
public class AdController {
    private final AdService adService;


    public AdController(AdService adService) {
        this.adService = adService;
    }

//
//    //광고 생성
//    @PostMapping("/ads")
//    public void adCreate(@Valid @RequestBody AdRequest request) {
//        adService.save(request);
//    }
//
//
//    //여러광고 조회
//    @GetMapping("/ads")
//    public List<AdResponse> adRead(@RequestParam(required = false) Long id) {
//        if (id == null) {
//            return adService.findAllAds(); // 모든 광고 조회
//        } else {
//            AdRequest request = new AdRequest(id);
//            return adService.read(request); // 특정 ID의 광고 조회
//        }
//    }


    //광고 예산 소진 상태 확인 API
    @GetMapping("/ads/{id}/budget-status")
    public List<AdResponse> getBudgetStatus(@PathVariable Long id) {
        // 광고 ID에 해당하는 예산 상태를 조회하는 로직 구현
        List<AdResponse> adResponses = adService.getBudgetStatus(id);
        return adResponses;
    }


    // 여러 광고 조회 API (상태별로도 조회 가능)
    @GetMapping("/ads")
    public List<AdResponse> adRead(
            @RequestParam(required = false) AdStatuse status) { // 상태 추가
        if (status == null) {
            return adService.findAllAds(); // 모든 광고 조회
        }
        return adService.findAdsByStatus(status); // 상태에 맞는 광고 조회

    }


    // 상태별 광고 조회 API (ACTIVE, INACTIVE, EXPIRED 포함)
    @GetMapping("/ads/status")
    public List<AdResponse> getAdsByStatus(
            @RequestParam(value = "status", required = false) AdStatuse status) {

        if (status != null) {
            return adService.findAdsByStatus(status); // 상태가 주어지면 해당 상태의 광고만 조회
        } else {
            return adService.findAllAds(); // 상태가 없으면 모든 광고 조회
        }
    }

    //예산 추가시 광고 생성
    @PostMapping("/ads")
    public ResponseEntity<AdResponse> adCreate(@Valid @RequestBody AdRequest request) {
        AdResponse adResponse = adService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(adResponse);

}



}
