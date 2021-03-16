package games.transformers.transformersapi.service;


import games.transformers.transformersapi.dao.TransformerDAO;
import games.transformers.transformersapi.domain.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A service that interacts with transformers DAO
 */
@Service
public class TransformerServiceImpl implements TransformerService {

    private TransformerDAO transformerDAO;

    @Autowired
    public TransformerServiceImpl(TransformerDAO transformerDAO) {
        this.transformerDAO = transformerDAO;
    }

    /**
     * Provides a lis of all available transformers
     *
     * @return A list of transformers
     */
    public Optional<Transformer> getTransformer(int id){
        return Optional.ofNullable(transformerDAO.getTransformer(id));
    }

    /**
     * Provides a lis of all available transformers
     *
     * @return A list of transformers
     */
    @Override
    public List<Transformer> getAllTransformers() {
        List<Transformer> transformers = transformerDAO.getAllTransformers();
        if (transformers == null || transformers.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else {
            return transformers;
        }
    }

    /**
     * Inserts a new transformer in the existing collection of transformers
     *
     * @param transformer A new transformer to be created
     * @return id of newly created transformer
     */
    @Override
    public int insertTransformer(Transformer transformer) {
        //Could add validations here if required.
        return transformerDAO.insertTransformer(transformer);
    }

    /**
     * Updates an existing transformer
     *
     * @param transformer Details of transformer that needs an update
     * @return true if successfully updated
     */
    @Override
    public boolean updateTransformer(Transformer transformer) {
        //Could add validations here if required.
        return transformerDAO.updateTransformer(transformer);
    }

    /**
     * Deletes a transformer that is already present
     *
     * @param id
     * @return true if successfully deleted
     */
    @Override
    public boolean deleteTransformer(int id) {
        return transformerDAO.deleteTransformer(id);
    }
}
