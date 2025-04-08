package team.cardpick_project.cardpick.cardAdverise;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.*;
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




    //광고 클릭시 예산소진
    @PostMapping("/ads/{adId}/click")
    public ResponseEntity<String> clickAd(@PathVariable Long adId) {
        advertiseService.handleAdClick(adId);  // 광고 클릭 처리
        return ResponseEntity.ok("광고 클릭이 성공적으로 처리되었습니다.");
    }


    // 상태별 광고 조회 API (ACTIVE, INACTIVE, EXPIRED 포함) ok
    @GetMapping("/ads/status")
    public List<BudgetResponse> getAdsByStatus(
            @RequestParam(value = "status", required = false) AdStatus status) {

        if (status != null) {
            return advertiseService.findAdsByStatus(status); // 상태가 주어지면 해당 상태의 광고만 조회
        } else {
            return advertiseService.findAllAds(); // 상태가 없으면 모든 광고 조회
        }
    }

    //예산 추가시 광고 생성 ok
    @PostMapping("/ads")
    public ResponseEntity<BudgetResponse> adCreate(@Valid @RequestBody BudgetRequest request) {
        BudgetResponse budgetResponse = advertiseService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetResponse);

    }


    //광고 예산 소진 상태 확인 API ok
    @GetMapping("/ads/{id}/budget-status")
    public List<BudgetResponse> getBudgetStatus(@PathVariable Long id) {
        // 광고 ID에 해당하는 예산 상태를 조회하는 로직 구현
        List<BudgetResponse> response = advertiseService.getBudgetStatus(id);
        return response;
    }


    // 여러 광고 조회 API (상태별로도 조회 가능) OK
    @GetMapping("/ads")
    public List<BudgetResponse> adRead(
            @RequestParam(required = false) AdStatus status) { // 상태 추가
        if (status == null) {
            return advertiseService.findAllAds(); // 모든 광고 조회
        }
        return advertiseService.findAdsByStatus(status); // 상태에 맞는 광고 조회

    }
}
