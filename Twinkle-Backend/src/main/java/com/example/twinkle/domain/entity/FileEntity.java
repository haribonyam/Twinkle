package com.example.twinkle.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    private Long id;
    private String path;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tradeBoard_id")
    private TradeBoardEntity tradeBoard;

    @Builder
    public FileEntity(String path, String name, TradeBoardEntity tradeBoardEntity){
        this.path = path;
        this.name = name;
        this.tradeBoard = tradeBoardEntity;
    }

}
