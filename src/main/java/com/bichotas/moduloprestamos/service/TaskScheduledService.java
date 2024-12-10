package com.bichotas.moduloprestamos.service;

import com.bichotas.moduloprestamos.entity.Prestamo;
import com.bichotas.moduloprestamos.entity.dto.PrestamoVencidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskScheduledService {

    @Autowired
    private PrestamoService prestamoService;

    @Scheduled(cron = "0 0 10 * * *")
    private void changeStatusLoanWhenExpired() {
        LocalDate today = LocalDate.now();
        List<Prestamo> prestamos = prestamoService.getPrestamos("Prestado");
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getFechaDevolucion().plusDays(1).isBefore(today)) {
                prestamoService.changeStatusOfLoanExpire(prestamo);
                PrestamoVencidoDTO prestamoVencido = PrestamoVencidoDTO.builder()
                        .prestamoId(prestamo.getId())
                        .userId(prestamo.getIdEstudiante())
                        .emailGuardian("")
                        .bookId(prestamo.getIdLibro())
                        .bookName("")
                        .fechaInicialPrestamo(prestamo.getFechaPrestamo())
                        .fechaFinalPrestamo(prestamo.getFechaDevolucion())
                        .build();
                //TODO: send email to guardian with prestamoVencido to API Notificaciones
            }
        }
    }
}
