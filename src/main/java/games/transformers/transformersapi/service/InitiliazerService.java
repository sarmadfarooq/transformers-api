package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * This service is responsible of creating Transformers at the startup
 */
@Service
public class InitiliazerService {
    private TransformerService transformerService;

    @Autowired
    public InitiliazerService(TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @PostConstruct
    public void init() {
        Transformer auto1 = buildStartupTransformer("Auto1", Transformer.Type.A);
        Transformer auto2 = buildStartupTransformer("Auto2", Transformer.Type.A);
        auto2.setRank(2);
        auto2.setStrength(10);
        Transformer auto3 = buildStartupTransformer("Auto3", Transformer.Type.A);
        auto3.setRank(3);
        auto3.setSkill(10);
        Transformer dec1= buildStartupTransformer("Dec1", Transformer.Type.D);
        Transformer dec2= buildStartupTransformer("Dec2", Transformer.Type.D);
        dec2.setRank(2);
        dec2.setCourage(10);
        Transformer dec3= buildStartupTransformer("Dec3", Transformer.Type.D);
        dec3.setRank(3);
        dec3.setEndurance(10);
        transformerService.insertTransformer(auto1);
        transformerService.insertTransformer(auto2);
        transformerService.insertTransformer(auto3);
        transformerService.insertTransformer(dec1);
        transformerService.insertTransformer(dec2);
        transformerService.insertTransformer(dec3);
    }

    private static Transformer buildStartupTransformer(String name, Transformer.Type type) {
        return Transformer.builder().name(name)
                .type(type).strength(1)
                .intelligence(2).speed(3)
                .endurance(4).rank(5)
                .courage(6).firepower(7)
                .skill(8).build();
    }
}
