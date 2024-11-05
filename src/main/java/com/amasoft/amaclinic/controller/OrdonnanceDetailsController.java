package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.OrdonnanceDetailsService;
import com.amasoft.amaclinic.criteria.OrdonnanceDetailsCriteria;
import com.amasoft.amaclinic.dto.request.OrdonnanceDetailsRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceDetailsResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/ordonnanceDetails")
public class OrdonnanceDetailsController {

    private OrdonnanceDetailsService ordonnanceDetailsService;
    @PostMapping
    public OrdonnanceDetailsResponseDTO save(@RequestBody OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDto){
        return ordonnanceDetailsService.addOrdonnanceDetails(ordonnanceDetailsRequestDto);
    }

    @PutMapping
    public OrdonnanceDetailsResponseDTO update(@RequestBody OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDto){
        return ordonnanceDetailsService.updateOrdonnanceDetails(ordonnanceDetailsRequestDto);
    }

    @DeleteMapping
    public void delete(@RequestParam(name ="code") String code){
        ordonnanceDetailsService.deleteOrdonnanceDetails(code);
    }

    @GetMapping
    Page<OrdonnanceDetailsResponseDTO> getOrdonnanceDetailsByCriteria(@RequestParam(defaultValue = "0", name ="page") int page,
                                                  @RequestParam(defaultValue = "10" , name = "size") int size,
                                                  @RequestParam( name = "id", required = false) Long id ,
                                                  @RequestParam(name = "dose", required = false) String dose ,
                                                  @RequestParam(name = "horaires", required = false) String horaires ,
                                                  @RequestParam(name="dureeTraitement",required = false)Integer dureeTraitement,
                                                  @RequestParam(name = "frequence",required = false)Integer frequence,
                                                  @RequestParam(name = "instructionsSpeciales",required = false)String instructionsSpeciales,
                                                  @RequestParam(name = "codeOrdonnanceDetails", required = false) String codeOrdonnanceDetails){

        OrdonnanceDetailsCriteria ordonnanceDetailsCriteria = new OrdonnanceDetailsCriteria();
        ordonnanceDetailsCriteria.setId(id);
        ordonnanceDetailsCriteria.setDose(dose);
        ordonnanceDetailsCriteria.setHoraires(horaires);
        ordonnanceDetailsCriteria.setDureeTraitement(dureeTraitement);
        ordonnanceDetailsCriteria.setFrequence(frequence);
        ordonnanceDetailsCriteria.setInstructionsSpeciales(instructionsSpeciales);
        ordonnanceDetailsCriteria.setCodeOrdonnanceDetails(codeOrdonnanceDetails);
        return ordonnanceDetailsService.findOrdonnanceDetailsByCriteria(ordonnanceDetailsCriteria,page,size);
    }

}
