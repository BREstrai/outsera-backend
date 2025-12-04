package com.outsera.outsera_backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntervaloRetornoDTO {

    private List<IntervaloDTO> min;

    private List<IntervaloDTO> max;
}
