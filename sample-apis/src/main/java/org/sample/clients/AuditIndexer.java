package org.sample.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.sample.utils.JSONUtils;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

public class AuditIndexer {

    private static final Logger logger = Logger.getLogger(AuditIndexer.class.getName());
    private final String auditIndex;
    private final String auditAlias;
    private final ElasticSearchClient esUtil;
    private final String mappings;
    private final String settings;

    public AuditIndexer(String esHost, int esPort, String auditIndex, String auditAlias) throws Exception {
        mappings = JSONUtils.convertJson(getStream("audit-mappings.json"), JsonNode.class).toString();
        settings = JSONUtils.convertJson(getStream("es-settings.json"), JsonNode.class).toString();
        this.auditIndex = auditIndex;
        this.auditAlias = auditAlias;
        esUtil = new ElasticSearchClient(esHost, esPort);
    }

    public void createDocument(Map<String, Object> event) throws Exception {
        createDocument(event, auditIndex, auditAlias);
    }

    public String getIndexName(long ets, String indexName){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
        cal.setTime(new Date(ets));
        return indexName + "_" + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.WEEK_OF_YEAR);
    }

    private InputStream getStream(String filename){
        return getClass().getClassLoader().getResourceAsStream(filename);
    }


    public void createDocument(Map<String,Object> event, String indexName, String indexAlias) throws Exception {
        try {
            String index = getIndexName((Long) event.get("ets"), indexName);
            String mid = (String) event.get("mid");
            esUtil.addIndex(settings, mappings, index, indexAlias);
            esUtil.addDocumentWithIndex(JSONUtils.serialize(event), index, mid);
            logger.info("Audit document created for mid: " + mid);
        } catch (Exception e) {
            throw new Exception("Error while processing event :: " + event + " :: " + e.getMessage());
        }
    }
}
