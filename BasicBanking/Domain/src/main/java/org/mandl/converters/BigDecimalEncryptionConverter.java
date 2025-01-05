package org.mandl.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class BigDecimalEncryptionConverter implements AttributeConverter<BigDecimal, String> {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final String SECRET_KEY = Constants.getKey();
    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

    @Override
    public String convertToDatabaseColumn(BigDecimal attribute) {
        if (attribute == null) return null;

        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

            var iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            var spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC, spec);

            var encryptedBytes = cipher.doFinal(attribute.toPlainString().getBytes());

            var encryptedWithIv = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, encryptedWithIv, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public BigDecimal convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

            var encryptedWithIv = Base64.getDecoder().decode(dbData);
            var iv = new byte[IV_LENGTH];
            var encryptedBytes = new byte[encryptedWithIv.length - IV_LENGTH];

            System.arraycopy(encryptedWithIv, 0, iv, 0, IV_LENGTH);
            System.arraycopy(encryptedWithIv, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            var spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, spec);

            return new BigDecimal(new String(cipher.doFinal(encryptedBytes)));
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}
