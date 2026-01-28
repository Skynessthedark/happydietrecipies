package com.happydieting.dev.util;

import com.happydieting.dev.model.MediaModel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MediaUtil {

    private MediaUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void printMedia(MediaModel mediaModel, HttpServletResponse response) throws IOException {
        if (Objects.nonNull(mediaModel)) {
            response.setContentType(mediaModel.getContentType());
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(mediaModel.getContent());
            outputStream.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
