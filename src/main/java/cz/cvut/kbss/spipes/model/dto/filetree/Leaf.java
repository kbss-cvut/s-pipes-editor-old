package cz.cvut.kbss.spipes.model.dto.filetree;

import java.io.File;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 25.06.2018.
 */
public class Leaf implements FileTree {
    private File file;
    private String name;

    public Leaf(File file, String name) {
        this.file = file;
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
