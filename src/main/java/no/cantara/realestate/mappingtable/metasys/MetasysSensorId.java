package no.cantara.realestate.mappingtable.metasys;

import no.cantara.realestate.mappingtable.SensorId;

public class MetasysSensorId extends SensorId {

    private final String metasysDbId;
    private final String metasysObjectReference;

    public MetasysSensorId(String metasysDbId, String metasysObjectReference) {
        this.metasysDbId = metasysDbId;
        this.metasysObjectReference = metasysObjectReference;
    }

    public String getMetasysDbId() {
        return metasysDbId;
    }

    public String getMetasysObjectReference() {
        return metasysObjectReference;
    }

    public MetasysUniqueKey getMappingKey() {
        return new MetasysUniqueKey(metasysDbId);
    }

    @Override
    public String toString() {
        return "MetasysSensorId{" +
                "metasysDbId='" + metasysDbId + '\'' +
                ", metasysObjectReference='" + metasysObjectReference + '\'' +
                "} " + super.toString();
    }
}
