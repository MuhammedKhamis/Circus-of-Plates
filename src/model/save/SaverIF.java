package model.save;

public interface SaverIF {

    public boolean save(Snapshot saved, String path, String name);

    public Snapshot load(String path, String name);

}
