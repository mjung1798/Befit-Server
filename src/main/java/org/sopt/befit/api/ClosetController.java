package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.sopt.befit.model.ClosetReq;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.BrandsService;
import org.sopt.befit.service.ClosetService;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.LikesService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("closet")
public class ClosetController {

    private final ClosetService closetService;

    private final JwtService jwtService;

    public ClosetController(ClosetService closetService, JwtService jwtService) {
        this.closetService = closetService;
        this.jwtService = jwtService;
    }

    // 하는중 ... Json 객체로 변환해야함
    // 나의 옷장 리스트 조회 - 카테고리 별
    @Auth
    @GetMapping("/category/{category_idx}")
    public ResponseEntity getClosetProduct(@RequestHeader("Authorization") final String header,
                                           @PathVariable(value = "category_idx") final int category_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(closetService.getClosetProduct(curIdx, category_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 아이템 등록
    @Auth
    @PostMapping("")
    public ResponseEntity postProductToCloset(@RequestHeader("Authorization") final String header,
                                              @RequestBody final ClosetReq closetReq) {
        try {
            if(closetReq!=null){
                if(header != null){
                    int curIdx = jwtService.decode(header).getIdx();
                    return new ResponseEntity(closetService.postProductToCloset(curIdx , closetReq), HttpStatus.OK);
                }
                return new ResponseEntity(new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUNT_CLOSET), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 아이템 삭제
    @Auth
    @DeleteMapping("/{closet_idx}")
    public ResponseEntity deleteProductToCloset(@RequestHeader("Authorization") final String header,
                                                @PathVariable(value = "closet_idx") final int closet_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(closetService.deleteProductToCloset(curIdx,closet_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 특정 아이템 조회 - ? 왜하는지 모르겠음...

    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
}
