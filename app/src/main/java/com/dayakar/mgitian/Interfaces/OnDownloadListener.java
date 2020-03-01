package com.dayakar.mgitian.Interfaces;

public interface OnDownloadListener {
    void onDownload(String filepath, String name);
    void onFileOpen(String path, String name);
    void onItemSelected(String filePath,String name);
}
