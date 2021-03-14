package games.transformers.transformersapi.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Transformer {
    public enum Type {
        A("Autobot"), D("Decepticon");

        private final String value;

        Type(String type) {
            value = type;
        }
    }
    private int id;
    private String name;
    private Type type;
    private int strength;
    private int intelligence;
    private int speed;
    private int endurance;
    private int rank;
    private int courage;
    private int firepower;
    private int skill;

    /**
     * This represents the overall rating of a transformer
     * @return The overall rating of a transformer
     */
    public int getOverallRating() {
        return strength + intelligence + speed + endurance + firepower;
    }
}
