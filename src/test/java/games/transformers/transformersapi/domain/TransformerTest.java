package games.transformers.transformersapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransformerTest {

    @Test
    void calculateOverallRating() {
        assertEquals(17, buildTransformer("Test", Transformer.Type.A).getOverallRating());
    }

    public static Transformer buildTransformer(String name, Transformer.Type type) {
        return Transformer.builder().name(name)
                .type(type).strength(1)
                .intelligence(2).speed(3)
                .endurance(4).rank(5)
                .courage(6).firepower(7)
                .skill(8).build();
    }
}