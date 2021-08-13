package com.linln.modules.notice.service.impl;

import com.linln.common.data.PageSort;
import com.linln.modules.notice.domain.Notice;
import com.linln.modules.notice.repository.NoticeRepository;
import com.linln.modules.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public Page<Notice> getPageList(Example<Notice> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.DESC);
        return noticeRepository.findAll(example, page);
    }

    @Override
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public void deleteNotice(Long id) {
         noticeRepository.deleteById(id);
    }

    @Override
    public Notice getNoticeById(Long id) {
        return noticeRepository.getOne(id);
    }

}
