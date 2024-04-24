package com.example.yzs.service.impl.consultation;

import com.example.yzs.dao.consultation.ConsultationDao;
import com.example.yzs.entity.consultation.ConsultationTable;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import com.example.yzs.service.consultation.ConsultationService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Resource
    private ConsultationDao consultationDao;
    @Override
    public void add(ConsultationTable consultation) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        consultation.setCid(id);
        consultationDao.add(consultation);
    }

    @Override
    public List<ConsultationTable> findTable() {
        return consultationDao.findConsultationTable();
    }

    @Override
    public void updateStatus(ConsultationTable consultationTable) {
        if (consultationTable.getCid()==null){
            Map<String,Object> map = ThreadLocalUtil.get();
            Integer cid = (Integer)map.get("cid");
            consultationTable.setCid(cid);
        }

        LocalTime now = LocalTime.now();
        // 格式化当前时间到小时和分钟，忽略秒和纳秒
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeString = now.format(formatter);
        // 解析格式化的时间字符串到LocalTime
        LocalTime time = LocalTime.parse(timeString);
        if ("正在咨询".equals(consultationTable.getStatus())){
            consultationTable.setStartTime(String.valueOf(time));
        }else {
            consultationTable.setEndTime(String.valueOf(time));
        }
        System.out.println(consultationTable);
        consultationDao.updateStatus(consultationTable);

    }

    @Override
    public void addEvaluation(ConsultationEvaluation consultationEvaluation) {

        if (consultationEvaluation.getMedia() != null){
            System.out.println(consultationEvaluation.getMedia());
            String mediaString = String.join(",", consultationEvaluation.getMedia())
                    .substring(2, consultationEvaluation.getMedia().length() - 2);
            consultationDao.evaluationConsultants(consultationEvaluation, mediaString);
        }else {
            String mediaString =null;
            consultationDao.evaluationConsultants(consultationEvaluation, mediaString);
        }
    }
}
