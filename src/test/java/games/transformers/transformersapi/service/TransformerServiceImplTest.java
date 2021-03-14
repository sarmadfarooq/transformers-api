package games.transformers.transformersapi.service;

import games.transformers.transformersapi.dao.TransformerDAO;
import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.domain.TransformerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransformerServiceImplTest {

    TransformerServiceImpl transformerService;

    private TransformerDAO mockTransformerDAO = mock(TransformerDAO.class);

    @BeforeEach
    public void setup() {
        transformerService = new TransformerServiceImpl(mockTransformerDAO);
    }

    @Test
    void shouldGetNoTransformer() {
        //assemble
        when(mockTransformerDAO.getAllTransformers()).thenReturn(Collections.emptyList());
        //act
        List<Transformer> allTransformers = transformerService.getAllTransformers();
        //assert
        assertEquals(0, allTransformers.size());
        verify(mockTransformerDAO, times(1)).getAllTransformers();
    }

    @Test
    void shouldGetAllTransformers() {
        //assemble
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        when(mockTransformerDAO.getAllTransformers()).thenReturn(Arrays.asList(TransformerTest.buildTransformer("Autobot", Transformer.Type.A)));
        //act
        List<Transformer> allTransformers = transformerService.getAllTransformers();
        //assert
        assertAll("Transformer",
                () -> assertEquals(1, allTransformers.size()),
                () -> assertEquals(expected, allTransformers.get(0)));
        verify(mockTransformerDAO, times(1)).getAllTransformers();
    }

    @Test
    void shouldInsertTransformer() {
        //assemble
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        when(mockTransformerDAO.insertTransformer(any())).thenReturn(1);
        //act
        int id = transformerService.insertTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        //assert
        assertEquals(1,id);
        verify(mockTransformerDAO, times(1)).insertTransformer(any());
    }

    @Test
    void updateTransformer() {
        //assemble
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        when(mockTransformerDAO.updateTransformer(any())).thenReturn(true);
        //act
        boolean success = transformerService.updateTransformer(TransformerTest.buildTransformer("Autobot", Transformer.Type.A));
        //assert
        assertTrue(success);
        verify(mockTransformerDAO, times(1)).updateTransformer(any());
    }

    @Test
    void shouldDeleteTransformer() {
        //assemble
        Transformer expected = TransformerTest.buildTransformer("Autobot", Transformer.Type.A);
        when(mockTransformerDAO.deleteTransformer(1)).thenReturn(true);
        //act
        boolean success = transformerService.deleteTransformer(1);
        //assert
        assertTrue(success);
        verify(mockTransformerDAO, times(1)).deleteTransformer(1);
    }
}