package org.apache.ydata.controller;

import com.beust.jcommander.internal.Maps;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Resource
    private HttpServletRequest request;

    @Resource
    HubDataService hubDataService;

    @Resource
    HubInfoService hubInfoService;

    @GetMapping(value = "/")
    public Object root() {
        return "404";
    }

    @GetMapping(value = Constants.ERROR)
    public Object error() {
        return "errmsg";
    }

    @GetMapping(value = Constants.ADMIN_LOGIN)
    public Object adminLogin() {
        return "login";
    }

    @GetMapping(value = Constants.ADMIN_INDEX)
    public Object adminIndex() {
        return "index2";
    }

    @GetMapping(value = Constants.ADMIN_DASHBOARD)
    public Object adminDashboard() throws ParseException {
        //统计可用三方数量，今日在做的三方数量，今日总跑量，今日总收益
        List<HubInfo> hubInfoList = hubInfoService.selectAll();
        request.setAttribute("hubCount", !ObjectUtils.isEmpty(hubInfoList) ? hubInfoList.size() : 0);
        int avaliHub = 0;   //在做的三方数量
        double totalPayAmount = 0;  //今日总跑量
        double totalBenfit = 0;  //今日总收益
        double totalPayAmount2 = 0;  //昨日总跑量
        double totalBenfit2 = 0;  //昨日总收益
        if(!ObjectUtils.isEmpty(hubInfoList)) {
            List<Long> hubIds = hubInfoList.stream().map(HubInfo::getId).collect(Collectors.toList());
            String today = Tools.timeDateString(System.currentTimeMillis(), "yyyyMMdd");
            String yesterday = Tools.timeDateString(System.currentTimeMillis() - 24*60*60*1000L, "yyyyMMdd");
            List<HubData> hubDataList = hubDataService.selectByDateAndHubIds(today, hubIds);
            List<HubData> hubDataList2 = hubDataService.selectByDateAndHubIds(yesterday, hubIds);
            Map<Long, HubData> hubDataDataMap = !ObjectUtils.isEmpty(hubDataList) ? hubDataList.stream().collect(Collectors.toMap(HubData::getHubId, HubData -> HubData)) : Maps.newHashMap();
            Map<Long, HubData> hubDataDataMap2 = !ObjectUtils.isEmpty(hubDataList2) ? hubDataList2.stream().collect(Collectors.toMap(HubData::getHubId, HubData -> HubData)) : Maps.newHashMap();

            for(int i = 0;i<hubInfoList.size();i++) {
                HubInfo hubInfo = hubInfoList.get(i);
                if(hubDataDataMap.containsKey(hubInfo.getId())) {
                    HubData hubData = hubDataDataMap.get(hubInfo.getId());
                    if(hubData.getOrderPayFee() > 0) {
                        avaliHub++;//表示在做
                    }
                    totalPayAmount += (hubData != null ? hubData.getOrderPayFee() : 0); //总跑量
                    totalBenfit += (hubData != null ? hubData.getBenfit() : 0); //总收益
                }
                if(hubDataDataMap2.containsKey(hubInfo.getId())) {
                    HubData hubData2 = hubDataDataMap2.get(hubInfo.getId());
                    totalPayAmount2 += (hubData2 != null ? hubData2.getOrderPayFee() : 0); //总跑量
                    totalBenfit2 += (hubData2 != null ? hubData2.getBenfit() : 0); //总收益
                }
            }
        }
        request.setAttribute("hubCountAvali", avaliHub);
        request.setAttribute("totalPayAmount", new BigDecimal(totalPayAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        request.setAttribute("totalBenfit", new BigDecimal(totalBenfit).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        request.setAttribute("totalPayAmount2", new BigDecimal(totalPayAmount2).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        request.setAttribute("totalBenfit2", new BigDecimal(totalBenfit2).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        return "dashboard";
    }

    //业务相关
    @GetMapping(value = Constants.CONFIG_HUBINFO_LIST)
    public Object userinfoList() {
        return "config/hubinfo/list";
    }
    @GetMapping(value = Constants.CONFIG_HUBINFO_ADD_OR_UPDATE)
    public Object userinfoAddOrUpdate() {
        return "config/hubinfo/addOrUpdate";
    }
    @GetMapping(value = Constants.CONFIG_HUBINFO_VIEW_STAT)
    public Object userinfoViewState() {
        return "config/hubinfo/viewStat";
    }

    @GetMapping(value = Constants.BUSINESS_STAT_LIST)
    public Object hubdataStat() {
        return "systemstat/hubdata/stat";
    }

    @GetMapping(value = Constants.BUSINESS_HUBGROUP_LIST)
    public Object hubgroupList() {
        return "config/hubgroup/list";
    }

    @GetMapping(value = Constants.BUSINESS_HUBGROUP_ADD_OR_UPDATE)
    public Object hubgroupAddOrUpdate() {
        return "config/hubgroup/addOrUpdate";
    }

    @GetMapping(value = Constants.BUSINESS_HUBRECHARGE_RECORD_LIST)
    public Object hubrechargerecordList() {
        return "config/hubrechargerecord/list";
    }

    @GetMapping(value = Constants.BUSINESS_HUBRECHARGE_RECORD_ADD_OR_UPDATE)
    public Object hubrechargerecordAddOrUpdate() {
        return "config/hubrechargerecord/addOrUpdate";
    }

    @GetMapping(value = Constants.BUSINESS_BOT_ACCOUNT_LIST)
    public Object botaccountList() {
        return "config/botaccount/list";
    }

    @GetMapping(value = Constants.CONFIG_HUB_PAY_CHANNEL_LIST)
    public Object hubpaychannelList() {
        return "config/hubpaychannel/list";
    }
    @GetMapping(value = Constants.CONFIG_HUB_PAY_CHANNEL_ADD_OR_UPDATE)
    public Object hubpaychannelAddOrUpdate() {
        return "config/hubpaychannel/addOrUpdate";
    }

    @GetMapping(value = Constants.ORDERINFO_LIST)
    public Object orderinfoList() {
        return "business/orderinfo/list";
    }
    @GetMapping(value = Constants.ORDERINFO_UPDATE)
    public Object orderinfoView() {
        return "business/orderinfo/update";
    }

    //商户下单记录
    @GetMapping(value = Constants.MERCHANT_PO_RECORD_LIST)
    public Object merchantPoRecordList() {
        return "business/merchant/po_record_list";
    }
    @GetMapping(value = Constants.HUB_PROFIT_RECORD_LIST)
    public Object hubProfitRecordList() {
        return "business/hubprofitrecord/list";
    }

    @GetMapping(value = Constants.STAT_STAT)
    public Object statList() {
        return "systemstat/stat";
    }

    @GetMapping(value = Constants.PAYORDER_STAT)
    public Object payorderStatList() {
        return "systemstat/payorder/stat";
    }

    //系统相关
    @GetMapping(value = Constants.SYS_ADMIN_LIST)
    public Object sysAdminList() {
        return "system/admin/list";
    }
    @GetMapping(value = Constants.SYS_ADMIN__ADD_OR_UPDATE)
    public Object sysAdminAddOrUpdate() {
        return "system/admin/addOrUpdate";
    }
    @GetMapping(value = Constants.SYS_LOGS)
    public Object sysLogsList() {
        return "system/logs/list";
    }
    @GetMapping(value = Constants.SYS_SETTINGS)
    public Object sysSettingsList() {
        return "system/settings";
    }

    @GetMapping(value = Constants.SYS_PAY_CHANNEL_LIST)
    public Object payChannelList() {
        return "system/paychannel/list";
    }
    @GetMapping(value = Constants.SYS_PAY_CHANNEL_ADD_OR_UPDATE)
    public Object payChannelAddOrUpdate() {
        return "system/paychannel/addOrUpdate";
    }

    @GetMapping(value = Constants.CONFIG_MERCHANT_LIST)
    public Object merchantList() {
        return "config/mchinfo/list";
    }
    @GetMapping(value = Constants.CONFIG_MERCHANT_ADD_OR_UPDATE)
    public Object merchantAddOrUpdate() {
        return "config/mchinfo/addOrUpdate";
    }

    @GetMapping(value = Constants.CONFIG_MCH_PAY_CHANNEL_LIST)
    public Object mchPayChannelList() {
        return "config/mchpaychannel/list";
    }
    @GetMapping(value = Constants.CONFIG_MCH_PAY_CHANNEL_ADD_OR_UPDATE)
    public Object mchPayChannelAddOrUpdate() {
        return "config/mchpaychannel/addOrUpdate";
    }

    @GetMapping(value = Constants.HUBINFO_STAT)
    public Object userinfoStat() {
        return "systemstat/hubinfo/stat";
    }
    @GetMapping(value = Constants.MERCHANT_STAT)
    public Object merchantStat() {
        return "systemstat/merchant/stat";
    }

    @GetMapping(value = Constants.SYS_RECHARGE_RECORD_LIST)
    public Object rechargeRecordList() {
        return "system/rechargerecord/list";
    }
    @GetMapping(value = Constants.SYS_RECHARGE_RECORD_ADD_OR_UPDATE)
    public Object rechargeRecordAddOrUpdate() {
        return "system/rechargerecord/addOrUpdate";
    }

    //商户积分
    @GetMapping(value = Constants.MCH_POINTS_LIST)
    public Object mchpointsList() {
        return "business/merchantpoints/list";
    }
    @GetMapping(value = Constants.MCH_POINTS_ADD_OR_VIEW)
    public Object mchpointsAddOrView() {
        return "business/merchantpoints/addOrView";
    }

    @GetMapping(value = Constants.INDEX_PAY)
    public Object indexPay() {
        return "index-pay";
    }

}
