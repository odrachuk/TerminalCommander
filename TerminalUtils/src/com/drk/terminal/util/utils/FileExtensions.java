/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.drk.terminal.util.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The known file extensions...
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
    },
    VIDEO("Video") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("avi", "container (a shell, which enables any form of compression to be used)");
            map.put("flv", "Flash video (encoded to run in a flash animation)");
            map.put("mpeg", "multimedia container (most often used for Sony's PlayStation Portable and Apple's iPod)");
            map.put("mp4", " multimedia container (most often used for Sony's PlayStation Portable and Apple's iPod)");
            return map;
        }
    },
    MUSIC("Music") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bwf", "Broadcast Wave Format (BWF), an extension of WAVE");
            map.put("wav", "Microsoft Wave");
            map.put("flac", "(free lossless codec of the Ogg project)");
            map.put("la", "Lossless Audio (.la)");
            map.put("pac", "LPAC (.pac)");
            map.put("ape", "Monkey's Audio (APE)");
            map.put("mp2", "MPEG Layer 2");
            map.put("mp3", "MPEG Layer 3");
            map.put("wma", "Windows Media Audio (.WMA)");
            map.put("aac", "Advanced Audio Coding (usually in an MPEG-4 container)");
            map.put("mp4", "Advanced Audio Coding (usually in an MPEG-4 container)");
            map.put("mpc", "Musepack");
            return map;
        }
    },
    WEB("Webpage") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("xml", "eXtensible Markup Language");
            map.put("html", "HyperText Markup Language");
            map.put("xhtml", "eXtensible HyperText Markup Language");
            map.put("mhtml", "Archived HTML, store all data on one web page (text, images, etc.) in one big file");
            map.put("jsp", "JavaServer Pages");
            return map;
        }
    },
    COMPUTER_PROGRAMS("Computer programs") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("ada", "Ada (body) source");
            map.put("ads", "Ada (specification) source");
            map.put("asm", "Assembly language source");
            map.put("bas", "BASIC, FreeBASIC, Visual Basic, BASIC-PLUS source,[2] PICAXE basic");
            map.put("bb", "Blitz3D");
            map.put("bmx", "BlitzMax");
            map.put("c", "C source");
            map.put("clj", "Clojure source code");
            map.put("cls", "Visual Basic class");
            map.put("cob", "COBOL source");
            map.put("cbl", "COBOL source");
            map.put("cpp", "C++ source");
            map.put("cc", "C++ source");
            map.put("cxx", "C++ source");
            map.put("cbp", "C++ source");
            map.put("cs", "C# source");
            map.put("csproj", "C# project (Visual Studio .NET)");
            map.put("d", "D source");
            map.put("dba", "DarkBASIC source");
            map.put("dbpro123", "DarkBASIC Professional project");
            map.put("e", "Eiffel source");
            map.put("efs", "EGT Forever Source File");
            map.put("egt", "EGT Asterisk Source File, could be J, C#, VB.net, EF 2.0 (EGT Forever)");
            map.put("el", "Emacs Lisp source");
            map.put("for", "Fortran source");
            map.put("frm", "Visual Basic form");
            map.put("frx", "Visual Basic form stash file (binary form file)");
            map.put("fth", "Forth source");
            map.put("ged", "Game Maker Extension Editable file as of version 7.0");
            map.put("gm6", "Game Maker Editable file as of version 6.x");
            map.put("gmd", "Game Maker Editable file up to version 5.x");
            map.put("gmk", "Game Maker Editable file as of version 7.0");
            map.put("gml", "Game Maker Language script file");
            map.put("go", "Go source");
            map.put("h", "C/C++ header file");
            map.put("hpp", "C++ header file");
            map.put("hxx", "C++ header file");
            map.put("hs", "Haskell source");
            map.put("i", "SWIG interface file");
            map.put("inc", "Turbo Pascal included source");
            map.put("java", "Java source");
            map.put("l", "lex source");
            map.put("lgt", "Logtalk source");
            map.put("lisp", "Common Lisp source");
            map.put("m", "Objective-C source");
            map.put("pas", "Pascal source (DPR for projects)");
            map.put("php", "PHP source");
            map.put("php3", "PHP source");
            map.put("php4", "PHP source");
            map.put("php5", "PHP source");
            map.put("phps", "PHP source");
            map.put("pl", "Perl");
            map.put("py", "Python source");
            map.put("r", "R source");
            map.put("rb", "Ruby source");
            map.put("resx", "Resource file for .NET applications");
            map.put("vb", "Visual Basic.NET source");
            return map;
        }
    },
    IMAGES("Raster graphics") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bmp", "Microsoft Windows Bitmap formatted image");
            map.put("gif", "CompuServe's Graphics Interchange Format");
            map.put("ico", "a file format used for icons in Microsoft Windows. Contains small bitmap images at multiple resolutions and sizes.");
            map.put("jpeg", "Joint Photographic Experts Group – a lossy image format widely used to display photographic images.");
            map.put("jpj", "Joint Photographic Experts Group – a lossy image format widely used to display photographic images.");
            map.put("mng", "Multiple Network Graphics, the animated version of PNG");
            map.put("png", "Portable Network Graphic (lossless, recommended for display and edition of graphic images)");
            map.put("psd", "Adobe Photoshop Drawing");
            map.put("pdd", "Adobe Photoshop Drawing");
            map.put("pxm", "Pixelmator image file");
            map.put("tiff", "Tagged Image File Format (usually lossless, but many variants exist, including lossy ones)");
            map.put("xcf", "GIMP image (from Gimp's origin at the eXperimental Computing Facility of the University of California)");
            return map;
        }
    },
    OFFICE_DOCUMENT("Office format") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("doc", "Microsoft Word document");
            map.put("docx", "Office Open XML document");
            map.put("dot", "Microsoft Word document template");
            map.put("dotx", "Office Open XML text document template");
            map.put("rtf", "Rich Text document");
            map.put("txt", "ASCII nebo Unicode plaintext Text file");
            map.put("info", "Texinfo");
            return map;
        }
    },
    BOOK_DOCUNET("Books format") {
        @Override
        public Map<String, String> categoryExtensions() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("fb2", "FB2 format");
            map.put("pdf", "Portable Document Format");
            map.put("djvu", "DjVu for scanned documents");
            return map;
        }
    }
    ;
    String description;

    private FileExtensions(String description) {
        this.description = description;
    }

    public abstract Map<String, String> categoryExtensions();
}
