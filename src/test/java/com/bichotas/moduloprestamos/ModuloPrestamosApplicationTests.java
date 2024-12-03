package com.bichotas.moduloprestamos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ModuloPrestamosApplicationTests {

    @Test
    public void applicationFailsToStartWithNullArgsV() {
        assertThrows(IllegalArgumentException.class, () -> ModuloPrestamosApplication.main(null));
    }
}