package com.bichotas.moduloprestamos.entity.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrestamoVencidoDTO {
    private String prestamoId;
    private String userId;
    private String emailGuardian;
    private String bookId;
    private String bookName;
    private LocalDate fechaInicialPrestamo;
    private LocalDate fechaFinalPrestamo;

}
