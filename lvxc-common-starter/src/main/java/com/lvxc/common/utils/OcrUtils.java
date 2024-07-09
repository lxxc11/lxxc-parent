package com.lvxc.common.utils;

import com.lvxc.common.config.TencentConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @Author 周锐
 * @Version 1.0
 */
@Component
public class OcrUtils {

    /**
     * 通用印刷体识别（高精度版）-图片
     *
     * @param imageUrl
     * @return
     */
    @SneakyThrows
    public String universalPrintRecognitionHighPrecisionVersion(String imageUrl) {
        Credential cred = new Credential(TencentConfig.SECRET_ID, TencentConfig.SECRET_KEY);
        OcrClient client = new OcrClient(cred, TencentConfig.REGION);
        GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();
        req.setImageUrl(imageUrl);
        GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
        TextDetection[] textDetectionList = resp.getTextDetections();
        StringBuffer buffer = new StringBuffer();
        for (TextDetection textDetection : textDetectionList) {
            buffer.append(textDetection.getDetectedText()).append("\n");
        }
        return buffer.toString();
    }

    /**
     * 通用印刷体识别（高精度版）-pdf
     *
     * @param imageUrl
     * @param pdfPageNumber
     * @return
     */
    @SneakyThrows
    public String universalPrintRecognitionHighPrecisionVersion(String imageUrl, Long pdfPageNumber) {
        Credential cred = new Credential(TencentConfig.SECRET_ID, TencentConfig.SECRET_KEY);
        OcrClient client = new OcrClient(cred, TencentConfig.REGION);
        GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();
        req.setImageUrl(imageUrl);
        req.setIsPdf(true);
        req.setPdfPageNumber(pdfPageNumber);
        GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
        TextDetection[] textDetectionList = resp.getTextDetections();
        StringBuffer buffer = new StringBuffer();
        for (TextDetection textDetection : textDetectionList) {
            buffer.append(textDetection.getDetectedText()).append("\n");
        }
        return buffer.toString();
    }
}