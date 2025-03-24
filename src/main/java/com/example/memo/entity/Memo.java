package com.example.memo.entity;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Memo {
    // int보다 크고
    // Wrapper 클래스이기 때문에 null 포함 가능.
    // 식별자는 안전하게 다루기 위해서 long 타입을 많이 사용함.
    private Long id;
    private String title;
    private String contents;

    public void update(MemoRequestDto dto){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    public void updateTitle(MemoRequestDto dto){
        this.title = dto.getTitle();
    }

}
