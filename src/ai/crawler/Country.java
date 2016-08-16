package ai.crawler;

/**
 *
 * @author arsalan
 */
public class Country {

    private final int id;
    private final String name;

    public String getName() {
        return name;
    }

    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

}
