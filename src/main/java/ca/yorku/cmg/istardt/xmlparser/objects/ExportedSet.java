package ca.yorku.cmg.istardt.xmlparser.objects;

import java.util.ArrayList;
import java.util.List;

public class ExportedSet {
    List<Export> exports = new ArrayList<>();

    public List<Export> getExports() {
        return exports;
    }
    public void addExport(Export export) {
        exports.add(export);
    }
    public void setExports(List<Export> exports) {
        this.exports = exports;
    }
}
