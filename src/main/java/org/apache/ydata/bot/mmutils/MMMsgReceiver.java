package org.apache.ydata.bot.mmutils;

import com.alibaba.fastjson2.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.mpobjects.bdparsii.eval.Expression;
import com.mpobjects.bdparsii.eval.Parser;
import com.mpobjects.bdparsii.tokenizer.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.model.hub.HubRechargeRecord;
import org.apache.ydata.service.RedisUtils;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.service.hub.HubRechargeRecordService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MMMsgReceiver {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private MMMsgSender mmMsgSender;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private ZyAdapter zyAdapter;

    @Resource
    private IdUtil idUtil;
    @Resource
    private HubRechargeRecordService rechargeRecordService;

    @Resource
    HubDataService hubDataService;

    /**
     * 消息处理
     *
     * @param username
     * @param update
     */
    public void process(String username, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String recvMsg = update.getMessage().getText();
            if(recvMsg.startsWith("@" + username)) {
                //处理 相关绑定、解绑指令
                processCmd(username, update);
            } else if(recvMsg.equalsIgnoreCase(MMMsgType.id)) {
                //查询三方ID
                replyHubId(update);
            } else if(recvMsg.equalsIgnoreCase(MMMsgType.walletAddress)) {
                //充值地址: 返回充值地址
                responseRechargeAddr(update);
            } else if(recvMsg.startsWith(MMMsgType.addFee)) {
                //加款: 系统费添加
                addFee(update);
            } else if(recvMsg.startsWith(MMMsgType.jieFee)) {
                //预借: 系统费添加
                jieFee(update);
            } else if(recvMsg.equalsIgnoreCase(MMMsgType.balanceFee)) {
                //查询系统费余额
                getBalanceFee(update);
            } else if(recvMsg.equalsIgnoreCase(MMMsgType.detail)) {
                //查询明细数据
                try {
                    getDetail(update);
                } catch (java.text.ParseException e) {
                    mmMsgSender.sendReply(update, e.getMessage());
                }
            } else if(recvMsg.equalsIgnoreCase(MMMsgType.paoliang)) {
                //跑量
                try {
                    paoliang(update);
                } catch (Exception e) {
                    mmMsgSender.sendReply(update, e.getMessage());
                }
            }
        }

    }

    /**
     * 明细数据：日期、名称、跑量、收益、成率、余额
     * @param update
     */
    private void getDetail(Update update) throws java.text.ParseException {
        String today = Tools.timeDateString(System.currentTimeMillis(), "yyyyMMdd");
        List<HubInfo> hubInfoList = hubInfoService.selectAll(1);
        List<Long> userIdList = hubInfoList.stream().map(HubInfo::getId).collect(Collectors.toList());
        Map<Long, HubInfo> hubInfoDataMap = !ObjectUtils.isEmpty(hubInfoList) ? hubInfoList.stream().collect(Collectors.toMap(HubInfo::getId, HubInfo -> HubInfo)) : Maps.newHashMap();

        List<HubData> hubDataList = hubDataService.selectByDateAndHubIds(today, userIdList);
        StringBuffer replyMsg = new StringBuffer("日期: ").append(today).append("\n");
        if(!ObjectUtils.isEmpty(hubDataList)) {
            hubDataList.forEach(hubData -> {
                if(hubData.getOrderTotalCount() > 0) {
                    replyMsg.append("---------------\n");
                    replyMsg.append("名称: ").append(hubInfoDataMap.get(hubData.getHubId()).getName()).append("\n");
                    replyMsg.append("跑量: ").append(BigDecimal.valueOf(hubData.getOrderPayFee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("\n");
                    replyMsg.append("收益: ").append(BigDecimal.valueOf(hubData.getBenfit()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("\n");
                    replyMsg.append("成率: ").append(BigDecimal.valueOf(hubData.getOrderSuccessRate()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("%\n");
                    replyMsg.append("余额: ").append(BigDecimal.valueOf(hubData.getSysBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("\n");
                    replyMsg.append("---------------\n");
                }
            });
        }
        mmMsgSender.sendReply(update, replyMsg.toString());
    }

    /**
     * 跑量
     * @param update
     */
    private void paoliang(Update update) throws java.text.ParseException {
        if(!checkPermission(update)) return;
        //统计可用三方数量，今日在做的三方数量，今日总跑量，今日总收益
        List<HubInfo> hubInfoList = hubInfoService.selectAll(null);
        int avaliHub = 0;   //在做的三方数量
        double totalPayAmount = 0;  //今日总跑量
        double totalBenfit = 0;  //今日总收益
        String today = Tools.timeDateString(System.currentTimeMillis(), "yyyyMMdd");
        if(!ObjectUtils.isEmpty(hubInfoList)) {
            List<Long> hubIds = hubInfoList.stream().map(HubInfo::getId).collect(Collectors.toList());
            List<HubData> hubDataList = hubDataService.selectByDateAndHubIds(today, hubIds);
            Map<Long, HubData> hubDataDataMap = !ObjectUtils.isEmpty(hubDataList) ? hubDataList.stream().collect(Collectors.toMap(HubData::getHubId, HubData -> HubData)) : Maps.newHashMap();

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
            }
        }
        StringBuffer replyMsg = new StringBuffer("日期: ").append(today)
                .append("\n跑量: ").append(BigDecimal.valueOf(totalPayAmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                .append("\n收益: ").append(BigDecimal.valueOf(totalBenfit).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        mmMsgSender.sendReply(update, replyMsg.toString());
    }

    /**
     * 当前三方余额 - 实时
     * @param update
     */
    private void getBalanceFee(Update update) {
        Long chatId = update.getMessage().getChatId();
        HubInfo hubInfo = hubInfoService.selectByChatId(chatId);
        if(hubInfo == null) {
            mmMsgSender.sendReply(update, "未绑定，请先绑定");
            return;
        }
        try {
            JSONObject jsonDataObject = zyAdapter.stat(hubInfo, Tools.timeDateString(System.currentTimeMillis(), "yyyy-MM-dd"));
            if(jsonDataObject == null || !jsonDataObject.containsKey("statDate")) {
                mmMsgSender.sendReply(update, "错误，请检查目标服务是否正常");
                return;
            }
            Double balance = jsonDataObject.getJSONObject("userDashboardVo").getJSONObject("sysProp").getDouble("sysBalance");
            mmMsgSender.sendReply(update, "当前余额: " + BigDecimal.valueOf(balance).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            return;
        } catch (Exception e) {
            mmMsgSender.sendReply(update, "Err:错误 " + e.getMessage());
            return;
        }

    }

    /**
     * 返回绑定的三方ID
     * @param update
     */
    private void replyHubId(Update update) {
        Long chatId = update.getMessage().getChatId();
        HubInfo hubInfo = hubInfoService.selectByChatId(chatId);
        if(hubInfo == null) {
            mmMsgSender.sendReply(update, "未绑定，请先绑定");
            return;
        }
        mmMsgSender.sendReply(update, String.valueOf(hubInfo.getId()));
    }

    /**
     * 加款
     * @param update
     */
    private void addFee(Update update) {
//        if(!checkPermission(update)) return;
        String recvMsg = update.getMessage().getText();
        String txt = recvMsg.replace(MMMsgType.addFee, "").trim();
        if(ObjectUtils.isEmpty(txt)) {
            mmMsgSender.sendReply(update, "指令内容缺失");
            return;
        }

        BigDecimal fee = null;
        try {
            Expression parsiiExpr = Parser.parse(txt);
            fee = parsiiExpr.evaluate().setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (ParseException e) {
            log.info("checkExpression err = {}", e.getMessage());
            mmMsgSender.sendReply(update, "表达式错误");
            return;
        }

        HubInfo hubInfo = hubInfoService.selectByChatId(update.getMessage().getChatId());
        if(hubInfo == null) {
            mmMsgSender.sendReply(update, "错误，未绑定ID");
            return;
        }
        HubRechargeRecord vo = new HubRechargeRecord();
        vo.setAmount(fee.doubleValue());
        vo.setMemo(update.getMessage().getFrom().getUserName() + " 通过机器人指令添加");
        JSONObject jsonObject = zyAdapter.addSysFee(hubInfo, vo);
        if(jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200) {
            HubRechargeRecord rechargeRecord = new HubRechargeRecord();
            rechargeRecord.setId(idUtil.nextId());
            rechargeRecord.setHubId(hubInfo != null ? hubInfo.getId() : 0);
            rechargeRecord.setAmount(vo.getAmount());
            rechargeRecord.setMemo(vo.getMemo());
            rechargeRecord.setCreated(System.currentTimeMillis());
            rechargeRecordService.insert(rechargeRecord);
            mmMsgSender.sendReply(update, "加款成功,加款:" + fee.toString());
        } else {
            mmMsgSender.sendReply(update, "加款失败,原因:" + jsonObject.getString("msg"));
        }

    }

    /**
     * 预借
     * @param update
     */
    private void jieFee(Update update) {
        if(!checkPermission(update)) return;
        String recvMsg = update.getMessage().getText();
        String txt = recvMsg.replace(MMMsgType.addFee, "").trim();
        if(ObjectUtils.isEmpty(txt)) {
            mmMsgSender.sendReply(update, "指令内容缺失");
            return;
        }

        BigDecimal fee = null;
        try {
            Expression parsiiExpr = Parser.parse(txt);
            fee = parsiiExpr.evaluate().setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (ParseException e) {
            log.info("checkExpression err = {}", e.getMessage());
            mmMsgSender.sendReply(update, "表达式错误");
            return;
        }

        HubInfo hubInfo = hubInfoService.selectByChatId(update.getMessage().getChatId());
        if(hubInfo == null) {
            mmMsgSender.sendReply(update, "错误，未绑定ID");
            return;
        }
        HubRechargeRecord vo = new HubRechargeRecord();
        vo.setAmount(fee.doubleValue());
        vo.setMemo(update.getMessage().getFrom().getUserName() + " 通过机器人指令预借");
        JSONObject jsonObject = zyAdapter.addSysFee(hubInfo, vo);
        if(jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200) {
            HubRechargeRecord rechargeRecord = new HubRechargeRecord();
            rechargeRecord.setId(idUtil.nextId());
            rechargeRecord.setHubId(hubInfo != null ? hubInfo.getId() : 0);
            rechargeRecord.setAmount(vo.getAmount());
            rechargeRecord.setMemo(vo.getMemo());
            rechargeRecord.setCreated(System.currentTimeMillis());
            rechargeRecordService.insert(rechargeRecord);
            mmMsgSender.sendReply(update, "预借成功,加款:" + fee.toString());
        } else {
            mmMsgSender.sendReply(update, "预借失败,原因:" + jsonObject.getString("msg"));
        }
    }

    /**
     * 返回充值地址
     * @param update
     */
    private void responseRechargeAddr(Update update) {
        String addr = redisUtils.getSysConfig(SystemSettingsKeys.RECHARGE_ADDR);
        String addrImgUrl = redisUtils.getSysConfig(SystemSettingsKeys.RECHARGE_ADDR_IMG_URL);
        if(ObjectUtils.isEmpty(addrImgUrl) || ObjectUtils.isEmpty(addr)) {
            mmMsgSender.sendReply(update, "请管理员先设置充值地址信息");
            return;
        }
        mmMsgSender.sendPhotoReply(update, addrImgUrl, "充值地址：" + addr);
    }

    /**
     * 处理绑定指定
     * @param username
     * @param update
     */
    private void processCmd(String username, Update update) {
        String recvMsg = update.getMessage().getText();
        String txt = recvMsg.replace("@" + username, "").trim();
        if(ObjectUtils.isEmpty(txt)) {
            mmMsgSender.sendReply(update, "指令内容缺失");
            return;
        }
        if(recvMsg.contains(MMMsgType.help)) {
            //返回帮助
            mmMsgSender.sendReply(update, getHelpInfo());
            return;
        }

        //其它指令则验证权限
        if(!checkPermission(update)) return;
        if(txt.startsWith(MMMsgType.bind)) {
            String id = txt.replace(MMMsgType.bind, "").trim();
            if(ObjectUtils.isEmpty(id) || !isNumeric(id)) {
                //非数字
                mmMsgSender.sendReply(update, "绑定ID错误");
                return;
            }
            //绑定 三方和chatid
            bind(update, Long.parseLong(id));
        } else if(txt.startsWith(MMMsgType.unbind)) {
            String id = txt.replace(MMMsgType.unbind, "").trim();
            if(ObjectUtils.isEmpty(id) || !isNumeric(id)) {
                //非数字
                mmMsgSender.sendReply(update, "解绑ID错误");
                return;
            }
            //绑定 三方和chatid
            unbind(update, Long.parseLong(id));
        }

    }

    private void unbind(Update update, Long id) {
        HubInfo hubInfo = hubInfoService.selectByPrimaryKey(id);
        if(hubInfo == null ) {
            mmMsgSender.sendReply(update, "要解绑ID不存在");
            return;
        }
        hubInfo.setChatId(null);
        hubInfoService.updateByPrimaryKey(hubInfo);
        mmMsgSender.sendReply(update, "解绑成功");
    }

    private void bind(Update update, Long id) {
        HubInfo hubInfo = hubInfoService.selectByPrimaryKey(id);
        if(hubInfo == null ) {
            mmMsgSender.sendReply(update, "要绑定的ID不存在");
            return;
        }
        hubInfo.setChatId(update.getMessage().getChatId());
        hubInfoService.updateByPrimaryKeySelective(hubInfo);
        mmMsgSender.sendReply(update, "绑定成功");
    }

    private String getHelpInfo() {
        String helpText = "指令集：\n<b>id</b> - 获取当前系统分配的ID\n<b>地址</b> - 获取系统充值钱包地址\n<b>加款 [数学表达式]</b> - 系统加款\n<b>余额</b> - 显示当前系统费余额";
        return helpText;
    }
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private boolean checkPermission(Update update) {
        String botOperUsername = redisUtils.getSysConfig(SystemSettingsKeys.RECHARGE_BOT_OPER);
        if(ObjectUtils.isEmpty(botOperUsername)) {
            String replyMsg = "请先设置机器人管理";
            mmMsgSender.sendReply(update, replyMsg);
            return false;
        }
        String[] operUsernames = botOperUsername.split(",");
        List<String> names = Lists.newArrayList();
        for(String username : operUsernames) {
            names.add(username.trim());
        }
        if(names.contains(update.getMessage().getFrom().getUserName())) {
            return true;
        } else {
            String replyMsg = "您当前无权限执行该指令";
            mmMsgSender.sendReply(update, replyMsg);
            return false;
        }

    }
}
