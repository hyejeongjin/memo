package com.example.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {

    // id는 식별자로, 서버에서 관리하면 됨.
    private String title;
    private String contents;

}
