package greencity.service.impl;

import greencity.dto.advice.AdvicePostDTO;
import greencity.dto.fact.HabitFactPostDTO;
import greencity.entity.FactTranslation;
import greencity.entity.HabitFact;
import greencity.repository.FactTranslationRepo;
import greencity.service.FactTranslationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link FactTranslationService}.
 *
 * @author Vitaliy Dzen
 */
@Service
@AllArgsConstructor
public class FactTranslationServiceImpl implements FactTranslationService {
    private FactTranslationRepo factTranslationRepo;
    private HabitFactServiceImpl habitFactService;
    @Autowired
    private final ModelMapper modelMapper;


    /**
     * Method saves new {@link HabitFact} and list of new {@link FactTranslation} with relationship
     * to {@link HabitFact}.
     *
     * @param habitFactPostDTO {@link AdvicePostDTO}
     * @return List of {@link FactTranslation}
     * @author Vitaliy Dzen
     */
    public List<FactTranslation> saveHabitFactAndFactTranslation(HabitFactPostDTO habitFactPostDTO) {
        HabitFact habitFact = habitFactService.save(habitFactPostDTO);
        List<FactTranslation> factTranslations = modelMapper.map(habitFactPostDTO.getTranslations(),
            new TypeToken<List<FactTranslation>>() {
            }.getType());
        factTranslations = factTranslations
            .stream()
            .peek(a -> a.setHabitFact(habitFact))
            .collect(Collectors.toList());
        return saveFactTranslation(factTranslations);
    }

    /**
     * Method saves new {@link FactTranslation}.
     *
     * @param factTranslations {@link FactTranslation}
     * @return List of {@link FactTranslation}
     * @author Vitaliy Dzen
     */
    @Override
    public List<FactTranslation> saveFactTranslation(List<FactTranslation> factTranslations) {
        return factTranslationRepo.saveAll(factTranslations);
    }
}