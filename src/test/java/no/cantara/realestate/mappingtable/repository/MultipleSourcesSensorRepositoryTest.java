package no.cantara.realestate.mappingtable.repository;

import no.cantara.realestate.mappingtable.MappedSensorId;
import no.cantara.realestate.mappingtable.bacnet.BacnetMappingKey;
import no.cantara.realestate.mappingtable.bacnet.BacnetSensorId;
import no.cantara.realestate.mappingtable.ecostruxure.EcoStruxureTrendSensorId;
import no.cantara.realestate.mappingtable.metasys.MetasysSensorId;
import no.cantara.realestate.mappingtable.rec.SensorRecObject;
import no.cantara.realestate.mappingtable.tfm.Tfm;
import no.cantara.realestate.mappingtable.tfm.TfmMappingKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultipleSourcesSensorRepositoryTest {

    MappedIdRepository repository;
//    final Tfm tfm = new Tfm("TFM-RY02101");
    int expectedCount = 0;

    @BeforeEach
    void setUp() {
        repository = new MappedIdRepositoryImpl();
        MappedSensorId metasysMappedId = buildMetasysMappedId("dbId1", "objectRef1", "recid1", "TFM-1");
        repository.add(metasysMappedId);
        repository.add(buildMetasysMappedId("dbId2", "objectRef2", "recid2", "TFM-2"));
        repository.add(buildMetasysMappedId("dbId3", "objectRef3", "recid3", "TFM-3"));
        MappedSensorId bacnetMappedId = buildBacnetMappedId(1001, 345006, "AnalogValue", "recId4", "TFM-4");
        repository.add(bacnetMappedId);
        repository.add(buildBacnetMappedId(1002, 345006, "AnalogValue", "recId5", "TFM-5"));
        repository.add(buildBacnetMappedId(1001, 345007, "AnalogValue", "recId6", "TFM-6"));
        repository.add(buildBacnetMappedId(1001, 345008, "DigitalInput", "recId7", "TFM-7"));
        MappedSensorId ecoStruxureTrendMappedId = buildEcoStruxureTrendMappedId("TrendId1", "recId8", "TFM-8");
        repository.add(ecoStruxureTrendMappedId);
        expectedCount = 8;
    }

    private MappedSensorId buildEcoStruxureTrendMappedId(String trendId, String recId, String tfm) {
        EcoStruxureTrendSensorId sensorId = new EcoStruxureTrendSensorId(trendId);
        SensorRecObject recObject = new SensorRecObject(recId);
        recObject.setTfm(new Tfm(tfm));
        return new MappedSensorId(sensorId, recObject);
    }

    private MappedSensorId buildBacnetMappedId(int deviceId, int instanceId, String objectType, String recId, String tfm) {
        BacnetSensorId sensorId = new BacnetSensorId(deviceId, instanceId, objectType);
        SensorRecObject recObject = new SensorRecObject(recId);
        recObject.setTfm(new Tfm(tfm));
        return new MappedSensorId(sensorId, recObject);
    }

    MappedSensorId buildMetasysMappedId(String metasysDbId, String metasysObjectReference, String recId, String tfm) {
        MetasysSensorId sensorId = new MetasysSensorId(metasysDbId, metasysObjectReference);
        SensorRecObject recObject = new SensorRecObject(recId);
        recObject.setTfm(new Tfm(tfm));
        return new MappedSensorId(sensorId, recObject);
    }

    @Test
    void findUsingMappingKey() {
        assertEquals(expectedCount, repository.size());
        List<MappedSensorId> matchingSersorIds = repository.find(new TfmMappingKey(new Tfm("TFM-7")));
        assertEquals(1, matchingSersorIds.size());
        matchingSersorIds = repository.find(new BacnetMappingKey(1001, 345008, "DigitalInput"));
    }
}
