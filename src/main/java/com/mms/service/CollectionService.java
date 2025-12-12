package com.mms.service;

import com.mms.model.Collection;
import com.mms.repository.CollectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    public List<Collection> getCollectionsByMonth(int month, int year) {
        return collectionRepository.findByMonth(month, year);
    }

    public List<Collection> getMemberCollections(Integer memberId, int month, int year) {
        return collectionRepository.findByMember(memberId, month, year);
    }

    public Collection getCollectionById(Integer id) {
        return collectionRepository.findById(id);
    }

    @Transactional
    public Collection saveCollection(Collection collection) {
        Integer id = collectionRepository.save(collection);
        collection.setCollectionId(id);
        return collection;
    }

    @Transactional
    public void updateCollection(Collection collection) {
        collectionRepository.update(collection);
    }

    @Transactional
    public void deleteCollection(Integer id) {
        collectionRepository.delete(id);
    }

    /**
     * Get total collection for a member in a specific month
     */
    public BigDecimal getMemberTotalCollection(Integer memberId, int month, int year) {
        List<Collection> collections = collectionRepository.findByMember(memberId, month, year);
        return collections.stream()
                .map(Collection::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}