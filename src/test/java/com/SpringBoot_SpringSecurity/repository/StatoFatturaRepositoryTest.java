package com.SpringBoot_SpringSecurity.repository;

import com.SpringBoot_SpringSecurity.entity.*;
import com.SpringBoot_SpringSecurity.runner.StatoFattureRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatoFatturaRepositoryTest {



    @Autowired private StatoFatturaRepository statoFatturaRepository;
    @Autowired private ProvinceRepository provinceRepository;
    @Autowired private ComuniRepository comuniRepository;
    @Autowired private IndirizziRepository indirizziRepository;
    @Autowired private ClientiRepository clientiRepository;
    @Autowired private FattureRepository fattureRepository;

    Logger log= LoggerFactory.getLogger(StatoFatturaRepository.class);




    //tests
    @Test
    void existsByNome() {
    var nome ="minacciato";
    assertThat(statoFatturaRepository.existsByNome(nome)).isTrue();
    }

    //tests CLIENTI REPOSITORY

    @Test
    void findByFatturatoAnnuale() {
        log.info("SIZE OF CLIENTI FATTURATO IS: "+clientiRepository.findByFatturatoAnnuale(BigDecimal.valueOf(200000)).size());
        assertThat(clientiRepository.findByFatturatoAnnuale(BigDecimal.valueOf(200000))).size().isEqualTo(1);
    }



    //beforeEach/afterEach
    @BeforeEach
    public  void popolaDB(){

        popolaDBStatiFatture();
        popolaDBProvince();
        popolaDBComuni();
        popolaDBIndirizzi();
        popolaDBClienti();
        popolaDBFatture();
    }
    @AfterEach
    void logging(){

        clientiRepository.deleteAll();
    }

    //metodi ausiliari
    public void popolaDBStatiFatture(){
        var statiFatture= Stream.of(
                new BeServiceStatoFattura("pagato"),
                new BeServiceStatoFattura("non pagato"),
                new BeServiceStatoFattura("pagato"),
                new BeServiceStatoFattura("non pagato")
        ).toList();

        statoFatturaRepository.saveAll(statiFatture);
    }

    public void popolaDBProvince(){
        var lista= Stream.of(
                new BeServiceProvince("land1", "MD"),
                new BeServiceProvince("land2", "KK"),
                new BeServiceProvince("land3", "CG"),
                new BeServiceProvince("land4", "BP")
        ).toList();

       provinceRepository.saveAll(lista);
    }

    public void popolaDBComuni(){
        var lista= Stream.of(

                new BeServiceComuni("Comune1", provinceRepository.findById(1l).get()),
                new BeServiceComuni("Comune2",provinceRepository.findById(2l).get()),
                new BeServiceComuni("Comune3", provinceRepository.findById(3l).get()),
                new BeServiceComuni("Comune4", provinceRepository.findById(4l).get()) 
        ).toList();

        comuniRepository.saveAll(lista);
    }
    public void popolaDBIndirizzi(){
        var list = Stream.of(
                new BeServiceIndirizzi("00104", "69", "Via gg", "Via anonima", comuniRepository.findById(1l).get()),
                new BeServiceIndirizzi("222", "111", "Via coco'", "Via opinioni", comuniRepository.findById(2l).get()),
                new BeServiceIndirizzi("333", "666", "Campo bello", "Via soda", comuniRepository.findById(3l).get()),
                new BeServiceIndirizzi("444", "777", "Zona coco", "Via dei padri",comuniRepository.findById(4l).get())

        ).toList();

        indirizziRepository.saveAll(list);
    }

    public void popolaDBClienti(){
        var list = Stream.of(
                new BeServiceClienti("Postina", Timestamp.valueOf("1998-04-07 00:00:00"), Timestamp.valueOf("2000-04-09 00:00:00"), "pinapostina@saponettamail.com",
                        "pina@postina.it", BigDecimal.valueOf(200000), "Pina", "partitamondialicalcio",
                        "Mv6jhsx","criticaDellaRagionPura", "12345678", "0987654", "Affidabile", indirizziRepository.findById(1l).get(),
                        indirizziRepository.findById(1l).get()),
                new BeServiceClienti("Bunny", Timestamp.valueOf("2022-04-01 00:00:00"),
                        Timestamp.valueOf("2023-04-09 00:00:00"),
                        "bunnyfet@onlyfans.com", "nonèvero@gmail.com",
                        BigDecimal.valueOf(250000),"Bugs","partita di testa", "XY628Bn", "nessuna", "234567", "876543", "pericoloso",
                        indirizziRepository.findById(4l).get(),
                        indirizziRepository.findById(4l).get()
                )

        ).toList();

        clientiRepository.saveAll(list);
    }
    public void popolaDBFatture(){
        var fattura= new BeServiceFatture(1995, Timestamp.valueOf("1995-04-09 00:00:00"), BigDecimal.valueOf(2000l),33,
                clientiRepository.findById(1l).get(), statoFatturaRepository.findById(4l).get());

        fattureRepository.save(fattura);
    }

}