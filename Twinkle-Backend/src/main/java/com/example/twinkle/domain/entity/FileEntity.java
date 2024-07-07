package com.example.twinkle.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void addTradeBoard(TradeBoardEntity tradeBoardEntity){
        this.tradeBoard =tradeBoardEntity;
    }

}
