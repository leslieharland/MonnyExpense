package com.example.leslie.monnyfree.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.io.File;

/**
 * Created by Leslie on 3/18/2018.
 */

public class FileUtil {

    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char OTHER_SEPARATOR;
    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }
    public static String getBaseName(final String filename) {
        return removeExtension(getName(filename));
    }

    static boolean isSystemWindows() {
        return File.separatorChar == WINDOWS_SEPARATOR;
    }

    public static String getName(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }
    public static String removeExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);

        final int index = indexOfExtension(filename);
        if (index == NOT_FOUND) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }
    private static void failIfNullBytePresent(String path) {
        int len = path.length();
        for (int i = 0; i < len; i++) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no " +
                        "known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }

    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static int indexOfExtension(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        final int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
    }

    public static void scaleButtonDrawables(Button btn, double fitFactor) {
        Drawable[] drawables = btn.getCompoundDrawables();

        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                int imgWidth = drawables[i].getIntrinsicWidth();
                int imgHeight = drawables[i].getIntrinsicHeight();
                if ((imgHeight > 0) && (imgWidth > 0)) {    //might be -1
                    float scale;
                    if ((i == 0) || (i == 2)) { //left or right -> scale height
                        scale = (float) (btn.getHeight() * fitFactor) / imgHeight;
                    } else { //top or bottom -> scale width
                        scale = (float) (btn.getWidth() * fitFactor) / imgWidth;
                    }
                    if (scale < 1.0) {
                        Rect rect = drawables[i].getBounds();
                        int newWidth = (int) (imgWidth * scale);
                        int newHeight = (int) (imgHeight * scale);
                        rect.left = rect.left + (int) (0.5 * (imgWidth - newWidth));
                        rect.top = rect.top + (int) (0.5 * (imgHeight - newHeight));
                        rect.right = rect.left + newWidth;
                        rect.bottom = rect.top + newHeight;
                        drawables[i].setBounds(rect);
                    }
                }
            }
        }
    }
}
