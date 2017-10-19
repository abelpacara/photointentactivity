package com.example.administrador.photointentactivity;

/**
 * Created by Administrador on 19/10/2017.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
