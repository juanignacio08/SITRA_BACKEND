package com.sitra.sitra.expose.response.base;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private List<?> data;
    private String message;
    private String status;
    private String code;
}
