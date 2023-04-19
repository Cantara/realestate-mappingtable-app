package no.cantara.realestate.mappingtable.metasys;

import no.cantara.config.ApplicationProperties;
import no.cantara.realestate.mappingtable.MappedSensorId;
import no.cantara.realestate.mappingtable.SensorId;
import no.cantara.realestate.mappingtable.csv.CsvCollection;
import no.cantara.realestate.mappingtable.csv.CsvReader;
import no.cantara.realestate.mappingtable.importer.CsvSensorImporter;
import no.cantara.realestate.mappingtable.rec.SensorRecObject;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static no.cantara.realestate.mappingtable.Main.getConfigValue;
import static org.slf4j.LoggerFactory.getLogger;

public class MetasysCsvSensorImporter extends CsvSensorImporter {
    private static final Logger log = getLogger(MetasysCsvSensorImporter.class);

    public MetasysCsvSensorImporter(File importDirectory) {
        super(importDirectory);
    }

    public List<SensorId> importSensorsFromFile(Path filepath) {
        List<SensorId> sensorIds = new ArrayList<>();
        CsvCollection collection = CsvReader.parse(filepath.toString());
        for (Map<String, String> record : collection.getRecords()) {
            log.debug("ColumnNames: {}",collection.getColumnNames());
            //MetasysObjectReference,MetasysObjectId,RecId
            //tfm,metasysObjectReference,metasysDbId,name,description,realEstate,interval,building,floor,electricityZone,sensorType,measurementUnit
            SensorId sensorId = new MetasysSensorId(record.get("metasysObjectReference"), record.get("MetasysObjectId"));
            sensorIds.add(sensorId);
        }
        return sensorIds;
    }

    @Override
    public List<MappedSensorId> importMappedIdFromFile(Path filepath) {
        List<MappedSensorId> mappedSensorIds = new ArrayList<>();
        CsvCollection collection = CsvReader.parse(filepath.toString());
        for (Map<String, String> record : collection.getRecords()) {
            log.debug("ColumnNames: {}",collection.getColumnNames());
            //MetasysObjectReference,MetasysObjectId,RecId
            //tfm,metasysObjectReference,metasysDbId,name,description,realEstate,interval,building,floor,electricityZone,sensorType,measurementUnit
            SensorId sensorId = new MetasysSensorId(record.get("metasysObjectReference"), record.get("MetasysObjectId"));
            SensorRecObject sensorRecObject = new SensorRecObject(record.get("RecId"));
            MappedSensorId mappedSensorId = new MappedSensorId(sensorId, sensorRecObject);
            mappedSensorIds.add(mappedSensorId);
        }
        return mappedSensorIds;
    }

    public static void main(String[] args) {
        ApplicationProperties.builder().defaults().buildAndSetStaticSingleton();
        String importDirectoryPath = getConfigValue("CSV_IMPORT_DIRECTORY");
        File importDirectory = new File(importDirectoryPath);
        MetasysCsvSensorImporter csvImporter = new MetasysCsvSensorImporter(importDirectory);
        List<Path> convertableFiles = csvImporter.findEligibleFiles("metasys");
        for (Path convertableFile : convertableFiles) {
            log.debug("Convert from csv 2 json: {}", convertableFile);
            /*
            JsonArray csvAsJson = convertFile(convertableFile);
            String convertableFilePath = convertableFile.toString();
            String writeToFile = convertableFilePath.replace("csv", "json");
            writeJson2File(csvAsJson, writeToFile);

             */
        }
        log.info("Wrote {} files to {}", convertableFiles.size(), importDirectoryPath);
    }
}
