package com.drk.terminal.util.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 12/4/13
 *
 * @author Drachuk O.V.
 */
public enum FileExtensions {
    ARCHIVE_OR_COMPRESSED("Archive and compressed") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("7z", "7-Zip compressed file");
            map.put("ace", "ACE compressed file");
            map.put("alz", "ALZip compressed file");
            map.put("apk", "Applications installable on Android");
            map.put("arj", "ARJ compressed file");
            map.put("bin", "Compressed Archive");
            map.put("bzip2", "(.bz2)");
            map.put("deb", "Debian install package");
            map.put("gzip", "Compressed file");
            map.put("gz", "Compressed file");
            map.put("jar", "ZIP file with manifest for use with Java applications.");
            map.put("lzip", "Compressed file");
            map.put("lz", "Compressed file");
            map.put("pak", "Enhanced type of .ARC archive");
            map.put("rar", "Multiple file archive (rar to .r01-.r99 to s01 and so on)");
            map.put("tar", "Group of files, packaged as one file");
            map.put("tar.gz", "gzipped tar file");
            map.put("tgz", "gzipped tar file");
            map.put("wax", "Wavexpress – A ZIP alternative optimized for packages containing video, allowing multiple packaged files to be all-or-none delivered with near-instantaneous unpacking via NTFS file system manipulation.");
            map.put("z", "Unix compress file");
            map.put("zip", "Popular compression format");
            return map;
        }
    };
    String description;

    private FileExtensions(String description) {
        this.description = description;
    }

    public abstract Map<String, String> categoryExtensions();
}
