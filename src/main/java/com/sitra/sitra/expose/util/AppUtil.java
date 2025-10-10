package com.sitra.sitra.expose.util;

import com.sitra.sitra.expose.response.base.ConsultaResponseDTO;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
@UtilityClass
public class AppUtil {

    private static final String SUCCESS = "success";

    public static ResponseDTO<Object> build(Object result, String message) {
        return ResponseDTO.builder()
                .data(result)
                .status(SUCCESS)
                .code("200")
                .message(message)
                .build();
    }

    public static ConsultaResponseDTO buildPaginated(Page<?> pageable, String message) {
        return ConsultaResponseDTO.builder()
                .data(pageable.getContent())
                .status(SUCCESS)
                .code("200")
                .message(message)
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }

    public static ConsultaResponseDTO buildPaginated(Page<?> pageable, List<?> similarSource) {
        return ConsultaResponseDTO.builder()
                .data(similarSource)
                .status(SUCCESS)
                .code("200")
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }

}
