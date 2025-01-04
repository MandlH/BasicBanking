package org.mandl.converters;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Converter
public class LocalDateTimeEncryptionConverter implements AttributeConverter<LocalDateTime, String> {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final String SECRET_KEY = "dSh98a43hpYokPg4";
    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) return null;

        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            var iv = new byte[IV_LENGTH];
            var spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC, spec);

            var plainText = attribute.format(FORMATTER);
            var encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            var iv = new byte[IV_LENGTH];
            var spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, spec);

            var decodedBytes = Base64.getDecoder().decode(dbData);
            var decryptedText = new String(cipher.doFinal(decodedBytes));

            return LocalDateTime.parse(decryptedText, FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}
