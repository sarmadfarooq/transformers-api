package games.transformers.transformersapi.web;

import games.transformers.transformersapi.domain.BattleResponse;
import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.domain.TransformerTest;
import games.transformers.transformersapi.service.BattleService;
import games.transformers.transformersapi.service.EndOfGameException;
import games.transformers.transformersapi.service.TransformerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static games.transformers.transformersapi.domain.Transformer.Type.A;
import static games.transformers.transformersapi.domain.Transformer.Type.D;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GamesControllerTest {
    private GamesController gamesController;

    private TransformerService transformerServiceMock = mock(TransformerService.class);
    private BattleService battleServiceMock = mock(BattleService.class);

    @BeforeEach
    public void setup() {
        gamesController = new GamesController(transformerServiceMock, battleServiceMock);
    }

    @Test
    void shouldGetListOfTransformersOK() {
        //assemble
        when(transformerServiceMock.getAllTransformers()).thenReturn(Arrays.asList(TransformerTest.buildTransformer("A", A),TransformerTest.buildTransformer("D", Transformer.Type.D)));
        //act
        ResponseEntity<List<Transformer>> responseEntity = gamesController.getListOfTransformers();
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2,responseEntity.getBody().size());
        assertEquals(A,responseEntity.getBody().stream().filter(transformer -> transformer.getName().equals("A")).findFirst().get().getType());
        assertEquals(D,responseEntity.getBody().stream().filter(transformer -> transformer.getName().equals("D")).findFirst().get().getType());
    }
    @Test
    void shouldGetListOfTransformersEmpty() {
        //assemble
        when(transformerServiceMock.getAllTransformers()).thenReturn(Collections.EMPTY_LIST);
        //act
        ResponseEntity<List<Transformer>> responseEntity = gamesController.getListOfTransformers();
        //assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0,responseEntity.getBody().size());
    }

    @Test
    void shouldCreateTransformer() {
        //assemble
        when(transformerServiceMock.insertTransformer(any())).thenReturn(1);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        URI expected = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(1).toUri();
        //act
        ResponseEntity<String> responseEntity = gamesController.createTransformer(TransformerTest.buildTransformer("Test", A));
        //assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expected,responseEntity.getHeaders().getLocation());
    }

    @Test
    void shouldFailToCreateTransformer() {
        //assemble
        when(transformerServiceMock.insertTransformer(any())).thenThrow(NullPointerException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        //act
        ResponseEntity<String> responseEntity = gamesController.createTransformer(TransformerTest.buildTransformer("Test", A));
        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void shouldUpdateTransformer() {
        //assemble
        Transformer test = TransformerTest.buildTransformer("Test", A);
        test.setId(1);
        when(transformerServiceMock.updateTransformer(any())).thenReturn(true);
        //act
        ResponseEntity<String> responseEntity = gamesController.updateTransformer(test,1);
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldNotUpdateTransformerThatDoesNoHaveMatchingID() {
        //assemble
        Transformer test = TransformerTest.buildTransformer("Test", A);
        test.setId(1);
        //act
        ResponseEntity<String> responseEntity = gamesController.updateTransformer(test,12);
        //assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void shouldNotUpdateTransformerThatDoesNotExist() {
        //assemble
        Transformer test = TransformerTest.buildTransformer("Test", A);
        test.setId(1);
        when(transformerServiceMock.updateTransformer(any())).thenReturn(false);
        //act
        ResponseEntity<String> responseEntity = gamesController.updateTransformer(test,1);
        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void shouldFailToUpdateTransformer() {
        //assemble
        Transformer test = TransformerTest.buildTransformer("Test", A);
        test.setId(1);
        when(transformerServiceMock.updateTransformer(any())).thenThrow(NullPointerException.class);
        //act
        ResponseEntity<String> responseEntity = gamesController.updateTransformer(test,1);
        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void shouldDeleteTransformer() {
        //assemble
        when(transformerServiceMock.deleteTransformer(1)).thenReturn(true);
        //act
        ResponseEntity<String> responseEntity = gamesController.deleteTransformer(1);
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldNotDeleteTransformerThatDoesNotExist() {
        //assemble
        when(transformerServiceMock.deleteTransformer(1)).thenReturn(false);
        //act
        ResponseEntity<String> responseEntity = gamesController.deleteTransformer(1);
        //assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void shouldFailToDeleteTransformer() {
        //assemble
        when(transformerServiceMock.deleteTransformer(1)).thenThrow(NullPointerException.class);
        //act
        ResponseEntity<String> responseEntity = gamesController.deleteTransformer(1);
        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void shouldStartFight() throws EndOfGameException {
        //assemble
        when(transformerServiceMock.getTransformer(1)).thenReturn(Optional.of(TransformerTest.buildTransformer("Auto",A)));
        when(transformerServiceMock.getTransformer(2)).thenReturn(Optional.of(TransformerTest.buildTransformer("Dec",D)));
        when(battleServiceMock.fight(any())).thenReturn(BattleResponse.builder().battleCount(1).build());
        //act
        ResponseEntity<BattleResponse> responseEntity = gamesController.beginFight(Arrays.asList(1,2));
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getBattleCount());
    }

    @Test
    void shouldStartFightButGameEnds() throws EndOfGameException {
        //assemble
        when(transformerServiceMock.getTransformer(1)).thenReturn(Optional.of(TransformerTest.buildTransformer("Auto",A)));
        when(transformerServiceMock.getTransformer(2)).thenReturn(Optional.of(TransformerTest.buildTransformer("Dec",D)));
        when(battleServiceMock.fight(any())).thenThrow(EndOfGameException.class);
        //act
        ResponseEntity<BattleResponse> responseEntity = gamesController.beginFight(Arrays.asList(1,2));
        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().getBattleCount());
        assertEquals(null, responseEntity.getBody().getWinningTeam());
        assertEquals(null, responseEntity.getBody().getSurvivors());
        assertEquals(null, responseEntity.getBody().getWinners());
    }

    @Test
    void shouldStartFightAndEncounterError() throws EndOfGameException {
        //assemble
        when(transformerServiceMock.getTransformer(1)).thenReturn(Optional.of(TransformerTest.buildTransformer("Auto",A)));
        when(transformerServiceMock.getTransformer(2)).thenReturn(Optional.of(TransformerTest.buildTransformer("Dec",D)));
        when(battleServiceMock.fight(any())).thenThrow(NullPointerException.class);
        //act
        ResponseEntity<BattleResponse> responseEntity = gamesController.beginFight(Arrays.asList(1,2));
        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}