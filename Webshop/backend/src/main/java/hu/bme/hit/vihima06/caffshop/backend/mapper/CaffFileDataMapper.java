package hu.bme.hit.vihima06.caffshop.backend.mapper;

import hu.bme.hit.vihima06.caffshop.backend.model.CaffFileData;
import hu.bme.hit.vihima06.caffshop.backend.models.CaffDetailsResponse;
import hu.bme.hit.vihima06.caffshop.backend.models.CaffResponse;
import hu.bme.hit.vihima06.caffshop.backend.models.FileUploadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CaffFileDataMapper {
    CaffFileDataMapper INSTANCE = Mappers.getMapper(CaffFileDataMapper.class);

    @Mapping(source = "width", target = "size.width")
    @Mapping(source = "height", target = "size.height")
    CaffDetailsResponse fileToCaffDetailsResponse(CaffFileData caffFileData);

    FileUploadResponse fileToFileUploadResponse(CaffFileData caffFileData);

    CaffResponse fileToCaffResponse(CaffFileData caffFileData);
}
