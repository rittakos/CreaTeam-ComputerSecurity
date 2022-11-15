package hu.bme.hit.vihima06.caffshop.backend.service;

import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.ForbiddenException;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.NotFoundException;
import hu.bme.hit.vihima06.caffshop.backend.mapper.CaffFileDataMapper;
import hu.bme.hit.vihima06.caffshop.backend.model.CaffFileData;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.models.CaffDetailsResponse;
import hu.bme.hit.vihima06.caffshop.backend.models.CaffResponse;
import hu.bme.hit.vihima06.caffshop.backend.models.ModifyCaffRequest;
import hu.bme.hit.vihima06.caffshop.backend.repository.CaffFileDataRepository;
import hu.bme.hit.vihima06.caffshop.backend.security.service.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaffFileDataService {

    @Autowired
    private CaffFileDataRepository caffFileDataRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public void deleteById(Integer id) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        if (!loggedInUser.getId().equals(caffFileData.getCreator().getId()) && !loggedInUserService.isAdmin()) {
            throw new ForbiddenException("File access denied");
        }

        // TODO delete caff file and preview as well

        caffFileDataRepository.delete(caffFileData);
    }

    public CaffDetailsResponse getFileDetailsById(Integer id) {
        Optional<CaffFileData> caffFileData = caffFileDataRepository.findById(id);

        if (caffFileData.isEmpty()) {
            throw new NotFoundException("File not found");
        }

        return CaffFileDataMapper.INSTANCE.fileToCaffDetailsResponse(caffFileData.get());
    }

    public List<CaffResponse> searchCaffFiles(String query) {
        List<CaffFileData> results = caffFileDataRepository.findAllByNameOrCreatorOrTags(query, query, query);

        return results.stream().map(r -> CaffFileDataMapper.INSTANCE.fileToCaffResponse(r)).toList();
    }

    public CaffDetailsResponse modifyFile(Integer id, ModifyCaffRequest modifyCaffRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        CaffFileData caffFileData = caffFileDataRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found"));

        if (!loggedInUser.getId().equals(caffFileData.getCreator().getId()) && !loggedInUserService.isAdmin()) {
            throw new ForbiddenException("File access denied");
        }

        caffFileData.setName(modifyCaffRequest.getName());

        caffFileDataRepository.save(caffFileData);

        return CaffFileDataMapper.INSTANCE.fileToCaffDetailsResponse(caffFileData);
    }
}
