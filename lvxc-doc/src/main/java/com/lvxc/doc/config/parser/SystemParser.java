package com.lvxc.doc.config.parser;

import com.lvxc.doc.domain.SystemBaseDoc;
import com.lvxc.doc.domain.SystemModuleBaseDoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按系统解析主要实现类，可重新设置自定义解析类
 * controllerParser
 * serviceParser
 * entityParser
 * mapperParser
 */
public class SystemParser {
    private BaseParser controllerParser;
    private BaseParser serviceParser;
    private BaseParser entityParser;
    private BaseParser mapperParser;
    //没特定要求，先不整api接口
    private BaseParser apiParser;
    private SystemBaseDoc<? extends SystemModuleBaseDoc> baseDoc;
    private Map<String, Object> result;

    public SystemParser() {
        this.result = new HashMap<>();
        this.controllerParser = new ControllerParser();
        this.serviceParser = new ServiceParser();
        this.entityParser = new EntityParser();
        this.mapperParser = new MapperParser();
    }

    public void setControllerParser(BaseParser controllerParser) {
        this.controllerParser = controllerParser;
    }

    public void setServiceParser(BaseParser serviceParser) {
        this.serviceParser = serviceParser;
    }

    public void setEntityParser(BaseParser entityParser) {
        this.entityParser = entityParser;
    }

    public void setMapperParser(BaseParser mapperParser) {
        this.mapperParser = mapperParser;
    }

    public void setApiParser(BaseParser apiParser) {
        this.apiParser = apiParser;
    }

    public void setBaseDoc(SystemBaseDoc<? extends SystemModuleBaseDoc> baseDoc) {
        this.baseDoc = baseDoc;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public SystemParser doParser() {
        List<? extends SystemModuleBaseDoc> moduleDocList = baseDoc.getModuleDocList();
        List<Map<String, Object>> moduleMap = new ArrayList<>();
        Map<String, Object> moduleItemMap = null;
        for (SystemModuleBaseDoc doc : moduleDocList) {
            moduleItemMap = new HashMap<>();
            moduleItemMap.put("name", doc.getName());
            moduleItemMap.putAll(controllerParser.doParser(doc.getControllers()).resultMap);
            moduleItemMap.putAll(serviceParser.doParser(doc.getServices()).resultMap);
            moduleItemMap.putAll(mapperParser.doParser(doc.getMappers()).resultMap);
            moduleItemMap.putAll(entityParser.doParser(doc.getEntities()).resultMap);
            if (apiParser != null){
                moduleItemMap.putAll(apiParser.doParser(doc.getControllers()).resultMap);
            }
            moduleMap.add(moduleItemMap);
        }
        result.put("name", baseDoc.getName());
        result.put("modules", moduleMap);
        return this;
    }

    public <T extends SystemModuleBaseDoc> SystemParser doParser(SystemBaseDoc<T> baseDoc) {
        setBaseDoc(baseDoc);
        return doParser();
    }
}
