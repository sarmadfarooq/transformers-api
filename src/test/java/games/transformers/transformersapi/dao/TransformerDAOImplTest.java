package games.transformers.transformersapi.dao;

import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.domain.TransformerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformerDAOImplTest {

    TransformerDAOImpl transformerDAO;

    @BeforeEach
    public void setup() {
        transformerDAO = new TransformerDAOImpl();
    }

    @Test
    void shouldGetNoTransformer() {
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        assertEquals(0, allTransformers.size());
    }

    @Test
    void shouldInsertAndGetOneTransformer() {
        //assemble
        Transformer transformer = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        expected.setId(1);
        // act
        transformerDAO.insertTransformer(transformer);
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        //assert
        assertAll("Transformer",
                () -> assertEquals(1, allTransformers.size()),
                () -> assertEquals(expected, allTransformers.get(0)));
    }

    @Test
    void shouldGetOneTransformerEach() {
        //assemble
        Transformer autobot = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        Transformer expectedAutobot = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        expectedAutobot.setId(1);
        Transformer dec = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        Transformer expectedDec = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        expectedDec.setId(2);
        // act
        transformerDAO.insertTransformer(autobot);
        transformerDAO.insertTransformer(dec);
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        //assert
        assertAll("Transformer",
                () -> assertEquals(2, allTransformers.size()),
                () -> assertEquals(expectedAutobot, allTransformers.get(0)),
                () -> assertEquals(expectedDec, allTransformers.get(1)));
    }

    @Test
    void shouldUpdateExistingTransformer() {
        //assemble
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        expected.setRank(11);
        expected.setId(1);
        // act
        transformerDAO.insertTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        Transformer transformerUpdate = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        transformerUpdate.setId(1);
        transformerUpdate.setRank(11);
        transformerDAO.updateTransformer(transformerUpdate);
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        //assert
        assertAll("Transformer",
                () -> assertEquals(1, allTransformers.size()),
                () -> assertEquals(expected, allTransformers.get(0)));
    }
    @Test
    void shouldFailInUpdate() {
        //assemble
        String expectedMessage ="Transformer that you are trying to update does not exist.";
        // act
        transformerDAO.insertTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        Transformer transformerUpdate = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        transformerUpdate.setId(2);
        transformerUpdate.setRank(11);
        //assert
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> transformerDAO.updateTransformer(transformerUpdate));
        assertTrue(actualException.getMessage().equals(expectedMessage));
    }

    @Test
    void shouldDeleteTransformer() {
        //assemble
        // act
        transformerDAO.insertTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        transformerDAO.deleteTransformer(1);
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        //assert
        assertEquals(0, allTransformers.size());
    }

    @Test
    void shouldFailToDeleteTransformer() {
        //assemble
        String expectedMessage ="Transformer that you are trying to delete does not exist.";
        // act
        transformerDAO.insertTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        List<Transformer> allTransformers = transformerDAO.getAllTransformers();
        //assert
        assertEquals(1, allTransformers.size());
        //assert
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> transformerDAO.deleteTransformer(2));
        assertTrue(actualException.getMessage().equals(expectedMessage));
    }
}