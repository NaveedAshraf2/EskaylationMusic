package com.kunkun.mp3player.ui.fragment.main.folder_selected.loader;

import com.eskaylation.downloadmusic.model.Folder;
import java.util.ArrayList;


public interface ScaningFolderListener {
    void onScanningSuccessFull(ArrayList<Folder> arrayList);
}
