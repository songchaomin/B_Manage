package com.linln.modules.notice.service;

import com.linln.modules.notice.domain.Notice;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

public interface NoticeService {
    /**
     * 获取积分页数据
     * @param example
     * @return
     */
    Page<Notice> getPageList(Example<Notice> example);


    /**
     * 保存积分
     * @param integral
     * @return
     */
    Notice save(Notice integral);


    void deleteNotice(Long id);

    Notice getNoticeById(Long id);


}
