// Service -> talks to repository
package nl.carsforyou.garage.services;

import nl.carsforyou.garage.dtos.part.PartRequestDto;
import nl.carsforyou.garage.dtos.part.PartResponseDto;
import nl.carsforyou.garage.entities.PartEntity;
import nl.carsforyou.garage.entities.UserEntity;
import nl.carsforyou.garage.mappers.PartDTOMapper;
import nl.carsforyou.garage.repositories.PartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PartService {
    private final PartRepository partRepository;
    private final PartDTOMapper partDTOMapper;

    public PartService(PartRepository partRepository, PartDTOMapper partDTOMapper) {
        this.partRepository = partRepository;
        this.partDTOMapper = partDTOMapper;
    }

    public List<PartResponseDto> getAllParts() {
        return partDTOMapper.mapToDtoList(partRepository.findAll());
    }

    public PartResponseDto getPartById(Long id) {
        PartEntity part = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Part with Id " + id + " was not found"));

        return partDTOMapper.mapToDto(part);
    }

    public PartResponseDto createPart(PartRequestDto dto) {
        //store passed DTO in entityMapper
        PartEntity entity = partDTOMapper.mapToEntity(dto);

        //save to repository and return saved data
        PartEntity saved = partRepository.save(entity);
        return partDTOMapper.mapToDto(saved);
    }

    public PartResponseDto updatePart(Long id, PartRequestDto dto) {
        PartEntity existing = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Part with Id " + id + " was not found"));

        //if no errors, set values that were passed from dto
        existing.setItemNumber(dto.getItemNumber());
        existing.setItemDescription(dto.getItemDescription());
        existing.setQtyOnHand(dto.getQtyOnHand());
        existing.setQtyOnOrder(dto.getQtyOnOrder());
        existing.setUnitCost(dto.getUnitCost());
        existing.setUnitPrice(dto.getUnitPrice());

        //save to repository and return saved data
        PartEntity saved = partRepository.save(existing);
        return partDTOMapper.mapToDto(saved);
    }

    public void deletePart(Long id) {
        PartEntity user = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Part with Id " + id + " was not found"));

        //only delete if id did not throw an exception
        partRepository.delete(user);
    }
}
