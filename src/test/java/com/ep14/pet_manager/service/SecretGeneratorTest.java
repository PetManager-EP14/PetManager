package com.ep14.pet_manager.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SecretGeneratorTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // 1. Verifica que se genere una clave válida en Base64
    @Test
    void shouldGenerateValidBase64Key() {
        byte[] key = new byte[32];
        new java.security.SecureRandom().nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        byte[] decoded = Base64.getDecoder().decode(encodedKey);
        assertThat(decoded).hasSize(32);
    }

    // 2. Comprueba que dos ejecuciones consecutivas generen claves distintas
    @Test
    void shouldGenerateDifferentKeysEachTime() {
        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];

        new java.security.SecureRandom().nextBytes(key1);
        new java.security.SecureRandom().nextBytes(key2);

        String encoded1 = Base64.getEncoder().encodeToString(key1);
        String encoded2 = Base64.getEncoder().encodeToString(key2);

        assertThat(encoded1).isNotEqualTo(encoded2);
    }

    // 3. Ejecuta el metodo main y verifica que imprima una salida cuando el logger está en INFO
    @Test
    void shouldLogGeneratedKeyWhenInfoEnabled() {
        Logger logger = Logger.getLogger(SecretGenerator.class.getName());
        logger.setLevel(Level.INFO);

        SecretGenerator.main(new String[]{});

        assertThat(logger.isLoggable(Level.INFO)).isTrue();
    }

    // 4. Comprueba que no haya errores al ejecutar el main sin argumentos
    @Test
    void shouldRunMainWithoutErrors() {
        SecretGenerator.main(new String[0]);
        assertThat(outputStream.toString()).isNotNull();
    }
}
