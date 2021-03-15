package games.transformers.transformersapi.web;

import games.transformers.transformersapi.domain.BattleResponse;
import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.service.BattleService;
import games.transformers.transformersapi.service.EndOfGameException;
import games.transformers.transformersapi.service.TransformerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

/**
 * Rest controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/game")
public class GamesController {

    private TransformerService transformerService;
    private BattleService battleService;

    @Autowired
    public GamesController(TransformerService transformerService, BattleService battleService) {
        this.transformerService = transformerService;
        this.battleService = battleService;
    }

    @ApiOperation(value = "Operation to list all transformers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data available")}
    )
    @GetMapping("/transformers")
    public ResponseEntity<List<Transformer>> getListOfTransformers() {
        List<Transformer> transformers = transformerService.getAllTransformers();
        if (transformers.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body(Collections.EMPTY_LIST);
        } else {
            return ResponseEntity.ok(transformers);
        }
    }

    @ApiOperation(value = "Operation to add a new transformer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Server Error")}
    )
    @PostMapping(
            value = "/transformer")
    public ResponseEntity<String> createTransformer(@RequestBody Transformer transformer) {
        try {
            int id = transformerService.insertTransformer(transformer);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(id).toUri();
            log.info("Transformer created successfully:" + location);
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Operation to update an existing transformer")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Unable to update transformer id"),
            @ApiResponse(code = 404, message = "Unable to update given transformer"),
            @ApiResponse(code = 500, message = "Server Error")}
    )
    @PutMapping(
            value = "/transformer/{id}")
    public ResponseEntity<String> updateTransformer(@RequestBody Transformer transformer,
                                                    @PathVariable("id") int id) {
        try {
            if (id != transformer.getId()) {
                log.error("Transformer id cannot be updated");
                return ResponseEntity.badRequest().body("Unable to update transformer id");
            }
            boolean success = transformerService.updateTransformer(transformer);
            if (success) {
                log.info("Updated Transformer id: {}", id);
                return ResponseEntity.ok("SUCCESS");
            } else {
                log.error("Transformer id cannot be updated, possible not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("An error occurred while processing update request. {}", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();

        }
    }

    @ApiOperation(value = "Operation to delete an existing transformer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Unable to delete given transformer"),
            @ApiResponse(code = 500, message = "Server Error")}
    )
    @DeleteMapping(
            value = "/transformer/{id}")
    public ResponseEntity<String> deleteTransformer(@PathVariable("id") int id) {
        log.info("Deleting Transformer id: {}", id);
        try {
            boolean success = transformerService.deleteTransformer(id);
            if (success) {
                log.info("Deleted Transformer id: {}", id);
                return ResponseEntity.ok("SUCCESS");
            } else {
                log.error("Transformer id cannot be deleted, possible not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("An error occurred while processing delete request. {}", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Operation to initiate a fight between list of transformers.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Server Error")}
    )
    @PostMapping(
            value = "/fight")
    public ResponseEntity<BattleResponse> beginFight(@RequestBody List<Integer> transformerIDs) {
        log.info("Initiating fight ");

        // Skipping validation for now. Will not fail even if invalid id is passed in the input
        List<Transformer> transformers = transformerIDs.stream()
                .map(id -> transformerService.getTransformer(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        try {
            BattleResponse fight = battleService.fight(transformers);
            return ResponseEntity.ok(fight);

        } catch (EndOfGameException e) {
            log.info("All transformers destroyed, sending empty response back");
            return ResponseEntity.ok(BattleResponse.builder().build());
        } catch (Exception e) {
            log.info("Internal error: {}", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
