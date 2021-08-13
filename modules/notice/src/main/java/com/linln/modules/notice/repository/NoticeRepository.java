package com.linln.modules.notice.repository;

import com.linln.modules.notice.domain.Notice;
import com.linln.modules.task.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeRepository extends BaseRepository<Notice,Long>, JpaSpecificationExecutor<Notice> {


}
