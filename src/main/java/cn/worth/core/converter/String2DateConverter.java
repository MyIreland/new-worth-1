package cn.worth.core.converter;

import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.DateUtils;
import cn.worth.common.utils.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.util.regex.Pattern;

public class String2DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String dateString) {
        if (StringUtils.isNullOrEmpty(dateString)) {
            return null;
        }
        String patternDate = "\\d{4}-\\d{1,2}-\\d{1,2}";
        String patternTimeMinutes = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}";
        String patternTimeSeconds = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";

        boolean dateFlag = Pattern.matches(patternDate, dateString);
        boolean timeMinutesFlag = Pattern.matches(patternTimeMinutes, dateString);
        boolean timeSecondsFlag = Pattern.matches(patternTimeSeconds, dateString);

        if (dateFlag) {
            return DateUtils.parseDate(dateString);
        } else if (timeMinutesFlag) {
            return DateUtils.parseTimeMinutes(dateString);
        } else if (timeSecondsFlag) {
            return DateUtils.parseTime(dateString);
        } else {
            throw new BusinessException("时间格式错误");
        }

    }
}