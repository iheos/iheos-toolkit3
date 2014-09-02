package gov.nist.hit.ds.eventLog;

public class Artifacts {
    def artifactMap = [:]
    def artifactOrder = []

    def add(name, value) {
        artifactMap[name] = value
        artifactOrder << name
    }

    def empty() { artifactOrder.size() == 0 }

}
