package com.bichotas.moduloprestamos.entity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DevolucionDTO {
    private String userId;
    private String emailGuardian;
    private String bookId;
    private String bookName;
    private boolean loanReturn;
}
