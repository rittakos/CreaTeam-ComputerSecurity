package hu.bme.hit.vihima06.caffshop.backend.repository;

import hu.bme.hit.vihima06.caffshop.backend.model.CaffFileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaffFileDataRepository extends JpaRepository<CaffFileData, Integer> {
    List<CaffFileData> findAllByNameOrCreatorOrTags(String name, String creator, String tags);
}
