package org.tyniest.files.utils;

import javax.annotation.Nonnull;

import org.tyniest.common.GlobalConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Should be moved to file handling
public class FilePath {

    public static final String SEP = "/";

    @NonNull private BucketType type;
    @NonNull private String path;

    enum BucketType {
        PRIVATE, PUBLIC
    }

    public static String pathOf(
            final String e1, final String e2, final String e3, final String e4, final String e5) {
        return String.join(SEP, e1, e2, e3, e4, e5);
    }

    public static String pathOf(
            final String e1, final String e2, final String e3, final String e4) {
        return String.join(SEP, e1, e2, e3, e4);
    }

    public static String pathOf(final String e1, final String e2, final String e3) {
        return String.join(SEP, e1, e2, e3);
    }

    public static String pathOf(final String e1, final String e2) {
        return String.join(SEP, e1, e2);
    }

    public static String pathOf(final String e1) {
        return String.join(SEP, e1);
    }

    public FilePath addPath(final String path) {
        this.path = this.path + SEP + path;
        return this;
    }

    public FilePath addPath(final Long path) {
        this.path = this.path + SEP + path;
        return this;
    }

    public FilePath addPath(final Integer path) {
        this.path = this.path + SEP + path;
        return this;
    }

    public static FilePath privateOf(final String path) {
        return new FilePath(BucketType.PRIVATE, path);
    }

    public static FilePath publicOf(final String path) {
        return new FilePath(BucketType.PUBLIC, path);
    }

    @Nonnull
    public String getBucket() {
        switch (type) {
            case PRIVATE:
                return GlobalConstants.PRIVATE_BUCKET;
            case PUBLIC:
                return GlobalConstants.PUBLIC_BUCKET;
                // should never happen
            default:
                return "";
        }
    }

    public boolean isPrivate() {
        return this.type.equals(BucketType.PRIVATE);
    }
}
