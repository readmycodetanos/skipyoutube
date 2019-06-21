package com.tanos.skiptool;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import static android.accessibilityservice.AccessibilityService.*;
import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;


public class EventUtil {

    private static EventUtil eventUtil;

    public static EventUtil getInstance() {
        if (eventUtil == null) {
            eventUtil = new EventUtil();
        }
        return eventUtil;
    }


    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    public boolean checkAccessibilityEnabled(AccessibilityManager accessibilityManager, String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @return 是否启用
     */
    public boolean isAccessibilitySettingsOn(Context mContext, Class serviceCls) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + serviceCls.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }


    /**
     * 前往开启辅助服务界面
     */
    public void goAccess(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void clearNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    public boolean performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        boolean result = false;
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                result = true;
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
        return result;
    }

    /**
     * 模拟返回操作
     */
    public boolean performBackClick(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(GLOBAL_ACTION_BACK);
    }

    public boolean performHomeClick(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(GLOBAL_ACTION_HOME);
    }

    /**
     * 模拟最近操作
     */
    public boolean performRecentClick(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(GLOBAL_ACTION_RECENTS);
    }


    /**
     * 模拟下滑操作
     */
    @Deprecated
    public boolean performScrollBackward(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }



/*    public AccessibilityNodeInfo ScrollForwardfindNodeByText(AccessibilityService accessibilityService,String txt,int scrollcount){

        AccessibilityNodeInfo viewByText = findViewByText(accessibilityService, txt);
        if(viewByText==null){

            if(scrollcount>0){

               return ScrollForwardfindNodeByText(accessibilityService, txt, scrollcount-1);
            }else{
                return null;
            }

        }else{
            return viewByText;
        }

    }*/

