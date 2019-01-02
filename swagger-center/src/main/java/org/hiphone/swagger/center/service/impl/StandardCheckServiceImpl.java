package org.hiphone.swagger.center.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.Constant;
import org.hiphone.swagger.center.service.StandardCheckService;
import org.hiphone.swagger.center.utils.StandardNumUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class StandardCheckServiceImpl implements StandardCheckService {

    @Value("${bean.check}")
    private boolean isBeanCheck;

    @Value("${bean.white-list}")
    private String beanWhiteListString;

    private String[] beanWhiteList = null;

    @PostConstruct
    private void init() {
        beanWhiteList = beanWhiteListString.split(",");
    }

    @Override
    public Integer getNotStandardNum(JSONObject swaggerApiDocs) {
        int notStandardNum = 0;
        JSONObject infoObj = swaggerApiDocs.getJSONObject(Constant.API_INFO);
        JSONArray tagsArray = swaggerApiDocs.getJSONArray(Constant.API_TAGS);
        JSONObject pathObj = swaggerApiDocs.getJSONObject(Constant.API_PATH);
        JSONObject definitionObj = null;

        if (infoObj != null) {
            int infoNum = StandardNumUtil.getNotStandardNumOfInfo(infoObj);
            if (infoNum == -1) {
                log.warn("swaggerJson's contact is null!");
                notStandardNum += 3;
            } else {
                notStandardNum += infoNum;
            }
        } else {
            log.warn("swaggerJson's infoObject is null!");
            return -1;
        }

        if (tagsArray != null) {
            notStandardNum += StandardNumUtil.getNotStandardNumOfTags(tagsArray);
        } else {
            log.warn("swaggerJson's tagsJsonArray is null!");
            return -1;
        }

        if (pathObj != null) {
            notStandardNum += StandardNumUtil.getNotStandardNumOfPaths(pathObj);
        } else {
            log.warn("swaggerJson's pathJsonObject is null!");
            return -1;
        }

        if(isBeanCheck) {
            definitionObj = swaggerApiDocs.getJSONObject(Constant.API_DEFINITIONS);
            if (definitionObj != null) {
                notStandardNum += StandardNumUtil.getNotStandardNumOfDefinitions(definitionObj, beanWhiteList);
            } else {
                log.warn("swaggerJson's definitionJsonObject is null");
            }
        }

        return notStandardNum;
    }
}
