package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 데이터를 JSON 형태로 통신하기 위해
@RestController
// prefix url 설정 시 사용. 공통적으로 들어가는 url.
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    // create 시 post 사용.
    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto  dto){

        // 식별자가 1씩 증가하도록 만듦.
        // 비어있는지 확인하고 비어있다면 초기 값을 1로 설정.
        // 아니라면 키 값 중 가장 큰 값 +1 하는 것.
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성.
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        // Inmemory DB에 Memo 저장.(따로 DB를 사용하는 게 아니라 java 자료 구조 안에 저장할 것.)
        memoList.put(memoId, memo);

        return new MemoResponseDto(memo);
    }

    @GetMapping("/{id}")
    // 식별자를 파라미터로 binding 할 때에는 @PathVariable 사용.
    public MemoResponseDto findMemoById(@PathVariable Long id){
       Memo memo = memoList.get(id);

        return new MemoResponseDto(memo);
    }

    // 단 건 전체 수정 PutMapping
    @PutMapping("/{id}")
    public MemoResponseDto updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto // 수정할 데이터를 RequestDto 형태로 받기로 함.
    ){

        Memo memo = memoList.get(id);
        memo.update(dto);

        return new MemoResponseDto(memo);
    }

    // 단 건 삭제 DeleteMapping
    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id){

        memoList.remove(id);
    }
}
