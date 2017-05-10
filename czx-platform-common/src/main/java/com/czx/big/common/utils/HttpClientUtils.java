package com.czx.big.common.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public abstract class HttpClientUtils {

    private static Log logger = LogFactory.getLog(HttpClientUtils.class);

    public static final String RETURN_SUCCESS = "success";

    public static final String RETURN_FAILURE = "failure";

    private static final int DEFAULT_TRY_NUM = 3;

    private static final int ERROR_SLEEP_TIME = 100;

    public static String retryExecuteHttpPost(String url, Map<String, String> paramMap) {
        int tryNum = 0;
        boolean sendSuccess = false;
        while (tryNum < DEFAULT_TRY_NUM) {
            String result = executeHttpPost(url, paramMap);
            if (result != null) {
                return result;
            }
            else {
                tryNum++;
                try {
                    Thread.sleep(ERROR_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!sendSuccess) {
            logger.warn("send HttpClientRequestData error : " + url + ", paramMap: " + paramMap);
        }

        return "";
    }

    public static String retryExecuteHttpGet(String url, Map<String, String> paramMap) {
        int tryNum = 0;
        boolean sendSuccess = false;
        while (tryNum < DEFAULT_TRY_NUM) {
            String result = executeHttpGet(url, paramMap);
            if (result != null) {
                return result;
            }
            else {
                tryNum++;
                try {
                    Thread.sleep(ERROR_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!sendSuccess) {
            logger.warn("send HttpClientRequestData error : " + url + ", paramMap: " + paramMap);
        }

        return "";
    }

    /**
     * 执行 http post 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static boolean executeHttpPostBooleanResult(String url, Map<String, String> paramMap) {
        return retryExecuteHttpPostBooleanResult(url, paramMap, DEFAULT_TRY_NUM);
    }

    /**
     * 执行 http post 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static boolean executeHttpGetBooleanResult(String url, Map<String, String> paramMap) {
        return retryExecuteHttpGetBooleanResult(url, paramMap, DEFAULT_TRY_NUM);
    }

    private static boolean retryExecuteHttpPostBooleanResult(String url, Map<String, String> paramMap, int maxTryNum) {
        int tryNum = 0;
        boolean sendSuccess = false;
        while (tryNum < maxTryNum) {
            String result = executeHttpPost(url, paramMap);
            if (isHttpClientRequestSuccess(result)) {
                sendSuccess = true;
                break;
            }
            else {
                tryNum++;
                try {
                    Thread.sleep(ERROR_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!sendSuccess) {
            logger.warn("send HttpClientRequestData error : " + url + ", paramMap: " + paramMap);
        }

        return sendSuccess;
    }

    private static boolean retryExecuteHttpGetBooleanResult(String url, Map<String, String> paramMap, int maxTryNum) {
        int tryNum = 0;
        boolean sendSuccess = false;
        while (tryNum < maxTryNum) {
            String result = executeHttpGet(url, paramMap);
            if (isHttpClientRequestSuccess(result)) {
                sendSuccess = true;
                break;
            }
            else {
                tryNum++;
                try {
                    Thread.sleep(ERROR_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!sendSuccess) {
            logger.warn("send HttpClientRequestData error : " + url + ", paramMap: " + paramMap);
        }

        return sendSuccess;
    }

    /**
     * 执行 http post 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String executeHttpPost(String url, Map<String, String> paramMap) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                nvps.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);

            return content;
        } catch (Exception e) {
            logger.error("executeHttpPost meet error, " + e.getMessage());
            return null;
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    /**
     * 执行 http post 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String executeHttpPostXml(String url, String xml) {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
				true);
        HttpPost httpPost = new HttpPost(url);

        try {
        	httpPost.addHeader("Content-type", "text/xml; charset=utf-8");
            httpPost.setEntity(new StringEntity(xml, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);

            return content;
        } catch (Exception e) {
            logger.error("executeHttpPostXml meet error, " + e.getMessage());
            return null;
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * 执行 http get 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String executeHttpGet(String url, Map<String, String> paramMap) {

        HttpClient httpclient = new DefaultHttpClient();

        StringBuffer buf = new StringBuffer(url);
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                if (buf.toString().contains("?")) {
                    buf.append("&");
                }
                else {
                    buf.append("?");
                }
                buf.append(key).append("=").append(encodeUrl(paramMap.get(key)));
            }
        }
        HttpGet httpGet = new HttpGet(buf.toString());

        try {
            HttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);

            return content;
        } catch (Exception e) {
            logger.error("executeHttpPost meet error, ", e);
            return null;
        } finally {
            httpGet.releaseConnection();
        }
    }

    private static boolean isHttpClientRequestSuccess(String result) {
        return StringUtils.equalsIgnoreCase(result, RETURN_SUCCESS);
    }

    private static String encodeUrl(String param) {
        try {
            return URLEncoder.encode(param, "utf-8");
        } catch (Exception e) {
            return param;
        }
    }
}
