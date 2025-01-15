package com.cisco.wccai.grpc.utils;

import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Utils {

    private static final Properties PROPERTY = LoadProperties.loadProperties();
    private static final String AUDIO_ENCODING_TYPE = PROPERTY.getProperty("AUDIO_ENCODING_TYPE");
    private static final int CHUNK_SIZE = 4080;
    private static final String LINEAR_16 = "LINEAR_16";

    Utils() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static void isOrgAuthorized(String orgFromToken, String orgId) {
        if (orgFromToken != null && !orgFromToken.isEmpty()) {
            if (orgFromToken.equals(orgId)) {
                LOGGER.info("token is validated successfully against orgId");
            } else {
                throw new IllegalArgumentException("orgId is not valid");
            }
        }
    }

    public static byte[] getAudioBytes() {
        byte[] audioBytes;
        if (LINEAR_16.equalsIgnoreCase(AUDIO_ENCODING_TYPE)) {
            audioBytes = new byte[16 * CHUNK_SIZE];
        } else {
            audioBytes = new byte[CHUNK_SIZE];
        }
        Arrays.fill(audioBytes, (byte) 1);
        return audioBytes;
    }

    public static InputStream getInputStreamForBookAFlight() {
        return Utils.class.getClassLoader().getResourceAsStream("audio/welcome.wav");
    }

    public static ByteString getAudioBytesForBookAFlight() {
        try {
            return ByteString.readFrom(Utils.class.getClassLoader().getResourceAsStream("audio/bookflight.wav"));
        } catch (Exception e) {
            LOGGER.error("Error occurred while reading audio file: {}", e.getMessage());
        }
        return ByteString.EMPTY;
    }

    public static void write(ByteString audioContent, String fileName) {
        File dstFile = new File("target/" + fileName);
        if (dstFile.exists()) {
            dstFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(dstFile);
            InputStream in = new ByteArrayInputStream(audioContent.toByteArray());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
