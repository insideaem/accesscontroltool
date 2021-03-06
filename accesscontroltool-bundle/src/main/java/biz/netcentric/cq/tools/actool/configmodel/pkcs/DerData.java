package biz.netcentric.cq.tools.actool.configmodel.pkcs;

import java.security.InvalidKeyException;
import java.util.regex.Pattern;

public class DerData {
    final byte[] data;
    final DerType type;

    // as defined in https://tools.ietf.org/html/rfc7468#section-13 (lax version)
    static final Pattern PUBLIC_KEY_PATTERN = Pattern.compile(
            "-+BEGIN PUBLIC KEY[^-]*-+(?:\\s)+" + // Header
                    "([a-z0-9+/=\\s]+)" + // Base64 text
                    "-+END PUBLIC KEY[^-]*-+", // FootDer
            Pattern.CASE_INSENSITIVE);

    // https://tools.ietf.org/html/rfc7468#section-10 (lax version)
    static final Pattern PRIVATE_KEY_PATTERN = Pattern.compile(
            "-+BEGIN PRIVATE KEY[^-]*-+(?:\\s)+" + // Header
                    "([a-z0-9+/=\\s]+)" + // Base64 text
                    "-+END PRIVATE KEY[^-]*-+", // Footer
            Pattern.CASE_INSENSITIVE);

    // https://tools.ietf.org/html/rfc7468#section-11 (lax version)
    static final Pattern ENCRYPTED_PRIVATE_KEY_PATTERN = Pattern.compile(
            "-+BEGIN ENCRYPTED PRIVATE KEY[^-]*-+(?:\\s)+" + // Header
                    "([a-z0-9+/=\\s]+)" + // Base64 text
                    "-+END ENCRYPTED PRIVATE KEY[^-]*-+", // Footer
            Pattern.CASE_INSENSITIVE);

    // https://tools.ietf.org/html/rfc7468#section-5.1 (lax version)
    static final Pattern CERTIFICATE_PATTERN = Pattern.compile(
            "-+BEGIN CERTIFICATE[^-]*-+(?:\\s)+" + // Header
                    "([a-z0-9+/=\\s]+)" + // Base64 text
                    "-+END CERTIFICATE[^-]*-+", // Footer
            Pattern.CASE_INSENSITIVE);

    public DerData(byte[] data, DerType type) {
        super();
        this.data = data;
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public DerType getType() {
        return type;
    }

    static DerData parseFromPem(String pem) throws InvalidKeyException {
        for (DerType type : DerType.values()) {
            byte[] data = type.fromPem(pem);
            if (data != null) {
                return new DerData(data, type);
            }
        }
        throw new InvalidKeyException(
                "No supported PEM format as defined in https://tools.ietf.org/html/rfc7468 detected!");
    }
}
