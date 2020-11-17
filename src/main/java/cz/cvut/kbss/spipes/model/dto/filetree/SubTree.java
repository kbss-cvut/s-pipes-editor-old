package cz.cvut.kbss.spipes.model.dto.filetree;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 25.06.2018.
 */
public class SubTree implements FileTree {
    private FileTree[] children;
    private String name;

    public SubTree(FileTree[] children, String name) {
        this.children = children;
        this.name = name;
    }

    public FileTree[] getChildren() {
        return children;
    }

    public void setChildren(FileTree[] children) {
        this.children = children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
