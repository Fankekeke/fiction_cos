package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.EvaluateInfo;
import cc.mrbird.febs.cos.dao.EvaluateInfoMapper;
import cc.mrbird.febs.cos.service.IEvaluateInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class EvaluateInfoServiceImpl extends ServiceImpl<EvaluateInfoMapper, EvaluateInfo> implements IEvaluateInfoService {

    /**
     * 分页获取书籍评价信息
     *
     * @param page         分页对象
     * @param evaluateInfo 书籍评价信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectEvaluatePage(Page<EvaluateInfo> page, EvaluateInfo evaluateInfo) {
        return baseMapper.selectEvaluatePage(page, evaluateInfo);
    }

    /**
     * 根据图书ID获取评价信息
     *
     * @param bookId 图书ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectListByBookId(Integer bookId) {
        return baseMapper.selectListByBookId(bookId);
    }
}
