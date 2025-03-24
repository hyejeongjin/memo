package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// 데이터를 JSON 형태로 통신하기 위해
@RestController
// prefix url 설정 시 사용. 공통적으로 들어가는 url.
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    // create 시 post 사용.
    @PostMapping
    // 상태 코드를 따로 반환할 때 사용하는 것이 ResponseEntity
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto  dto){

        // 식별자가 1씩 증가하도록 만듦.
        // 비어있는지 확인하고 비어있다면 초기 값을 1로 설정.
        // 아니라면 키 값 중 가장 큰 값 +1 하는 것.
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성.
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        // Inmemory DB에 Memo 저장.(따로 DB를 사용하는 게 아니라 java 자료 구조 안에 저장할 것.)
        memoList.put(memoId, memo);
        // 생성 시 사용하는 HttpStatus 코드 CREATED.
        return new ResponseEntity<> (new MemoResponseDto(memo), HttpStatus.CREATED);
    }

    // 아무것도 적지 않으면 상단에 있는 RequestMapping("/memos") 을 따라감.
    @GetMapping
    // 전체 목록 조회기 때문에 파라미터 값 필요 없음.
    public List<MemoResponseDto> findAllMemos(){
        // init List
        // 리스트는 인터페이스이기 때문에 구현체(ArrayList)를 사용해서 초기화해줘야함.
        // 인터페이스는 new에서 인스턴스화 할 수 없기 때문.
        List<MemoResponseDto> responseList = new ArrayList<>();

        // HashMap<Memo> -> List<MemoResponseDto>
        // memoList.values()를 통해 모든 메모를 꺼내
        // 꺼내진 메모만큼 memo에 들어가면서 반복문이 실행됨.
        for (Memo memo : memoList.values()){
            MemoResponseDto  responseDto = new MemoResponseDto(memo);
            responseList.add(responseDto);
        }

        // Map to List
        // stream을 이용하는 방법.
        // responseList = memoList.values().stream().map(MemoResponseDto::new).toList();
        return responseList;
    }


    @GetMapping("/{id}")
    // 식별자를 파라미터로 binding 할 때에는 @PathVariable 사용.
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id){
        Memo memo = memoList.get(id);

        //NPE 방지
        if(memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
}

        // 단 건 전체 수정 PutMapping
        @PutMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateMemoById(
                @PathVariable Long id,
                @RequestBody MemoRequestDto dto // 수정할 데이터를 RequestDto 형태로 받기로 함.
        ){

            Memo memo = memoList.get(id);

            // NPE 방지
            if(memo == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }

            // 필수값 검증
            if(dto.getTitle() == null || dto.getContents() == null){
                // 필수값을 client가 전달해주지 않았으므로 BAD_REQUEST
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            memo.update(dto);

            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }

        // 단 건 일부 수정이기 때문에 PatchMapping 사용.
        @PatchMapping("/{id}")
        public ResponseEntity<MemoResponseDto> updateTitle(
                @PathVariable Long id,
                @RequestBody MemoRequestDto dto
        ){
            Memo memo = memoList.get(id);

            // NPE 방지
            if(memo == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 필수값 검증
            // 내용이 있거나 제목이 null이면 BAD REQUEST
            if(dto.getTitle() == null || dto.getContents() != null){
                // 필수값을 client가 전달해주지 않았으므로 BAD_REQUEST
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            memo.updateTitle(dto);

            return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        }

        // 단 건 삭제 DeleteMapping
        @DeleteMapping("/{id}")
        // 응답할 데이터가 필요없기 때문에 제네릭으로 Void 사용.
        public ResponseEntity<Void> deleteMemo(@PathVariable Long id){

            // memoList의 key값에 id를 포함하고 있다면
            if(memoList.containsKey(id)){
                memoList.remove(id);

                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