    /**
     * 模拟上滑操作
     */
    @Deprecated
    public boolean performScrollForward(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    /**
     * 模拟下滑操作
     */
    public boolean performScrollBackward(AccessibilityNodeInfo accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    /**
     * 模拟上滑操作
     */
    public boolean performScrollForward(AccessibilityNodeInfo accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }


    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(AccessibilityService accessibilityService, String text, int parent_step) {
        return findViewByText(accessibilityService, text, false, parent_step);
    }


    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(AccessibilityService accessibilityService, String text) {
        return findViewByText(accessibilityService, text, false, 0);
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(AccessibilityService accessibilityService, String text, boolean clickable, int parent_step) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        AccessibilityNodeInfo result = null;
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                boolean check_click = true;
                if (clickable) {
                    check_click = nodeInfo.isClickable();
                }
                if (nodeInfo != null && check_click) {
                    result = nodeInfo;
                    for (int i = 0; i < parent_step; i++) {
                        result = result.getParent();
                        if (result == null) {
                            return result;
                        }
                    }
                    return result;
                }
            }
        }
        return null;
    }


    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    public AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo accessibilityNodeInfo, String text, boolean clickable, int parent_step) {
        if (accessibilityNodeInfo == null) {
            return null;
        }
        AccessibilityNodeInfo result = null;
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                boolean check_click = true;
                if (clickable) {
                    check_click = nodeInfo.isClickable();
                }
                if (nodeInfo != null && check_click) {
                    result = nodeInfo;
                    for (int i = 0; i < parent_step; i++) {
                        result = result.getParent();
                        if (result == null) {
                            return result;
                        }
                    }
                    return result;
                }
            }
        }
        return null;
    }


    public AccessibilityNodeInfo getParent(AccessibilityNodeInfo accessibilityNodeInfo, int step) {
        AccessibilityNodeInfo result = accessibilityNodeInfo;
        for (int i = 0; i < step; i++) {
            result = result.getParent();
            if (result == null) {
                return result;
            }
        }
        if (result != null) {
            return result;
        }

        return null;
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public AccessibilityNodeInfo findViewByID(AccessibilityService accessibilityService, String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }


    public boolean clickTextViewByText(AccessibilityService accessibilityService, String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return false;
        }
        boolean result = false;
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    result = performViewClick(nodeInfo);
                    break;
                }
            }
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean clickTextViewByID(AccessibilityService accessibilityService, String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return false;
        }
        boolean result = false;
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    result = performViewClick(nodeInfo);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param accessibilityNodeInfo
     * @param text_str
     * @return
     */
    public AccessibilityNodeInfo findViewByTextWebView(AccessibilityNodeInfo accessibilityNodeInfo, String text_str, boolean blur) {
        if (accessibilityNodeInfo == null) return null;

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            boolean result = false;
            if (child == null) {
                continue;
            }
            if (blur) {
                result = child.getText() != null && child.getText().toString().contains(text_str);
            } else {
                result = child.getText() != null && child.getText().toString().equalsIgnoreCase(text_str);
            }
            if (result) {
                return child;
            } else {
                AccessibilityNodeInfo focusAbleView = findViewByTextWebView(child, text_str, blur);
                if (focusAbleView != null) {
                    return focusAbleView;
                }
            }

        }

        return null;

    }

    /**
     * 根据搜索，低效率自创   用于WebView中
     *
     * @param accessibilityNodeInfo
     * @param id
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public AccessibilityNodeInfo findViewByIdWebView(AccessibilityNodeInfo accessibilityNodeInfo, String id) {
        if (accessibilityNodeInfo == null) return null;

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }
            if (child.getViewIdResourceName() != null && child.getViewIdResourceName().contains(id)) {
                return child;
            } else {
                AccessibilityNodeInfo focusAbleView = findViewByIdWebView(child, id);
                if (focusAbleView != null) {
                    return focusAbleView;
                }
            }

        }

        return null;

    }

    /**
     * 根据搜索，低效率自创   用于WebView中
     *
     * @param accessibilityNodeInfo
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public AccessibilityNodeInfo findViewByContentWebView(AccessibilityNodeInfo accessibilityNodeInfo, String content_desc) {
        if (accessibilityNodeInfo == null) return null;

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }

            if (child.getContentDescription() != null && child.getContentDescription().toString().contains(content_desc)) {
                return child;
            } else {
                AccessibilityNodeInfo focusAbleView = findViewByContentWebView(child, content_desc);
                if (focusAbleView != null) {
                    return focusAbleView;
                }
            }

        }

        return null;

    }

//    long lastEventPost =0L;

//    final long  TERM = 5000L;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void parseNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {

        if (accessibilityNodeInfo == null) return;
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }

            String log = "";
            for (int j = 0; j < getParentDeap(child); j++) {
                log += " _ ";
            }
            log += "[{" + i + "}class: " + child.getClassName() + ",id: " + child.getViewIdResourceName() + ",des: " + child.getContentDescription() + ",tex: " + child.getText() + ",click: " + child.isClickable() + ",scroll:" + child.isScrollable() + "]";
            Log.v("view", log);

            parseNodeInfo(child);

        }

    }


    public int getParentDeap(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return -1;
        }
        int result = 0;
        AccessibilityNodeInfo temp = accessibilityNodeInfo.getParent();
        while (temp != null) {
            result += 1;
            temp = temp.getParent();
        }

        return result;
    }


    /**
     * 根据View类型  EditText等判断，低效率自创。 用于WebView中
     *
     * @param accessibilityNodeInfo
     * @param view_type
     * @return
     */
    public AccessibilityNodeInfo findFocusAbleViewWebView(AccessibilityNodeInfo accessibilityNodeInfo, String view_type) {

        if (accessibilityNodeInfo == null) return null;

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }
            if (child.isFocusable() && child.getClassName() != null && child.getClassName().toString().contains(view_type)) {
                return child;
            } else {
                AccessibilityNodeInfo focusAbleView = findFocusAbleViewWebView(child, view_type);
                if (focusAbleView != null) {
                    return focusAbleView;
                }
            }

        }

        return null;
    }


    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo 文本框
     * @param text     text
     */
    public boolean inputText(Context context, AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            if (nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)) {
                return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        }
        return false;
    }


    public void uninstallApp(Context context, String appName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        if (context instanceof Activity) {

        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setData(Uri.parse("package:" + appName));
        context.startActivity(intent);
    }




    public boolean performPowerClick(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(GLOBAL_ACTION_HOME);
    }

    public boolean performSettingClick(AccessibilityService accessibilityService) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return accessibilityService.performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS);
    }

    public List<AccessibilityNodeInfo> collectClickableNode(AccessibilityNodeInfo accessibilityNodeInfo) {
        List<AccessibilityNodeInfo> infos = new ArrayList<>();

        if (accessibilityNodeInfo == null) return infos;
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }

            if (!"문서 저장하기".equalsIgnoreCase(child.getText() + "") && !TextUtils.isEmpty(child.getText()) &&
//                        child.getText().length()>"A사천옥한식한식한식".length()
                    child.getText().length() > "굴삭기 포크레인 공사시공 배수로공사 부동산개발 토목공사 건축터파기 기초공사".length()
            ) {
                infos.add(child);
            }
            if (infos.size() >= 100) {
                break;
            }
            infos.addAll(collectClickableNode(child));
        }
        return infos;
    }

    public List<AccessibilityNodeInfo> collectTextNode(AccessibilityNodeInfo accessibilityNodeInfo, String text, boolean blur) {
        List<AccessibilityNodeInfo> infos = new ArrayList<>();

        if (accessibilityNodeInfo == null) return infos;
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }

            if ((blur && !TextUtils.isEmpty(child.getText().toString()) && child.getText().toString().contains(text)) || (!blur && !TextUtils.isEmpty(child.getText().toString()) && text.equalsIgnoreCase(child.getText().toString()))
            ) {
                infos.add(child);
            }
            if (infos.size() >= 100) {
                break;
            }
            infos.addAll(collectTextNode(child, text, blur));
        }
        return infos;
    }

    public List<AccessibilityNodeInfo> collectClickableNode2(AccessibilityNodeInfo accessibilityNodeInfo) {
        List<AccessibilityNodeInfo> infos = new ArrayList<>();

        if (accessibilityNodeInfo == null) return infos;
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }

            if (!"문서 저장하기".equalsIgnoreCase(child.getText() + "") && !TextUtils.isEmpty(child.getText()) &&
//                        child.getText().length()>"A사천옥한식한식한식".length()
                    child.getText().length() > "굴삭기 포크레인 공사시공 공사시공".length()
            ) {
                infos.add(child);
            }
            if (infos.size() >= 100) {
                break;
            }
            infos.addAll(collectClickableNode(child));
        }
        return infos;
    }

    public AccessibilityNodeInfo findScrollAbleView(AccessibilityNodeInfo accessibilityNodeInfo) {

        if (accessibilityNodeInfo == null) return null;

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
            if (child == null) {
                continue;
            }
            if (child.isScrollable() && child.getClassName().toString().contains("WebView")) {
                return child;
            } else {
                AccessibilityNodeInfo focusAbleView = findScrollAbleView(child);
                if (focusAbleView != null) {
                    return focusAbleView;
                }
            }

        }

        return null;

    }


}
