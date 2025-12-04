package com.outsera.outsera_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntervaloDTO {

    private String producer;

    private Integer interval;

    private Integer previousWin;

    private Integer followingWin;
}
