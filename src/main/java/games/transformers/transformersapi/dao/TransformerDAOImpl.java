package games.transformers.transformersapi.dao;

import games.transformers.transformersapi.domain.Transformer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class implements the {@link TransformerDAO} using a map.
 * Nothing is persisted to file storage or a DB
 */
@Component
public class TransformerDAOImpl implements TransformerDAO {

    private Map<Integer, Transformer> transformerMap;
    private AtomicInteger transformerID;

    public TransformerDAOImpl() {
        this.transformerMap = new ConcurrentHashMap<>();
        this.transformerID = new AtomicInteger();
    }

    /**
     * Returns a transformer if it exists
     *
     * @return A transformer or null;
     */
    @Override
    public Transformer getTransformer(int id) {
        return transformerMap.get(id);
    }

    /**
     * Provides a lis of all available transformers
     *
     * @return A list of transformers
     */
    @Override
    public List<Transformer> getAllTransformers() {
        return transformerMap.values().stream().collect(Collectors.toList());
    }

    /**
     * Inserts a new transformer in the existing collection of transformers
     *
     * @param transformer A new transformer to be created
     * @return id of newly created transformer
     */
    @Override
    public int insertTransformer(Transformer transformer) {
        int id = transformerID.addAndGet(1);
        if (transformer != null) {
            transformer.setId(id);
            transformerMap.put(id, transformer);
        } else {
            throw new IllegalArgumentException("Invalid input: Transformer is null");
        }
        return id;
    }

    /**
     * Updates an existing transformer
     *
     * @param transformer Details of transformer that needs an update
     * @return true if successfully updated
     */
    @Override
    public boolean updateTransformer(Transformer transformer) {
        boolean success = false;
        if (transformer != null) {
            if (transformerMap.get(transformer.getId()) != null) {
                transformerMap.put(transformer.getId(), transformer);
                success = true;
            } else {
                throw new IllegalArgumentException("Transformer that you are trying to update does not exist.");
            }
        }
        return success;
    }

    /**
     * Deletes a transformer that is already present
     *
     * @param id id of the transformer to be deleted
     * @return true if successfully deleted
     */
    @Override
    public boolean deleteTransformer(int id) {
        boolean success = false;
        if (transformerMap.get(id) != null) {
            transformerMap.remove(id);
            success = true;
        } else {
            throw new IllegalArgumentException("Transformer that you are trying to delete does not exist.");
        }
        return success;
    }
}
