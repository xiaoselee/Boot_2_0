/*
 *
 * (c) Copyright Ascensio System SIA 2020
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */


package com.je.cloud.busi.document.helpers;


import com.je.cloud.busi.document.config.DocumentConfig;
import com.je.cloud.busi.document.entities.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class FileUtility {

    @Autowired
    DocumentConfig documentConfig;

    public FileType GetFileType(String fileName) {
        String ext = GetFileExtension(fileName).toLowerCase();

        if (ExtsDocument.contains(ext))
            return FileType.Text;

        if (ExtsSpreadsheet.contains(ext))
            return FileType.Spreadsheet;

        if (ExtsPresentation.contains(ext))
            return FileType.Presentation;

        return FileType.Text;
    }

    public List<String> ExtsDocument = Arrays.asList
            (
                    ".doc", ".docx", ".docm",
                    ".dot", ".dotx", ".dotm",
                    ".odt", ".fodt", ".ott", ".rtf", ".txt",
                    ".html", ".htm", ".mht",
                    ".pdf", ".djvu", ".fb2", ".epub", ".xps"
            );

    public List<String> ExtsSpreadsheet = Arrays.asList
            (
                    ".xls", ".xlsx", ".xlsm",
                    ".xlt", ".xltx", ".xltm",
                    ".ods", ".fods", ".ots", ".csv"
            );

    public List<String> ExtsPresentation = Arrays.asList
            (
                    ".pps", ".ppsx", ".ppsm",
                    ".ppt", ".pptx", ".pptm",
                    ".pot", ".potx", ".potm",
                    ".odp", ".fodp", ".otp"
            );


    public String GetFileName(String url) {
        if (url == null) return null;

        //for external file url
        String tempstorage = documentConfig.getTempstorageUrl();// ConfigManager.GetProperty("files.docservice.url.tempstorage");
        if (!tempstorage.isEmpty() && url.startsWith(tempstorage)) {
            Map<String, String> params = GetUrlParams(url);
            return params == null ? null : params.get("filename");
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        return fileName;
    }

    public String GetFileNameWithoutExtension(String url) {
        String fileName = GetFileName(url);
        if (fileName == null) return null;
        String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        return fileNameWithoutExt;
    }

    public String GetFileExtension(String url) {
        String fileName = GetFileName(url);
        if (fileName == null) return null;
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        return fileExt.toLowerCase();
    }

    public Map<String, String> GetUrlParams(String url) {
        try {
            String query = new URL(url).getQuery();
            String[] params = query.split("&");
            Map<String, String> map = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
            return map;
        } catch (Exception ex) {
            return null;
        }
    }
}
