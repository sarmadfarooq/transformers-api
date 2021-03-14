package games.transformers.transformersapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BattleResponse {
    int battleCount;
    Transformer.Type winningTeam;
    List<String> winners;
    List<String> survivors;

}
